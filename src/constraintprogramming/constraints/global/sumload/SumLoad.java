package constraintprogramming.constraints.global.sumload;

import java.util.ArrayList;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class SumLoad implements IConstraint {

	private VarIntCP[] _x;
	private int[] _w;
	private int _low;
	private int _up;
	private int _min;
	private int _max;
	public SumLoad(VarIntCP[] x, int[] w, int low,  int up){
		// semantic: low <= \sum_{v=0}^{w.length-1}\sum{i=0}^{x.length-1}(x[i] = v)*w[v] <= up 
		_x = x; _w = w; _low = low; _up = up;
		if(w.length>0)
		{
			_min=_max=w[0];
		}
		for(int i=1;i<w.length;++i)
		{
			if(_min>w[i])
				_min=w[i];
			if(_max<w[i])
				_max=w[i];
		}
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		//System.out.println("aaaa");
		if(!instantiated()) return false;
		int load = 0;
		//for(int i=0;i<_x.length;++i)
			//System.out.print(_x[i].getValue()+"  ");
		//System.out.println();
		
		for(int i = 0; i < _x.length; i++){
				load += _w[_x[i].getValue()];
		}
		
		
		return _low <= load && load <= _up;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		for(int i = 0; i < _x.length; i++)
			if(!_x[i].instantiated()) return false;
		return true;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		int load = 0;
		int cnt=0;
		for(int i=0;i<_x.length;++i)
			if(_x[i].instantiated())
			{
				cnt++;
				load+=_x[i].getValue();
			}
		
		
		if(load+(_x.length-cnt)*_max<_low)
			return OutputPropagation.FAILURE;
		if(load+(_x.length-cnt)*_min>_up)
			return OutputPropagation.FAILURE;
		if(load+(_x.length-cnt)*_max==_low)
		{
			for(int i=0;i<_x.length;++i)
				if(!_x[i].instantiated())
				{
					ArrayList<Integer>pos=_x[i].getPossibleValues();
					for(Integer vl:pos)
					{
						if(_w[vl]!=_max)
							_x[i].removeValue(vl);
						if(_x[i].domainEmpty()) return OutputPropagation.FAILURE;
					}
					if(_x[i].getDomain().size() == 1){
						java.util.Iterator it = _x[i].getDomain().iterator();
						int v = (int)it.next();
						_x[i].assignValue(v);
					}
				}
		}
		if(load+(_x.length-cnt)*_min==_up)
		{
			for(int i=0;i<_x.length;++i)
				if(!_x[i].instantiated())
				{
					ArrayList<Integer>pos=_x[i].getPossibleValues();
					for(Integer vl:pos)
					{
						if(_w[vl]!=_min)
							_x[i].removeValue(vl);
						if(_x[i].domainEmpty()) return OutputPropagation.FAILURE;
					}
					if(_x[i].getDomain().size() == 1){
						java.util.Iterator it = _x[i].getDomain().iterator();
						int v = (int)it.next();
						_x[i].assignValue(v);
					}
				}
		}
		return OutputPropagation.SUSPEND;
		
		/*
		if(load <= _up&&(cnt<_x.length||load>_low))
			return OutputPropagation.SUSPEND;
		else
			return OutputPropagation.FAILURE;
		*/
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
		return "SumLoad";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
