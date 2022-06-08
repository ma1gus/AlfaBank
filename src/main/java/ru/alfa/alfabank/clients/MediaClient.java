package ru.alfa.alfabank.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "media0", url = "${mediaUrl0}", qualifier = "media0")
public interface MediaClient extends DownloadingGifInterface {
}
