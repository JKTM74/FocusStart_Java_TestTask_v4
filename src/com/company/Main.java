package com.company;

import java.util.ArrayList;

public class Main {
    private static String dataType;
    private static String outputFileName;
    private static ArrayList<String> inFiles = new ArrayList<>();

    public static void main(String[] args) { // Считываем аргументы и запускаем сортировку
        int i = 0, j;
        String sortMode;
        if (args[0].equals("-a")) {
            sortMode = "asc";
            i++;
        } else if (args[0].equals("-d")) {
            sortMode = "desc";
            i++;
        } else {
            sortMode = "asc";
        }
        if (args[i].equals("-s")) {
            dataType = "string";
            outputFileName = args[i + 1];
            i += 2;
        } else if (args[i].equals("-i")) {
            dataType = "integer";
            outputFileName = args[i + 1];
            i += 2;
        } else {
            System.out.println("Parameters error.");
            System.exit(0);
        }
        for (j = i; j < args.length; j++) {
            inFiles.add(args[j]);
        }
        MergeSort mergeSort = new MergeSort(sortMode, dataType, outputFileName);
        mergeSort.sortArray(inFiles);
    }
}
