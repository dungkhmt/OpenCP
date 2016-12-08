/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.applications;

import java.util.*;
import java.io.*;


import constraintprogramming.constraints.basic.Implicate;
import constraintprogramming.model.*;
import constraintprogramming.constraints.basic.*;
import constraintprogramming.constraints.basic.eq.*;
import constraintprogramming.constraints.basic.leq.*;
import constraintprogramming.search.*;

class Item{
	int w,h;
	public Item(int w, int h){
		this.w = w; this.h = h;
	}
}
public class BinPacking2D {

	public int W, H;
	public int n;
	public int[] w;
	public int[] h;
	
	
	CPManager cp;
	VarIntCP[] x;
	VarIntCP[] y;
	VarIntCP[] o;
	public BinPacking2D(){
		
	}
	public void readData(String fn){
		try{
			Scanner in = new Scanner(new File(fn));
			W = in.nextInt();
			H = in.nextInt();
			ArrayList<Item> I = new ArrayList<Item>();
			while(true){
				int wi = in.nextInt();
				if(wi == -1) break;
				int hi = in.nextInt();
				I.add(new Item(wi,hi));
			}
			in.close();
			
			n = I.size();
			w = new int[n];
			h = new int[n];
			for(int i = 0; i < I.size(); i++){
				w[i] = I.get(i).w;
				h[i] = I.get(i).h;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void stateModel(){
		cp = new CPManager();
		
		
		x = new VarIntCP[n];
		y = new VarIntCP[n];
		o = new VarIntCP[n];
		for (int i = 0; i < n; i++) {
			x[i] = new VarIntCP(cp, 0, W, "x[" + i + "]");
			y[i] = new VarIntCP(cp, 0, H, "y[" + i + "]");
			o[i] = new VarIntCP(cp, 0, 1, "o[" + i + "]");
		}

		for (int i = 0; i < n; i++) {
			cp.post(new Implicate(new Eq(o[i],0), 
					new Leq(x[i],W-w[i])));
			cp.post(new Implicate(new Eq(o[i],0), 
					new Leq(y[i],H-h[i])));
			cp.post(new Implicate(new Eq(o[i],1), 
					new Leq(x[i],W-h[i])));
			cp.post(new Implicate(new Eq(o[i],1), 
					new Leq(y[i],H-w[i])));
		}

		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				IConstraint[] c = new IConstraint[4];
				c[0] = new Leq(x[j], w[j], x[i]); // l1.x>r2.x
				c[1] = new Leq(x[i], w[i], x[j]); // l2.x>r1.x
				c[2] = new Leq(y[i], h[i], y[j]); // l1.y<r2.y
				c[3] = new Leq(y[j], h[j], y[i]); // l2.y<r1.y
				cp.post(new Implicate(new AND(new Eq(o[i], 0), new Eq(o[j], 0)), new OR(c)));

				// o[i] = o, o[j] = 1
				c = new IConstraint[4];
				c[0] = new Leq(x[j], h[j], x[i]); // l1.x>r2.x
				c[1] = new Leq(x[i], w[i], x[j]); // l2.x>r1.x
				c[2] = new Leq(y[i], h[i], y[j]); // l1.y<r2.y
				c[3] = new Leq(y[j], w[j], y[i]); // l2.y<r1.y
				cp.post(new Implicate(new AND(new Eq(o[i], 0), new Eq(
						o[j], 1)), new OR(c)));

				// o[i] = 1, o[j] = 0
				c = new IConstraint[4];
				c[0] = new Leq(x[j], w[j], x[i]); // l1.x>r2.x
				c[1] = new Leq(x[i], h[i], x[j]); // l2.x>r1.x
				c[2] = new Leq(y[i], w[i], y[j]); // l1.y<r2.y
				c[3] = new Leq(y[j], h[j], y[i]); // l2.y<r1.y
				cp.post(new Implicate(new AND(new Eq(o[i], 1), new Eq(
						o[j], 0)), new OR(c)));

				// o[i] = 1, o[j] = 1
				c = new IConstraint[4];
				c[0] = new Leq(x[j], h[j], x[i]); // l1.x>r2.x
				c[1] = new Leq(x[i], h[i], x[j]); // l2.x>r1.x
				c[2] = new Leq(y[i], w[i], y[j]); // l1.y<r2.y
				c[3] = new Leq(y[j], w[j], y[i]); // l2.y<r1.y
				cp.post(new Implicate(new AND(new Eq(o[i], 1), new Eq(
						o[j], 1)), new OR(c)));

			}
		}

		cp.close();
		//cp.printBindAssignValue();
		//cp.closeLog();
		//System.exit(-1);
		//cp.printVars();
		//System.exit(-1);
	}
	
	public void search(){
		cp.printVars();
		DFSSearch s = new DFSSearch(cp);
		//s.setVariableSelector(new LexVariableSelector(cp));
		s.search();
		System.out.println("Solution = " + s.getNumberSolutions());
	}
	public void solve(){
		stateModel();
		search();
	}
	public void printSolution(){
		for(int i = 0; i < n; i++){
			System.out.println("Item " + i + ": "  + x[i].getValue() + "," + y[i].getValue());
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinPacking2D bp = new BinPacking2D();
		bp.readData("data\\BinPacking2D\\bin-packing-2D-W10-H8-I6.txt");
		bp.solve();
		bp.printSolution();
	}

}
