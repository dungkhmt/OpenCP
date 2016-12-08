package constraintprogramming.constraints.basic.eq;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;
import constraintprogramming.function.*;
public class EqFunctionConstant implements IConstraint {
	private IFunction f;
	private int c;
	private VarIntCP[] vars;
	public EqFunctionConstant(IFunction f, int c){
		this.f = f; this.c = c;
		vars = f.getVariables();
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		if(!f.instantiated())
			return false;
		return f.getValue() == c;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return f.instantiated();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return f.propagateAssignValueFunctionEqual(x, val, c);
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
		// TODO Auto-generated method stub
		return OutputPropagation.SUSPEND;
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OutputRevise revise(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return OutputRevise.SUSPEND;
	}

	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return vars;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "EqFunctionConstant";
	}

}
