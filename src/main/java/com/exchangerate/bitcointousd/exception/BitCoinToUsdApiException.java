package com.exchangerate.bitcointousd.exception;

public class BitCoinToUsdApiException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public BitCoinToUsdApiException(String message) {
        super(message);
    }
}
