/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.model;
import java.util.*;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.enums.OutputRevise;
public interface IConstraint {
	public boolean check();
	public boolean instantiated();
	public OutputPropagation propagateAssignValue(VarIntCP x, int val);
	public OutputPropagation propagateRemoveValue(VarIntCP x, int value);
	public boolean reviseAC3(VarIntCP x);
	public OutputRevise revise(VarIntCP x, int val);
	public VarIntCP[] getVariables();
	public String name();
}
