package constraintprogramming.constraints.global.alldifferent.Algo;

import java.util.HashSet;

/**
 * Created by Prime on 28/01/2015.
 */
public class Var {
    String name;
    HashSet<Integer> domain;

    public Var(String name){
        this.name=name;
        domain=new HashSet<Integer>();
    }
    public Var(int[] val){
        this.name="Var";
        domain=new HashSet<Integer>();
        for(int i : val){
            domain.add(i);
        }
    }

    public String getName(){
        return name;
    }
    public HashSet<Integer> getDomain(){
        return domain;
    }
    public void addValue(int i){
        domain.add(i);
    }
    public void removeValue(int i){
        domain.remove(i);
    }

}
