package com.exchangerate.bitcointousd.controller;

import com.exchangerate.bitcointousd.dto.UsdRateDto;
import com.exchangerate.bitcointousd.model.UsdRate;
import com.exchangerate.bitcointousd.service.UsdRateService;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/bitcoin")
public class UsdRateController {

    private UsdRateService usdRateService;
    private ModelMapper modelMapper;

    public UsdRateController(UsdRateService usdRateService, ModelMapper modelMapper) {
        this.usdRateService = usdRateService;
        this.modelMapper = modelMapper;
    }

    /**
     * find latest bitcoin-usd Rate
     *
     * @return usd-rate value
     */
    @GetMapping("/USD")
    public UsdRateDto getLatestRate() {
        return toDTO(usdRateService.getLatestRate(), UsdRateDto.class);
    }

    /**
     * find bitcoin-usd Rates between given dates
     *
     * @return list of usd-rate values in given range
     */
    @GetMapping("/USD/byDateRange")
    public List<UsdRateDto> getHistoricalRates(@RequestParam(value = "startDate")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                            @RequestParam("endDate")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return usdRateService.getRatesByDateRange(start, end)
                .stream()
                .map(model -> toDTO(model, UsdRateDto.class))
                .collect(Collectors.toList());
    }

    /**
     * convert entity object to Dto representation
     *
     * note: This method not moved to utility class to follow principle:
     * Eliminate a class: If a class isn’t doing much, move its code into other classes that are more cohesive
     * and eliminate the class. (Code Complete, Steve McConnell)
     * @return Dto Object
     */
    private <T> T toDTO(UsdRate usdRate, Class<T> classType) {
        return modelMapper.map(usdRate, classType);
    }

}
