package com.lanceraro.api.service;

import com.lanceraro.api.dto.PixResponseDTO;
import com.lanceraro.api.model.Relic;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class AbacatePayService {

    // TODO: Trocar pelo seu token real gerado no painel do Abacate Pay
    private final String apiToken = "abc_dev_4JXBhcyFWgkUNQhcBf5eRaSq";
    private final String apiUrl = "https://api.abacatepay.com/v1/billing/create";

    public PixResponseDTO generatePixBilling(Relic relic) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        int precoCentavos = (int) (relic.getPrice() * 100);

        Map<String, Object> product = new HashMap<>();
        product.put("externalId", relic.getId().toString());
        product.put("name", relic.getTitle());
        product.put("quantity", 1);
        product.put("price", precoCentavos);

        // O Abacate Pay exige os dados do cliente (Customer) para gerar a cobrança
        Map<String, String> customer = new HashMap<>();
        customer.put("name", "Aventureiro da Taverna");
        customer.put("cellphone", "11999999999");
        customer.put("taxId", "08768036213"); // CPF de mentira (matematicamente válido) para testes
        customer.put("email", "aventureiro@lanceraro.com.br");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("frequency", "ONE_TIME");
        requestBody.put("methods", Arrays.asList("PIX"));
        requestBody.put("products", Arrays.asList(product));
        requestBody.put("returnUrl", "https://lanceraro.com.br/sucesso");

        // URL do Ngrok que o Abacate Pay vai usar para avisar o Java que o PIX foi pago!
        requestBody.put("completionUrl", "https://cresting-ecologist-gamma.ngrok-free.dev/api/payments/webhook");

        requestBody.put("customer", customer); // <--- Adicionamos o cliente aqui!

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                PixResponseDTO dto = new PixResponseDTO();
                dto.setSuccess(true);
                dto.setPaymentUrl((String) data.get("url")); // O link de pagamento oficial

                // Retornando mock do QR Code para a sua interface do React Native renderizar:
                dto.setQrCode("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=00020126580014br.gov.bcb.pix0136abacate-pay-mock-" + relic.getId());
                dto.setPixCopiaECola("00020126580014br.gov.bcb.pix0136abacate-pay-" + relic.getId());
                return dto;
            }
        } catch (Exception e) {
            System.out.println("Erro API Abacate Pay: " + e.getMessage());
        }
        return new PixResponseDTO(); // Retorna vazio se falhar
    }
}
