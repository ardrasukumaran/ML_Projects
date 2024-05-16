package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatasetLoader {
    public void LoadData(String csvFile) { // saving dataset into a string
        String line = "";
        int lineCount = 0;

        try {
            // Load CSV file from resources
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(csvFile);

            if (is != null) { // Check if the InputStream is not null
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                System.out.println("Head of the dataset is:");
                System.out.println("-------------------------");

                while ((line = br.readLine()) != null && lineCount < 11) { // to print first few lines
                    String[] data = line.split(",");
                    // Process each row of the dataset here
                    for (String value : data) {
                        System.out.print(value + ",");
                    }
                    System.out.println("\n"); // Print a new line after each row
                    lineCount++;
                }
                br.close(); // Close the BufferedReader
            } else {
                System.out.println("Failed to load CSV file: " + csvFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
