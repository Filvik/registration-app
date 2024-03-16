package com.test_example.registration_app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestCreationResponse {

    private String description;
    private String url;

    public RequestCreationResponse(String description) {
        this.description = description;
    }

    public RequestCreationResponse(String description, String url) {
        this.description = description;
        this.url = url;
    }
}

