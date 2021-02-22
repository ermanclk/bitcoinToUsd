package com.exchangerate.bitcointousd.scheduler;

import com.exchangerate.bitcointousd.model.UsdRate;
import com.exchangerate.bitcointousd.repository.UsdRateRepository;
import com.exchangerate.bitcointousd.service.BitCoinApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UsdRateCheckScheduler {

    Logger logger = LoggerFactory.getLogger(UsdRateCheckScheduler.class);

    private UsdRateRepository usdRateRepository;
    private BitCoinApiService bitCoinApiService;

    @Value("${schedule.rate_checker.period.in.milliseconds}")
    private String SCHEDULE_PERIOD;

    private long ONE_MINUTE_IN_MILLISECONDS = TimeUnit.MINUTES.toMillis(1);

    public UsdRateCheckScheduler(UsdRateRepository usdRateRepository, BitCoinApiService bitCoinApiService) {
        this.usdRateRepository = usdRateRepository;
        this.bitCoinApiService = bitCoinApiService;
    }

    /**
     * Gets current usd rate value from remote service and saves into database.
     * Method run period is scheduled by configuration, if it is set to less than a minute, then application uses
     * default value ONE_MINUTE_IN_MILLISECONDS for minimum period.
     */
    @Scheduled(fixedRateString = "#{@getSchedulePeriod}")
    public void checkCurrentUsdRate() {
        UsdRate newRate = bitCoinApiService.getUsdRate();
        usdRateRepository.save(newRate);
        logger.info("usd rate saved:{}", newRate);
    }

    /**
     * Schedule period parameter is configurable, but if configured less than Minimum Available Value then
     * sets to Minimum Available Value
     * @return schedule period in milliseconds
     */
    @Bean
    public String getSchedulePeriod() {

        if (Long.valueOf(SCHEDULE_PERIOD) < ONE_MINUTE_IN_MILLISECONDS) {
            logger.warn("Schedule Configuration: {} is less than minimum, it is set to {} minute.", SCHEDULE_PERIOD,
                    ONE_MINUTE_IN_MILLISECONDS);
            return String.valueOf(ONE_MINUTE_IN_MILLISECONDS);
        }

        return SCHEDULE_PERIOD;
    }
}
