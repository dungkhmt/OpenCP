package constraintprogramming.applications;

import java.util.*;

import constraintprogramming.model.*;
import constraintprogramming.search.DFSSearch;
import constraintprogramming.search.LexVariableSelector;
import constraintprogramming.constraints.basic.*;
import constraintprogramming.enums.OutputPropagation;
public class Sudoku {

	/**
	 * @param args
	 */
	private CPManager m;
	private VarIntCP[] x;
	private VarIntCP[][] y;
	private int n;
	private int N;
	private boolean hasSolution = false;
	public Sudoku(int n){
		m = new CPManager();
		this.n = n; N = n*n;
		y = new VarIntCP[N][N];
		int idx = -1;
		x = new VarIntCP[N*N];
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++){
				y[i][j] = new VarIntCP(m,1,N,"y[" + i + "," + j + "]");
				idx++;
				x[idx] = y[i][j];
			}
		
		for(int i = 0; i < N; i++){
			for(int j1 = 0; j1 < N-1; j1++)
				for(int j2 = j1+1; j2 < N; j2++)
					m.post(new Neq(y[i][j1],y[i][j2]));
		}
		for(int j = 0; j < N; j++){
			for(int i1 = 0; i1 < N-1; i1++)
				for(int i2 = i1+1; i2 < N; i2++)
					m.post(new Neq(y[i1][j],y[i2][j]));
		}
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				VarIntCP[] z = new VarIntCP[N];
				idx = -1;
				for(int i1 = 0; i1 < n; i1++){
					for(int j1 = 0; j1 < n; j1++){
						idx++;
						z[idx] = y[i*n+i1][j*n+j1];
					}
				}
				for(int i1 = 0; i1 < N-1; i1++)
					for(int i2 = i1+1; i2 < N; i2++)
						m.post(new Neq(z[i1],z[i2]));
			}
		}
		m.close();
		
		
	}
	public void solution(){
		System.out.println("Solution: ");
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++)
				System.out.print(y[i][j].getValue() + " ");
			System.out.println();
		}
	}
	public void TRY(int k){
		//System.out.println("TRY(" + k + ") hasSolution = " + hasSolution);
		if(hasSolution) return;
		Iterator it = x[k].getDomain().iterator();
		ArrayList<Integer> V = new ArrayList<Integer>();
		while(it.hasNext()){
			int v = (Integer)it.next();
			V.add(v);
		}
		for(int i = 0; i < V.size(); i++){
			int v = V.get(i);
			OutputPropagation rs = m.assignValue(x[k], v);
			if(rs == OutputPropagation.SUSSCESS){
				hasSolution = true;
				solution();
			}else if(rs == OutputPropagation.SUSPEND){
				if(k == N*N-1){
					if(m.check()){
						hasSolution = true;
						solution();
					}
				}else{
					if(!hasSolution)
					TRY(k+1);
				}
			}else{
				//System.out.println("Sudoku::TRY(" + k + ") failure");
				//System.exit(-1);
			}
			m.recover();
		}
	}
	public void search(){
		//m.printVars();
		//if(true) return;
		//hasSolution= false;
		//TRY(0);
		//if(true) return;
		DFSSearch s = new DFSSearch(m);
		s.setVariableSelector(new LexVariableSelector(m));
		s.search();
		if(s.hasSolution()){
			solution();
		}else{
			System.out.println("No solution found");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sudoku S = new Sudoku(2);
		S.search();
		//S.TRY(0);
	}

}
