

import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;

public class MultithreadedWebCrawler {

    // Thread-safe queue for URLs to be crawled
    private final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
    // Set to track visited URLs
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    // Thread pool for concurrent crawling
    private final ExecutorService executorService;

    public MultithreadedWebCrawler(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void startCrawling(String seedUrl) {
        urlQueue.add(seedUrl);
        while (!urlQueue.isEmpty()) {
            String currentUrl = urlQueue.poll();
            if (currentUrl != null && !visitedUrls.contains(currentUrl)) {
                visitedUrls.add(currentUrl);
                executorService.submit(new CrawlTask(currentUrl));
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Crawling interrupted: " + e.getMessage());
        }
    }

    private class CrawlTask implements Runnable {
        private final String url;

        public CrawlTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder content = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        content.append(scanner.nextLine()).append("\n");
                    }
                    scanner.close();

                    // Save or process the content
                    System.out.println("Crawled: " + url);
                    System.out.println("Content Length: " + content.length());

                    // Extract links from the content and add them to the queue
                    extractAndQueueLinks(content.toString());
                } else {
                    System.err.println("Failed to fetch: " + url + ", Response Code: " + responseCode);
                }
            } catch (IOException e) {
                System.err.println("Error fetching URL: " + url + ", Error: " + e.getMessage());
            }
        }

        private void extractAndQueueLinks(String content) {
            // Simple regex to extract URLs (for demonstration purposes)
            String regex = "(https?://[^\\s\"]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                String newUrl = matcher.group(1);
                if (!visitedUrls.contains(newUrl)) {
                    urlQueue.add(newUrl);
                }
            }
        }
    }

    public static void main(String[] args) {
        MultithreadedWebCrawler crawler = new MultithreadedWebCrawler(10); // 10 threads
        crawler.startCrawling("https://example.com"); // Seed URL
    }
}