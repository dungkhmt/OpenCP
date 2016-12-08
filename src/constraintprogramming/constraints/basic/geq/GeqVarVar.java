/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.basic.geq;

import java.util.ArrayList;
import java.util.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class GeqVarVar implements IConstraint {

	private VarIntCP _x;
	private VarIntCP _y;
	private int _dx;
	private int _dy;
	private VarIntCP[] _vars;
	public GeqVarVar(VarIntCP x, VarIntCP y){
		// semantic: x <= y
		_x = x; _y = y;
		_dx = 0; _dy = 0;
		_vars = new VarIntCP[2];
		_vars[0] = _x;
		_vars[1] = _y;
		//_x.addBindAssignValue(this);
		//_y.addBindAssignValue(this);
	}
	public GeqVarVar(VarIntCP x, int dx, VarIntCP y, int dy){
		// semantic: x + dx <= y + dy
		_x = x; _y = y;
		_dx = dx; _dy = dy;
		_vars = new VarIntCP[2];
		_vars[0] = _x;
		_vars[1] = _y;
		//_x.addBindAssignValue(this);
		//_y.addBindAssignValue(this);
	}
	public GeqVarVar(VarIntCP x, int dx, VarIntCP y){
		// semantic: x + dx <= y
		_x = x; _y = y;
		_dx = dx; _dy = 0;
		_vars = new VarIntCP[2];
		_vars[0] = _x;
		_vars[1] = _y;
		//_x.addBindAssignValue(this);
		//_y.addBindAssignValue(this);
	}
	public GeqVarVar(VarIntCP x, VarIntCP y, int dy){
		// semantic: x <= y + dy
		_x = x; _y = y;
		_dx = 0; _dy = dy;
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
		// TODO Auto-generated method stub
		return _x.getValue() + _dx >= _y.getValue() + _dy;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		//x.getCPManager().log.println(name() + "::propagateAssignValue(" + x.name() + "," + val +")");
		System.out.println(name() + "::propagateAssignValue(" + x.name() + "," + val +")");
		if(x == _x){
			ArrayList<Integer> vy = _y.getPossibleValues();
			for(int i = 0; i < vy.size(); i++){
				int v = vy.get(i);
				if(val + _dx < v + _dy){
					_y.removeValue(v);
					/*
					x.getCPManager().log.println(name() + "::propagateAssignValue(" + x.name() + "," + val +") --> " 
					+ _y.name() + ".removeValue(" + v + ")");
					System.out.println(name() + "::propagateAssignValue(" + x.name() + "," + val +") --> " 
							+ _y.name() + ".removeValue(" + v + ")");
					*/		
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
				if(val + _dy < v + _dx){
					_x.removeValue(v);
					/*
					x.getCPManager().log.println(name() + "::propagateAssignValue(" + x.name() + "," + val +") --> " 
							+ _x.name() + ".removeValue(" + v + ")");
					*/
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
	public OutputPropagation propagateRemoveValue(VarIntCP x, int val){
		return OutputPropagation.SUSPEND;
	}
	@Override
	public boolean reviseAC3(VarIntCP x){
		boolean change = false;
		if(x == _x){
			Iterator itx = _x.getDomain().iterator();
			ArrayList<Integer> VX = new ArrayList<Integer>();
			while(itx.hasNext()){
				int vx = (Integer)itx.next();
				VX.add(vx);
			}
			for(int ix = 0; ix < VX.size(); ix++){
				int vx = VX.get(ix);
				boolean ok = false;
				Iterator ity = _y.getDomain().iterator();
				while(ity.hasNext()){
					int vy = (Integer)ity.next();
					if(vx + _dx >= vy + _dy){
						ok = true; break;
					}
				}
				if(!ok){
					_x.removeValue(vx);
					change = true;
				}
					
			}
		}else if(x == _y){
			Iterator ity = _y.getDomain().iterator();
			ArrayList<Integer> VY = new ArrayList<Integer>();
			
			while(ity.hasNext()){
				int vy = (Integer)ity.next();
				VY.add(vy);
			}
			for(int iy = 0; iy < VY.size(); iy++){
				int vy = VY.get(iy);
				boolean ok = false;
				Iterator itx = _x.getDomain().iterator();
				while(itx.hasNext()){
					int vx = (Integer)itx.next();
					if(vx + _dx >= vy + _dy){
						ok = true; break;
					}
				}
				if(!ok){
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
				if(val + _dx <= _y.getValue() + _dy) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
			}			
		}else if(x == _y){
			if(_x.instantiated()){
				if(_x.getValue() + _dx <= val + _dy) return OutputRevise.SUCCESS; else return OutputRevise.FAILURE;
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
	public String name(){
		String s = "GeqVarVar(" + _x.name() + "," + _dx + "," + _y.name() + "," + _dy + ")";
		return s;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}

}
