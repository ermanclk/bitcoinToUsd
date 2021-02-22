package com.exchangerate.bitcointousd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class UsdRate {

    @Id
    private String id;
    private String rate;
    private LocalDateTime time;

    public UsdRate() {
    }

    public UsdRate(String rate, LocalDateTime time) {
        this.rate = rate;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UsdRate{" +
                " rate='" + rate + '\'' +
                ", time=" + time +
                '}';
    }


}
