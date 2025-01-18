package com.sothrose.assetflow_trade_service.validator;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;

import com.sothrose.assetflow_trade_service.model.TradeRequestDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TradeRequestDtoValidator {

  public List<String> validateTradeRequestDto(TradeRequestDto tradeRequestDto) {
    List<String> validationErrors = newArrayList();

    if (isNull(tradeRequestDto)) {
      validationErrors.add("TradeRequestDto cannot be null");
    }

    if (isNull(tradeRequestDto.getUserId())) {
      validationErrors.add("UserId cannot be null");
    }

    if (isNullOrEmpty(tradeRequestDto.getAssetToPay())) {
      validationErrors.add("AssetToPay cannot be null or empty");
    }

    if (isNull(tradeRequestDto.getAmountToPay())
        || tradeRequestDto.getAmountToPay().compareTo(ZERO) <= 0) {
      validationErrors.add("AmountToPay cannot be null or less or equal to 0");
    }

    if (isNullOrEmpty(tradeRequestDto.getAssetToBuy())) {
      validationErrors.add("AssetToBuy cannot be null or empty");
    }

    if (isNullOrEmpty(tradeRequestDto.getExchangeName())) {
      validationErrors.add("ExchangeName cannot be null or empty");
    }

    return validationErrors;
  }
}
