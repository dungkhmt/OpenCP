package constraintprogramming.function.plus;

import java.util.ArrayList;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.function.IFunction;
import constraintprogramming.model.VarIntCP;

public class PlusVarVar implements IFunction {

	private VarIntCP x1;
	private VarIntCP x2;
	private VarIntCP[] vars;
	public PlusVarVar(VarIntCP x1, VarIntCP x2){
		this.x1 = x1; this.x2 = x2;
		vars = new VarIntCP[2];
		vars[0] = x1; vars[1] = x2;
	}
	
	@Override
	public OutputPropagation propagateAssignValueFunctionEqual(VarIntCP x,
			int valx, int value) {
		// TODO Auto-generated method stub
		if(x == x1){
			// remove invalid values from the domain of x2
			int tv = value - valx;
			if(!x2.getDomain().contains(tv)) return OutputPropagation.FAILURE;
			ArrayList<Integer> V2 = x2.getPossibleValues();
			for(int v : V2){
				if(v + valx != value){
					x2.removeValue(v);
					if(x2.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x2.singleton()){
				x2.assignValue(x2.getValue());
			}
		}else if(x == x2){
			// remove invalid values from the domain of x1
			int tv = value - valx;
			if(!x1.getDomain().contains(tv)) return OutputPropagation.FAILURE;
			ArrayList<Integer> V1 = x1.getPossibleValues();
			for(int v : V1){
				if(v + valx != value){
					x1.removeValue(v);
					if(x1.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x1.singleton()){
				x1.assignValue(x1.getValue());
			}
		}
		return OutputPropagation.SUSPEND;
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionNotEqual(VarIntCP x,
			int valx, int value) {
		// TODO Auto-generated method stub
		if(x == x1){
			// remove invalid values from the domain of x2
			int tv = value - valx;
			ArrayList<Integer> V2 = x2.getPossibleValues();
			for(int v : V2){
				if(v + valx == value){
					x2.removeValue(v);
					if(x2.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x2.singleton()){
				x2.assignValue(x2.getValue());
			}
		}else if(x == x2){
			// remove invalid values from the domain of x1
			int tv = value - valx;
			ArrayList<Integer> V1 = x1.getPossibleValues();
			for(int v : V1){
				if(v + valx == value){
					x1.removeValue(v);
					if(x1.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x1.singleton()){
				x1.assignValue(x1.getValue());
			}
		}
		return OutputPropagation.SUSPEND;
	}

	@Override
	public OutputPropagation propagateAssignValueFunctionLessThanOrEqual(
			VarIntCP x, int valx, int value) {
		// TODO Auto-generated method stub
		if(x == x1){
			// remove invalid values from the domain of x2
			int tv = value - valx;
			ArrayList<Integer> V2 = x2.getPossibleValues();
			for(int v : V2){
				if(v + valx > value){
					x2.removeValue(v);
					if(x2.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x2.singleton()){
				x2.assignValue(x2.getValue());
			}
		}else if(x == x2){
			// remove invalid values from the domain of x1
			int tv = value - valx;
			ArrayList<Integer> V1 = x1.getPossibleValues();
			for(int v : V1){
				if(v + valx > value){
					x1.removeValue(v);
					if(x1.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x1.singleton()){
				x1.assignValue(x1.getValue());
			}
		}
		return OutputPropagation.SUSPEND;

	}

	@Override
	public OutputPropagation propagateAssignValueFunctionGreaterOrEqual(
			VarIntCP x, int valx, int value) {
		// TODO Auto-generated method stub
		if(x == x1){
			// remove invalid values from the domain of x2
			int tv = value - valx;
			ArrayList<Integer> V2 = x2.getPossibleValues();
			for(int v : V2){
				if(v + valx < value){
					x2.removeValue(v);
					if(x2.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x2.singleton()){
				x2.assignValue(x2.getValue());
			}
		}else if(x == x2){
			// remove invalid values from the domain of x1
			int tv = value - valx;
			ArrayList<Integer> V1 = x1.getPossibleValues();
			for(int v : V1){
				if(v + valx < value){
					x1.removeValue(v);
					if(x1.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x1.singleton()){
				x1.assignValue(x1.getValue());
			}
		}
		return OutputPropagation.SUSPEND;

	}

	@Override
	public OutputPropagation propagateAssignValueFunctionLessThan(VarIntCP x,
			int valx, int value) {
		// TODO Auto-generated method stub
		if(x == x1){
			// remove invalid values from the domain of x2
			int tv = value - valx;
			ArrayList<Integer> V2 = x2.getPossibleValues();
			for(int v : V2){
				if(v + valx >= value){
					x2.removeValue(v);
					if(x2.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x2.singleton()){
				x2.assignValue(x2.getValue());
			}
		}else if(x == x2){
			// remove invalid values from the domain of x1
			int tv = value - valx;
			ArrayList<Integer> V1 = x1.getPossibleValues();
			for(int v : V1){
				if(v + valx >= value){
					x1.removeValue(v);
					if(x1.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x1.singleton()){
				x1.assignValue(x1.getValue());
			}
		}
		return OutputPropagation.SUSPEND;

	}

	@Override
	public OutputPropagation propagateAssignValueFunctionGreaterThan(
			VarIntCP x, int valx, int value) {
		// TODO Auto-generated method stub
		if(x == x1){
			// remove invalid values from the domain of x2
			int tv = value - valx;
			ArrayList<Integer> V2 = x2.getPossibleValues();
			for(int v : V2){
				if(v + valx <= value){
					x2.removeValue(v);
					if(x2.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x2.singleton()){
				x2.assignValue(x2.getValue());
			}
		}else if(x == x2){
			// remove invalid values from the domain of x1
			int tv = value - valx;
			ArrayList<Integer> V1 = x1.getPossibleValues();
			for(int v : V1){
				if(v + valx <= value){
					x1.removeValue(v);
					if(x1.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(x1.singleton()){
				x1.assignValue(x1.getValue());
			}
		}
		return OutputPropagation.SUSPEND;

	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return x1.instantiated() && x2.instantiated();
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		if(!instantiated()){
			System.out.println(name() + "::getValue() --> EXCPETION, variables not all instantiated");
			x1.getCPManager().exit(-1);
		}
		return x1.getValue() + x2.getValue();
	}

	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return vars;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "PlusVarVar";
	}

}
