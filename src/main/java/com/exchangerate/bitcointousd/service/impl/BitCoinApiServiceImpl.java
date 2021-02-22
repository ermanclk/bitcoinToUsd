package com.exchangerate.bitcointousd.service.impl;

import com.exchangerate.bitcointousd.model.UsdRate;
import com.exchangerate.bitcointousd.service.BitCoinApiService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BitCoinApiServiceImpl implements BitCoinApiService {

    Logger logger = LoggerFactory.getLogger(BitCoinApiServiceImpl.class);

    private static final String JSON_BPI = "bpi";
    private static final String JSON_USD = "USD";
    private static final String JSON_RATE = "rate";
    private static final String JSON_TIME = "time";
    private static final String JSON_UPDATED_ISO = "updatedISO";

    private final RestTemplate restTemplate;

    @Value("${bitcoin.to.usd.server.url}")
    private String SERVICE_URL;

    public BitCoinApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsdRate getUsdRate() {
        logger.info("Sending request to get bitcoin-usd rate data.");
        ResponseEntity<String> response = restTemplate.getForEntity(SERVICE_URL, String.class);
        return new UsdRate(parseUsdRate(response), parseTime(response));
    }

    private String parseUsdRate(ResponseEntity<String> response) {
        return new JSONObject(response.getBody()).getJSONObject(JSON_BPI).getJSONObject(JSON_USD).getString(JSON_RATE);
    }

    private LocalDateTime parseTime(ResponseEntity<String> response) {
        String currencyUpdateTime = new JSONObject(response.getBody()).getJSONObject(JSON_TIME).getString(JSON_UPDATED_ISO);
        return LocalDateTime.parse(currencyUpdateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

}
