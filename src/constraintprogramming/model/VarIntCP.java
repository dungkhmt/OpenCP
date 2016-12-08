/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.model;

import core.VarInt;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayList;
public class VarIntCP extends VarInt implements ITrailableVariable{

	/**
	 * @param args
	 */
	private Stack<Integer> stack = null;
	private int nullValue;
	private HashSet<Integer> _domainSet;
	private String _name;
	private CPManager _cp;
	public VarIntCP(CPManager cp, int min, int max, String name){
		super(min,max);
		this._name = name;
		stack = new Stack<Integer>();
		nullValue = _min-1;
		_domainSet = new HashSet<Integer>();
		for(int v = _min; v <= _max; v++)
			_domainSet.add(v);
		_cp = cp;
		cp.post(this);
	}
	public VarIntCP(CPManager cp, Set<Integer> D, String name){
		super(D);
		this._name = name;
		stack = new Stack<Integer>();
		nullValue = _min-1;
		_domainSet = new HashSet<Integer>();
		for(int v : D)
			_domainSet.add(v);
		_cp = cp;
		_cp.post(this);
	}
	public CPManager getCPManager(){
		return _cp;
	}
	public String name(){ return _name;}
	public void recover(){
		while(stack.size() > 0){
			int v = stack.pop();
			if(v == nullValue) break;
			enableValue(v);
			_domainSet.add(v);
		}
	}
	public boolean instantiated(){
		if(_domainSet.size() == 1){
			Iterator it = _domainSet.iterator();
			int v = (int)it.next();
			if(v == _value) return true;
		}
		return false;
	}
	public void assignValue(int val){
		if(!_domainSet.contains(val)){
			System.out.println("VarIntCP::assignValue(val) --> exception ????????, " + val + " is not in the domain");
			System.exit(-1);
		}
		//System.out.println("VarIntCP[" + getID() + "]::assignValue(" + val + "), _domainSet.sz = " + _domainSet.size());
		_value = val;
		Iterator it = _domainSet.iterator();
		while(it.hasNext()){
			int v = (Integer)it.next();
			if(v != val){
				stack.push(v);
				disableValue(v);
			}
		}
		_domainSet.clear();
		_domainSet.add(val);
		_cp.raiseAssignValue(this, val);
	}
	public void removeValue(int value){
		stack.push(value);
		_domainSet.remove(value);
		disableValue(value);
		if(_domainSet.size() == 1){
			_value = (Integer) _domainSet.iterator().next();
		}			
		_cp.raiseRemoveValue(this, value);
	}
	public void initStackRemove(){
		stack.push(nullValue);
	}
	public HashSet<Integer> getDomain(){ return _domainSet;}
	public boolean domainEmpty(){ return _domainSet.size() == 0;}
	public ArrayList<Integer> getPossibleValues(){
		ArrayList<Integer> V = new ArrayList<Integer>();
		Iterator it = _domainSet.iterator();
		while(it.hasNext()){
			int v = (Integer)it.next();
			V.add(v);
		}
		return V;
	}
	public int size(){
		return _domainSet.size();
	}
	public boolean singleton(){
		return size() == 1;
	}
	public int getAValueInDomain(){
		int ret = 0;
		if(size() == 0){
			System.out.println(name() + "::getAValueInDomain --> EXCEPTION, domain is empty");
			_cp.exit(-1);
		}
		for(int v : _domainSet){
			ret = v;
			break;
		}
		return ret;
	}
	
	public void addBindRemoveValue(IConstraint c){
		_cp.addBindRemoveValue(this, c);
	}
	public void addBindAssignValue(IConstraint c){
		//System.out.println(name() + "::addBindAssignValue(" + c.name() + ")");
		_cp.addBindAssignValue(this, c);
	}
	public void removeBindAssignValue(IConstraint c){
		_cp.removeBindAssignValue(this, c);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
