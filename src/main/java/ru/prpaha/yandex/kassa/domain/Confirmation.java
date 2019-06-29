package ru.prpaha.yandex.kassa.domain;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public class Confirmation implements Serializable {

    private ConfirmationType type;
    @SerializedName("confirmation_url")
    private String confirmationUrl;

    public enum ConfirmationType {
        external, redirect
    }

}
