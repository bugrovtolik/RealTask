package com.abugrov.realtask.domain.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class PBPayment {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private Integer state;

    @XmlAttribute
    private String message;

    @XmlAttribute
    private String ref;

    @XmlAttribute
    private Double amt;

    @XmlAttribute
    private String ccy;

    @XmlAttribute
    private Double comis;

    @XmlElement
    private List<PBProp> prop = new ArrayList<>();

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Double getComis() {
        return comis;
    }

    public void setComis(Double comis) {
        this.comis = comis;
    }

    public List<PBProp> getProp() {
        return prop;
    }

    public void setProp(List<PBProp> prop) {
        this.prop = prop;
    }

    public void addProp(PBProp prop) {
        this.prop.add(prop);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
