package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class MergeSort {

    private final String sortMode;
    private final String dataType;
    private String lastLine = null;
    private int lastInt;
    private final String outputFileName;

    MergeSort(String sortMode, String dataType, String outputFileName) { // Сохраняем параметры в глобальных переменных и в зависимости от метода сортировки утанавливаем значение lastInt
        this.outputFileName = outputFileName;
        this.sortMode = sortMode;
        this.dataType = dataType;
        if (dataType.equals("integer")) {
            if (sortMode.equals("asc")) {
                lastInt = Integer.MIN_VALUE;
            } else {
                lastInt = Integer.MAX_VALUE;
            }
        }
    }

    void sortArray(ArrayList<String> inFiles) { // Создаем экземпляры класса ReadFile для каждого имени файла из параметров и запускаем сортировку в соответствии с типом данных.
        deleteOutputFile(outputFileName);
        HashMap<ReadFile, String> inputFiles = new HashMap<>();
        for (String fileName : inFiles) {
            ReadFile file = new ReadFile(fileName);
            inputFiles.put(file, fileName);
        }
        if (dataType.equals("integer")) {
            getDataInt(inputFiles);
        } else {
            getDataStr(inputFiles);
        }
    }

    private void deleteOutputFile(String fileName) { // Удаляем результирующий файл, если он существует.
        File outputFile = new File(fileName);

        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    private void getDataInt(HashMap<ReadFile, String> inputFiles) { // Получаем из каждого файла элемент в соответствии с текущим положением каретки и передаем массив с элементами в метод записи в файл.
        HashMap<Integer, ReadFile> values = new HashMap<>();
        while (true) {
            values.clear();
            for (ReadFile file : inputFiles.keySet()) {
                if (file.isEof()) {
                    if (isNumeric(file.getElement())) {
                        int element = Integer.parseInt(file.getElement());
                        values.put(element, file);
                    } else {
                        file.incrementCounter();
                    }
                }
            }
            if (values.size() == 0) {
                return;
            }
            writeOutputInt(values);
        }
    }

    private void getDataStr(HashMap<ReadFile, String> inputFiles) { // Получаем из каждого файла элемент в соответствии с текущим положением каретки и передаем массив с элементами в метод записи в файл.
        HashMap<String, ReadFile> values = new HashMap<>();
        while (true) {
            values.clear();
            for (ReadFile file : inputFiles.keySet()) {
                if (file.isEof()) {
                    String element = file.getElement();
                    values.put(element, file);
                } else {
                    file.incrementCounter();
                }
            }
            if (values.size() == 0) {
                return;
            }
            writeOutputStr(values);
        }
    }

    private void writeOutputInt(HashMap<Integer, ReadFile> values) { // Получаем нужный элемент, сравниваем с предыдущим записанным в файл элементом, записываем в файл если элемент удовлетворяет условиям.
        try (FileWriter output = new FileWriter(outputFileName, true)) {
            int element = getIntElement(values);

            if (sortMode.equals("asc")) {
                if (element >= lastInt) {
                    lastInt = element;
                    output.write(element + System.getProperty("line.separator"));
                }
            } else {
                if (element <= lastInt) {
                    lastInt = element;
                    output.write(element + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeOutputStr(HashMap<String, ReadFile> values) { // Получаем нужный элемент, сравниваем с предыдущим записанным в файл элементом, записываем в файл если элемент удовлетворяет условиям.
        try (FileWriter output = new FileWriter(outputFileName, true)) {
            String element = getStrElement(values);

            if (sortMode.equals("asc")) {
                if (lastLine == null || checkLine(lastLine, element)) {
                    lastLine = element;
                    output.write((element) + System.getProperty("line.separator"));
                }
            } else {
                if (lastLine == null || checkLine(element, lastLine)) {
                    lastLine = element;
                    output.write((element) + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getIntElement(HashMap<Integer, ReadFile> sortArray) { // Получаем нужный элемент и увеличиваем значение каретки нужного файла на 1.
        Map.Entry<Integer, ReadFile> minEntry = null;
        for (Map.Entry<Integer, ReadFile> entry : sortArray.entrySet()) {
            if (sortMode.equals("asc")) {
                if (minEntry == null || entry.getKey() < minEntry.getKey()) {
                    minEntry = entry;
                }
            } else {
                if (minEntry == null || entry.getKey() > minEntry.getKey()) {
                    minEntry = entry;
                }
            }
        }
        ReadFile fileWithValue = minEntry.getValue();
        fileWithValue.incrementCounter();
        return minEntry.getKey();
    }

    private String getStrElement(HashMap<String, ReadFile> sortArray) { // // Получаем нужный элемент и увеличиваем значение каретки нужного файла на 1.
        Map.Entry<String, ReadFile> minEntry = null;
        for (Map.Entry<String, ReadFile> entry : sortArray.entrySet()) {
            if (sortMode.equals("asc")) {
                if (minEntry == null || checkLine(entry.getKey(), minEntry.getKey())) {
                    minEntry = entry;
                }
            } else {
                if (minEntry == null || !checkLine(entry.getKey(), minEntry.getKey())) {
                    minEntry = entry;
                }
            }
        }
        ReadFile fileWithValue = minEntry.getValue();
        fileWithValue.incrementCounter();
        return minEntry.getKey();
    }

    private boolean checkLine(String checkedLine, String oldSmallestLine) { // Возвращает true если проверяемая строка меньше чем предыдущая строка (по таблице ASCII), иначе false.
        String smallestLine;
        boolean result = false;

        if (checkedLine.length() <= oldSmallestLine.length()) {
            smallestLine = checkedLine;
            result = true;
        } else {
            smallestLine = oldSmallestLine;
        }

        for (int i = 0; i < smallestLine.length(); i++) {
            int checkedStringCharacter = checkedLine.charAt(i);
            int oldSmallestLineCharacter = oldSmallestLine.charAt(i);

            if (checkedStringCharacter < oldSmallestLineCharacter) {
                return true;
            } else if (checkedStringCharacter > oldSmallestLineCharacter) {
                return false;
            }
        }
        return result;
    }

    private static boolean isNumeric(String str) { // Проверка, является ли элемент числом (для сортировки чисел).
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
