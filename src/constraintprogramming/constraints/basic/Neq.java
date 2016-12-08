/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic;

import java.util.ArrayList;
import java.util.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Neq implements IConstraint {

	private VarIntCP[] _vars;
	private VarIntCP _x;
	private VarIntCP _y;
	private int _dx;
	private int _dy;

	public Neq(VarIntCP x, int dx, VarIntCP y, int dy) {
		this._x = x;
		this._y = y;
		this._dx = dx;
		this._dy = dy;
		_vars = new VarIntCP[2];
		_vars[0] = _x;
		_vars[1] = _y;
		//_x.addBindAssignValue(this);
		//_y.addBindAssignValue(this);
	}

	public Neq(VarIntCP x, VarIntCP y) {
		this._x = x;
		this._y = y;
		this._dx = 0;
		this._dy = 0;
		_vars = new VarIntCP[2];
		_vars[0] = _x;
		_vars[1] = _y;
		//_x.addBindAssignValue(this);
		//_y.addBindAssignValue(this);
	}

	@Override
	public boolean instantiated(){
		for(int i = 0; i < _vars.length; i++)
			if(_vars[i].getDomain().size() != 1) return false;
		return true;
	}
	@Override
	public boolean check() {
		if(!instantiated()) return false;
		// TODO Auto-generated method stub
		boolean ok = _x.getValue() + _dx != _y.getValue() + _dy;
		// System.out.println(name() + ", _x = " + _x.getValue() + ", _y = " +
		// _y.getValue() + " --> check = " + ok);
		return ok;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		if(x == _x){
			ArrayList<Integer> vy = _y.getPossibleValues();
			boolean change = false;
			for(int i = 0; i < vy.size(); i++){
				int v = vy.get(i);
				if(val + _dx == v + _dy){
					_y.removeValue(v);
					//change = true;
					//_y.getCPManager().log.println(name() + "::propagateAssignvalue --> " + _y.name() + ".removeValue(" + v + ")");
					//System.out.println(name() + "::propagateAssignvalue --> " + _y.name() + ".removeValue(" + v + ")");
					if(_y.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			
			if(change)if(_y.getDomain().size() == 1){
				Iterator it = _y.getDomain().iterator();
				int v = (int)it.next();
				_y.assignValue(v);				
			}
			
		}else if(x == _y){
			ArrayList<Integer> vx = _x.getPossibleValues();
			boolean change = false;
			for(int i = 0; i < vx.size(); i++){
				int v = vx.get(i);
				if(val + _dy == v + _dx){
					_x.removeValue(v);
					//schange = true;
					//_x.getCPManager().log.println(name() + "::propagateAssignvalue --> " + _x.name() + ".removeValue(" + v + ")");
					//System.out.println(name() + "::propagateAssignvalue --> " + _x.name() + ".removeValue(" + v + ")");
					if(_x.domainEmpty()) return OutputPropagation.FAILURE;
				}
			}
			
			if(change)if(_x.getDomain().size() == 1){
				Iterator it = _x.getDomain().iterator();
				int v = (int)it.next();
				_x.assignValue(v);
				
			}
			
		}
		return OutputPropagation.SUSPEND;
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int val) {
		
		return OutputPropagation.SUSPEND;
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
		boolean change = false;
		if (x == _x) {
			Iterator itx = _x.getDomain().iterator();
			ArrayList<Integer> VX = new ArrayList<Integer>();
			while (itx.hasNext()) {
				int vx = (Integer) itx.next();
				VX.add(vx);
			}
			for (int ix = 0; ix < VX.size(); ix++) {
				int vx = VX.get(ix);
				boolean ok = false;
				Iterator ity = _y.getDomain().iterator();
				while (ity.hasNext()) {
					int vy = (Integer) ity.next();
					if (vx + _dx != vy + _dy) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					_x.removeValue(vx);
					change = true;
				}

			}
		} else if (x == _y) {
			Iterator ity = _y.getDomain().iterator();
			ArrayList<Integer> VY = new ArrayList<Integer>();

			while (ity.hasNext()) {
				int vy = (Integer) ity.next();
				VY.add(vy);
			}
			for (int iy = 0; iy < VY.size(); iy++) {
				int vy = VY.get(iy);
				boolean ok = false;
				Iterator itx = _x.getDomain().iterator();
				while (itx.hasNext()) {
					int vx = (Integer) itx.next();
					if (vx + _dx != vy + _dy) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					_y.removeValue(vy);
					change = true;
				}

			}
		}
		return change;
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		if(x == _x){
			if(_y.instantiated()){
				if(val + _dx != _y.getValue() + _dy) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
			}			
		}else if(x == _y){
			if(_x.instantiated()){
				if(_x.getValue() + _dx != val + _dy) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
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
		String s = "Neq(" + _x.name();
		// if(_dx < 0) s = s + " " + _dx; else s = s + " + " + _dx;
		s = s + " + " + _dx;
		s = s + " != " + _y.name();
		s = s + " + " + _dy;
		// if(_dy < 0) s = s + " " + _dy; else s = s + " + " + _dy;
		s = s + ")";
		return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
