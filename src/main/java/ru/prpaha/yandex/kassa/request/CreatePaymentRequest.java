package ru.prpaha.yandex.kassa.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreatePaymentRequest implements Serializable {

    private Amount amount;
    private boolean capture;
    private IConfirmation confirmation;
    private String description;
    @SerializedName("payment_token")
    private String paymentToken;

}
