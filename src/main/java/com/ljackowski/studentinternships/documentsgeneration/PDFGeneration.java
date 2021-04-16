package com.ljackowski.studentinternships.documentsgeneration;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.ljackowski.studentinternships.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class PDFGeneration {

    private final TemplateEngine templateEngine;
    private final ServletContext servletContext;

    public PDFGeneration(TemplateEngine templateEngine, ServletContext servletContext) {
        this.templateEngine = templateEngine;
        this.servletContext = servletContext;
    }

    public ResponseEntity<?> generateStudentPDF(HttpServletRequest request, HttpServletResponse response, String url, Student student) throws IOException {
        WebContext context = new WebContext(request, response, servletContext);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        context.setVariable("year", Calendar.getInstance().get(Calendar.YEAR));
        context.setVariable("student", student);
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(templateEngine.process(url, context), target, converterProperties);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(target.toByteArray());
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
