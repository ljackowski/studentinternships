package com.ljackowski.studentinternships.documentsgeneration;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.ljackowski.studentinternships.intern.Intern;
import com.ljackowski.studentinternships.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class PDFGeneration {

    private final TemplateEngine templateEngine;
    private final ServletContext servletContext;
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ConverterProperties converterProperties;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;


    public PDFGeneration(TemplateEngine templateEngine, ServletContext servletContext, ByteArrayOutputStream byteArrayOutputStream,
                         ConverterProperties converterProperties, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.templateEngine = templateEngine;
        this.servletContext = servletContext;
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.converterProperties = converterProperties.setBaseUri("http://localhost:8080");
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    public WebContext generateContext(Object object, String objectName) {
        WebContext webContext = new WebContext(httpServletRequest, httpServletResponse, servletContext);
        webContext.setVariable("date", LocalDate.now());
        webContext.setVariable(objectName, object);
        return webContext;
    }

    public ResponseEntity<?> generateStudentPDF(String url, Student student) {
        HtmlConverter.convertToPdf(templateEngine.process(url, generateContext(student, "student")), byteArrayOutputStream, converterProperties);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(byteArrayOutputStream.toByteArray());
    }

    public ResponseEntity<?> generateInternPDF(String url, Intern intern) {
        HtmlConverter.convertToPdf(templateEngine.process(url, generateContext(intern, "intern")), byteArrayOutputStream, converterProperties);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(byteArrayOutputStream.toByteArray());
    }

    public ResponseEntity<?> generatePDF(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        WebContext context = new WebContext(request, response, servletContext);
        ConverterProperties converterProperties = new ConverterProperties();
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(templateEngine.process(url, context), target, converterProperties);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(target.toByteArray());
    }

}
