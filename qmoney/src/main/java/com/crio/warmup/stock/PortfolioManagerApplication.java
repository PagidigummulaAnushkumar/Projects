
package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManagerImpl;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    String resultOfResolveFilePathArgs0 = "qmoney/src/main/resources/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@2f9f7dcf";
    String functionNameFromTestFileInStackTrace = "PortfolioManagerApplication.mainReadFile()";
    String lineNumberFromTestFileInStackTrace = "29";


   return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
       toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
       lineNumberFromTestFileInStackTrace});
 }

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
    String url = "https://api.tiingo.com/tiingo/daily/"+trade.getSymbol()+"/prices?startDate="+trade.getPurchaseDate()+"&endDate=" +endDate + "&token=" + token;
      return url;
  }



  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.




  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
     return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
     return candles.get(candles.size()-1).getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    
    String url = prepareUrl(trade, endDate,token);
    TiingoCandle[] Candle = new RestTemplate().getForObject(url, TiingoCandle[].class);
  return Arrays.stream(Candle).collect(Collectors.toList());
  }

   public static   String getToken() {
     return "675a478a549d0aaf46c228675042d4562e2bc1e4";
  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {    
      File file = resolveFileFromResources(args[0]);
      ObjectMapper objectMapper = getObjectMapper();
      PortfolioTrade [] trades = objectMapper.readValue(file, PortfolioTrade[].class ); 
      List<AnnualizedReturn> ar = new ArrayList<>();
      LocalDate localDate = LocalDate.parse(args[1]);
      for(PortfolioTrade portfolioTrades  : trades){
        String url = prepareUrl(portfolioTrades, localDate,"675a478a549d0aaf46c228675042d4562e2bc1e4");
        TiingoCandle[] tiingoCandle = new RestTemplate().getForObject(url, TiingoCandle[].class);
        ar.add(calculateAnnualizedReturns (localDate,portfolioTrades,tiingoCandle[0].getOpen(),tiingoCandle[tiingoCandle.length-1].getClose()));   
 }
    Comparator<AnnualizedReturn> byAnnualizedreturns = Comparator.comparing(AnnualizedReturn::getAnnualizedReturn);
   Collections.sort(ar, byAnnualizedreturns);
   Collections.reverse(ar);
    return ar;
  }


  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade portfolioTrades, Double buyPrice, Double sellPrice) {
    Double totalReturn = (sellPrice - buyPrice) / buyPrice;
    Double totalNoOfYears = ChronoUnit.DAYS.between(portfolioTrades.getPurchaseDate(), endDate) / 365.0;
    Double annualizedReturn = Math.pow((1 + totalReturn), (1.0 / totalNoOfYears)) - 1;
    return new AnnualizedReturn(portfolioTrades.getSymbol(), annualizedReturn, totalReturn);
    }

    public static String readFileAsString(String file) throws IOException, URISyntaxException {
      return file;
    }

    
    public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
    throws Exception {
     String file = args[0];
     LocalDate endDate = LocalDate.parse(args[1]);
     String contents = readFileAsString(file);
     File file1 = resolveFileFromResources(contents);
     ObjectMapper objectMapper = getObjectMapper();
     
    List<PortfolioTrade> portfolioTrades = objectMapper.readValue(file1,
    new TypeReference<List<PortfolioTrade>>() {});
    RestTemplate restTemplate = new RestTemplate();
    PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager(restTemplate);
     return portfolioManager.calculateAnnualizedReturn(portfolioTrades, endDate);
}


  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());



    printJsonObject(mainCalculateSingleReturn(args));

  }
}

