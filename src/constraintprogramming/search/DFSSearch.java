/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.search;

import java.util.*;
import java.util.Iterator;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.model.*;
public class DFSSearch {

	/**
	 * @param args
	 */
	protected CPManager m;
	protected VarIntCP[] x;
	protected int n;
	protected int nbSolutions;
	private int[] val;
	private HashMap<VarIntCP, Integer> map;
	private int nbInstantiated;
	private VariableSelector variableSelector;
	
	// store solutions to pool
	private ArrayList<Solution> solutionPool;
	private int maxNbSolutions;
	
	public DFSSearch(CPManager m){
		this.m = m;
		x = m.getVariables();
		val = new int[x.length];
		n = x.length;
		map = new HashMap<VarIntCP, Integer>();
		for(int i = 0; i < x.length; i++)
			map.put(x[i], i);
		
		variableSelector = new MinDomainVariableSelector(m);
	}
	public void setVariableSelector(VariableSelector sel){ variableSelector = sel;}
	public void solution(){
		nbSolutions++;
		for(int i = 0; i < val.length; i++)
			val[i] = x[i].getValue();
		
		solutionPool.add(new Solution(x));
		
		//System.out.println(name() + "::solution, nbSolutions = " + nbSolutions);
	}
	public String name(){ return "DFSSearch";}
	public int getNumberSolutions(){
		return nbSolutions;
	}
	public boolean hasSolution(){ return nbSolutions > 0;}
	
	public void TRYONE(VarIntCP z){
		int k = map.get(z);
		//m.log.println(name() + "::TRYONE(" + z.name() + ") nbInstantiated = " + nbInstantiated + ", nbSolutions = " + nbSolutions);
		if(nbSolutions > 0){
			//m.log.println(name() + "::TRYONE(" + z.name() + ") nbSolutions = " + nbSolutions + " RETURN");
			return;
		}
		
		ArrayList<Integer> V = x[k].getPossibleValues();
		for(int i = 0; i < V.size(); i++){
			int v = V.get(i);
			OutputPropagation rs = m.assignValue(x[k], v);
			nbInstantiated++;
			if(rs == OutputPropagation.SUSSCESS){
				//if(m.check())
					solution();
			}else if(rs == OutputPropagation.SUSPEND){
				VarIntCP nextZ = variableSelector.selectNextVaraible(z);//selectMinDomVar();
				if(nextZ == null){
					if(m.check()){
						solution();
					}
				}else{
					if(!hasSolution())
						TRYONE(nextZ);
				}
				
			}else{
				//System.out.println("Sudoku::TRY(" + k + ") failure");
				//System.exit(-1);
			}
			nbInstantiated--;
			m.recover();
		}
	}
	/*
	public void TRYONE(int k){
		//int k = map.get(z);
		System.out.println(name() + "::TRY(" + k + ") nbInstantiated = " + nbInstantiated + ", nbSolutions = " + nbSolutions);
		if(nbSolutions > 0){
			System.out.println(name() + "::TRY(" + k + ") nbSolutions = " + nbSolutions + " RETURN");
			return;
		}
		
		ArrayList<Integer> V = x[k].getPossibleValues();
		for(int i = 0; i < V.size(); i++){
			int v = V.get(i);
			OutputPropagation rs = m.assignValue(x[k], v);
			System.out.println(name() + "::TRY(" + k + ") assign x[" + k + "] = " + v);
			nbInstantiated++;
			if(rs == OutputPropagation.SUSSCESS){
				//if(m.check())
					solution();
			}else if(rs == OutputPropagation.SUSPEND){
				//VarIntCP nextZ = null;//x[k+1];//selectMinDomVar();
				//if(k < x.length-1) nextZ = x[k+1];
				if(k == x.length-1){
					if(m.check()){
						solution();
					}
				}else{
					if(!hasSolution())
						TRYONE(k+1);
				}
				
			}else{
				//System.out.println("Sudoku::TRY(" + k + ") failure");
				//System.exit(-1);
			}
			nbInstantiated--;
			m.recover();
		}
	}
	*/
	public void TRY(VarIntCP z){
		
		
		int k = map.get(z);
		//System.out.println(name() + "::TRY(" + k + ") nbSolutions = " + nbSolutions + 
		//		", maxNbSolutions = " + maxNbSolutions + ", solutionPool.sz = " + solutionPool.size());
		if(maxNbSolutions >= 0 && solutionPool.size() >= maxNbSolutions) return;
		
		/*
		Iterator it = x[k].getDomain().iterator();
		
		ArrayList<Integer> V = new ArrayList<Integer>();
		while(it.hasNext()){
			int v = (Integer)it.next();
			V.add(v);
		}
		*/
		
		
		ArrayList<Integer> V = x[k].getPossibleValues();
		//System.out.println(name() + "::TRY(" + k + ") V.sz =  " + V.size());
		for(int i = 0; i < V.size(); i++){
			int v = V.get(i);
			//System.out.println(name() + "::TRY(" + k + ") consider " + v);
			OutputPropagation rs = m.assignValue(x[k], v);
			nbInstantiated++;
			if(rs == OutputPropagation.SUSSCESS){
				//if(m.check())
					solution();
			}else if(rs == OutputPropagation.SUSPEND){
				VarIntCP nextZ = variableSelector.selectNextVaraible(z);//selectMinDomVar();
				if(nextZ == null){
					if(m.check()){
						//System.out.println(name() + "::solution");
						solution();
					}
				}else{
					TRY(nextZ);
				}
				/*
				if(k == n-1){
					if(m.check()){
						solution();
					}
				}else{
					if(!hasSolution()){
						TRYONE(k+1);
					}
				}
				*/
			}else{
				//System.out.println("Sudoku::TRY(" + k + ") failure");
				//System.exit(-1);
			}
			nbInstantiated--;
			m.recover();
		}
	}
	
	public void search(){
		nbSolutions = 0;
		maxNbSolutions = 1;
		solutionPool = new ArrayList<Solution>();
		
		VarIntCP z = variableSelector.selectFirstVariable();//selectMinDomVar();
		//System.out.println(name() + "::selectMinDomVar, z = " + map.get(z));
		m.initLog();
		TRYONE(z);
		//TRYONE(0);
		if(hasSolution()){
			for(int i = 0; i < x.length; i++)
				x[i].setValue(val[i]);
		}
		m.closeLog();
	}
	
	public void searchAll(){
		nbSolutions = 0;
		maxNbSolutions = -1;
		solutionPool = new ArrayList<Solution>();
		
		VarIntCP z = variableSelector.selectFirstVariable();//selectMinDomVar();
		//System.out.println(name() + "::selectMinDomVar, z = " + map.get(z));
		TRY(z);
		
		if(hasSolution()){
			for(int i = 0; i < x.length; i++)
				x[i].setValue(val[i]);
		}
	}
	public void searchMany(int maxNbSolutions){
		nbSolutions = 0;
		this.maxNbSolutions = maxNbSolutions;
		solutionPool = new ArrayList<Solution>();
		
		VarIntCP z = variableSelector.selectFirstVariable();//selectMinDomVar();
		//System.out.println(name() + "::selectMinDomVar, z = " + map.get(z));
		TRY(z);
		
		if(hasSolution()){
			for(int i = 0; i < x.length; i++)
				x[i].setValue(val[i]);
		}
	}
	public ArrayList<Solution> getSolutions(){
		return solutionPool;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
