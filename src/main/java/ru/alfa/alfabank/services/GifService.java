package ru.alfa.alfabank.services;

import ru.alfa.alfabank.clients.DownloadingGifInterface;
import ru.alfa.alfabank.clients.SearchingGifClient;
import ru.alfa.alfabank.models.GifObject;
import ru.alfa.alfabank.models.SearchingGifResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.Map;

@Service
public class GifService {
    @Value("${giphyToken}")
    private String token;

    private SearchingGifClient searchingGifClient;

    private ApplicationContext applicationContext;

    public GifService(SearchingGifClient searchingGifClient, ApplicationContext applicationContext) {
        this.searchingGifClient = searchingGifClient;
        this.applicationContext = applicationContext;
    }

    public byte[] getRandomGif(boolean isCourseBiggerThenYesterday) throws IOException {
        String searchingWorld = isCourseBiggerThenYesterday ? "rich" : "broke";
        SearchingGifResponse searchingGifResponse = searchingGifClient.searchGif(token, searchingWorld);
        GifObject[] gifs = searchingGifResponse.getData();

        int random = (int)(Math.random() * gifs.length);
        Map<String, String> selectedOriginalImage = gifs[random].getImages().getOriginal();

        URL urlForDownload = new URL(selectedOriginalImage.get("url"));
        String host = urlForDownload.getHost();
        String mediaDomainName = host.substring(0, host.indexOf("."));
        String queryString = urlForDownload.getQuery();
        String path = urlForDownload.getPath();

        DownloadingGifInterface mediaClient = (DownloadingGifInterface) applicationContext.getBean(mediaDomainName);

        return mediaClient.downloadGif(path, queryString);
    }
}
