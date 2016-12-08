/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic.eq;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.function.IFunction;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Eq implements IConstraint {
	private IConstraint _c;
	
	public Eq(VarIntCP x, VarIntCP y, int d){
		_c = new EqVarVar(x,y,d);
	}
	public Eq(VarIntCP x, int v){
		_c = new EqVarConstant(x,v);
	}
	public Eq(IFunction f1, IFunction f2){
		_c = new EqFunctionFunction(f1,f2);
	}
	public Eq(IFunction f, int c){
		_c = new EqFunctionConstant(f,c);
	}
	public Eq(int c, IFunction f){
		_c = new EqFunctionConstant(f,c);
	}
	public Eq(IFunction f, VarIntCP x){
		_c = new EqFunctionVar(f,x);
	}
	public Eq(VarIntCP x, IFunction f){
		_c = new EqFunctionVar(f,x);
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return _c.check();
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return _c.instantiated();
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return _c.propagateAssignValue(x, val);
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
		// TODO Auto-generated method stub
		return _c.propagateRemoveValue(x, value);
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
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
	public String name() {
		// TODO Auto-generated method stub
		return _c.name();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
