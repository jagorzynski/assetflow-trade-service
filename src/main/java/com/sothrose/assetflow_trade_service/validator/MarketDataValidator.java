package com.sothrose.assetflow_trade_service.validator;

import static com.google.common.collect.Lists.newArrayList;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;

import com.sothrose.assetflow_trade_service.model.MarketData;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MarketDataValidator {
  public List<String> validateMarketData(MarketData marketData) {
    List<String> validationErrors = newArrayList();

    if (isNull(marketData)) {
      validationErrors.add("MarketData cannot be null");
    }

    if (isNull(marketData.getPrice()) || marketData.getPrice().compareTo(ZERO) <= 0) {
      validationErrors.add("Price cannot be null or less or equal to 0");
    }

    if (isNull(marketData.getTimestamp()) || marketData.getTimestamp().isAfter(now())) {
      validationErrors.add("Timestamp cannot be null or represent feature");
    }

    return validationErrors;
  }
}
