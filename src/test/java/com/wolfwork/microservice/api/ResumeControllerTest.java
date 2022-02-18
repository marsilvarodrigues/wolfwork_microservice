package com.wolfwork.microservice.api;

import com.wolfwork.microservice.entities.Resume;
import io.netty.handler.codec.base64.Base64Encoder;
import lombok.SneakyThrows;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest()
class ResumeControllerTest {

    private final URL sampleFile = ClassLoader.getSystemResource("sample.docx");
    private POITextExtractor extractedFile;
    private String content;

    @BeforeEach
    @SneakyThrows
    private void setupClass() {

        this.extractedFile = ExtractorFactory.createExtractor(new File(sampleFile.toURI()));
        this.content = Base64.getEncoder().encodeToString(extractedFile.getText().getBytes(StandardCharsets.UTF_8));

    }

    @Autowired
    private ResumeController controller;

    @Test
    void upload() {
        ResponseEntity<Resume> resume = controller.upload(this.content);
        assertEquals("+55 21 98136-3699", resume.getBody().getMobile());
        assertEquals("marsilvarodrigues@gmail.com",resume.getBody().getEmail());
        assertEquals(1 , resume.getBody().getEducations().size());
        assertTrue( resume.getBody().getEducations().contains("Bachelor in Computer Science – UniverCidade"));

    }
}