package com.sothrose.assetflow_trade_service.repository;

import com.sothrose.assetflow_trade_service.model.Trade;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends MongoRepository<Trade, String> {
  List<Trade> findAllByUserId(Long userId);
}
