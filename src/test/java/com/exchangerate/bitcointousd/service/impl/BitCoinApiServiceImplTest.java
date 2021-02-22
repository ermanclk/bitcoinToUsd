package com.exchangerate.bitcointousd.service.impl;

import com.exchangerate.bitcointousd.model.UsdRate;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BitCoinApiServiceImplTest {

    private RestTemplate restTemplate;
    private BitCoinApiServiceImpl classUnderTest;
    private final String TEST_RESPONSE_FILE = "test_response.json";
    private final String TEST_INVALID_RESPONSE_FILE = "test_response_fail.json";
    private final String TEST_RATE_IN_RESPONSE = "57,000.1000";
    private final String DUMMY_URL = "test";
    private final String SERVICE_URL = "SERVICE_URL";
    ResponseEntity<String> mockResponse;

    @BeforeEach
    public void setUp(){

        mockResponse = mock(ResponseEntity.class);
        restTemplate = mock(RestTemplate.class);

        when(restTemplate.getForEntity( DUMMY_URL,String.class)).thenReturn(mockResponse);

        classUnderTest = spy(new BitCoinApiServiceImpl(restTemplate));
        ReflectionTestUtils.setField(classUnderTest, SERVICE_URL, DUMMY_URL);

    }

    @Test
    @DisplayName("GetUsdRate calls rest api")
    void givenInvalidResponseWhenGetUsdRateThenThrowException() throws IOException {
        String success_response = new String(getClass().getClassLoader().getResourceAsStream(TEST_RESPONSE_FILE).readAllBytes());
        when(mockResponse.getBody()).thenReturn(success_response);

        UsdRate actual = classUnderTest.getUsdRate();

        assertThat(actual.getRate(), is(equalTo(TEST_RATE_IN_RESPONSE)));
    }

    @Test
    @DisplayName("GetUsdRate fails to parse response")
    void whenGetUsdRateThenReturnRemoteResponse() throws IOException {
        String invalidResponse = new String(getClass().getClassLoader().getResourceAsStream(TEST_INVALID_RESPONSE_FILE).readAllBytes());
        when(mockResponse.getBody()).thenReturn(invalidResponse);

        Throwable throwable =  assertThrows(Throwable.class, () -> {
            classUnderTest.getUsdRate();
        });

        assertThat(throwable.getClass(), is(equalTo(JSONException.class)));
    }
}