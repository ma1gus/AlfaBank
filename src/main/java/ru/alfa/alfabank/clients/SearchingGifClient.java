package ru.alfa.alfabank.clients;

import ru.alfa.alfabank.models.SearchingGifResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gify", url = "${searchingGifUrl}" )
public interface SearchingGifClient {
    @RequestMapping(value = "/v1/gifs/search", method = RequestMethod.GET, params = {"api_key", "q"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    SearchingGifResponse searchGif(@RequestParam(value = "api_key") String token, @RequestParam(value = "q") String searchingQuery);
}
