package com.lanceraro.api.controller;

import com.lanceraro.api.model.Notification;
import com.lanceraro.api.model.Relic;
import com.lanceraro.api.repository.NotificationRepository;
import com.lanceraro.api.repository.RelicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${ABACATE_PAY_TOKEN}")
    private String abacatePayToken;

    @Autowired
    private RelicRepository relicRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public static class CheckoutRequest {
        public Long relicId;
        public int boostLevel;
        public String payerEmail; // O e-mail do usuário logado
    }

    @PostMapping("/create-billing")
    public ResponseEntity<?> createBilling(@RequestBody CheckoutRequest request) {
        try {
            Relic relic = relicRepository.findById(request.relicId)
                    .orElseThrow(() -> new RuntimeException("Relíquia não encontrada!"));

            int priceInCents = getAmountForBoostLevel(request.boostLevel);
            String title = getTitleForBoostLevel(request.boostLevel);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(abacatePayToken);

            // Monta o pacote de dados exigido pelo AbacatePay
            Map<String, Object> product = Map.of(
                    // A etiqueta secreta agora leva o ID e o Nível! (Ex: relic-15-3)
                    "externalId", "relic-" + relic.getId() + "-" + request.boostLevel,
                    "name", title + " - " + relic.getTitle(),
                    "quantity", 1,
                    "price", priceInCents,
                    "description", "Destaque VIP na Taverna Lance Raro"
            );

            Map<String, Object> customer = Map.of(
                    "email", request.payerEmail != null ? request.payerEmail : "aventureiro@guilda.com"
            );

            Map<String, Object> body = Map.of(
                    "frequency", "ONE_TIME",
                    "methods", List.of("PIX"),
                    "products", List.of(product),
                    "customer", customer,
                    "returnUrl", "https://lance-raro-api.onrender.com",
                    "completionUrl", "https://lance-raro-api.onrender.com/api/payments/webhook"
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // Dispara a requisição para a API real do AbacatePay
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.abacatepay.com/v1/billing/create",
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                String checkoutUrl = (String) data.get("url");

                return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A guilda do AbacatePay recusou o pedido.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro na magia de pagamento: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // O AbacatePay manda os dados do pagamento dentro do objeto "data"
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if (data != null && "PAID".equals(data.get("status"))) {
                List<Map<String, Object>> products = (List<Map<String, Object>>) data.get("products");

                if (products != null && !products.isEmpty()) {
                    String externalId = (String) products.get(0).get("externalId");

                    // Quebra o texto "relic-15-3" para pegar o ID (15) e o Nível (3)
                    if (externalId != null && externalId.startsWith("relic-")) {
                        String[] parts = externalId.split("-");
                        if (parts.length >= 3) {
                            Long relicId = Long.parseLong(parts[1]);
                            Integer level = Integer.parseInt(parts[2]);

                            Relic relic = relicRepository.findById(relicId).orElse(null);
                            if (relic != null) {
                                relic.setBoostLevel(level);
                                int dias = (level == 3) ? 30 : ((level == 2) ? 7 : 3);
                                relic.setBoostExpiresAt(java.time.LocalDateTime.now().plusDays(dias));
                                relicRepository.save(relic);

                                // Envia a notificação (Corvo) pro Aventureiro!
                                Notification notif = new Notification();
                                notif.setUserId(relic.getUserId());
                                notif.setTitle("Destaque VIP Ativado! ✨");
                                notif.setMessage("A relíquia '" + relic.getTitle() + "' brilhará no topo do mural por " + dias + " dias.");
                                notificationRepository.save(notif);
                            }
                        }
                    }
                }
            }
            return ResponseEntity.ok("Webhook recebido pela Taverna!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no Correio: " + e.getMessage());
        }
    }

    private int getAmountForBoostLevel(int level) {
        return switch (level) {
            case 1 -> 500;  // R$ 5,00 em centavos (Bronze)
            case 2 -> 1000; // R$ 10,00 em centavos (Prata)
            case 3 -> 2500; // R$ 25,00 em centavos (Ouro)
            default -> throw new IllegalArgumentException("Nível de destaque inválido.");
        };
    }

    private String getTitleForBoostLevel(int level) {
        return switch (level) {
            case 1 -> "Destaque Bronze";
            case 2 -> "Destaque Prata";
            case 3 -> "Destaque Ouro";
            default -> "Destaque VIP";
        };
    }
}