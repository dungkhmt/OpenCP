package constraintprogramming.constraints.basic.lt;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class LtVarConstant implements IConstraint {
	private VarIntCP x;
	private double c;
	private VarIntCP[] vars;
	public LtVarConstant(VarIntCP x, double c){
		this.x = x; this.c = c;
		vars = new VarIntCP[1];
		vars[0] = x;
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		if(!instantiated())
			return false;
		return x.getValue() < c;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return x.instantiated();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateAssignValue(" + x.getID() + "," + val + ")");
		if(x == this.x){
			if(val >= c) return OutputPropagation.FAILURE;
			else return OutputPropagation.SUSSCESS;
		}
		return OutputPropagation.SUSPEND;
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
		// TODO Auto-generated method stub
		return OutputPropagation.SUSPEND;
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::reviseAC3 --> NOT IMPLEMENTED");
		System.exit(-1);
		
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
		return "LtVarConstant(VarIntCP, " + c + ")";
	}

}
