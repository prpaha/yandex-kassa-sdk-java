package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class Payment implements Serializable {

    private String id;
    private String status;
    private Boolean paid;
    private Amount amount;
    private Confirmation confirmation;
    private String createdAt;
    private String description;
    private Metadata metadata;
    private Recipient recipient;
    private Boolean test;
    private Map<String, Object> additionalProperties;

}
