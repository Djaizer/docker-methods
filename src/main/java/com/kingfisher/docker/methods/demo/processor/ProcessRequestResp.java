package com.kingfisher.docker.methods.demo.processor;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

@Log4j2
public class ProcessRequestResp {
    public static String BASE_PATH = "src/main/resources/";

    public  String getFullJsonResponseFromGetRequest(String fullUrl, int pages) throws IOException {
        StringJoiner data = new StringJoiner(",");

        for (int i = 1; i <= pages; i++) {
            if (i != 1)
                fullUrl = fullUrl.replace("?p=" + (i-1) + "&ps", "?p=" + i + "&ps");
            URL url = new URL(fullUrl);
            log.info("Iter # {} ; URL is {}",i,  url);
            HttpURLConnection connection = buildURLConnection(url);
            //  DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            //   dataOutputStream.writeBytes();
            String debug = readResponse(connection);
            log.info("Iter # {} , Response is {}",i, debug);
            data.add(debug);

        }
        return "[" + data.toString() + "]";

    }

    private static String readResponse(HttpURLConnection con) throws IOException {
        StringBuffer content = new StringBuffer();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));) {
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }
        }
        return content.toString();
    }

    private static HttpURLConnection buildURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        return connection;
    }

    public static void persistResponse(String fileName, String text) throws IOException {
        Path path = Paths.get(BASE_PATH + fileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.createFile(path);
        Files.write(path, text.getBytes());
        log.info("File is created {}, file mane: {}", Files.exists(path), path);
    }
}
