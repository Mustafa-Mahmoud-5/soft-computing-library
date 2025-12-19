package org.example.lib.nncasestudy;

import org.example.lib.datahelper.DataLoader;
import org.example.lib.datahelper.DataRow;
import org.example.lib.datahelper.DataSplitter;
import org.example.lib.neuralnetwork.FeedForwardNeuralNetwork;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;
import org.example.lib.neuralnetwork.enums.WEIGHT_INITIALIZATION_TYPE;
import org.example.lib.neuralnetwork.layer.LayerConfig;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException {

        // load data and split it between train and test

        List<DataRow> dataset = DataLoader.loadData("data.csv");
        List<List<DataRow>> splittedData = DataSplitter.split(dataset, 0.8);
        List<DataRow> trainData = splittedData.get(0);
        List<DataRow> testData = splittedData.get(1);



        FeedForwardNeuralNetwork nn = new FeedForwardNeuralNetwork();

        nn.setInputSize(3);
        nn.setEpochs(5);
        nn.setWeightInitializationStrategy(WEIGHT_INITIALIZATION_TYPE.RANDOM);
        List<LayerConfig> layers = List.of(
                new LayerConfig(5, ACTIVATION_FUNCTION_TYPE.RELU),
                new LayerConfig(5, ACTIVATION_FUNCTION_TYPE.SOFT_MAX)
            );
        nn.setLayers(layers);
        nn.initializeNetwork();
        nn.printLayers();

        nn.train(trainData);
        double accuracy = nn.test(testData);
        System.out.println("Accuracy: " + accuracy);
    }
}
