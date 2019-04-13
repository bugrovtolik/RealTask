package com.abugrov.helpinghand.domain.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrivatBankResponseDto {
    @XmlElement
    private PBMerchant merchant;

    @XmlElement
    private PBData data;

    public PBMerchant getMerchant() {
        return merchant;
    }

    public void setMerchant(PBMerchant merchant) {
        this.merchant = merchant;
    }

    public PBData getData() {
        return data;
    }

    public void setData(PBData data) {
        this.data = data;
    }
}
