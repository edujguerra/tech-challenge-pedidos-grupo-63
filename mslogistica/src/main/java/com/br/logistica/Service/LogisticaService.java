package com.br.logistica.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.br.logistica.Entity.EntregadorDTO;
import com.br.logistica.Entity.PedidoDTO;
import com.br.logistica.Entity.Enum.StatusPedidoEnum;
import com.fasterxml.jackson.databind.JsonNode;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LogisticaService {

    @Autowired
    RestTemplate restTemplate;

    int i = 0;

    public String adicionarEntregador(EntregadorDTO dto){
        Long pedidoId = dto.getIdPedido();
        Long entregadorId = dto.getIdEntregador();
        String url = "http://mspedidos:8083/api/pedidos/entregador/" + pedidoId + "/" + entregadorId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        if (response.getStatusCode().is2xxSuccessful())
            return "Entregador foi adicionado ao pedido";
        else
            return "O serviço retornou: "+response.getStatusCode().toString()+ " body: " + response.getBody();
    }

    public String alterarStatus(StatusPedidoEnum statusPedidoEnum, Long pedidoId){
        String url = "http://mspedidos:8083/api/pedidos/" + pedidoId ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String status = statusPedidoEnum.name();
        Map<String, String> params = new HashMap();
        params.put("status", status);

        HttpEntity<Map<String,String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.PUT, request,String.class);

        if (response.getStatusCode().is2xxSuccessful())
            return "Status foi alterado do pedido";
        else
            return "O serviço retornou: "+response.getStatusCode().toString()+ " body: " + response.getBody();
        
    }


    private void gerarRelatorio(List<PedidoDTO> pedidos) throws JRException, IOException{

        // Caminho do arquivo JRXML dentro da pasta resources
        String jrxmlFile = "relatorioPedidosPagos.jrxml";

        // Compila o arquivo JRXML em um arquivo Jasper
        JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource(jrxmlFile).getInputStream());

        // Preenche o relatório com os dados
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JRBeanCollectionDataSource(pedidos));

        // Exporta o relatório para um array de bytes no formato PDF
        byte[] pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);

        // Configura os headers para o navegador fazer o download do arquivo PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "report"+pedidos.get(0).getCep().substring(0,4)+".pdf");


        // Exporta o relatório para um arquivo PDF
        String pdfFile = "relatorioPedidosPagos"+pedidos.get(0).getCep().substring(0,4)+".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, "/app/relatorios/"+pdfFile);
        // Retorna a resposta HTTP com o relatório em PDF
        // return ResponseEntity.ok().headers(headers).body(pdfReport);

    }

    public String processoRelatorio() throws JRException, IOException{
        List<PedidoDTO> pedidos = new ArrayList<>();

        String url = "http://mspedidos:8083/api/pedidos/status/" +"PEDIDO_PAGO" ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class);
        
        response.getBody().elements().forEachRemaining(t -> {
            pedidos.add(new PedidoDTO(
                t.findValue("id").asLong(),
                StatusPedidoEnum.obterStatusPorNomeOuStringEnum(t.findValue("status").asText()),
                t.findValue("cpfCliente").asText(),
                t.findValue("nomeCliente").asText(),
                t.findValue("cep").asText()));

        });

         // Criando um mapa para armazenar os pedidos por sub-setor (baseado no último dígito do CEP)
         Map<Character, List<PedidoDTO>> pedidosPorSubsetor = new HashMap<>();
 
         // Processando os pedidos e organizando-os por sub-setor
         for (PedidoDTO pedido : pedidos) {
             char ultimoDigitoCep = pedido.getCep().charAt(3);
             pedidosPorSubsetor.computeIfAbsent(ultimoDigitoCep, k -> new ArrayList<>()).add(pedido);
         }
         // Gerando o relatório
         for (Map.Entry<Character, List<PedidoDTO>> entry : pedidosPorSubsetor.entrySet()) {
             char subSetor = entry.getKey();
             
             List<PedidoDTO> pedidosSubsetor = entry.getValue();
             gerarRelatorio(pedidosSubsetor);


             
             // Imprimindo os pedidos do sub-setor
             for (PedidoDTO pedido : pedidosSubsetor) {
                 alterarStatus(StatusPedidoEnum.PREPARANDO_PARA_ENVIO,pedido.getPedido_id());
             }
         }

         return "Foi gerado os relatorios e alterado os status dos pedidos";
    }


}
