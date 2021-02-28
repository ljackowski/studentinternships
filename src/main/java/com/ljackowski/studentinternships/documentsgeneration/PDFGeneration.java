package com.ljackowski.studentinternships.documentsgeneration;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFGeneration {

    private TemplateEngine templateEngine;
    private ServletContext servletContext;

    public PDFGeneration(TemplateEngine templateEngine, ServletContext servletContext) {
        this.templateEngine = templateEngine;
        this.servletContext = servletContext;
    }

    public ResponseEntity<?> generateQuestionnaire(HttpServletRequest request, HttpServletResponse response, String url, OrganizationAgreement organizationAgreement) throws IOException {
        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("studentAgreementForm", organizationAgreement);
        String orderHtml = templateEngine.process(url, context);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);
        byte[] bytes = target.toByteArray();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
    }

    public ResponseEntity<?> generatePDF(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        WebContext context = new WebContext(request, response, servletContext);
        String orderHtml = templateEngine.process(url, context);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);
        byte[] bytes = target.toByteArray();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
    }

}
