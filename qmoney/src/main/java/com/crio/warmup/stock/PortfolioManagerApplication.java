
package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.UUID;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerApplication {

  public static List<String> mainReadFile(String[]  args)throws  IOException, URISyntaxException {
    File filepath = resolveFileFromResources(args[0]);
    ObjectMapper objectmapper = getObjectMapper();
    PortfolioTrade[] portfolioTrade = objectmapper.readValue(filepath, PortfolioTrade[].class);
    List<String> resultList = new ArrayList<>();
    for (PortfolioTrade trade : portfolioTrade) {
     resultList .add(trade.getSymbol());
    }
    return resultList;
  }



  // TODO: CRIO_TASK_MODULE_REST_API
  // Find out the closing price of each stock on the end_date and return the list
  // of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  // and deserialize the results in List<Candle>

  private static void printJsonObject(Object object) throws IOException,  URISyntaxException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));

  }

  

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
 




  public static List<String> debugOutputs() {

    String valueOfArgument0 = "trades.json";
    String resultOfResolveFilePathArgs0 = "";
    String toStringOfObjectMapper = "";
    String functionNameFromTestFileInStackTrace = "";
    String lineNumberFromTestFileInStackTrace = "";


   return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
       toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
       lineNumberFromTestFileInStackTrace});
 }

//  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
//    ObjectMapper objectMapper = getObjectMapper();
//    List<PortfolioTrade> trades = Arrays.asList(objectMapper.readValue(resolveFileFromResources(args[0]), PortfolioTrade[].class));

//    List<TotalReturnsDto> sortedByValue = mainReadQuotesHelper(args, trades);
//    Collections.sort(sortedByValue , new Comparator<TotalReturnsDto>() {
//       public int compare(TotalReturnsDto t1 , TotalReturnsDto t2){
//         return (int) (t1.getClosingPrice().compareTo(t2.getClosingPrice()));
//       }    
//    });
//    List<String> stocks = new ArrayList<String>();

//    for(TotalReturnsDto trd : sortedByValue){
//      stocks.add(trd.getSymbol());
//    }
//    return stocks;
//  }

//  private static List<TotalReturnsDto> mainReadQuotesHelper(String[] args, List<PortfolioTrade> trades) {
//   RestTemplate restTemplate = new RestTemplate();
//   String token = "675a478a549d0aaf46c228675042d4562e2bc1e4";
//   String endDate = args[1];
//   List<TotalReturnsDto> tests= new ArrayList<TotalReturnsDto>();
//   LocalDate localDate = LocalDate.parse(endDate);
//   for(PortfolioTrade t : trades){
//     String uri = prepareUrl(t, localDate, token);
//     TiingoCandle[] results = restTemplate.getForObject(uri, TiingoCandle[].class);

//     if(results != null){
//       tests.add(new TotalReturnsDto(t.getSymbol(), results[results.length - 1].getClose()));
//     }
//   }
//   return tests;
// }



  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
  List<PortfolioTrade> portfolioTrades = readTradesFromJson(args[0]);
    List<TotalReturnsDto> totalReturnsDtos = new ArrayList<TotalReturnsDto>();
    
    List<String> resultantList = new ArrayList<>();
    
    for(PortfolioTrade portfolioTrade : portfolioTrades){
      String date = args[1];
      LocalDate localDate = LocalDate.parse(date);
      totalReturnsDtos.add(fetchTiingo(portfolioTrade,localDate));
    }

   Collections.sort(totalReturnsDtos, new Comparator<TotalReturnsDto>() {
   @Override
    public int compare(TotalReturnsDto totalReturnsDto,
                       TotalReturnsDto totalReturnsDto1)
     {
      //  if(totalReturnsDto.getClosingPrice() > totalReturnsDto1.getClosingPrice()) return 1;
      //  if(totalReturnsDto.getClosingPrice() < totalReturnsDto1.getClosingPrice()) return -1;
      //  return 0;
      return (int) (totalReturnsDto.getClosingPrice().compareTo(totalReturnsDto1.getClosingPrice()));
     }
  });

  for(TotalReturnsDto totalReturnsDto : totalReturnsDtos) {
    resultantList.add(totalReturnsDto.getSymbol());
  }
  return resultantList;
}
    
 

 public static TotalReturnsDto fetchTiingo(PortfolioTrade portfolioTrade, LocalDate localDate) {
  String url = prepareUrl(portfolioTrade, localDate,"675a478a549d0aaf46c228675042d4562e2bc1e4");
   TiingoCandle[] tiingoCandle = new RestTemplate().getForObject(url, TiingoCandle[].class);
  
    return new TotalReturnsDto(portfolioTrade.getSymbol(), tiingoCandle[tiingoCandle.length - 1].getClose());
      
  
}
 



  // TODO:
  // After refactor, make sure that the tests pass by using these two commands
  // ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  // ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile

  

  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    ObjectMapper objectmapper = getObjectMapper();
    List <PortfolioTrade> portfolioTrade =Arrays.asList(objectmapper.readValue(resolveFileFromResources(filename ), PortfolioTrade[].class));  
  return portfolioTrade;
  }

 

 

  // TODO:
  // Build the Url using given parameters and use this function in your code to
  // cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
 // String url = "https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2021-01-01&endDate=2021-11-19&token=675a478a549d0aaf46c228675042d4562e2bc1e4";
    String url = "https://api.tiingo.com/tiingo/daily/"+trade.getSymbol()+"/prices?startDate="+trade.getPurchaseDate()+"&endDate=" +endDate + "&token=" + token;
      return url;
  }

  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    printJsonObject(mainReadQuotes(args));

  }
  
}
