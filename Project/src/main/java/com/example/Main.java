package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        String option;

        // Create new object of DatasetLoader class
        DatasetLoader dataLoader = new DatasetLoader();
        // Load dataset from file
        dataLoader.LoadData("HIV_dataset.csv");
        String filePath = "HIV_dataset.csv";
        
        // Clean and preprocess data
        DataPreprocessor preprocessed_data = new DataPreprocessor();
        String[] cleanedData = preprocessed_data.cleanData(filePath);
        String[] encodedData = preprocessed_data.encodeData(cleanedData);
        preprocessed_data.dataTrim(encodedData);

        // Remove headers
        String[] dataWithoutHeaders = preprocessed_data.removeHeaders(encodedData);

        // Initialize DataScale and split the data
        DataScale dataScale = new DataScale();
        String[][] inputData = dataScale.splitFeatures(dataWithoutHeaders);

        int[] outputData = new int[dataScale.splitTarget(dataWithoutHeaders).length];
        int count = 0;
        for (Integer i : dataScale.splitTarget(dataWithoutHeaders)) {
            outputData[count] = i;
            count++;
        }

        // Scale the features
        double[][] scaledInputData = dataScale.scaleFeatures(inputData);

        // Split the data into training and testing sets
        DataScale.SplitData splitData = dataScale.trainTestSplit(scaledInputData, outputData, 0.2);

        do {
            System.out.println("Please enter the option you want to choose: ");
            System.out.println("============================================= ");
            System.out.println("1. View raw data");
            System.out.println("2. View encoded data");
            System.out.println("3. View scaled data");
            System.out.println("4. View features and target");
            System.out.println("5. View training and testing sets");
            System.out.println("6. Fit using decision tree");
            System.out.println("7. Fit using SVM");
            System.out.println("8. Exit");
            int choice = Integer.parseInt(scan.nextLine());

            switch (choice) {
                case 1:
                    dataLoader.LoadData("HIV_dataset.csv");
                    break;

                case 2:
                    // Print encodedData dataset
                    System.out.println("\nHead of Encoded dataset is:");
                    System.out.println("---------------------------------\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.println(encodedData[i] + "\n");
                    }
                    break;

                case 3:
                    // Print scaledData dataset
                    System.out.println("\nHead of Scaled dataset is:");
                    System.out.println("---------------------------------\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.print("Row " + (i + 1) + ": ");
                        for (int j = 0; j < scaledInputData[i].length; j++) {
                            System.out.print(scaledInputData[i][j] + " ");
                        }
                        System.out.println();
                    }
                    break;

                case 4:
                    System.out.println("View features and target variable");
                    System.out.println("Features (X) are:");
                    System.out.println("------------------\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.print("Row " + (i + 1) + ": ");
                        for (int j = 0; j < inputData[i].length; j++) {
                            System.out.print(inputData[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("\n");

                    // Print the y array (target)
                    System.out.println("\n" + "Target (y):");
                    System.out.println("------------------\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.print(outputData[i] + ", ");
                    }
                    System.out.println(); 
                    break;

                case 5:
                    // Print the training and testing sets
                    System.out.println("\n" + "Training Features (X_train):" + "\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.print("Row " + (i + 1) + ": ");
                        for (int j = 0; j < splitData.X_train[i].length; j++) {
                            System.out.print(splitData.X_train[i][j] + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("\n" + "Testing Features (X_test):" + "\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.print("Row " + (i + 1) + ": ");
                        for (int j = 0; j < splitData.X_test[i].length; j++) {
                            System.out.print(splitData.X_test[i][j] + " ");
                        }
                        System.out.println();
                    }

                    System.out.println( "Training Target (y_train):" + "\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.println("Row " + (i + 1) + ": " + splitData.y_train[i]);
                    }

                    System.out.println("\n" + "Testing Target (y_test):" + "\n");
                    for (int i = 0; i < 15; i++) {
                        System.out.println("Row " + (i + 1) + ": " + splitData.y_test[i]);
                    }
                    break;

                case 6:
                    try {
                        decisionTree model = new decisionTree();
                        model.fit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case 7:
                    try {
                        SVMClassifier model3 = new SVMClassifier();
                        model3.fit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case 8:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    break;
            }

            if (choice == 8) {
                break;
            }

            System.out.println("Do you want to continue (y/no)? Press 'no' to exit.");
            option = scan.nextLine();

        } while (option.equalsIgnoreCase("y"));

        scan.close();
    }
}
