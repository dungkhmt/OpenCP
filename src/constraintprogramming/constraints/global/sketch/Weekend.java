package constraintprogramming.constraints.global.sketch;

import java.util.HashMap;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Weekend implements IConstraint {

	private VarIntCP[] _x;
	private int _numWeekend;
	private int _maxWorkWeekend;
	private HashMap<VarIntCP, Integer> _map;
	
	public Weekend(VarIntCP[] x, int maxWorkWeekend){
		_x = x;
		_numWeekend = x.length/2;
		_maxWorkWeekend = maxWorkWeekend;
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
		
		int countWorkWeekend=0;
		for(int i = 0; i < _numWeekend; ++i)
		{
			if(_x[i*2].getValue() == 0 && _x[i*2+1].getValue() > 0 )
				return false;
			if(_x[i*2].getValue() > 0 && _x[i*2+1].getValue() == 0 )
				return false;
			if(_x[i*2].getValue() > 0)
				countWorkWeekend++ ;
		}
		if(countWorkWeekend > _maxWorkWeekend)
			return false;
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
		int weekInd = i/2;
		VarIntCP nearWeekenDay = _x[weekInd*2+1-i%2];
		if(val == 0)
		{
			if(nearWeekenDay.instantiated())
			{
				if(nearWeekenDay.getValue() != 0)
					return OutputPropagation.FAILURE;
			}
			else
			{
				if(!nearWeekenDay.isValue(0))
					return OutputPropagation.FAILURE;
				nearWeekenDay.assignValue(0);
			}
		}
		else
		{
			nearWeekenDay.removeValue(0);
			if(nearWeekenDay.domainEmpty())
				return OutputPropagation.FAILURE;
		}
		int countWorkedWeekend=0;
		for(i = 0; i < _numWeekend; ++i)
		{
			if(!_x[i*2].isValue(0) || !_x[i*2+1].isValue(0) )
				countWorkedWeekend++;
		}
		if(countWorkedWeekend > _maxWorkWeekend)
			return OutputPropagation.FAILURE;
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

