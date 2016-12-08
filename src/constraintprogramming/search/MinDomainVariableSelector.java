/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.search;

import constraintprogramming.model.*;

public class MinDomainVariableSelector implements VariableSelector {

	private CPManager _cp;
	private VarIntCP[] _x;
	
	public MinDomainVariableSelector(CPManager cp) {
		// TODO Auto-generated constructor stub
		this._cp = cp;
		_x = _cp.getVariables();
	}
	
	@Override
	public VarIntCP selectFirstVariable() {
		// TODO Auto-generated method stub
		return selectNextVaraible(null);
	}
	@Override
	public VarIntCP selectNextVaraible(VarIntCP x) {
		// TODO Auto-generated method stub
		VarIntCP x_min = null;
		int minDomainSz = 10000000;
		for(int j = 0; j < _x.length; j++){
			if(_x[j].getDomain().size() > 1){
				if(_x[j].getDomain().size() < minDomainSz){
					x_min = _x[j];
					minDomainSz = _x[j].getDomain().size();
				}
			}
		}
		//System.out.println(name() + "::selectMinDomVar, minDomainSz = " + minDomainSz + ", x_min = " + map.get(x_min));
		return x_min;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
