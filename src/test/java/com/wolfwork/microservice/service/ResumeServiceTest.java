package com.wolfwork.microservice.service;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ResumeServiceTest {

    private final URL sampleFile = ClassLoader.getSystemResource("sample.docx");
    private POITextExtractor extractedFile;
    private String content;
    private ResumeService service = new ResumeService();


    @BeforeEach
    @SneakyThrows
    private void setupClass() {

        this.extractedFile = ExtractorFactory.createExtractor(new File(sampleFile.toURI()));
        this.content = extractedFile.getText();

    }

    @Test
    void verifyData() {

        val resume = service.transform(content);

        assertEquals("+55 21 98136-3699", resume.getMobile());
        assertEquals("marsilvarodrigues@gmail.com",resume.getEmail());
        assertEquals(1 , resume.getEducations().size());
        assertTrue( resume.getEducations().contains("Bachelor in Computer Science – UniverCidade"));

    }
}