package constraintprogramming.function;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.model.VarIntCP;

public interface IFunction {
	public OutputPropagation propagateAssignValueFunctionEqual(VarIntCP x, int valx, int value);
	public OutputPropagation propagateAssignValueFunctionNotEqual(VarIntCP x, int valx, int value);
	public OutputPropagation propagateAssignValueFunctionLessThanOrEqual(VarIntCP x, int valx, int value);
	public OutputPropagation propagateAssignValueFunctionGreaterOrEqual(VarIntCP x, int valx, int value);
	public OutputPropagation propagateAssignValueFunctionLessThan(VarIntCP x, int valx, int value);
	public OutputPropagation propagateAssignValueFunctionGreaterThan(VarIntCP x, int valx, int value);
	
	public boolean instantiated();
	public int getValue();
	public VarIntCP[] getVariables();
	public String name();
}
