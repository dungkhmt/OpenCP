/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

import java.util.*;

public class AND implements IConstraint {
	private VarIntCP[]  	_vars;
	private IConstraint[] 	_cstr;
	
	public AND(IConstraint c1, IConstraint c2){
		_cstr = new IConstraint[2];
		_cstr[0] = c1;
		_cstr[1] = c2;
		
		post();
	}
	public AND(IConstraint[] cstr){
		_cstr = new IConstraint[cstr.length];
		for(int i = 0; i < _cstr.length; i++)
			_cstr[i] = cstr[i];
		post();
	}
	private void post(){
		HashSet<VarIntCP> S = new HashSet<VarIntCP>();
		for(int i = 0; i < _cstr.length; i++){
			IConstraint c = _cstr[i];
			VarIntCP[] V = c.getVariables();
			for(int j = 0; j < V.length; j++)
				S.add(V[j]);
		}
		Iterator it = S.iterator();
		_vars = new VarIntCP[S.size()];
		int idx = -1;
		while(it.hasNext()){
			VarIntCP x = (VarIntCP)it.next();
			idx++;
			_vars[idx] = x;
			_vars[idx].addBindAssignValue(this);
		}
	}
	@Override
	public boolean instantiated(){
		for(int i = 0; i < _vars.length; i++)
			if(_vars[i].getDomain().size() != 1) return false;
		return true;
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		if(!instantiated()) return false;
		for(int i = 0; i < _cstr.length; i++)
			if(_cstr[i].check()) return false;
		return true;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		ArrayList<IConstraint> C = x.getCPManager().getConstraints(x);
		for(int i = 0; i < C.size(); i++){
			IConstraint c = C.get(i);
			if(c.propagateAssignValue(x, val) == OutputPropagation.FAILURE) return OutputPropagation.FAILURE;
			if(c.propagateAssignValue(x, val) == OutputPropagation.SUSSCESS) return OutputPropagation.SUSSCESS;
			
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
		int count = 0;
		for(int i = 0; i < _cstr.length; i++){
			if(_cstr[i].revise(x, val) == OutputRevise.FAILURE) return OutputRevise.FAILURE;
			if(_cstr[i].revise(x, val) == OutputRevise.SUCCESS) count++;
		}
		if(count == _cstr.length) return OutputRevise.SUCCESS;
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
		String s = "AND(";
		for(int i = 0; i < _cstr.length; i++)
			s = s + _cstr[i].name() + ",";
		s = s + ")";
		return s;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
