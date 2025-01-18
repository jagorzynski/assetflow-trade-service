package com.sothrose.assetflow_trade_service.exception;

public class MarketDataValidationError extends RuntimeException {
  public MarketDataValidationError(String message) {
    super(message);
  }
}
