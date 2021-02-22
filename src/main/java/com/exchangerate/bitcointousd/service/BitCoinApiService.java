package com.exchangerate.bitcointousd.service;

import com.exchangerate.bitcointousd.model.UsdRate;

public interface BitCoinApiService {

    UsdRate getUsdRate();
}
