package constraintprogramming.applications;
import java.util.HashSet;

import constraintprogramming.constraints.basic.eq.Eq;
import constraintprogramming.constraints.basic.leq.Leq;
import constraintprogramming.function.plus.Plus;
import constraintprogramming.model.*;
import constraintprogramming.search.Minimizer;

public class TestOptimizer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CPManager mgr = new CPManager();
		int n = 6;
		VarIntCP[] x = new VarIntCP[n];
		HashSet<Integer> D = new HashSet<Integer>();
		for(int i = 1; i <= 2*n; i++)
			D.add(10*i);
		for(int i = 0; i < n; i++)
			x[i] = new VarIntCP(mgr,1,100,"x[" + i + "]");
		VarIntCP obj = null;//new VarIntCP(mgr,1,100,"obj");
		
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				mgr.post(new Leq(x[i],1,x[j]));
		VarIntCP z = new VarIntCP(mgr,D,"z");
		mgr.post(new Eq(z,new Plus(x[0],x[1])));
		
		for(int i = 2; i < n; i++){
			obj = new VarIntCP(mgr,1,100,"obj");
			mgr.post(new Eq(obj,new Plus(z,x[i])));
			z = obj;
		}
		mgr.post(new Eq(x[2],new Plus(x[1],x[0])));
		mgr.post(new Eq(new Plus(x[2],x[n-1]),60));
		mgr.close();
		
		Minimizer M = new Minimizer(obj);
		
		M.search();
		System.out.println("Has solution = " + M.hasSolution());
		for(int i = 0;  i < n; i++)
			System.out.println("x["+ i + "] = " + x[i].getValue());
		System.out.println("FINISHED, obj = " + obj.getValue());
		
	}

}
