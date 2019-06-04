package ru.prpaha.yandex.kassa.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmationRedirect implements IConfirmation {

    private ConfirmationType type;
    @SerializedName("return_url")
    private String returnUrl;

}
