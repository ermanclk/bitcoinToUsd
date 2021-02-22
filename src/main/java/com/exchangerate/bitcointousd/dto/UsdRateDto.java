package com.exchangerate.bitcointousd.dto;

import java.io.Serializable;

public class UsdRateDto  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rate;
    private String time;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UsdRateDto{" +
                "rate='" + rate + '\'' +
                ", time=" + time +
                '}';
    }
}
