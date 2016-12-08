/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.constraints.layout;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class SegmentAtMost implements IConstraint {

	public SegmentAtMost(VarIntCP x1, VarIntCP y1, VarIntCP x2, VarIntCP y2, double L){
		// semantic: segment (x1,y1) -- (x2,y2) must be less than or equal to L
	}
	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean instantiated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OutputPropagation propagateAssignValue(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reviseAC3(VarIntCP x) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public OutputRevise revise(VarIntCP x, int val){
		
		return OutputRevise.SUSPEND;
	}
	@Override
	public VarIntCP[] getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
