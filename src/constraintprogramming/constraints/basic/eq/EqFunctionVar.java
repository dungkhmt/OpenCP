package constraintprogramming.constraints.basic.eq;

import java.util.ArrayList;
import java.util.HashSet;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.function.IFunction;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class EqFunctionVar implements IConstraint {
	private IFunction f;
	private VarIntCP y;
	private VarIntCP[] vars;
	public EqFunctionVar(IFunction f, VarIntCP y){
		this.f = f;
		this.y = y;
		HashSet<VarIntCP> S = new HashSet<VarIntCP>();
		for(int i = 0; i < f.getVariables().length; i++){
			S.add(f.getVariables()[i]);
		}
		S.add(y);
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
		return f.getValue() == y.getValue();
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return f.instantiated() && y.instantiated();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		if(f.instantiated()){
			int vf = f.getValue();
			ArrayList<Integer> V = y.getPossibleValues();
			for(int v : V){
				if(v != vf){
					y.removeValue(v);
					if(y.size() == 0) return OutputPropagation.FAILURE;
				}
			}
			if(y.singleton()){
				y.assignValue(y.getValue());
			}
		}else if(x == y){
			return f.propagateAssignValueFunctionEqual(x, val, val);
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
		return "EqFunctionVar";
	}

}
