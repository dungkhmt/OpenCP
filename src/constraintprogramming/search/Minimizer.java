package constraintprogramming.search;

import constraintprogramming.constraints.basic.lt.Lt;
import constraintprogramming.model.VarIntCP;
import constraintprogramming.model.CPManager;

class MSearcher extends DFSSearch{
	private Minimizer minimizer;
	private VarIntCP obj;
	private CPManager mgr;
	public MSearcher(Minimizer minimizer){
		super(minimizer.getCPManager());
		this.minimizer = minimizer;
		this.obj = minimizer.getObjective();
		this.mgr = minimizer.getCPManager();
	}
	
	public void solution(){
		System.out.println("MSearch::solution trigged");
		super.solution();
		minimizer.setIncumbentValue(obj.getValue());
		mgr.post(new Lt(obj,minimizer.getIncumbentValue()));
	}
	
}
public class Minimizer {
	protected VarIntCP obj;
	protected int incumentValue;
	private MSearcher searcher;
	private CPManager mgr;
	public Minimizer(VarIntCP obj){
		this.obj  = obj;
		mgr = obj.getCPManager();
		searcher = new MSearcher(this);
	}
	public CPManager getCPManager(){
		return mgr;
	}
	public VarIntCP getObjective(){
		return obj;
	}
	public void setIncumbentValue(int incumbentvalue){
		this.incumentValue = incumbentvalue;
	}
	public int getIncumbentValue(){
		return incumentValue;
	}
	public void search(){
		searcher.searchAll();
	}
	public boolean hasSolution(){
		return searcher.hasSolution();
	}
}
