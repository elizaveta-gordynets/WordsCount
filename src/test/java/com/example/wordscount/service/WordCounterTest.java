package com.example.wordscount.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WordCounterTest {

    public WordsCounter counter = new WordsCounter();

    ConcurrentHashMap<String, AtomicInteger> testStorage = Mockito.mock(ConcurrentHashMap.class);

    @Test
    @SneakyThrows
    public void test_01_addWordAddsOneWord() {
        List<String> testWords = List.of("One");
        when(testStorage.compute(any(), any())).thenAnswer(inv -> new AtomicInteger(1));

        Field field = counter.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        field.set(counter, testStorage);

        counter.addWords(testWords);

        verify(testStorage, times(testWords.size())).compute(anyString(), any());
    }

    @Test
    @SneakyThrows
    public void test_02_addWordAddsWords() {
        List<String> testMultipleWords = List.of("one", "two");
        when(testStorage.compute(any(), any())).thenAnswer(inv -> new AtomicInteger(1));

        Field field = counter.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        field.set(counter, testStorage);

        counter.addWords(testMultipleWords);

        verify(testStorage, times(testMultipleWords.size())).compute(anyString(), any());
    }

    @Test
    @SneakyThrows
    public void test_03_addWordThrowsException() {
        List<String> testWords = List.of("On!");

        Field field = counter.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        field.set(counter, testStorage);

        assertThrows(IllegalArgumentException.class, () -> counter.addWords(testWords));
    }

    @Test
    @SneakyThrows
    public void test_04_theCountIsCorrect() {
        List<String> testMultipleWords = List.of("one", "two", "one", "two", "two");

        Field field = counter.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        ConcurrentHashMap<String, AtomicInteger> testMap = (ConcurrentHashMap<String, AtomicInteger>) field.get(counter);

        counter.addWords(testMultipleWords);

        assertEquals(2, testMap.get("one").get());
        assertEquals(3, testMap.get("two").get());
    }

    @Test
    @SneakyThrows
            public void test_05_getWordCountReturnsCorrectCount() {
        List<String> testMultipleWords = List.of("one", "two", "one", "two", "two");

        Field field = counter.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        ConcurrentHashMap<String, AtomicInteger> testMap = (ConcurrentHashMap<String, AtomicInteger>) field.get(counter);

        counter.addWords(testMultipleWords);

        assertEquals(3, counter.getWordCount("two").get());
    }


}
