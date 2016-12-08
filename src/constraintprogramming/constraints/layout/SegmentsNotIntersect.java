package constraintprogramming.constraints.layout;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
import constraintprogramming.model.IConstraint;
import constraintprogramming.model.VarIntCP;

public class SegmentsNotIntersect implements IConstraint {

	public SegmentsNotIntersect(VarIntCP xA, VarIntCP yA, VarIntCP xB, VarIntCP yB,
			VarIntCP xC, VarIntCP yC, VarIntCP xD, VarIntCP yD){
		// semantic: segments (A--B) and (C--D) not not intersect
		
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
	public OutputRevise revise(VarIntCP x, int val) {
		// TODO Auto-generated method stub
		return null;
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

}
