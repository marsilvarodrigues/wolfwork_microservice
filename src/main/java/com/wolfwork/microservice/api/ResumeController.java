package com.wolfwork.microservice.api;

import com.wolfwork.microservice.entities.Resume;
import com.wolfwork.microservice.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.input.CharacterSetFilterReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping(path = "/resume")
@Slf4j
public class ResumeController {

    private ResumeService service;

    private ResumeController(ResumeService service){

        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resume> upload(@RequestBody(required = true) String resume) {
        try {
            log.info("received a resume to evaluate");

            val decoded = new String(Base64.getDecoder().decode(resume), StandardCharsets.UTF_8);
            if( log.isDebugEnabled() ){
                log.debug("resume received to evaluate {}", decoded);
            }

            val entity = service.transform(decoded);

            return ResponseEntity.ok(entity);
        }finally {
            if( log.isDebugEnabled() ) {
                log.debug("finished the evaluation for resume {}", resume);
            }
        }
    }
}
