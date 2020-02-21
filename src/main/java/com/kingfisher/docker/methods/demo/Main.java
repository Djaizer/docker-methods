package com.kingfisher.docker.methods.demo;

import com.kingfisher.docker.methods.demo.processor.AnaliseJson;
import com.kingfisher.docker.methods.demo.processor.ProcessRequestResp;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.kingfisher.docker.methods.demo.processor.ProcessRequestResp.*;

@Log4j2
public class Main {

    public static void main(String[] args) throws IOException {

        log.info("I am here");
        String url_java8 = "http://atg-solaris-aws-sonar01.aws.gha.kfplc.com:9000/api/issues/search?p=1&ps=500&s=FILE_LINE&asc=true&additionalFields=_all&facets=severities%2Cresolutions&resolved=false&severities=MAJOR&componentUuids=AXBhqr_wFlY0BJMJCoEy";
        String url_java6 = "http://atg-solaris-aws-sonar01.aws.gha.kfplc.com:9000/api/issues/search?p=1&ps=500&s=FILE_LINE&asc=true&additionalFields=_all&facets=severities%2Cresolutions&severities=MAJOR&componentUuids=AXBhyzyyFlY0BJMJCwdL";
        ProcessRequestResp process = new ProcessRequestResp();
    //    persistResponse("sonar_java8.json", process.getFullJsonResponseFromGetRequest(url_java8, 10));
    //    persistResponse("sonar_java6.json", process.getFullJsonResponseFromGetRequest(url_java6, 10));


        analise ();
        log.info("Finish parsing");

    }

    private static void analise () throws IOException {
        String fileNameJ8 =  "sonar_java8.json";
        String fileNameJ6 =  "sonar_java6.json";

        Path file_8 = Paths.get(BASE_PATH + fileNameJ8);
        Path file_6 = Paths.get(BASE_PATH + fileNameJ6);
        Map<String, Map<Integer, Set<String>>> map_6 =  AnaliseJson.parseJsonFile(file_6);
        Map<String, Map<Integer, Set<String>>> map_8 =  AnaliseJson.parseJsonFile(file_8);
        Map<String, String> compare_6_vs_8 = AnaliseJson.analiseJsons(map_6, map_8);
        Map<String, String> compare_8_vs_6 = AnaliseJson.analiseJsons(map_8, map_6);


        System.out.println("compare_6_vs_8");
        compare_6_vs_8.keySet().forEach( k -> System.out.printf("\n Rule ID: %s , %s", k, compare_6_vs_8.get(k)));

        System.out.println("\n compare_8_vs_6");
        compare_8_vs_6.keySet().forEach( k -> System.out.printf("\n Rule ID: %s , %s", k, compare_8_vs_6.get(k)));



        log.info("Finish");
    }

}
