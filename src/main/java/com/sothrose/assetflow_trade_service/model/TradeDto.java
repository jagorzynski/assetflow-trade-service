package com.sothrose.assetflow_trade_service.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TradeDto {
  private String id;
  private Long userId;
  private String assetToPay;
  private BigDecimal amountToPay;
  private String assetToBuy;
  private BigDecimal amountBought;
  private BigDecimal price;
  private AssetType assetType;
  private String exchangeName;
  private LocalDateTime transactionTime;

  public Trade toTrade(BigDecimal price, LocalDateTime transactionTime) {
    return Trade.builder()
        .userId(userId)
        .assetToPay(assetToPay)
        .amountToPay(amountToPay)
        .assetToBuy(assetToBuy)
        .amountBought(amountBought)
        .price(price)
        .assetType(assetType)
        .exchangeName(exchangeName)
        .transactionTime(transactionTime)
        .build();
  }
}
