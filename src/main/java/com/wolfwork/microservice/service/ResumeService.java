package com.wolfwork.microservice.service;

import com.wolfwork.microservice.entities.Resume;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResumeService {

    private final String MOBILE_REGEXP = "\\+\\d{1,3} ?\\(?\\d{2,3}\\)? ?\\d{4,5}-?\\d{4}";
    private final String EMAIL_REGEXP = "[\\w]+@[\\w]+.[\\w]+.?[\\w]+?";
    private final String EDUCATION_REGEXP = "EDUCATION[S]?";
    private final String SKILL_REGEXP = "SKILL[S]?";
    private final String EXPERIENCE_REGEXP = "EXPERIENCE[S]?";

    private BiFunction<String, String, String> extrator = (regex, document) -> {
        val pattern = Pattern.compile(regex, Pattern.MULTILINE);
        var matcher = pattern.matcher(document);
        if( matcher.find() ) {
            return matcher.group(0).strip();
        }
        return null;
    };

    public Resume transform(final String content) {

        val mobile = extrator.apply(MOBILE_REGEXP, content);
        val email = extrator.apply(EMAIL_REGEXP, content);
        val educations = this.getListOf(EDUCATION_REGEXP, content);
        val skills = this.getListOf(SKILL_REGEXP, content);
        var experience = this.getListOf(EXPERIENCE_REGEXP, content, EDUCATION_REGEXP, SKILL_REGEXP);

        experience = experience
                .stream()
                .filter( e -> e.matches(".*Since.*|.*From.*|(.*to.*&\\{Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec})") )
                .collect(Collectors.toList());

        val resume = new Resume(mobile, email, educations, skills, experience);

        return resume;
    }

    private Collection<String> getListOf(final String regexp, final String content, String... clean) {
        val subtitle = extrator.apply(regexp, content);
        int indexOf = content.indexOf(subtitle);
        var values = content.substring(indexOf + subtitle.length() );
        values = values.strip();

        for( String c : clean) {
            val toclean = extrator.apply(c, values);
            if( toclean != null ) {
                int end = values.indexOf(toclean);
                values = values.substring(0, end);
            }
        }

        return Arrays.asList(values.strip().split("\n"))
                .stream()
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());

    }

    private Collection<String> getListOf(final String regexp, final String content) {

        val subtitle = extrator.apply(regexp, content);
        var values = content.substring(content.indexOf(subtitle) + subtitle.length() );
        values = values.strip().substring(0, values.indexOf("\n\n") );

        return Arrays.asList(values.split("\n"))
                .stream()
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());

    }

}
