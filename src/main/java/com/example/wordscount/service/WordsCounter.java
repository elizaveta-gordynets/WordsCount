package com.example.wordscount.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

@Service
public class WordsCounter {

    private final ConcurrentHashMap<String, AtomicInteger> storage = new ConcurrentHashMap<>();


    public void addWords(List<String> wordsToAdd) throws IllegalArgumentException {
        if (containsRestrictedSymbols(wordsToAdd)) {
            throw new IllegalArgumentException("Cannot use non-alphanumeric symbols");
        } else {
            wordsToAdd.parallelStream()
                    .map(String::toLowerCase)
                    .forEach(word -> {
                        storage.compute(word, (key, oldVal) -> {
                            if (isNull(oldVal)) {
                                return new AtomicInteger(1);
                            } else {
                                oldVal.incrementAndGet();
                                return oldVal;
                            }
                        });
                    });
        }
    }

    private boolean containsRestrictedSymbols(List<String> unfiltered) {

        /**
         * assuming here we make a call to a 3d party service, and it returns us a mix of translated words
         */
        //  wordsToAdd.forEach(Translator::translate);
        return unfiltered.stream()
                .anyMatch(word -> !StringUtils.isAlphanumeric(word));
    }

    public AtomicInteger getWordCount(String word) {
        /**
         * assuming here we make a call to a 3d party service, and it returns us a mix of translated words
         */
        //  translate(word);
        return storage.get(word);
    }

}
