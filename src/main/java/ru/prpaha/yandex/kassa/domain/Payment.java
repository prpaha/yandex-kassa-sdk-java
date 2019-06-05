package ru.prpaha.yandex.kassa.domain;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Payment implements Serializable {

    private String id;
    private String status;
    private Boolean paid;
    private Amount amount;
    private Confirmation confirmation;
    @SerializedName("created_at")
    private String createdAt;
    private String description;
    private Metadata metadata;
    private Recipient recipient;
    private Boolean test;
    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

}
