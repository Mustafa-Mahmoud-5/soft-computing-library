package org.example.lib.datahelper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataRow {
    private List<Double> inputs;
    private List<Double> targets;
}
