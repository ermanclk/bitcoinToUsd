package com.exchangerate.bitcointousd.repository;

import com.exchangerate.bitcointousd.model.UsdRate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsdRateRepository extends MongoRepository<UsdRate, String> {

     Optional<UsdRate> findTopByOrderByTimeDesc();

     List<UsdRate> findAllByTimeBetween(LocalDateTime start, LocalDateTime end);
}
