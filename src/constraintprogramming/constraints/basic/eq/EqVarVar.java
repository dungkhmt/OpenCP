/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic.eq;

import java.util.ArrayList;
import java.util.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class EqVarVar implements IConstraint {
	private VarIntCP _x;
	private VarIntCP _y;
	private int _d;
	private VarIntCP[] _vars;
	
	public EqVarVar(VarIntCP x, VarIntCP y, int d){
		//semantic: x = y + d
		_x = x; _y = y; _d = d;
		_vars = new VarIntCP[2];
		_vars[0] = _x; _vars[1] = _y;
		//_x.addBindAssignValue(this);
		//_y.addBindAssignValue(this);
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return _x.getValue() == _y.getValue() + _d;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		for(int i = 0; i < _vars.length; i++)
			if(_vars[i].getDomain().size() != 1) return false;
		return true;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		if(x == _x){
			ArrayList<Integer> vy = _y.getPossibleValues();
			for(int i = 0; i < vy.size(); i++){
				int v = vy.get(i);
				if(val != v + _d){
					_y.removeValue(v);
					if(_y.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(_y.getDomain().size() == 1){
				Iterator it = _y.getDomain().iterator();
				int v = (int)it.next();
				_y.assignValue(v);
			}
		}else if(x == _y){
			ArrayList<Integer> vx = _x.getPossibleValues();
			for(int i = 0; i < vx.size(); i++){
				int v = vx.get(i);
				if(val + _d != v){
					_x.removeValue(v);
					if(_x.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			if(_x.getDomain().size() == 1){
				Iterator it = _x.getDomain().iterator();
				int v = (int)it.next();
				_x.assignValue(v);
				
			}
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
		System.out.println(name() + "::reviseAV3 --> NOT IMPLEMENTED");
		System.exit(-1);
		return false;
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		if(x == _x){
			if(_y.instantiated()){
				if(val == _y.getValue() + _d) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
			}			
		}else if(x == _y){
			if(_x.instantiated()){
				if(_x.getValue() == val + _d) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
			}
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
		return "EqVarVar(" + _x.name() + "," + _y.name() + "," + _d + ")";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
