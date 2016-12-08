/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic.eq;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class EqVarConstant implements IConstraint {

	private VarIntCP _x;
	private int _v;
	private VarIntCP[] _vars;
	public EqVarConstant(VarIntCP x, int v){
		_x = x; _v = v;
		_vars = new VarIntCP[1];
		_vars[0] = _x;
		//_x.addBindAssignValue(this);
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return _x.getValue() == _v;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return _x.getDomain().size() == 1;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		// DO NOTHING
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
		System.out.println(name() + "::reviseAV3 --> NOT IMPLEMENTED");
		System.exit(-1);
		return false;
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		if(x == _x){
			if(val == _v) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
		}
		return OutputRevise.SUSPEND;
	}
	
	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return _vars;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "EqVarConstant(" + _x.name() + "," + _v + ")";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
