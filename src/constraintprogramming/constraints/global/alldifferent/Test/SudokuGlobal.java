package constraintprogramming.constraints.global.alldifferent.Test;

import constraintprogramming.constraints.basic.eq.Eq;
import constraintprogramming.constraints.global.alldifferent.AllDifferent;
import constraintprogramming.model.CPManager;
import constraintprogramming.model.VarIntCP;
import constraintprogramming.search.DFSSearch;
import constraintprogramming.search.LexVariableSelector;

public class SudokuGlobal {
    public CPManager m;
    public VarIntCP[][] y;
    public int n;
    public int N;

    public SudokuGlobal(int n) {
        this.n = n;
        N = n * n;
        y = new VarIntCP[N][N];
        m = new CPManager();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                y[i][j] = new VarIntCP(m, 1, N, "y[" + i + "," + j + "]");
            }
        }
    }

    public void model() {
        for (int i = 0; i < N; i++) {
            //m.post(new AllDifferent(y[i]));
        }
        m.post(new Eq(y[0][0], y[0][1], 0));        // Test => Memory over flow ???
        m.close();
    }

    public void printSolution() {
        System.out.println("Solution: ");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(y[i][j].getValue() + " ");
            System.out.println();
        }
    }

    public void search() {
        DFSSearch s = new DFSSearch(m);
        s.setVariableSelector(new LexVariableSelector(m));
        s.search();
        if (s.hasSolution()) {
            printSolution();
        } else {
            System.out.println("No solution found");
        }
    }

    public void outHTML() {

    }

    public void solve() {
        model();
        search();
        outHTML();
    }

    public static void main(String[] args) {
        SudokuGlobal mySudoku = new SudokuGlobal(2);
        mySudoku.solve();
    }
}
