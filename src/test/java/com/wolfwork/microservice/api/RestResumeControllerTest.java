package com.wolfwork.microservice.api;

import com.wolfwork.microservice.entities.Resume;
import lombok.SneakyThrows;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestResumeControllerTest {

    private RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();

    private final URL sampleFile = ClassLoader.getSystemResource("sample.docx");
    private POITextExtractor extractedFile;
    private String content;

    @BeforeEach
    @SneakyThrows
    private void setupClass() {

        this.extractedFile = ExtractorFactory.createExtractor(new File(sampleFile.toURI()));
        this.content = Base64.getEncoder().encodeToString(extractedFile.getText().getBytes(StandardCharsets.UTF_8));

    }

    @Test
    public void testPostMethod() {
        var resume = restTemplate.postForObject("http://localhost:8080/resume", new HttpEntity<String>(content), Resume.class);
        assertEquals("+55 21 98136-3699", resume.getMobile());
        assertEquals("marsilvarodrigues@gmail.com",resume.getEmail());
        assertEquals(1 , resume.getEducations().size());
        assertTrue( resume.getEducations().contains("Bachelor in Computer Science – UniverCidade"));
        assertEquals(15 , resume.getSkills().size());
        assertEquals(11 , resume.getExperience().size());

    }
}
