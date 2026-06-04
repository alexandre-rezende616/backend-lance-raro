package com.lanceraro.api.controller;

import com.lanceraro.api.dto.PixResponseDTO;
import com.lanceraro.api.model.Relic;
import com.lanceraro.api.repository.RelicRepository;
import com.lanceraro.api.service.AbacatePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private RelicRepository relicRepository;

    @Autowired
    private AbacatePayService abacatePayService;

    @GetMapping("/checkout/{relicId}")
    public ResponseEntity<PixResponseDTO> generateCheckout(@PathVariable Long relicId) {
        System.out.println("=====> [1/3] O APP BATEU NO JAVA! Pedido de PIX para relíquia ID: " + relicId);

        Relic relic = relicRepository.findById(relicId).orElse(null);
        if (relic == null) {
            System.out.println("=====> [ERRO] Relíquia não encontrada no banco!");
            return ResponseEntity.notFound().build();
        }

        System.out.println("=====> [2/3] Relíquia encontrada. Chamando a guilda do Abacate Pay...");
        PixResponseDTO response = abacatePayService.generatePixBilling(relic);

        System.out.println("=====> [3/3] Resposta do Abacate Pay recebida: Sucesso = " + response.isSuccess());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleAbacatePayWebhook(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Webhook do Abacate Pay recebido: " + payload);

            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            if (data != null && data.containsKey("externalId")) {
                String status = (String) data.get("status");

                if ("PAID".equals(status) || "COMPLETED".equals(status)) {
                    Long relicId = Long.parseLong((String) data.get("externalId"));
                    Relic relic = relicRepository.findById(relicId).orElse(null);

                    if (relic != null) {
                        relic.setInStock(false);
                        relicRepository.save(relic);
                        System.out.println("Relíquia " + relicId + " foi vendida com sucesso via PIX!");
                    }
                }
            }
            return ResponseEntity.ok("Webhook processado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no processamento do webhook");
        }
    }
}
