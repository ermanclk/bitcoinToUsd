package com.exchangerate.bitcointousd.service.impl;

import com.exchangerate.bitcointousd.exception.BitCoinToUsdApiException;
import com.exchangerate.bitcointousd.model.UsdRate;
import com.exchangerate.bitcointousd.repository.UsdRateRepository;
import com.exchangerate.bitcointousd.service.BitCoinApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class UsdRateServiceImplTest {

    private UsdRateRepository usdRateRepository;
    private BitCoinApiService bitCoinApiService;
    private UsdRateServiceImpl classUnderTest;
    private LocalDateTime startDummy = LocalDateTime.of(2021, 01, 01, 00, 00);
    private LocalDateTime endDummy = LocalDateTime.of(2022, 01, 01, 00, 00);
    private UsdRate dummyRateRemoteApi;
    private Optional<UsdRate> dummyRateDb;

    @BeforeEach
    public void setUp() {
        usdRateRepository = mock(UsdRateRepository.class);
        bitCoinApiService = mock(BitCoinApiService.class);
        String dummyRate = "10,000.000";
        dummyRateRemoteApi = new UsdRate(dummyRate,startDummy);
        dummyRateDb = Optional.of(new UsdRate(dummyRate, startDummy));

        classUnderTest = spy(new UsdRateServiceImpl(usdRateRepository, bitCoinApiService));

        when(bitCoinApiService.getUsdRate()).thenReturn(dummyRateRemoteApi);
        when(usdRateRepository.findTopByOrderByTimeDesc()).thenReturn(dummyRateDb);
    }

    @Test
    @DisplayName("getLatestRate return from remote service")
    void whenGetLatestRateThenCallRemoteService() {
        //Act
        UsdRate actual = classUnderTest.getLatestRate();
        //Assert
        assertThat(actual, is(equalTo(dummyRateRemoteApi)));
        verify(bitCoinApiService, times(1)).getUsdRate();
    }

    @Test
    @DisplayName("fallbackGetLatestRate return from repository")
    void whenFallbackGetLatestRateThenReturnFromRepository() {
        //Act
        UsdRate actual = classUnderTest.fallbackGetLatestRate();
        //Assert
        assertThat(actual, is(equalTo(dummyRateDb.get())));
        verify(usdRateRepository, times(1)).findTopByOrderByTimeDesc();
    }

    @Test
    @DisplayName("getRatesByDateRange returns from repository")
    void whenGetRatesByDateRangeThenReturnRateList() {
        //Arrange
        List<UsdRate> rates = new ArrayList<>();
        rates.add(dummyRateRemoteApi);
        rates.add(dummyRateRemoteApi);
        rates.add(dummyRateRemoteApi);
        when(usdRateRepository.findAllByTimeBetween(startDummy,endDummy)).thenReturn(rates);

        //Act
        List<UsdRate> actualList = classUnderTest.getRatesByDateRange(startDummy,endDummy);
        //Assert
        assertThat(actualList, is(equalTo(rates)));
        verify(usdRateRepository, times(1)).findAllByTimeBetween(startDummy,endDummy);
    }

    @Test
    @DisplayName("getRatesByDateRange with Start date later than end date should throw exception")
    void whenInvalidStartEndDateThenReturnThrowException() {
        //Act
        Throwable throwable =  assertThrows(Throwable.class, () -> {
            classUnderTest.getRatesByDateRange(endDummy,startDummy);
        });
        //Assert
        assertThat(throwable.getClass(), is(equalTo(BitCoinToUsdApiException.class)));
        verify(usdRateRepository, never()).findAllByTimeBetween(startDummy,endDummy);
    }

}