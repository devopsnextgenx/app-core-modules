package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    /**
     * Email with create IDP account if not exist
     */
    APPKEY("APP"),
    /**
     * Federated users belongs to external identity provider so no AppKey account is needed.
     */
    EXTERNAL("CES_APP");

    private final String appkeyValue;

    EmailTemplate(String value) {
        this.appkeyValue = value;
    }
}
