package constraintprogramming.constraints.global.sketch;

import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Sketch implements IConstraint {

	private VarIntCP[] _x;
	private int _shift;
	private int _low;
	private int _up;
	private int[][] _forbidenShifts;
	private HashMap<VarIntCP, Integer> _map;
	
	public Sketch(VarIntCP[] x, int shift, int[][] forbidenShifts, int low, int up){
		_x = x;
		_shift = shift;
		_low = low;
		_up = up;
		_forbidenShifts = forbidenShifts;
		_map = new HashMap<VarIntCP, Integer>();
		for(int i= 0; i < _x.length; i++)
			_map.put(_x[i], i);
		
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		//boolean ok = true;
		
		for(int i = 0; i < _x.length; i++)
			if(!_x[i].instantiated()) return false;
		
		/*
		System.out.print(name() + "::check, x = ");
		for(int i = 0; i < _x.length; i++)
			System.out.print(_x[i].getValue() + ",");
		
		System.out.println("shift = " + _shift + ", low = " + _low + ", up = " + _up);
		*/
		
		int i = 0;
		boolean hasShift = false;
		while(i < _x.length){
			if(_x[i].getValue() != _shift){ i++; continue;}
			hasShift = true;
			
			int j = i+1;
			while(j < _x.length){
				if(_x[j].getValue() != _shift) break;
				j++;
			}
			//if(j < _x.length){
				if(j-i < _low || j-i > _up) return false;
			//}
			if(j < _x.length){
				// check if _x[j+1] is in _forbidenShifts
				if(_forbidenShifts[_shift][_x[j].getValue()]==1) return false;
			}
			i = j;			
		}
		//if(!hasShift && _low >= 1) return false;
		return true;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		if(_map.get(x) == null) return OutputPropagation.SUSPEND;
		
		int i = _map.get(x);
		if(_shift!=val)
			return OutputPropagation.SUSPEND;
		
		int countL=0;
		int insL=0;
		int j=i;
		while(j>=0&&_x[j].isValue(val))
		{
			
			if(_x[j].instantiated())
				insL++;
			j--;
		}
		countL=i-j-1;
		int countR=0;
		int insR=0;
		j=i+1;
		while(j<_x.length&&_x[j].isValue(val))
		{
			
			if(_x[j].instantiated())
				insR++;
			j++;
		}
		countR=j-i-1;
		if(countL+countR+1<_low||insR+insL>_up)
			return OutputPropagation.FAILURE;
		
		if(countL<_low)
		{
			for(j=0;j<_low-countL;++j)
				if(j+i>=_x.length)
					return OutputPropagation.FAILURE;
				else{
					if(!_x[i+j].isValue(val))
						return OutputPropagation.FAILURE;
					else
						if(!_x[i+j].instantiated())
							_x[i+j].assignValue(val);
				}
		}
		if(countR<_low)
		{
			for(j=0;j<_low-countR;++j)
				if(j>i)
					return OutputPropagation.FAILURE;
				else{
					if(!_x[i-j].isValue(val))
						return OutputPropagation.FAILURE;
					else
						if(!_x[i-j].instantiated())
							_x[i-j].assignValue(val);
				}
		}
		if(i < _x.length-1){
			if(!_x[i+1].instantiated()){
				if(val ==_shift){
					for(j = 0; j < _forbidenShifts[_shift].length; j++){
						int v;
						if(_forbidenShifts[_shift][j]==1)
							v=j;
						else
							continue;
						_x[i+1].removeValue(v);
						if(_x[i+1].domainEmpty()) return OutputPropagation.FAILURE;
					}
					if(_x[i+1].getDomain().size() == 1){
						java.util.Iterator it = _x[i+1].getDomain().iterator();
						int v = (int)it.next();
						_x[i+1].assignValue(v);
					}
				}
			}
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
		return false;
	}

	@Override
	public OutputRevise revise(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return OutputRevise.SUSPEND;
	}

	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return _x;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Sketch";
	}

}
