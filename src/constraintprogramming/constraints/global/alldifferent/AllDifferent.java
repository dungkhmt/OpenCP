package constraintprogramming.constraints.global.alldifferent;

import constraintprogramming.constraints.global.alldifferent.Algo.Filter;
import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

import java.util.HashSet;

/**
 * @author nvthanh1994
 */

public class AllDifferent implements IConstraint {
    public VarIntCP[] x;
    public Filter filter;


    public AllDifferent(VarIntCP[] vars) {
        this.x = vars;
        filter = new Filter(x);
    }

    @Override
    public OutputPropagation propagateAssignValue(VarIntCP xi, int val) {
        OutputPropagation filterResult = filter.propagate();

        if (filterResult == OutputPropagation.FAILURE) {
            return OutputPropagation.FAILURE;
        }
        for (int i = 0; i < x.length; i++) {
            if (x[i].domainEmpty()) return OutputPropagation.FAILURE;
        }
        return OutputPropagation.SUSPEND;
    }

    @Override
    public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
        return OutputPropagation.SUSPEND;
    }

    @Override
    public boolean check() {
        if (!instantiated()) return false;
        HashSet<Integer> valueSet = new HashSet<>();
        for (int i = 0; i < x.length; i++) {
            valueSet.add(x[i].getValue());
        }
        return (valueSet.size() == x.length);
    }

    @Override
    public boolean instantiated() {
        for (int i = 0; i < x.length; i++)
            if (x[i].getDomain().size() != 1) return false;
        return true;
    }


    @Override
    public boolean reviseAC3(VarIntCP x) {
        return false;
    }

    @Override
    public OutputRevise revise(VarIntCP x, int val) {
        return OutputRevise.SUSPEND;
    }

    @Override
    public VarIntCP[] getVariables() {
        return this.x;
    }

    @Override
    public String name() {
        return "AllDifferent x[]";
    }
}
