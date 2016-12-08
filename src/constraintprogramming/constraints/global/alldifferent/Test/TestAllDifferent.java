package constraintprogramming.constraints.global.alldifferent.Test;

import constraintprogramming.constraints.basic.Neq;
import constraintprogramming.constraints.basic.eq.Eq;
import constraintprogramming.constraints.basic.leq.Leq;
import constraintprogramming.constraints.global.alldifferent.AllDifferent;
import constraintprogramming.model.CPManager;
import constraintprogramming.model.VarIntCP;
import constraintprogramming.search.DFSSearch;

class mySearch extends DFSSearch {
    VarIntCP[] x;

    public mySearch(CPManager m, VarIntCP[] x) {
        super(m);
        this.x = x;
    }

    @Override
    public void solution() {
        nbSolutions++;
        System.out.print(nbSolutions + "th solution, x = ");
        for (int i = 0; i < n; i++) System.out.print(x[i].getValue() + " ");
        System.out.println();
    }
}

public class TestAllDifferent {
    public static void main(String[] args) {
        /**
         * Constructor
         */
        int n;
        VarIntCP[] x;
        VarIntCP[] y;
        CPManager m;

        n = 4;
        x = new VarIntCP[n];
        m = new CPManager();
//        for(int i=0;i<n;i++){
//            x[i]= new VarIntCP(m,1,5,"x["+i+"]");
//        }
        x[0] = new VarIntCP(m, 1, 2, "x[0]");
        x[1] = new VarIntCP(m, 3, 5, "x[1]");
        x[2] = new VarIntCP(m, 1, 5, "x[2]");
        x[3] = new VarIntCP(m, 2, 5, "x[3]");
        /**
         * Model
         */
        //m.post(new Leq(x[0], 3));                // Uncomment to test AllDiff + Leq
        //m.post(new Eq(x[1], 2));
        //m.post(new Eq(x[2], 3));
        //m.post(new Eq(x[3], 4));
        m.post(new AllDifferent(x));                 // Global AllDifferent
        for (int i = 0; i < n; i++) {               // Compare with multi Binary Constraint
            for (int j = i + 1; j < n; j++) {
                m.post(new Neq(x[i], x[j]));
            }
        }
        m.close();

        /**
         * Search
         */
        long timeStart = System.nanoTime();
        mySearch ms = new mySearch(m, x);
        ms.searchAll();
        System.out.println("Number of solution : " + ms.getNumberSolutions());
        ms.solution();
        System.out.println((double) (System.nanoTime() - timeStart) / 1000_000_000);
    }
}
