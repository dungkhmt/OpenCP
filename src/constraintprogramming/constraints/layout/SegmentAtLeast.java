/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.layout;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class SegmentAtLeast implements IConstraint {
	private VarIntCP x1;
	private VarIntCP y1;
	private VarIntCP x2;
	private VarIntCP y2;
	
	private double L;
	private VarIntCP[] _vars;
	
	public SegmentAtLeast(VarIntCP x1, VarIntCP y1, VarIntCP x2, VarIntCP y2, double L){
		// semantic: segment (x1,y1) -- (x2,y2) must be greater than or equal to L
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; this.L = L;
		_vars = new VarIntCP[4];
		_vars[0] = x1; _vars[1] = y1; _vars[2] = x2; _vars[3] = y2;
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		if(!instantiated()) return false;
		
		double d = Math.sqrt(Math.pow((x1.getValue() - x2.getValue()),2) + Math.pow((y1.getValue() - y2.getValue()),2)); 
		return false;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		for(VarIntCP v : _vars) 
			if(!v.instantiated()) return false;
		return true;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		if(x1.instantiated() && y1.instantiated()){
			if(x == x2){
				// remove values from domain of y2
				int minY2 = -1;
				int maxY2 = -1;
				for(int v = minY2; v <= maxY2; v++){
					y2.removeValue(v);
					if(y2.domainEmpty()) return OutputPropagation.FAILURE;
				}
				if(y2.getDomain().size() == 1){
					for(int v : y2.getDomain()){
						y2.assignValue(v);
						break;
					}
				}
			}else if(x == y2){
				// remove values from domain of x2
				
			}
		}else if(x2.instantiated() && y2.instantiated()){
			
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
		System.out.println(name() + "::reviseAV3 --> NOT IMPLEMENTED");
		System.exit(-1);
		return false;
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		
		return OutputRevise.SUSPEND;
	}
	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return _vars;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "SegmentAtLeast";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
