package ru.alfa.alfabank.services;

import ru.alfa.alfabank.clients.ExchangeClient;
import ru.alfa.alfabank.models.ExchangeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@Service
public class ExchangeService {
    @Value("${openexchangeratesToken}")
    private String token;

    @Value("${baseCurrencyCode}")
    private String baseCurrencyCode;

    private ExchangeClient exchangeClient;

    public ExchangeService(ExchangeClient exchangeClient) {
        this.exchangeClient = exchangeClient;
    }

    public boolean isCourseBiggerThenYesterday(String requiredCurrencyCode){
        ExchangeResponse todayExchangeResponse = getTodayExchangeRate();
        ExchangeResponse yesterdayExchangeResponse = getYesterdayExchangeRate();
        double todayCourse = calculateCourseRelativeToBaseCurrency(todayExchangeResponse, requiredCurrencyCode.toUpperCase());
        double yesterdayCourse = calculateCourseRelativeToBaseCurrency(yesterdayExchangeResponse, requiredCurrencyCode.toUpperCase());
        return todayCourse > yesterdayCourse;
    }

    private ExchangeResponse getTodayExchangeRate(){
        Calendar todayCalendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(todayCalendar.getTime());
        return exchangeClient.getCourse(today, token);
    }

    private ExchangeResponse getYesterdayExchangeRate(){
        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = dateFormat.format(yesterdayCalendar.getTime());
        return exchangeClient.getCourse(yesterday, token);
    }

    private double calculateCourseRelativeToBaseCurrency(ExchangeResponse exchangeResponse, String requiredCurrencyCode) throws NullPointerException{
        Map<String, Double> rates = exchangeResponse.getRates();
        double dollarToBaseCurrencyCourse = rates.get(baseCurrencyCode);
        double dollarToRequiredCurrencyCourse = rates.get(requiredCurrencyCode);
        return dollarToBaseCurrencyCourse/dollarToRequiredCurrencyCourse;

    }

}
