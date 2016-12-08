/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.model;

import java.io.PrintWriter;
import java.util.*;

import constraintprogramming.enums.*;

class VarConstraint{
	public VarIntCP x;
	public IConstraint c;
	public VarConstraint(VarIntCP x, IConstraint c){
		this.x = x;
		this.c = c;
	}
}
class VarValueConstraintAction{
	public VarIntCP x;
	public IConstraint c;
	public int value;
	public Action action;
	public VarValueConstraintAction(VarIntCP x, int value, IConstraint c, Action action){
		this.x = x;
		this.value = value;
		this.c = c;
		this.action = action;
	}
}
public class CPManager {

	/**
	 * @param args
	 */
	private HashSet<VarIntCP> _varSet;
	private VarIntCP[] _vars;
	private HashSet<IConstraint> _constraintSet;
	//private IConstraint[] _constraints;
	private HashMap<VarIntCP, ArrayList<IConstraint>> _mapVar2Constraint;
	private HashMap<VarIntCP, ArrayList<IConstraint>> _mapBindRemoveValue;
	private HashMap<VarIntCP, ArrayList<IConstraint>> _mapBindAssignValue;
	
	
	private ArrayList<VarConstraint> QVarConstraint = new ArrayList<VarConstraint>();
	private ArrayList<VarValueConstraintAction> QVarValueConstraintAction = new ArrayList<VarValueConstraintAction>();
	
	//public PrintWriter log;
	
	public CPManager(){
		_varSet = new HashSet<VarIntCP>();
		_constraintSet = new HashSet<IConstraint>();
		_mapVar2Constraint = new HashMap<VarIntCP, ArrayList<IConstraint>>();
		_mapBindRemoveValue = new HashMap<VarIntCP, ArrayList<IConstraint>>();
		_mapBindAssignValue = new HashMap<VarIntCP, ArrayList<IConstraint>>();
		initLog();
	}
	public void initLog(){
		try{
			//log = new PrintWriter("CPManager-log.txt");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void closeLog(){
		//log.close();
	}
	public boolean checkExist(VarValueConstraintAction vvca){
		for(int i = 0; i < QVarValueConstraintAction.size(); i++){
			VarValueConstraintAction e = QVarValueConstraintAction.get(i);
			if(vvca.action == e.action && vvca.c == e.c && vvca.value == e.value && vvca.x == e.x) return true;
		}
		return false;
	}
	public boolean checkExist(VarIntCP x, IConstraint c, int value, Action act){
		for(int i = 0; i < QVarValueConstraintAction.size(); i++){
			VarValueConstraintAction e = QVarValueConstraintAction.get(i);
			if(act == e.action && c == e.c && value == e.value && x == e.x) return true;
		}
		return false;
	}
	public boolean checkAndAdd(VarValueConstraintAction vvca){
		if(checkExist(vvca)) return false;
		QVarValueConstraintAction.add(vvca); 
		return true;
	}
	public boolean checkAndAdd(VarIntCP x, IConstraint c, int value, Action act){
		if(checkExist(x,c,value,act)) return false;
		if(c.instantiated()) return false;
		QVarValueConstraintAction.add(new VarValueConstraintAction(x, value, c, act)); 
		return true;
	}
	public void post(VarIntCP x){
		if(_varSet.contains(x)){
			//System.out.println("CPManager::post(VarIntCP), variable posted");
			return;
		}
		x.setID(_varSet.size());
		_varSet.add(x);
		//_mapBindRemoveValue.put(x,  new ArrayList<IConstraint>());
	}
	public void post(IConstraint c){
		//System.out.println(name() + "::post(" + c.name() + ")");
		_constraintSet.add(c);
		VarIntCP[] x = c.getVariables();
		for(int i = 0; i < x.length; i++){
			//_varSet.add(x[i]);
			post(x[i]);
			if(_mapVar2Constraint.get(x[i]) == null) _mapVar2Constraint.put(x[i], new ArrayList<IConstraint>());
			_mapVar2Constraint.get(x[i]).add(c);
			
			x[i].addBindAssignValue(c);
		}
	}
	public void printVars(){
		for(int i = 0; i < _vars.length; i++){
			System.out.println(_vars[i].name() + ", id = " + _vars[i].getID());
		}
		//System.out.println();
	}
	public void close(){
		_vars = new VarIntCP[_varSet.size()];
		Iterator it = _varSet.iterator();
		int idx = -1;
		while(it.hasNext()){
			VarIntCP x = (VarIntCP)it.next();
			idx++;
			_vars[idx] = x;
		}
		// sort _vars in an increasing order of ids
		for(int i = 0; i < _vars.length-1; i++){
			for(int j = i+1; j < _vars.length; j++){
				if(_vars[i].getID() > _vars[j].getID()){
					VarIntCP tmp = _vars[i]; _vars[i] = _vars[j]; _vars[j] = tmp;
				}
			}
		}
		/*
		_constraints = new IConstraint[_constraintSet.size()];
		idx = -1;
		it = _constraintSet.iterator();
		while(it.hasNext()){
			IConstraint c = (IConstraint)it.next();
			idx++;
			_constraints[idx] = c;
		}
		*/
	}
	public void addBindRemoveValue(VarIntCP x, IConstraint c){
		if(_mapBindRemoveValue.get(x) == null) _mapBindRemoveValue.put(x, new ArrayList<IConstraint>());
		_mapBindRemoveValue.get(x).add(c);
	}
	public void addBindAssignValue(VarIntCP x, IConstraint c){
		if(_mapBindAssignValue.get(x) == null) _mapBindAssignValue.put(x, new ArrayList<IConstraint>());
		_mapBindAssignValue.get(x).add(c);
	}
	public void removeBindAssignValue(VarIntCP x, IConstraint c){
		if(_mapBindAssignValue.get(x) != null){
			int idx = _mapBindAssignValue.get(x).indexOf(c);
			if(0 <= idx && idx < _mapBindAssignValue.get(x).size())
				_mapBindAssignValue.get(x).remove(idx);
		}
	}
	public void printBindAssignValue(){
		System.out.println("_vars.length = " + _vars.length);
		for(int i = 0; i < _vars.length; i++){
			ArrayList<IConstraint> C = _mapBindAssignValue.get(_vars[i]);
			/*
			log.println(_vars[i].name() + " binds to ");
			if(C == null) log.println("NULL"); 
			else{
				for(int j = 0; j < C.size(); j++)
					log.println(C.get(j).name() + ", ");
				
			}
			*/
		}
	}
	public OutputPropagation assignValue(VarIntCP x, int val){
		//log.println(name() + "::assignValue(" + x.name() + ", val = " + val + ")");
		for(int i = 0; i < _vars.length; i++)
			_vars[i].initStackRemove();
		
		QVarValueConstraintAction.clear();
		
		x.assignValue(val);
		//return AC3();
		return propagateAssign(x, val);
	}
	public boolean check(){
		//System.out.println("CPManager::check");
		for(int i = 0; i < _vars.length; i++){
			if(_vars[i].getDomain().size() == 0) return false;
			if(_vars[i].getDomain().size() > 1){
				System.out.println("CPManager::check, domain of _vars[" + i + "] is " + _vars[i].getDomain().size());
				System.exit(-1);
			}
		}
		/*
		for(int i = 0; i < _constraints.length; i++){
			if(!_constraints[i].check()) return false;
		}
		*/
		for(IConstraint c : _constraintSet){
			if(!c.check()) return false;
		}
		return true;
	}
	public void raiseRemoveValue(VarIntCP x, int val){
		ArrayList<IConstraint> C = _mapBindRemoveValue.get(x);//_mapVar2Constraint.get(x);
		if(C == null) return;
		for(int i = 0; i < C.size(); i++){
			IConstraint c = C.get(i);
			QVarValueConstraintAction.add(new VarValueConstraintAction(x, val, c, Action.REMOVEVALUE));
		}
		//System.out.println(name() + "::raiseRemoveValue(x[" + x.getID() + "]," + val + "), Q.sz = " + QVarValueConstraintAction.size());
	}
	public void raiseAssignValue(VarIntCP x, int val){
		ArrayList<IConstraint> C = _mapBindAssignValue.get(x);//_mapVar2Constraint.get(x);
		if(C == null) return;
		for(int i = 0; i < C.size(); i++){
			IConstraint c = C.get(i);
			//QVarValueConstraintAction.add(new VarValueConstraintAction(x, val, c, Action.ASSIGN));
			checkAndAdd(x, c, val, Action.ASSIGN);
		}
		//System.out.println(name() + "::raiseAssignValue(x[" + x.getID() + "]," + val + "), Q.sz = " + QVarValueConstraintAction.size());
	}
	
	public String name(){ return "CPManager";}
	private OutputPropagation propagateAssign(VarIntCP x, int val){
		//QVarValueConstraintAction.clear();
		/*
		ArrayList<IConstraint> cstr = _mapVar2Constraint.get(x);
		if(cstr == null) return OutputPropagation.SUSPEND;
		for(int i = 0; i < cstr.size(); i++){
			IConstraint ci = cstr.get(i);
			QVarValueConstraintAction.add(new VarValueConstraintAction(x, val, ci, Action.ASSIGN));
		}
		*/
		//System.out.println(name() + "::propagateAssign(" + x.getID() + "," + val + "), Q.sz = " + QVarValueConstraintAction.size());
		
		while(QVarValueConstraintAction.size() > 0){
			VarValueConstraintAction vvca = QVarValueConstraintAction.get(0);
			QVarValueConstraintAction.remove(0);
			IConstraint c = vvca.c;
			VarIntCP xi = vvca.x;
			Action act = vvca.action;
			int value = vvca.value;
			
			if(act == Action.ASSIGN){
				//log.println(name() + "::propagateAssign Start call " + c.name() + ".propagateAssign(" + xi.name() + "," + value + ")");
				//System.out.println(name() + "::propagateAssign Start call " + c.name() + ".propagateAssign(" + xi.name() + "," + value + ")");
				OutputPropagation out = c.propagateAssignValue(xi,value);
				if(out == OutputPropagation.SUSSCESS || out == OutputPropagation.FAILURE) return out;
			}else if(act == Action.REMOVEVALUE){
				OutputPropagation out = c.propagateRemoveValue(xi, value);
				if(out == OutputPropagation.SUSSCESS || out == OutputPropagation.FAILURE) return out;
			}
		}
		return OutputPropagation.SUSPEND;
	}
	private OutputPropagation AC3(){
		//System.out.println("CPManager::AC3 start....");
		//ArrayList<VarConstraint> Q = new ArrayList<VarConstraint>();
		QVarConstraint.clear();
		/*
		for(int i = 0; i < _constraints.length; i++){
			IConstraint c = _constraints[i];
			VarIntCP[] x = c.getVariables();
			for(int j = 0; j < x.length; j++){
				QVarConstraint.add(new VarConstraint(x[j],c));
			}
		}
		*/
		for(IConstraint c : _constraintSet){
			VarIntCP[] x = c.getVariables();
			for(int j = 0; j < x.length; j++){
				QVarConstraint.add(new VarConstraint(x[j],c));
			}
		}
		//System.out.println("CPManager::AC3, init Q.sz = " + QVarConstraint.size());
		while(QVarConstraint.size() > 0){
			VarConstraint vc = QVarConstraint.get(0);
			QVarConstraint.remove(0);
			VarIntCP x = vc.x;
			IConstraint c = vc.c;
			//System.out.println("CPManager::AC3, Q.sz = " + Q.size() + ", POP x = " + x.getID() + ", c = " + c.name());
			if(c.reviseAC3(x)){
				if(x.getDomain().size() == 0) 
					return OutputPropagation.FAILURE;
				else{
					ArrayList<IConstraint> C = _mapVar2Constraint.get(x);
					for(int i = 0; i < C.size(); i++){
						IConstraint ci = C.get(i);
						if(c != c){
							VarIntCP[] y = c.getVariables();
							for(int j = 0; j < y.length; j++) if(y[j] != vc.x){
								QVarConstraint.add(new VarConstraint(y[j],ci));
								//System.out.println("CPManager::AC3, Q.push(x = " + y[j].getID() + ", ci = " + ci.name());
							}
						}
					}
				}
			}
		}
		boolean ok = true;
		for(int i = 0; i < _vars.length; i++){
			if(_vars[i].getDomain().size() > 1) ok = false;
			if(_vars[i].getDomain().size() == 0) return OutputPropagation.FAILURE;
		}
		if(ok){
			if(check())
			return OutputPropagation.SUSSCESS;
		}
		
		return OutputPropagation.SUSPEND;
	}
	public void recover(){
		for(int i = 0; i < _vars.length; i++)
			_vars[i].recover();
	}
	public VarIntCP[] getVariables(){
		return _vars;
	}
	public ArrayList<IConstraint> getConstraints(VarIntCP x){
		return _mapVar2Constraint.get(x);
	}
	public String getInstantiatedVariables(){
		String s = "";
		for(int i = 0; i < _vars.length; i++)if(_vars[i].instantiated()){
			s = s + "[" + _vars[i].name() + ":" + _vars[i].getValue() + "]";
		}
		return s;
	}
	public void exit(int code){
		System.exit(code);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
