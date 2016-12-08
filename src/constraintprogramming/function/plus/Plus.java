package constraintprogramming.function.plus;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.function.IFunction;
import constraintprogramming.model.VarIntCP;

public class Plus implements IFunction {
	private IFunction f;
	public Plus(VarIntCP x, VarIntCP y){
		f = new PlusVarVar(x,y);
	}
	@Override
	public OutputPropagation propagateAssignValueFunctionEqual(VarIntCP x,
			int valx, int value) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionEqual(x, valx, value);
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionNotEqual(VarIntCP x,
			int valx, int value) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionNotEqual(x, valx, value);
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionLessThanOrEqual(
			VarIntCP x, int valx, int value) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionLessThanOrEqual(x, valx, value);
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionGreaterOrEqual(
			VarIntCP x, int valx, int value) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionGreaterOrEqual(x, valx, value);
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionLessThan(VarIntCP x,
			int valx, int value) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionLessThan(x, valx, value);
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionGreaterThan(
			VarIntCP x, int valx, int value) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionGreaterThan(x, valx, value);
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return f.instantiated();
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return f.getValue();
	}

	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return f.getVariables();
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Plus";
	}

}
