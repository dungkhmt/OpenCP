package constraintprogramming.constraints.basic.eq;

import java.util.HashSet;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;
import constraintprogramming.function.*;
public class EqFunctionFunction implements IConstraint {
	private IFunction f1;
	private IFunction f2;
	private VarIntCP[] vars;
	
	public EqFunctionFunction(IFunction f1, IFunction f2){
		this.f1 = f1;
		this.f2 = f2;
		VarIntCP[] x1 = f1.getVariables();
		VarIntCP[] x2 = f2.getVariables();
		HashSet<VarIntCP> S = new HashSet();
		for(int i = 0; i < x1.length; i++)
			S.add(x1[i]);
		for(int i = 0; i < x2.length; i++)
			S.add(x2[i]);
		vars = new VarIntCP[S.size()];
		int idx = -1;
		for(VarIntCP x : S){
			idx++;
			vars[idx] = x;
		}
		
	}
	
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		if(!instantiated())
			return false;
		return f1.getValue() == f2.getValue();
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return f1.instantiated() && f2.instantiated();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		if(f1.instantiated()){
			int v1 = f1.getValue();
			return f2.propagateAssignValueFunctionEqual(x, val, v1);
		}else if(f2.instantiated()){
			int v2 = f2.getValue();
			return f1.propagateAssignValueFunctionEqual(x, val, v2);
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
		return "EqFunctionFunction(" + f1.name() + "," + f2.name() + ")";
	}

}
