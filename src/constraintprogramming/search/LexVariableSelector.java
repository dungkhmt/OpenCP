/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.search;

import constraintprogramming.model.*;

public class LexVariableSelector implements VariableSelector {

	private CPManager _cp;
	private VarIntCP[] _x;
	public LexVariableSelector(CPManager cp){
		_cp = cp;
		_x = _cp.getVariables();
	}
	@Override
	public VarIntCP selectFirstVariable(){
		if(_x == null) return null;
		if(_x.length <= 0) return null;
		int idx = 0;
		while(true){
			if(!_x[idx].instantiated()) return _x[idx];
			idx++;
			if(idx >= _x.length) break;
		}
		return null;
	}
	@Override
	public VarIntCP selectNextVaraible(VarIntCP x) {
		// TODO Auto-generated method stub
		int k = x.getID();
		if(k >= _x.length-1) return null;
		int idx = k+1;
		while(true){
			if(!_x[idx].instantiated()) return _x[idx];
			idx++;
			if(idx >= _x.length) break;
		}
		return null;//_x[k+1];
	}

}
