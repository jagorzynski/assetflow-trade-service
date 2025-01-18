package com.sothrose.assetflow_trade_service.client;

import com.sothrose.assetflow_trade_service.model.MarketData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class MarketDataServiceClient {

  public static final String MARKET_DATA_SINGLE_CRYPTO_PRICE_PATH = "/v1/assetflow/marketdata";
  public static final String SYMBOL = "symbol";
  public static final String CURRENCY = "currency";
  private final WebClient marketDataServiceWebClient;

  public MarketData fetchSingleCryptoPrice(String assetToBuy, String assetToPay) {
    return marketDataServiceWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(MARKET_DATA_SINGLE_CRYPTO_PRICE_PATH)
                    .queryParam(SYMBOL, assetToBuy)
                    .queryParam(CURRENCY, assetToPay)
                    .build())
        .retrieve()
        .bodyToMono(MarketData.class)
        .block();
  }
}
