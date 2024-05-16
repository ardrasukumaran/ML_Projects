package com.example;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import java.io.IOException;

public class DataPreprocessor {
    //to drop na values
    public String[] cleanData(String filePath) {
        String line = "";
        StringBuilder cleanedDataBuilder = new StringBuilder();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream(filePath);

            if (is != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    // Clean each data row
                    for (String value : data) {
                        if (value != null && !value.trim().isEmpty()) {
                            cleanedDataBuilder.append(value.trim()).append(",");
                        }
                    }
                    cleanedDataBuilder.append("\n"); // Add newline after each row
                }
                br.close(); // Close the BufferedReader
            } else {
                System.err.println("Failed to load CSV file: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // Convert the cleaned data to a string array
        String[] cleanedData = cleanedDataBuilder.toString().split("\n");
        return cleanedData;
    }


        // Remove headers from the data
        public String[] removeHeaders(String[] data) {
            // Skip the first row assuming it is the header
            String[] dataWithoutHeaders = new String[data.length - 1];
            System.arraycopy(data, 1, dataWithoutHeaders, 0, data.length - 1);
            return dataWithoutHeaders;
        }

    public void dataTrim(String[] data) {
        // Example preprocessing function: remove leading/trailing whitespace
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }
    
    }


    public String[] encodeData(String[] cleanedData) {
        StringBuilder cleanedDataBuilder = new StringBuilder();

        // Mapping for encoding categorical data
        Map<String, Integer> homoEncoding = new HashMap<>();
        homoEncoding.put("no", 0);
        homoEncoding.put("yes", 1);
        Map<String, Integer> hemoEncoding = new HashMap<>();
        hemoEncoding.put("no", 0);
        hemoEncoding.put("yes", 1);
        Map<String, Integer> drugsEncoding = new HashMap<>();
        drugsEncoding.put("no", 0);
        drugsEncoding.put("yes", 1);
        Map<String, Integer> opriorEncoding = new HashMap<>();
        opriorEncoding.put("no", 0);
        opriorEncoding.put("yes", 1);
        Map<String, Integer> z30Encoding = new HashMap<>();
        z30Encoding.put("no", 0);
        z30Encoding.put("yes", 1);
        Map<String, Integer> genderEncoding = new HashMap<>();
        genderEncoding.put("M", 0);
        genderEncoding.put("F", 1);
        Map<String, Integer> str2Encoding = new HashMap<>();
        str2Encoding.put("naive", 0);
        str2Encoding.put("experienced", 1);
        Map<String, Integer> stratEncoding = new HashMap<>();
        stratEncoding.put("Antiretroviral Naive", 0);
        stratEncoding.put("> 1 but <= 52 weeks of prior antiretroviral therapy", 1);
        stratEncoding.put("> 52 weeks", 2);
        Map<String, Integer> symptomEncoding = new HashMap<>();
        symptomEncoding.put("asymp", 0);
        symptomEncoding.put("symp", 1);
        Map<String, Integer> treatEncoding = new HashMap<>();
        treatEncoding.put("ZDV only", 0);
        treatEncoding.put("others", 1);
        Map<String, Integer> offtrtEncoding = new HashMap<>();
        offtrtEncoding.put("no", 0);
        offtrtEncoding.put("yes", 1);
        Map<String, Integer> infectedEncoding = new HashMap<>();
        infectedEncoding.put("no", 0);
        infectedEncoding.put("yes", 1);
        Map<String, Integer> trtEncoding = new HashMap<>();
        trtEncoding.put("ZDV only", 0);
        trtEncoding.put("ZDV + ddI", 1);
        trtEncoding.put("ZDV + Zal", 2);
        trtEncoding.put("ddI only", 3);

        try {
            for (String j : cleanedData) {
                if (j.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] data = j.split(",");
                StringJoiner encodedLine = new StringJoiner(",");

                for (int i = 0; i < data.length; i++) {
                    // Skip the 11th column (index 10)
                    if (i == 11) {
                        continue;
                    }

                    String value = data[i].trim();
                    if (i == 5) { // Assuming 'homo' is at index 5
                        Integer encodedValue = homoEncoding.get(value);
                        if (encodedValue != null) {
                            encodedLine.add(encodedValue.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'homo': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 4) { // Assuming 'hemo' is at index 4
                        Integer encodedValue2 = hemoEncoding.get(value);
                        if (encodedValue2 != null) {
                            encodedLine.add(encodedValue2.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'hemo': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 6) { // Assuming 'drugs' is at index 6
                        Integer encodedValue3 = drugsEncoding.get(value);
                        if (encodedValue3 != null) {
                            encodedLine.add(encodedValue3.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'drugs': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 8) { // Assuming 'oprior' is at index 8
                        Integer encodedValue4 = opriorEncoding.get(value);
                        if (encodedValue4 != null) {
                            encodedLine.add(encodedValue4.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'oprior': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 12) { // Assuming 'GENDER' is at index 12
                        Integer encodedValue6 = genderEncoding.get(value);
                        if (encodedValue6 != null) {
                            encodedLine.add(encodedValue6.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'GENDER': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } 
                    else if (i == 9) { // Assuming 'z30' is at index 9
                        Integer encodedValue5 = z30Encoding.get(value);
                        if (encodedValue5 != null) {
                            encodedLine.add(encodedValue5.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'z30': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } 
                     
                    else if (i == 13) { // Assuming 'str2' is at index 13
                        Integer encodedValue7 = str2Encoding.get(value);
                        if (encodedValue7 != null) {
                            encodedLine.add(encodedValue7.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'str2': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 14) { // Assuming 'strat' is at index 14
                        Integer encodedValue8 = stratEncoding.get(value);
                        if (encodedValue8 != null) {
                            encodedLine.add(encodedValue8.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'strat': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 15) { // Assuming 'symptom' is at index 15
                        Integer encodedValue9 = symptomEncoding.get(value);
                        if (encodedValue9 != null) {
                            encodedLine.add(encodedValue9.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'symptom': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 16) { // Assuming 'treat' is at index 16
                        Integer encodedValue10 = treatEncoding.get(value);
                        if (encodedValue10 != null) {
                            encodedLine.add(encodedValue10.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'treat': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 17) { // Assuming 'offtrt' is at index 17
                        Integer encodedValue11 = offtrtEncoding.get(value);
                        if (encodedValue11 != null) {
                            encodedLine.add(encodedValue11.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'offtrt': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 22) { // Assuming 'infected' is at index 22
                        Integer encodedValue12 = infectedEncoding.get(value);
                        if (encodedValue12 != null) {
                            encodedLine.add(encodedValue12.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'infected': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else if (i == 1) { // Assuming 'trt' is at index 1
                        Integer encodedValue13 = trtEncoding.get(value);
                        if (encodedValue13 != null) {
                            encodedLine.add(encodedValue13.toString());
                        } else {
                            // Handle unexpected values (optional)
                            System.err.println("Unexpected value in column 'trt': " + value);
                            encodedLine.add(value); // Default encoding for unexpected value
                        }
                    } else {
                        encodedLine.add(value);
                    }
                }
                cleanedDataBuilder.append(encodedLine.toString()).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
            e.printStackTrace();
        }

        return cleanedDataBuilder.toString().split("\n");
    }
}
