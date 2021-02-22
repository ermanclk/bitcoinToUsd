package com.exchangerate.bitcointousd.service;

import com.exchangerate.bitcointousd.model.UsdRate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface UsdRateService {

    UsdRate getLatestRate();

    List<UsdRate> getRatesByDateRange(LocalDateTime from, LocalDateTime to);
}
