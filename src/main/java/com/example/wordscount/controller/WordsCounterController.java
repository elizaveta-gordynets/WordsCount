package com.example.wordscount.controller;

import com.example.wordscount.dto.RequestDto;
import com.example.wordscount.service.WordsCounter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordsCounterController {

    private final WordsCounter counter;

    public WordsCounterController(WordsCounter counter) {
        this.counter = counter;
    }

    @PostMapping("/words")
    public ResponseEntity<String> addWords(@RequestBody RequestDto dto) {
        try {
            counter.addWords(dto.getWords());
            return ResponseEntity.ok("Words were added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<String> getCount(String word) {
        if(counter.getWordCount(word) == null) {
            return ResponseEntity.badRequest().body("Given word " + word + " wasn't added yet");
        }
         else {
            return ResponseEntity.ok(counter.getWordCount(word).toString());
        }
    }
}
