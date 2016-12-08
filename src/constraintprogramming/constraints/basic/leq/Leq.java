/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic.leq;

import java.util.ArrayList;
import java.util.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Leq implements IConstraint {

	private IConstraint _c;
	
	public Leq(VarIntCP x, VarIntCP y){
		// semantic: x <= y
		_c = new LeqVarVar(x,y);
	}
	public Leq(VarIntCP x, int dx, VarIntCP y, int dy){
		// semantic: x + dx <= y + dy
		_c = new LeqVarVar(x,dx,y,dy);
	}
	public Leq(VarIntCP x, int dx, VarIntCP y){
		// semantic: x + dx <= y
		_c = new LeqVarVar(x,dx,y,0);
	}
	public Leq(VarIntCP x, VarIntCP y, int dy){
		// semantic: x <= y + dy
		_c = new LeqVarVar(x,0,y,dy);
	}
	public Leq(VarIntCP x, int v){
		_c = new LeqVarConstant(x,v);
	}
	
	@Override
	public boolean instantiated(){
		return _c.instantiated();
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return _c.check();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return _c.propagateAssignValue(x, val);
	}
	@Override 
	public OutputPropagation propagateRemoveValue(VarIntCP x, int val){
		return _c.propagateRemoveValue(x, val);
	}
	@Override
	public boolean reviseAC3(VarIntCP x){
		return _c.reviseAC3(x);
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		return _c.revise(x, val);
	}
	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return _c.getVariables();
	}

	@Override
	public String name(){
		String s = _c.name();
		return s;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}

}
