/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic;

import java.util.HashSet;
import java.util.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Implicate implements IConstraint {

	private VarIntCP[] _vars;
	private IConstraint _c1;
	private IConstraint _c2;
	private IConstraint[] _cstr;
	public Implicate(IConstraint c1, IConstraint c2){
		//semantic: c1 -> c2
		_c1 = c1; _c2 = c2;
		_cstr = new IConstraint[2];
		_cstr[0] = _c1; _cstr[1] = _c2;
		HashSet<VarIntCP> S = new HashSet<VarIntCP>();
		for(int i = 0; i < _cstr.length; i++){
			IConstraint c = _cstr[i];
			VarIntCP[] V = c.getVariables();
			for(int j = 0; j < V.length; j++){
				S.add(V[j]);
				//V[j].removeBindAssignValue(c);
			}
		}
		Iterator it = S.iterator();
		_vars = new VarIntCP[S.size()];
		int idx = -1;
		while(it.hasNext()){
			VarIntCP x = (VarIntCP)it.next();
			idx++;
			_vars[idx] = x;
			//_vars[idx].addBindAssignValue(this);
		}
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		if(!instantiated()) return false;
		return !_c1.check() || _c2.check();
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
		if(_c1.instantiated()){
			if(_c1.check()){
				//System.out.println(x.getCPManager().getInstantiatedVariables() + " --> ");
				//System.out.println(name() + "::propagateAssignValue, c1 = " + _c1.name() + " is true --> CALL c2 = " + _c2.name() + ".propagateAssignValue....");
				return _c2.propagateAssignValue(x, val);
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
		System.out.println(name() + "::reviseAC3 --> NOT IMPLEMENTED");
		System.exit(-1);
		return false;
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		if(_c1.instantiated()){
			if(_c1.check()){
				//if(_c2.revise(x, val) == OutputRevise.FAILURE) return OutputRevise.FAILURE;
				return _c2.revise(x, val);
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
		return "Implicate(" + _c1.name() + " --> " + _c2.name() + ")";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
