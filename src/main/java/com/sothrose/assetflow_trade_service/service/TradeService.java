package com.sothrose.assetflow_trade_service.service;

import static com.sothrose.assetflow_trade_service.model.ProcessingStatus.SUCCESS;
import static java.lang.String.format;
import static java.lang.String.join;

import com.sothrose.assetflow_trade_service.client.MarketDataServiceClient;
import com.sothrose.assetflow_trade_service.exception.MarketDataValidationError;
import com.sothrose.assetflow_trade_service.exception.TradeRequestDtoValidationError;
import com.sothrose.assetflow_trade_service.model.*;
import com.sothrose.assetflow_trade_service.repository.TradeRepository;
import com.sothrose.assetflow_trade_service.validator.MarketDataValidator;
import com.sothrose.assetflow_trade_service.validator.TradeRequestDtoValidator;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TradeService {
  public static final String DELIMITER = ", ";

  @Value("${kafka.topic.trade-created}")
  private String tradeCreatedTopic;

  private final TradeRepository tradeRepository;
  private final MarketDataServiceClient marketDataServiceClient;
  private final TradeRequestDtoValidator tradeRequestDtoValidator;
  private final MarketDataValidator marketDataValidator;
  private final KafkaTemplate<String, TradeCreatedEvent> kafkaTemplate;

  public ProcessingStatus processTrade(TradeRequestDto tradeRequestDto) {
    var tradeRequestDtoValidationErrors =
        tradeRequestDtoValidator.validateTradeRequestDto(tradeRequestDto);

    if (!tradeRequestDtoValidationErrors.isEmpty()) {
      var validationErrors = join(DELIMITER, tradeRequestDtoValidationErrors);
      log.error("TradeRequestDto contains validation errors: [{}]", validationErrors);
      throw new TradeRequestDtoValidationError(
          format("TradeRequestDto contains validation errors: [%s]", validationErrors));
    }

    var marketData =
        marketDataServiceClient.fetchSingleCryptoPrice(
            tradeRequestDto.getAssetToBuy(), tradeRequestDto.getAssetToPay());
    var marketDataValidationErrors = marketDataValidator.validateMarketData(marketData);

    if (!marketDataValidationErrors.isEmpty()) {
      var validationErrors = join(DELIMITER, tradeRequestDtoValidationErrors);
      log.error("MarketData contains validation errors: [{}]", validationErrors);
      throw new MarketDataValidationError(
          format("MarketData contains validation errors: [%s]", validationErrors));
    }

    var quantityFrom = tradeRequestDto.getAmountToPay();
    var price = marketData.getPrice();
    var quantityTo = quantityFrom.divide(price, 8, RoundingMode.HALF_UP);
    var tradeCreatedEvent = tradeRequestDto.toTradeCreatedEvent(quantityTo);

    var trade = tradeCreatedEvent.toTrade(price, marketData.getTimestamp());
    tradeRepository.save(trade);
    kafkaTemplate.send(tradeCreatedTopic, tradeCreatedEvent);
    return SUCCESS;
  }

  public List<TradeDto> fetchAllTradesForUser(Long userId) {
    return tradeRepository.findAllByUserId(userId).stream().map(Trade::toTradeDto).toList();
  }
}
