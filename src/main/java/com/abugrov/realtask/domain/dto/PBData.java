package com.abugrov.realtask.domain.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class PBData {
    @XmlElement
    private String oper;

    @XmlElement
    private PBPayment payment;

    @XmlElement
    private Integer wait;

    @XmlElement
    private Integer test;

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public PBPayment getPayment() {
        return payment;
    }

    public void setPayment(PBPayment payment) {
        this.payment = payment;
    }

    public Integer getWait() {
        return wait;
    }

    public void setWait(Integer wait) {
        this.wait = wait;
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }
}
