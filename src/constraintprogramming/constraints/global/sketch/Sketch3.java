package constraintprogramming.constraints.global.sketch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class Sketch3 implements IConstraint{
	private VarIntCP[] _x;
	private int[] _low;
	private int[] _up;
	private int[][] _forbidenShifts;
	private HashMap<VarIntCP, Integer> _map;
	int nDay;
	int nShift;
	
	public Sketch3(VarIntCP[]x,int [][]forbidenShifts,int []low,int []up)
	{
		_x = x;
		_low = low;
		_up = up;
		_forbidenShifts=forbidenShifts;
		nDay=x.length;
		nShift=low.length;
		_map = new HashMap<VarIntCP, Integer>();
		for(int i= 0; i < _x.length; i++)
			_map.put(_x[i], i);
	}
	@Override
	public boolean check() {
		
		for(int i = 0; i < _x.length; i++)
			if(!_x[i].instantiated()) return false;
		
		int i = 0;
		int sumLoad=0;
		while(i < _x.length){
			int curShift=_x[i].getValue();
			int j = i+1;
			while(j < _x.length){
				if(_x[j].getValue() != curShift) break;
				j++;
			}
			
			if(j-i < _low[curShift] || j-i > _up[curShift])
			{
				//System.out.println("break range");
				return false;
				}
			
			if(j < _x.length){
				int nextShift=_x[j].getValue();
				if(_forbidenShifts[curShift][nextShift]==1)
				{
					//System.out.println("break forbinden");
					return false;
				}
			}
			
			i = j;			
		}
		return true;
	}

	
	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return false;
	}

	boolean isShouldContinue(int [][]a,int []low,int []up,int nDay,int nShift)
	{
		int [][][]edge=new int[nDay+1][nDay+1][nShift];
		int i,j;
		int d[]=new int[nDay];
		for(i=0;i<nDay+1;++i)
			for(j=0;j<nDay+1;++j)
				for(int t=0;t<nShift;++t)
				edge[i][j][t]=0;
		for(i=0;i<nShift;++i)
		{
			if(a[nDay-1][i]==1)
				d[nDay-1]=1;
			else
				d[nDay-1]=0;
			for(j=nDay-2;j>=0;--j)
			{
				if(a[j][i]==0)
					d[j]=0;
				else
					d[j]=d[j+1]+1;
			}
			//System.out.println(d[0]);
			for(j=0;j<nDay;++j)
			{
				for(int t=low[i];t<=Math.min(d[j],up[i]);t++)
					edge[j][j+t][i]=1;	
			}
		}
		int [][]mark=new int[nDay+1][nShift];
		for(i=0;i<=nDay;++i)
			for(j=0;j<nShift;++j)
				mark[i][j]=0;
		mark[0][0]=mark[0][1]=mark[0][2]=mark[0][3]=1;
		for(i=0;i<nDay+1;++i)
			for(int s1=0;s1<nShift;++s1)
			if(mark[i][s1]==1)
				for(j=i+1;j<=nDay;++j)
					for(int s2=0;s2<nShift;++s2)
					if(s1!=s2&&_forbidenShifts[s1][s2]==0&&edge[i][j][s2]==1)
						mark[j][s2]=1;
		for(i=0;i<nShift;++i)
			if(mark[nDay][i]==1)
				return true;
		return false;
	}
	void cutOff(int [][]a,int []low,int []up,int nDay,int nShift)
	{
		int i,j;
		int []b=new int[nShift];
		
		
		for(i=0;i<nDay;++i)
			for(j=0;j<nShift;++j)
			if(a[i][j]==1)
			{
				for(int t=0;t<nShift;++t)
					b[t]=a[i][t];
				for(int t=0;t<nShift;++t)
					if(t!=j)
						a[i][t]=0;
				if(isShouldContinue(a, low, up, nDay, nShift))
				{
					for(int t=0;t<nShift;++t)
						a[i][t]=b[t];
				}
				else{
					for(int t=0;t<nShift;++t)
						a[i][t]=b[t];
					a[i][j]=0;
					//System.out.println(a[i][j]+" ");
				}		
			}
	}
	
	public OutputPropagation cutOff()
	{
		int [][]a=new int[nDay][nShift];
		int i,j;
		for(i=0;i<nDay;++i)
			for(j=0;j<nShift;++j)
				a[i][j]=0;
		for(i=0;i<nDay;++i)
		{
			ArrayList<Integer>pos=_x[i].getPossibleValues();
			for(Integer t : pos)
			{
				a[i][t]=1;
			}
		}
		
		if(isShouldContinue(a, _low, _up, nDay, nShift))
		{
			System.out.println("Fail");
			return OutputPropagation.SUSPEND;
		}
		else
			return OutputPropagation.FAILURE;
	}
	
	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		for(int _shift=0;_shift<nShift;++_shift)
		{
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
			if(countL+countR+1<_low[_shift]||insR+insL>_up[_shift])
				return OutputPropagation.FAILURE;
			
			if(countL<_low[_shift])
			{
				for(j=0;j<_low[_shift]-countL;++j)
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
			if(countR<_low[_shift])
			{
				for(j=0;j<_low[_shift]-countR;++j)
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
		}
		return cutOff();
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
		return null;
	}

	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return _x;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "sketch v2";
	}

		// 111111
		// 333333
		// 222
		// 1,2 = 100; 3=150; minLoad=600,maxLoad=700
		// low=3,up=5
	
}
