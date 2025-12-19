package org.example.lib.nncasestudy;

import org.example.lib.datahelper.DataLoader;
import org.example.lib.datahelper.DataRow;
import org.example.lib.datahelper.DataSplitter;
import org.example.lib.neuralnetwork.FeedForwardNeuralNetwork;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;
import org.example.lib.neuralnetwork.enums.LOSS_FUNCTION_STRATEGY;
import org.example.lib.neuralnetwork.enums.WEIGHT_INITIALIZATION_TYPE;
import org.example.lib.neuralnetwork.layer.LayerConfig;

import java.io.IOException;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException {

        // load data and split it between train and test

        List<DataRow> dataset = DataLoader.loadData("D:/Mustafa/CU/SoftComputing/soft-computing-library/dataset.csv");
        List<List<DataRow>> splittedData = DataSplitter.split(dataset, .6);
        List<DataRow> trainData = splittedData.get(0);
        List<DataRow> testData = splittedData.get(1);



        FeedForwardNeuralNetwork nn = new FeedForwardNeuralNetwork();

        nn.setInputSize(9);
        nn.setEpochs(10);
        nn.setLearningRate(0.03);
        nn.setWeightInitializationStrategy(WEIGHT_INITIALIZATION_TYPE.HE);
        List<LayerConfig> layers = List.of(
                new LayerConfig(10, ACTIVATION_FUNCTION_TYPE.RELU),
                new LayerConfig(10, ACTIVATION_FUNCTION_TYPE.RELU),
                new LayerConfig(3, ACTIVATION_FUNCTION_TYPE.SOFT_MAX)
            );
        nn.setLossFunctionStrategy(LOSS_FUNCTION_STRATEGY.CROSS_ENTROPY);
        nn.setLayers(layers);
        nn.initializeNetwork();
        nn.printLayers();

        nn.train(trainData);
        double accuracy = nn.test(testData);
        System.out.println("Accuracy: " + accuracy * 100 + "%");




        // A real-world prediction cases
        Example example = new Example();
        example.predict(nn);
    }
}
