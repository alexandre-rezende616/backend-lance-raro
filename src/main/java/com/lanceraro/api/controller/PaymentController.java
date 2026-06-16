package com.lanceraro.api.controller;

import com.lanceraro.api.model.Relic;
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
                    "externalId", "relic-" + relic.getId(),
                    "name", title + " - " + relic.getTitle(),
                    "quantity", 1,
                    "price", priceInCents,
                    "description", "Destaque VIP na Taverna Lance Raro"
            );

            Map<String, Object> body = Map.of(
                    "frequency", "ONE_TIME",
                    "methods", List.of("PIX"),
                    "products", List.of(product),
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

        } catch (org.springframework.web.client.RestClientResponseException e) {
            // Captura o erro exato retornado pelo AbacatePay!
            return ResponseEntity.status(e.getStatusCode()).body("A API do AbacatePay rejeitou: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro na magia de pagamento: " + e.getMessage());
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