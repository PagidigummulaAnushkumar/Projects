
package com.crio.warmup.stock.optional;

import com.crio.warmup.stock.PortfolioManagerApplication;
import com.crio.warmup.stock.dto.PortfolioTrade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


class ModuleTwoTest {

  @Test
  void readStockFromJson() throws Exception {
    //given
    String filename = "assessments/trades.json";
    List<String> expected = Arrays.asList(new String[]{"MSFT", "CSCO", "CTS"});

    //when
    List<PortfolioTrade> trades = PortfolioManagerApplication
        .readTradesFromJson(filename);
    List<String> actual = trades.stream().map(PortfolioTrade::getSymbol).collect(Collectors.toList());

    //then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void prepareUrl() throws Exception {
    //given
    PortfolioTrade trade = new PortfolioTrade();
    trade.setPurchaseDate(LocalDate.parse("2021-01-01"));
    trade.setSymbol("aapl");
    String token = "675a478a549d0aaf46c228675042d4562e2bc1e4";
    //when
    String tiingoUrl = PortfolioManagerApplication
            .prepareUrl(trade, LocalDate.parse("2021-11-19"), token);

    //then
    String uri = "https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2021-01-01&endDate=2021-11-19&token=675a478a549d0aaf46c228675042d4562e2bc1e4";

    Assertions.assertEquals(tiingoUrl, uri);
  }


}
