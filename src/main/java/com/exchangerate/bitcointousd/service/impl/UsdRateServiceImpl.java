package com.exchangerate.bitcointousd.service.impl;

import com.exchangerate.bitcointousd.exception.BitCoinToUsdApiException;
import com.exchangerate.bitcointousd.model.UsdRate;
import com.exchangerate.bitcointousd.repository.UsdRateRepository;
import com.exchangerate.bitcointousd.service.BitCoinApiService;
import com.exchangerate.bitcointousd.service.UsdRateService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsdRateServiceImpl implements UsdRateService {

    private UsdRateRepository usdRateRepository;
    private BitCoinApiService bitCoinApiService;

    public UsdRateServiceImpl(UsdRateRepository usdRateRepository, BitCoinApiService bitCoinApiService) {
        this.usdRateRepository = usdRateRepository;
        this.bitCoinApiService = bitCoinApiService;
    }

    @HystrixCommand(
            fallbackMethod = "fallbackGetLatestRate",
            commandKey = "getLatestRateCommand")
    @Override
    public UsdRate getLatestRate() {
        return bitCoinApiService.getUsdRate();
    }

    public UsdRate fallbackGetLatestRate(){
        return usdRateRepository.findTopByOrderByTimeDesc().orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<UsdRate> getRatesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate.isAfter(endDate)) {
            throw new BitCoinToUsdApiException("Start date cannot be after End date");
        }
        return usdRateRepository.findAllByTimeBetween(startDate,endDate);
    }


}
