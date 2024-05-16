package com.example;


import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class RF implements Classifier {

    private RandomForest randomForest;

    public RF() {
        randomForest = new RandomForest();
    }

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
        int[] targetData = new int[dataScale.splitTarget(dataWithoutHeaders).length];

        int count = 0;
        for (Integer i : dataScale.splitTarget(dataWithoutHeaders)) {
            int v = i;
            targetData[count] = v;
            count++;
        }

        // Convert splitData to Instances
        Instances trainData = convertToInstances(inputData, targetData);

        // Set class index
        trainData.setClassIndex(trainData.numAttributes() - 1);

        // Train Random Forest model

        randomForest.setNumTrees(100);// Set the number of trees in the forest
        randomForest.buildClassifier(trainData);

        // Evaluate the model
        Evaluation evaluation = new Evaluation(trainData);
        evaluation.evaluateModel(randomForest, trainData);

        // Print evaluation results
        System.out.println(evaluation.toSummaryString("\nResults\n======\n", false));
        System.out.println("Accuracy: " + evaluation.pctCorrect() + "%");
    }

    private Instances convertToInstances(String[][] inputData, int[] y) {
        // Create the attribute information
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < inputData[0].length; i++) {
            attributes.add(new Attribute("attr" + (i + 1)));
        }
        // Add class attribute
        List<String> classValues = new ArrayList<>();
        classValues.add("0");
        classValues.add("1");
        attributes.add(new Attribute("class", classValues));

        // Create the Instances object
        Instances data = new Instances("Dataset", attributes, inputData.length);
        for (int i = 0; i < inputData.length; i++) {
            Instance instance = new DenseInstance(attributes.size());
            for (int j = 0; j < inputData[i].length; j++) {
                instance.setValue(attributes.get(j), Double.parseDouble(inputData[i][j]));
            }
            instance.setValue(attributes.get(attributes.size() - 1), y[i]);
            data.add(instance);
        }

        return data;
    }

    @Override
    public void buildClassifier(Instances data) throws Exception {
        randomForest.buildClassifier(data);
    }

    @Override
    public double classifyInstance(Instance instance) throws Exception {
        return randomForest.classifyInstance(instance);
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        return randomForest.distributionForInstance(instance);
    }

    @Override
    public Capabilities getCapabilities() {
        return randomForest.getCapabilities();
    }
}
