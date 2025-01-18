package com.sothrose.assetflow_trade_service.model;

import java.math.BigDecimal;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TradeRequestDto {
  private Long userId;
  private String assetToPay;
  private BigDecimal amountToPay;
  private String assetToBuy;
  private AssetType assetType;
  private String exchangeName;

  public TradeDto toTradeDto(BigDecimal quantity) {
    return TradeDto.builder()
        .userId(userId)
        .assetToPay(assetToPay)
        .amountToPay(amountToPay)
        .assetToBuy(assetToBuy)
        .amountBought(quantity)
        .assetType(assetType)
        .exchangeName(exchangeName)
        .build();
  }

  public TradeCreatedEvent toTradeCreatedEvent(BigDecimal quantity) {
    return TradeCreatedEvent.builder()
        .userId(userId)
        .assetToPay(assetToPay)
        .amountToPay(amountToPay)
        .assetToBuy(assetToBuy)
        .amountBought(quantity)
        .assetType(assetType)
        .exchangeName(exchangeName)
        .build();
  }
}
