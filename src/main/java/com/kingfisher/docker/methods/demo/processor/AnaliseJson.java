package com.kingfisher.docker.methods.demo.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
public class AnaliseJson {

    public static Map<String, Map<Integer, Set<String>>> parseJsonFile(Path path) throws IOException {
        String json = new String(Files.readAllBytes(path));

        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> parsedJson = mapper.readValue(Files.readAllBytes(path), List.class);
        //   AtomicInteger count = new AtomicInteger();
        AtomicInteger countManual = new AtomicInteger();
        List<LinkedHashMap<String, Object>> temporaryListMap = new ArrayList<>();
        for (Map m: parsedJson) {
            ((List<LinkedHashMap>)m.get("issues")).forEach(temporaryListMap::add);
        }
    /*    Map<Object, Map<Object, List<LinkedHashMap<String, Object>>>> result = temporaryMap.stream()
                .collect(Collectors.groupingBy(map -> {
                    return map.get("rule");
                }, Collectors.groupingBy(map -> map.get("message"))));*/

        Map<String, Map<Integer, Set<String>>> manual = new HashMap<>();

        temporaryListMap.forEach(map -> {
            Map<Integer, Set<String>> tempMap = new HashMap<>();
            Integer counter = 0;
            Set<String> s1 = new HashSet<>();
            s1.add(String.valueOf(map.get("message")));
            if (manual.containsKey(map.get("rule"))) {
                counter = Integer.valueOf((Integer) manual.get(map.get("rule")).keySet().toArray()[0]);
                s1.addAll(manual.get(map.get("rule")).get(counter));
            }
            tempMap.put(++counter, s1);

            manual.put(String.valueOf(map.get("rule")), tempMap);
        });

        //  result.forEach((k, v) -> count.addAndGet(v.size()));
        manual.forEach((k, v) -> countManual.addAndGet(v.keySet().stream().findFirst().get()));
        log.info("There are {} major issues", countManual);
        //  parsedJson.forEach((k, v) -> log.info("Key is: {}. Value is: {}", k, v));
        return manual;
    }

    public static Map<String, String> analiseJsons(Map<String, Map<Integer, Set<String>>> comparable, Map<String, Map<Integer, Set<String>>> compareWithThis) {
        AtomicInteger countManual = new AtomicInteger();
        Map<String, String> result = new HashMap<>();
        Set<String> firstSet = comparable.keySet();
        firstSet.forEach(rule -> {
            if (!compareWithThis.containsKey(rule)) {
                String ruleMassage = comparable.get(rule).values().stream().findAny().get().toString();
                Integer count = comparable.get(rule).keySet().stream().findAny().get();
                countManual.addAndGet(count);
                result.put(rule, "NO  count: " + count + "   " + ruleMassage);
            } else {
                int firstInt = Integer.valueOf((Integer) comparable.get(rule).keySet().toArray()[0]);
                int secondInt = Integer.valueOf((Integer) compareWithThis.get(rule).keySet().toArray()[0]);
                if (firstInt != secondInt) {
                    result.put(rule, String.format("%s  !=  %s", firstInt, secondInt));
                }
            }
        });

        log.info("major difference {}", countManual);
        return result;
    }

}
