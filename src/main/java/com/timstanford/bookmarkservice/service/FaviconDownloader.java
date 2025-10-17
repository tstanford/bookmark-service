package com.timstanford.bookmarkservice.service;

import com.timstanford.bookmarkservice.data.BookmarksRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
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
        String icon;

        try {
            try {
                trustAllCertificates();

                // Fetch and parse the HTML
                Document doc = Jsoup.connect(websiteUrl)
                        .userAgent("Mozilla/5.0 ...")
                        .referrer("https://www.google.com")
                        .header("Accept", "text/html")
                        .get();

                // Try to find favicon link in HTML

                for (Element link : doc.select("link[rel~=(?i)^(shortcut icon|icon)]")) {
                    faviconUrl = link.attr("abs:href"); // Get absolute URL
                    if (!faviconUrl.isEmpty()) break;
                }
            } catch(Exception exception) {
                System.out.println(exception);

            }

            // Fallback to /favicon.ico if not found
            if (faviconUrl == null || faviconUrl.isEmpty()) {
                URI url;
                url = URI.create(websiteUrl);
                faviconUrl = url.getScheme() + "://" + url.getHost() + "/favicon.ico";
            }

            WebClient client = WebClient.create(faviconUrl);

            //get mime type
            ResponseEntity<String> response = client.get()
                    .retrieve()
                    .toEntity(String.class)
                    .block();
            String contentType = response.getHeaders().getContentType().toString();

            // Download the favicon
            byte[] data = client.get()
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .share()
                    .block();
            icon = "data:"+contentType+";base64,"+Base64.getEncoder().encodeToString(data);

        } catch (Exception exception) {
            icon = "data:image/png;base64,"+Constants.DEFAULT_ICON;
        }
        return CompletableFuture.completedFuture(icon);
    }

    @Async
    public void updateFavicon(int bookmarkId, String url) {
        logger.debug("getting favicon for {}", url);
        getFavicon(url).thenAccept(icon -> bookmarksRepository
                .findById(bookmarkId)
                .ifPresent(bookmark -> {
                    bookmark.setFavicon(icon);
                    bookmarksRepository.save(bookmark);
                    logger.debug("saved favicon for {}", url);
        }));
    }

    private static void trustAllCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Disable hostname verification
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
