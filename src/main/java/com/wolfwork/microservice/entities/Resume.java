package com.wolfwork.microservice.entities;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;


@RequiredArgsConstructor
@NoArgsConstructor
public class Resume implements Serializable {

    @NonNull
    @Getter
    private String mobile;

    @NonNull
    @Getter
    private String email;

    @NonNull
    @Getter
    private Collection<String> educations;

    @NonNull
    @Getter
    private Collection<String> skills;

    @NonNull
    @Getter
    private Collection<String> experience;
}
