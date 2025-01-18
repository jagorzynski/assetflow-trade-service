package com.sothrose.assetflow_trade_service.exception;

public class TradeRequestDtoValidationError extends RuntimeException {
  public TradeRequestDtoValidationError(String message) {
    super(message);
  }
}
