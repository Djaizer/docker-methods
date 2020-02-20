package com.kingfisher.docker.methods.demo.controller;

import java.io.*;
import java.util.function.Consumer;

public class StreamGobbler implements Runnable, Closeable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

}
