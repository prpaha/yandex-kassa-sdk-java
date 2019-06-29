package ru.prpaha.yandex.kassa.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.prpaha.yandex.kassa.domain.Receipt;

import java.io.Serializable;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CreatePaymentRequest implements Serializable {

    private Amount amount;
    private Boolean capture;
    private IConfirmation confirmation;
    private String description;
    @SerializedName("payment_token")
    private String paymentToken;
    private Receipt receipt;
    private Map<String, Object> metadata;

}
