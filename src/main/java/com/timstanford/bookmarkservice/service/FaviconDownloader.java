package com.timstanford.bookmarkservice.service;

import com.timstanford.bookmarkservice.data.BookmarksRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Component
public class FaviconDownloader {
    private static final Logger logger = LoggerFactory.getLogger(FaviconDownloader.class);

    private final BookmarksRepository bookmarksRepository;

    public FaviconDownloader(BookmarksRepository bookmarksRepository) {
        this.bookmarksRepository = bookmarksRepository;
    }

    @Async
    private CompletableFuture<String> getFavicon(String websiteUrl) {
        String faviconUrl = null;

        try {
            // Fetch and parse the HTML
            Document doc = Jsoup.connect(websiteUrl)
                    .userAgent("Mozilla/5.0")
                    .get();

            // Try to find favicon link in HTML

            for (Element link : doc.select("link[rel~=(?i)^(shortcut icon|icon)]")) {
                faviconUrl = link.attr("abs:href"); // Get absolute URL
                if (!faviconUrl.isEmpty()) break;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        // Fallback to /favicon.ico if not found
        if (faviconUrl == null || faviconUrl.isEmpty()) {
            URL url = null;
            try {
                url = new URL(websiteUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            faviconUrl = url.getProtocol() + "://" + url.getHost() + "/favicon.ico";
        }

        // Download the favicon
        String icon = "";
        try {
            WebClient client = WebClient.create(faviconUrl);
            byte[] data = client.get()
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .share()
                    .block();
            icon = Base64.getEncoder().encodeToString(data);
        } catch (Exception exception){
            icon = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAoklEQVR4AU3PMcqDQBQE4HeZOY12NjYeIJJ4AnObYJN0Kr9eyTqgbiZvsiD6O82w34OFMYp0OeWxSJlEUTzKtqbCFej/wecCdY42graUaLTkOOCD/rXojgikRIW/BM8HbnSjpJVTirRFMYsmhhJDgmREGRiB8f823qdvfDrdVKHpkA7UFhNqha7HugORvdngsk8yjshqFOG0ZQSqwBOIx1anfpH8zz6kV+6TAAAAAElFTkSuQmCC";
        }
        return CompletableFuture.completedFuture(icon);
    }

    @Async
    public void updateFavicon(int bookmarkId, String url) {
        logger.debug("getting favicon for {}", url);
        getFavicon(url).thenAccept(icon -> {
            bookmarksRepository
                    .findById(bookmarkId)
                    .ifPresent(bookmark -> {
                        bookmark.setFavicon(Base64.getDecoder().decode(icon));
                        bookmarksRepository.save(bookmark);
                        logger.debug("saved favicon for {}", url);
            });
        });
    }
}
