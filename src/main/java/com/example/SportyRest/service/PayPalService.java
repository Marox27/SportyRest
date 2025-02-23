package com.example.SportyRest.service;

import com.example.SportyRest.model.Pago;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;
import java.util.Base64;

@Service
public class PayPalService {
    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    private final String PAYPAL_API_BASE = "https://api-m.sandbox.paypal.com"; // Usa live en producción

    private String obtenerAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PAYPAL_API_BASE + "/v1/oauth2/token",
                HttpMethod.POST,
                request,
                String.class
        );

        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("access_token");
    }

    public String autorizarPago(double monto, String moneda, String descripcion) {
        String accessToken = obtenerAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("intent", "AUTHORIZE");
        body.put("purchase_units", new org.json.JSONArray()
                .put(new JSONObject()
                        .put("amount", new JSONObject()
                                .put("currency_code", moneda)
                                .put("value", monto))
                        .put("description", descripcion)));

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PAYPAL_API_BASE + "/v2/checkout/orders",
                HttpMethod.POST,
                request,
                String.class
        );

        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("id"); // Retorna el ID de la orden de pago
    }

    public boolean capturarPago(Pago pago) {
        String accessToken = obtenerAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PAYPAL_API_BASE + "/v2/checkout/orders/" + pago.getId() + "/capture",
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getStatusCode() == HttpStatus.CREATED;
    }
}
