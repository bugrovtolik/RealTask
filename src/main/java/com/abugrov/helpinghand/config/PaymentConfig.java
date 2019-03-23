package com.abugrov.helpinghand.config;

import com.abugrov.helpinghand.domain.dto.PaymentResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentConfig {
    @Value("${liqpay.public_key}")
    private String publicKey;
    @Value("${liqpay.private_key}")
    private String privateKey;
    @Value("${hostname}")
    private String host;

    private static final ObjectReader OBJECT_READER = new ObjectMapper().readerFor(PaymentResponseDto.class);

    public String getHref(String userId, String taskId, String amount, String description) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", "3.01");
        params.put("currency", "UAH");
        params.put("description", description);
        params.put("order_id", userId + "_" + taskId);
        params.put("sandbox", "1");
        params.put("public_key", publicKey);
        params.put("version", LiqPay.API_VERSION);
        params.put("result_url", host + "/task/" + taskId + "/paid");

        String data = getData(params);
        String signature = getSignature(data);

        return "https://liqpay.com/api/3/checkout?data=" + data + "&signature=" + signature;
    }

    public PaymentResponseDto read(String data) throws IOException {
        return OBJECT_READER.readValue(Base64.getDecoder().decode(data));
    }

    public String getStatus(String userId, String taskId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("action", "status");
        params.put("version", LiqPay.API_VERSION);
        params.put("order_id", userId + "_" + taskId);

        LiqPay liqpay = new LiqPay(publicKey, privateKey);
        Map<String, Object> res = liqpay.api("request", params);
        return (String) res.get("status");
    }

    private String getSignature(String data) {
        return LiqPayUtil.base64_encode(LiqPayUtil.sha1(privateKey + data + privateKey));
    }

    private String getData(Map<String, String> params) {
        return LiqPayUtil.base64_encode(JSONObject.toJSONString(params));
    }

    public boolean isValidSignature(String data, String signature) {
        return signature.equals(getSignature(data));
    }
}
