package com.example;

import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

import java.util.ArrayList;
import java.util.List;

public class decisionTree {

    public void fit() throws Exception {
        String filePath = "HIV_dataset.csv";
        DataPreprocessor preprocessed_data = new DataPreprocessor();
        String[] cleanedData = preprocessed_data.cleanData(filePath);
        String[] encodedData = preprocessed_data.encodeData(cleanedData);

        // Remove headers
        String[] dataWithoutHeaders = preprocessed_data.removeHeaders(encodedData);

        // Initialize DataScale and split the data
        DataScale dataScale = new DataScale();
        String[][] inputData = dataScale.splitFeatures(dataWithoutHeaders);
        int[] targetData =new int[dataScale.splitTarget(dataWithoutHeaders).length];
        int count = 0;
        for (Integer i:dataScale.splitTarget(dataWithoutHeaders)){
            int v = i;
            targetData[count] = v;
            count++;
        }

        // Convert targetData to int[]
        int[] outputData = new int[targetData.length];
        for (int i = 0; i < targetData.length; i++) {
            outputData[i] = targetData[i];
        }

        // Scale the features
        double[][] scaledInputData = dataScale.scaleFeatures(inputData);

        // Split the data into training and testing sets
        DataScale.SplitData splitData = dataScale.trainTestSplit(scaledInputData, outputData, 0.2);

        // Convert splitData to Instances
        Instances trainData = convertToInstances(splitData.X_train, splitData.y_train);
        Instances testData = convertToInstances(splitData.X_test, splitData.y_test);

        // Set class index
        trainData.setClassIndex(trainData.numAttributes() - 1);
        testData.setClassIndex(testData.numAttributes() - 1);

        // Train Decision Tree model
        J48 decisionTree = new J48();
        decisionTree.setUnpruned(true); // Use unpruned tree
        decisionTree.buildClassifier(trainData);

        // Evaluate the model
        Evaluation evaluation = new Evaluation(trainData);
        evaluation.evaluateModel(decisionTree, testData);

        // Print evaluation results
        System.out.println(evaluation.toSummaryString("\n Decision Tree Model Evaluation\n===============\n", false));
        System.out.println("Accuracy: " + evaluation.pctCorrect() + "%");
    }

    private Instances convertToInstances(double[][] X, int[] y) {
        // Create the attribute information
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < X[0].length; i++) {
            attributes.add(new Attribute("attr" + (i + 1)));
        }
        // Add class attribute
        List<String> classValues = new ArrayList<>();
        classValues.add("0");
        classValues.add("1");
        attributes.add(new Attribute("class", classValues));

        // Create the Instances object
        Instances data = new Instances("Dataset", attributes, X.length);
        for (int i = 0; i < X.length; i++) {
            Instance instance = new DenseInstance(attributes.size());
            for (int j = 0; j < X[i].length; j++) {
                instance.setValue(attributes.get(j), X[i][j]);
            }
            instance.setValue(attributes.get(attributes.size() - 1), y[i]);
            data.add(instance);
        }

        return data;
    }
}
