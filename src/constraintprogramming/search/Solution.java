package constraintprogramming.search;

import java.util.HashMap;

import constraintprogramming.model.VarIntCP;

public class Solution {

	private int[] sol;
	private VarIntCP[] x;
	private HashMap<VarIntCP, Integer> map;
	public Solution(VarIntCP[] x){
		map = new HashMap<VarIntCP, Integer>();

		sol = new int[x.length];
		for(int i = 0; i < x.length; i++){
			sol[i] = x[i].getValue();
			map.put(x[i], i);
		}
		this.x = x;
		
	}
	public String name(){
		return "Solution";
	}
	public int getValue(VarIntCP x){
		if(map.get(x) != null){
			return sol[map.get(x)];
		}
		System.out.println(name() + "::getValue, variable x is not legal");
		System.exit(-1);
		return 0;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
