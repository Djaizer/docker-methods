package com.kingfisher.docker.methods.demo.controller;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Log4j2
@Data
public class ScriptRunner {

    private static String runAllDockerContainersPath = "src/main/resources/scripts/runAllDockerContainers.sh";
    private static String stopAllDockerContainersPath = "src/main/resources/scripts/stopAllDockerContainers.sh";

    public static String runAllDockerContainers(String... params) {
       return executeScript(runAllDockerContainersPath, params);
    }

    public static String stopAllDockerContainers(String... params) {
       return executeScript(stopAllDockerContainersPath, params);
    }

    private static String executeScript(String fileName, String... params) {

        String script = getScript(fileName);
        Process process = null;
        StringJoiner joiner = new StringJoiner("\n", "[", "]");
        try {
            process = Runtime.getRuntime().exec(script, params);
        } catch (IOException e) {
            log.error("I can not execute a script {}, with parameters {}", script, params);
            e.printStackTrace();
        }
        try (StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), joiner::add)) {
            streamGobbler.run();
            int exitCode = process.waitFor();
            assert exitCode == 0;

        } catch (IOException e) {
            log.error("I can not close an input stream");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(joiner.toString());
        return joiner.toString();
    }

    private static String getScript(String fileName) {
        String script = "echo 'nothing to run'";
        try {
            script = Files.lines(Paths.get(fileName)).collect(Collectors.joining("/n"));
        } catch (IOException e) {
            log.error("Wrong script path. Current path is: {}", fileName);
            e.printStackTrace();
        }
        return script;
    }

}
