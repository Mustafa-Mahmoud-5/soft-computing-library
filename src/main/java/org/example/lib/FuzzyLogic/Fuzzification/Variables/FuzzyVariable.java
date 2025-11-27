package org.example.lib.FuzzyLogic.Fuzzification.Variables;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

public class FuzzyVariable {
    //Represents a Linguistic Variable

    @Getter @Setter
    private String name;
    @Getter @Setter
    private double min;
    @Getter @Setter
    private double max;
    @Getter @Setter
    private Map<String, FuzzySet> fuzzySets = new HashMap<>();
    @Getter @Setter
        // Crisp value for this linguistic variable (nullable). If null, engine may use a default strategy.
    private Double value = null;

    public FuzzyVariable(String name, double min, double max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public void addFuzzySet(FuzzySet set){
        fuzzySets.put(set.getName(), set);
    }

    public FuzzySet getFuzzySet(String name){
        return fuzzySets.get(name);
    }
}
