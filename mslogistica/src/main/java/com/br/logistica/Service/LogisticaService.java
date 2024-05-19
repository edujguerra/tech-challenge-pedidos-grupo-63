package com.br.logistica.Service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.br.logistica.Entity.Enum.StatusPedidoEnum;

import jakarta.annotation.PostConstruct;
@Service
public class LogisticaService {

    @Autowired
    RestTemplate restTemplate;

    @PostConstruct
    public void adicionarEntregador(){
        Long pedidoId = 1L;
        Long entregadorId = 1L;
        String url = "http://mspedidos:8083/api/pedidos/entregador/" + pedidoId + "/" + entregadorId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        
        System.out.println(response);
    }

    private void alterarStatus(StatusPedidoEnum status, Long pedidoId){
        String url = "http://mspedidos:8083/api/pedidos/" + pedidoId ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, StatusPedidoEnum> requestBody = new HashMap<>();
            requestBody.put("statusPedidoEnum", status);
        
        HttpEntity<Map<String, StatusPedidoEnum>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        
        System.out.println(response);
    }

}
