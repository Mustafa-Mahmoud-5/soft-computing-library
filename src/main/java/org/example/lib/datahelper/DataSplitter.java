package org.example.lib.datahelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSplitter {

    public static List<List<DataRow>> split(List<DataRow> dataset, double trainRatio) {
        List<DataRow> shuffled = new ArrayList<>(dataset);
        Collections.shuffle(shuffled);
        int trainSize = (int) (shuffled.size() * trainRatio);
        return List.of(shuffled.subList(0, trainSize), shuffled.subList(trainSize, shuffled.size()));
    }
}
