package com.sothrose.assetflow_trade_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "trades")
public class Trade {
  @Id private String id;
  private Long userId;
  private String assetToPay;
  private BigDecimal amountToPay;
  private String assetToBuy;
  private BigDecimal amountBought;
  private BigDecimal price;
  private AssetType assetType;
  private String exchangeName;
  private LocalDateTime transactionTime;

  public TradeDto toTradeDto() {
    return TradeDto.builder()
        .id(id)
        .userId(userId)
        .amountToPay(amountToPay)
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
