/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.applications;

import java.util.ArrayList;

import constraintprogramming.model.*;
import constraintprogramming.constraints.basic.*;
import constraintprogramming.search.*;

class QSearch extends DFSSearch{
	VarIntCP[] x;
	public QSearch(CPManager m, VarIntCP[] x){
		super(m);
		this.x = x;
	}
	
	public void solution1(){
		nbSolutions++;
		System.out.print(nbSolutions + "th solution, x = ");
		for(int i = 0; i < n; i++) System.out.print(x[i].getValue() + " "); System.out.println();
		int[][] a = new int[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				a[i][j] = 0;
		for(int i = 0; i < n; i++){
			a[i][x[i].getValue()] = 1;
		}
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}
	}
}
public class Queen {

	/**
	 * @param args
	 */
	private int n;
	private CPManager 	m;
	private VarIntCP[] 	x;
	
	public Queen(int n){
		this.n = n;
		m = new CPManager();
		x = new VarIntCP[n];
		for(int i = 0; i < n; i++)
			x[i] = new VarIntCP(m,0,n-1,"x[" + i + "]");
		
		for(int i1 = 0; i1 < n; i1++){
			for(int i2 = 0; i2 < n; i2++) if(i1 < i2){
				m.post(new Neq(x[i1],x[i2]));
				m.post(new Neq(x[i1],i1,x[i2],i2));
				m.post(new Neq(x[i1],-i1,x[i2],-i2));
			}
		}
		m.close();
		
		DFSSearch s = new DFSSearch(m);
		//QSearch s = new QSearch(m,x);
		//s.setVariableSelector(new LexVariableSelector(m));
		s.search();
		
		//s.searchMany(20);
		
		
		ArrayList<Solution> solutions = s.getSolutions();
		for(Solution sol : solutions){
			for(int i = 0; i < x.length; i++){
				int v = sol.getValue(x[i]);
				System.out.print(v + " ");
			}
			System.out.println();
		}
		System.out.println("number of solutions = " + s.getNumberSolutions() + ", solutions.sz = " + solutions.size());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double t0 = System.currentTimeMillis();
		Queen Q = new Queen(8);
		double t = System.currentTimeMillis() - t0;
		t = t * 0.001;
		System.out.println("time = " + t);
	}

}
