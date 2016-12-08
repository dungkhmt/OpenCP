/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

import java.util.*;
public class OR implements IConstraint {
	private VarIntCP[]  	_vars;
	private IConstraint[] 	_cstr;
	
	public OR(IConstraint[] cstr){
		_cstr = new IConstraint[cstr.length];
		for(int i = 0; i < _cstr.length; i++)
			_cstr[i] = cstr[i];
		post();
	}
	public OR(IConstraint c1, IConstraint c2){
		_cstr = new IConstraint[2];
		_cstr[0] = c1;
		_cstr[1] = c2;
		post();
	}
	private void post(){
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
			if(_cstr[i].check()) return true;
		return false;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		//System.out.println(x.getCPManager().getInstantiatedVariables() + " --> ");
		//System.out.println(name() + "::propagateAssignValue(" + x.name() + "," + val + ")");
		for(int i = 0; i < _vars.length; i++)if(_vars[i] != x){
			ArrayList<Integer> V = _vars[i].getPossibleValues();
			for(int j = 0; j < V.size(); j++){
				int v = V.get(j);
				if(revise(_vars[i],v) == OutputRevise.FAILURE){
					_vars[i].removeValue(v);
					System.out.println(name() + "::propagateAssignValue(" + x.name() + "," + val + ") --> " + _vars[i].name() + 
							".removeValue(" + v + ")");
				}
			}
		}
		
		IConstraint ic = null;
		int nbF = 0;
		for(int i = 0; i < _cstr.length; i++){
			if(_cstr[i].instantiated()){
				if(!_cstr[i].check()) nbF++; else return OutputPropagation.SUSPEND;
			}else{
				ic = _cstr[i];
			}
		}
		if(nbF != _cstr.length-1) return OutputPropagation.SUSPEND;
		return ic.propagateAssignValue(x, val);
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
		// TODO Auto-generated method stub
		return OutputPropagation.SUSPEND;
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
		IConstraint ic = null;
		int nbF = 0;
		for(int i = 0; i < _cstr.length; i++){
			if(_cstr[i].instantiated()){
				if(!_cstr[i].check()) nbF++; else return false;
				
			}else{
				ic = _cstr[i];
			}
		}
		if(nbF != _cstr.length-1) return false;
		
		return ic.reviseAC3(x);
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		int count = 0;
		for(int i = 0; i < _cstr.length; i++){
			if(_cstr[i].revise(x, val) == OutputRevise.SUCCESS) return OutputRevise.SUCCESS;
			if(_cstr[i].revise(x, val) == OutputRevise.FAILURE) count++;
		}
		if(count == _cstr.length) return OutputRevise.FAILURE;
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
		String s = "OR(";
		for(int i = 0; i < _cstr.length; i++)
			s = s + _cstr[i].name() + ",";
		s = s + ")";
		return s;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
