package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataScale {
    String filePath = "HIV_dataset.csv";
    DataPreprocessor preprocessed_data = new DataPreprocessor();
    String[] cleanedData = preprocessed_data.cleanData(filePath);
    String[] encodedData = preprocessed_data.encodeData(cleanedData);

    /**
     * Splits the dataset into features (X) and target (y).
     *
     * @param encodedData The cleaned and encoded dataset as an array of strings.
     * @return A two-element array where the first element is X (features) and the second element is y (target).
     */
    public String[][] splitFeatures(String[] encodedData) {
        List<String[]> X = new ArrayList<>();
        List<Integer> y = new ArrayList<>();

        for (String row : encodedData) {
            if (row.trim().isEmpty()) {
                continue; // Skip empty lines
            }
            String[] columns = row.split(",");

            String[] features = new String[columns.length - 1]; // All columns except the 22nd
            for (int i = 0, k = 0; i < columns.length; i++) {
                if (i == 21) { // 22nd column is the target
                    Integer v = Integer.parseInt(columns[i].trim());
                    y.add(v);
                } else {
                    features[k++] = columns[i].trim();
                }
            }
            X.add(features);
        }

        // Convert List to array for X
        String[][] XArray = new String[X.size()][];
        X.toArray(XArray);

        // Convert List to array for y
        Integer[] yArray = new Integer[y.size()];
        y.toArray(yArray);

        return XArray;
    }


    public Integer[] splitTarget(String[] encodedData) {
        List<String[]> X = new ArrayList<>();
        List<Integer> y = new ArrayList<>();

        for (String row : encodedData) {
            if (row.trim().isEmpty()) {
                continue; // Skip empty lines
            }
            String[] columns = row.split(",");

            String[] features = new String[columns.length - 1]; // All columns except the 22nd
            for (int i = 0, k = 0; i < columns.length; i++) {
                if (i == 21) { // 22nd column is the target
                    y.add(Integer.parseInt(columns[i].trim()));
                } else {
                    features[k++] = columns[i].trim();
                }
            }
            X.add(features);
        }

        // Convert List to array for X
        String[][] XArray = new String[X.size()][];
        X.toArray(XArray);

        // Convert List to array for y
        Integer[] yArray = new Integer[y.size()];
        y.toArray(yArray);

        return yArray;
    }

    public double[][] scaleFeatures(String[][] XArray) {
        int rows = XArray.length;
        int cols = XArray[0].length;
        double[][] scaledX = new double[rows][cols];

        // Find min and max for each column
        double[] minValues = new double[cols];
        double[] maxValues = new double[cols];

        for (int j = 0; j < cols; j++) {
            minValues[j] = Double.MAX_VALUE;
            maxValues[j] = Double.MIN_VALUE;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = Double.parseDouble(XArray[i][j]);
                if (value < minValues[j]) {
                    minValues[j] = value;
                }
                if (value > maxValues[j]) {
                    maxValues[j] = value;
                }
            }
        }

        // Scale the data
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = Double.parseDouble(XArray[i][j]);
                if (maxValues[j] - minValues[j] == 0) {
                    scaledX[i][j] = 0; // Avoid division by zero
                } else {
                    scaledX[i][j] = (value - minValues[j]) / (maxValues[j] - minValues[j]);
                }
            }
        }

        return scaledX;
    }

    public SplitData trainTestSplit(double[][] X, int[] y, double testSize) {
        int numRows = X.length;
        int testRows = (int) (numRows * testSize);
        int trainRows = numRows - testRows;

        double[][] X_train = new double[trainRows][]; //to store X train dataset
        double[][] X_test = new double[testRows][];     //to store X test
        int[] y_train = new int[trainRows];             //to store y train
        int[] y_test = new int[testRows];               //to store y test
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            indices.add(i);
        }
        Random random = new Random();
        for (int i = 0; i < testRows; i++) {
            int index = random.nextInt(indices.size());
            X_test[i] = X[indices.get(index)];
            y_test[i] = y[indices.get(index)];
            indices.remove(index);
        }
        for (int i = 0; i < trainRows; i++) {
            X_train[i] = X[indices.get(i)];
            y_train[i] = y[indices.get(i)];
        }

        return new SplitData(X_train, X_test, y_train, y_test);
    }

    public static class SplitData {
        public double[][] X_train;
        public double[][] X_test;
        public int[] y_train;
        public int[] y_test;

        public SplitData(double[][] X_train, double[][] X_test, int[] y_train, int[] y_test) {
            this.X_train = X_train;
            this.X_test = X_test;
            this.y_train = y_train;
            this.y_test = y_test;
        }
    }

}

