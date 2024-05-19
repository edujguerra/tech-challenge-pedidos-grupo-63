package com.br.logistica.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.br.logistica.Entity.Entregador;
import com.br.logistica.Entity.PedidoDTO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PedidoService {

    private void gerarRelatorio() throws JRException, IOException{
        System.out.println("processofinalizado---------------------------");

        List<PedidoDTO> pedidos = new ArrayList<>();
        Entregador entregador = new Entregador(1L,"Cleber","4980825555");
        Entregador entregador2 = new Entregador(2L,"Alberto","14258768523");


        PedidoDTO pedido = new PedidoDTO(1L,"P",LocalDateTime.now(),entregador.getNome());
        pedidos.add(pedido);

        pedido = new PedidoDTO(2L,"P",LocalDateTime.now(),entregador2.getNome());
        pedidos.add(pedido);

        pedido = new PedidoDTO(3L,"P",LocalDateTime.now(),entregador.getNome());
        pedidos.add(pedido);

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
        headers.setContentDispositionFormData("filename", "report.pdf");


        // Exporta o relatório para um arquivo PDF
        String pdfFile = "relatorioPedidosPagos.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        // Retorna a resposta HTTP com o relatório em PDF
        // return ResponseEntity.ok().headers(headers).body(pdfReport);
        System.out.println("processofinalizado---------------------------");
    }

}
