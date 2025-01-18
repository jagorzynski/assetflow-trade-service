package com.sothrose.assetflow_trade_service.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.sothrose.assetflow_trade_service.model.ProcessingStatus;
import com.sothrose.assetflow_trade_service.model.TradeDto;
import com.sothrose.assetflow_trade_service.model.TradeRequestDto;
import com.sothrose.assetflow_trade_service.service.TradeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/assetflow/trades")
public class TradeController {

  private final TradeService tradeService;

  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ProcessingStatus processTrade(@RequestBody TradeRequestDto tradeRequestDto) {
    return tradeService.processTrade(tradeRequestDto);
  }

  @GetMapping(path = "/{userId}", produces = APPLICATION_JSON_VALUE)
  public List<TradeDto> getAllTradesForUser(@PathVariable Long userId) {
    return tradeService.fetchAllTradesForUser(userId);
  }
}
