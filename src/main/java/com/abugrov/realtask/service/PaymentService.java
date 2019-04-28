package com.abugrov.realtask.service;

import com.abugrov.realtask.domain.User;
import com.abugrov.realtask.domain.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    @Value("${liqpay.public_key}")
    private String publicKey;
    @Value("${liqpay.private_key}")
    private String privateKey;
    @Value("${hostname}")
    private String host;
    @Value("${p24.merchant_id}")
    private Long merchantId;
    @Value("${p24.merchant_password}")
    private String merchantPassword;

    private final RestTemplate restTemplate;

    private final static String TO_CARD_URL = "https://api.privatbank.ua/p24api/pay_pb";
    private final static String TO_PHONE_URL = "https://api.privatbank.ua/p24api/directfill";
    private final static ObjectReader OBJECT_READER = new ObjectMapper().readerFor(LiqPayResponseDto.class);

    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHref(User user, Integer amount) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("currency", "UAH");
        params.put("amount", amount.toString());
        params.put("description", "RealTaskPayment");
        params.put("order_id", UUID.randomUUID().toString());
        params.put("sandbox", "1");
        params.put("public_key", publicKey);
        params.put("version", LiqPay.API_VERSION);
        params.put("server_url", host + "/user/" + user.getId() + "/paid");
        params.put("product_url", host + "/user/" + user.getId() + "/profile");

        String data = getLiqPayData(params);
        String signature = getLiqPaySign(data);

        return "https://liqpay.ua/api/3/checkout?data=" + data + "&signature=" + signature;
    }

    public boolean transferToPhone(User user, Integer amount) throws Exception {
        JAXBContext context = JAXBContext.newInstance(PrivatBankRequestDto.class);
        Marshaller marshaller = context.createMarshaller();

        PBProp phone = new PBProp();
        phone.setName("phone");
        phone.setValue("+38" + user.getPhoneNumber());
        PBProp amt = new PBProp();
        amt.setName("amt");
        amt.setValue(amount.toString());

        PBPayment payment = new PBPayment();
        payment.addProp(phone);
        payment.addProp(amt);

        PBData data = new PBData();
        data.setOper("cmt");
        data.setWait(90);
        data.setTest(1);
        data.setPayment(payment);

        JAXBContext dataContext = JAXBContext.newInstance(PBData.class);
        Marshaller dataMarshaller = dataContext.createMarshaller();

        StringWriter datasw = new StringWriter();
        dataMarshaller.marshal(data, datasw);

        String dataXml = datasw.toString();
        dataXml = dataXml.substring(61, dataXml.length() - 7);
        System.out.println("dataXml: " + dataXml);

        PBMerchant merchant = new PBMerchant();
        merchant.setId(merchantId);
        merchant.setSignature(getPBSign(dataXml));

        PrivatBankRequestDto requestDto = new PrivatBankRequestDto();
        requestDto.setMerchant(merchant);
        requestDto.setData(data);

        StringWriter sw = new StringWriter();
        marshaller.marshal(requestDto, sw);

        String response = restTemplate.postForObject(TO_PHONE_URL, requestDto, String.class);

        System.out.println("response: " + response);
        //System.out.println(response != null ? response.getData().getPayment().getState() : "was null");

        return false;
    }

    public boolean transferToPrivatCard(User user, Integer amount) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(PrivatBankRequestDto.class);
        Marshaller marshaller = context.createMarshaller();

        PBProp card = new PBProp();
        card.setName("b_card_or_acc");
        card.setValue(user.getCreditCardNumber());
        PBProp amt = new PBProp();
        amt.setName("amt");
        amt.setValue(amount.toString());
        PBProp ccy = new PBProp();
        ccy.setName("ccy");
        ccy.setValue("UAH");
        PBProp details = new PBProp();
        details.setName("details");
        details.setValue("HelpingHandPayment");

        PBPayment payment = new PBPayment();
        payment.addProp(card);
        payment.addProp(amt);
        payment.addProp(ccy);
        payment.addProp(details);

        PBData data = new PBData();
        data.setOper("cmt");
        data.setWait(90);
        data.setTest(1);
        data.setPayment(payment);

        JAXBContext dataContext = JAXBContext.newInstance(PBData.class);
        Marshaller dataMarshaller = dataContext.createMarshaller();

        StringWriter datasw = new StringWriter();
        dataMarshaller.marshal(data, datasw);

        String dataXml = datasw.toString();
        dataXml = dataXml.substring(61, dataXml.length() - 7);
        System.out.println("dataXml: " + dataXml);

        PBMerchant merchant = new PBMerchant();
        merchant.setId(merchantId);
        merchant.setSignature(getPBSign(dataXml));

        PrivatBankRequestDto requestDto = new PrivatBankRequestDto();
        requestDto.setMerchant(merchant);
        requestDto.setData(data);

        StringWriter sw = new StringWriter();
        marshaller.marshal(requestDto, sw);

        System.out.println(sw.toString());
        PrivatBankResponseDto response = restTemplate.postForObject(TO_CARD_URL, requestDto, PrivatBankResponseDto.class);

        System.out.println("requestXml: " + response);
        System.out.println(response != null ? response.getData().getPayment().getState() : "was null");

        return false;
    }

    private String getLiqPaySign(String data) {
        return LiqPayUtil.base64_encode(LiqPayUtil.sha1(privateKey + data + privateKey));
    }

    private String getPBSign(String data) {
        return DigestUtils.sha1Hex(DigestUtils.md5Hex(data + merchantPassword));
    }

    private String getLiqPayData(Map<String, String> params) {
        return LiqPayUtil.base64_encode(JSONObject.toJSONString(params));
    }

    public LiqPayResponseDto read(String data) throws IOException {
        return OBJECT_READER.readValue(Base64.getDecoder().decode(data));
    }

    public boolean isValidSignature(String data, String signature) {
        return signature.equals(getLiqPaySign(data));
    }
}
