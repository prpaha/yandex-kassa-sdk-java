package ru.prpaha.yandex.kassa.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceiptItem {

    private String description;
    private String quantity;
    private Amount amount;
    @SerializedName("vat_code")
    private String vatCode;
    @SerializedName("payment_mode")
    private String paymentMode;
    @SerializedName("payment_subject")
    private String paymentSubject;
    @SerializedName("country_of_origin_code")
    private String countryOfOriginCode;
    @SerializedName("customs_declaration_number")
    private String customsDeclarationNumber;
    private String excise;

}
