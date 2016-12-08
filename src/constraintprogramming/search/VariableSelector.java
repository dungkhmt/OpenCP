/*
 * Author: PHAM Quang Dung (dungkhmt@gmail.com)
 */
package constraintprogramming.search;
import constraintprogramming.model.*;
public interface VariableSelector {
	VarIntCP selectNextVaraible(VarIntCP x);
	VarIntCP selectFirstVariable();
}
