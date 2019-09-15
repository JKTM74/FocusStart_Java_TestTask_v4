package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadFile {
    private final String fileName;
    private int counter = 0;

    ReadFile(String fileName) {
        this.fileName = fileName;
    }

    String getElement() { // Получаем строку в соответствии с положением каретки в кодировке "windows-12-51"
        String ansiString = null;
        try (Stream<String> linesToRead = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
            String line = linesToRead.skip(counter).findFirst().get();
            ansiString = new String(line.getBytes(StandardCharsets.ISO_8859_1), "windows-1251");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ansiString;
    }

    boolean isEof() { // Проверка кончился файл или нет
        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.skip(counter).findFirst().isPresent();
    }

    void incrementCounter() {
        this.counter++;
    }

}
