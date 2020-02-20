package com.kingfisher.docker.methods.demo;

import com.kingfisher.docker.methods.demo.controller.ScriptRunner;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

    public static void main(String[] args) {
     log.info("I am here");

        ScriptRunner.runAllDockerContainers(" ");
       // ScriptRunner.stopAllDockerContainers(" ");
    }
}
