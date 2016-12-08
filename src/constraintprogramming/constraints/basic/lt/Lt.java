package constraintprogramming.constraints.basic.lt;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Lt implements IConstraint {
	private IConstraint cstr;
	public Lt(VarIntCP x, double c){
		cstr = new LtVarConstant(x, c);
	}
	public Lt(VarIntCP x, int c){
		cstr = new LtVarConstant(x, c);
	}
	
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return cstr.check();
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return cstr.instantiated();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return cstr.propagateAssignValue(x, val);
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
		// TODO Auto-generated method stub
		return cstr.propagateRemoveValue(x, value);
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
		return cstr.reviseAC3(x);
	}

	@Override
	public OutputRevise revise(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return cstr.revise(x, val);
	}

	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return cstr.getVariables();
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return cstr.name();
	}

}
