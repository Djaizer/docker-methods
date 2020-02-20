package com.kingfisher.docker.methods.demo.api;

import com.kingfisher.docker.methods.demo.controller.ScriptRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class V1 {

    @RequestMapping("/start")
    public String start() {
        return ScriptRunner.runAllDockerContainers();
    }

    @RequestMapping("/stop")
    public String stop() {
        return ScriptRunner.stopAllDockerContainers();
    }

    @RequestMapping("/")
    public String test() {
        return "Base Page: there are '/stop' and '/start' apis'" ;
    }

}
