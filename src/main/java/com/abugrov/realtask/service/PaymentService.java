package com.abugrov.realtask.service;

import com.abugrov.realtask.model.User;
import com.abugrov.realtask.model.dto.LiqPayResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    private final RestTemplate restTemplate;

    private final static String W4P_URL = "https://api.fondy.eu/api/p2pcredit/";
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

    private String getLiqPaySign(String data) {
        return LiqPayUtil.base64_encode(LiqPayUtil.sha1(privateKey + data + privateKey));
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
