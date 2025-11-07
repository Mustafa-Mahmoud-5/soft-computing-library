package org.example.lib.FuzzyLogic.Fuzzification.Variables;
import lombok.Getter;
import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipFunction;

public class FuzzySet {
    //Represents a single fuzzy category
    @Getter
    private String name;
    @Getter
    private MembershipFunction membershipFunction;

    public FuzzySet(String name, MembershipFunction membershipFunction){
        this.name = name;
        this.membershipFunction = membershipFunction;
    }

}
