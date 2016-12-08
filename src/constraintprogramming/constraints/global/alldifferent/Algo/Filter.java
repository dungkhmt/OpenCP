package constraintprogramming.constraints.global.alldifferent.Algo;

import constraintprogramming.enums.OutputPropagation;
import constraintprogramming.model.CPManager;
import constraintprogramming.model.VarIntCP;


import java.util.*;

/**
 * Regin[94]
 */

public class Filter {
    int debug = 0;

    static int nbOfCall = 0;

    // Iput
    VarIntCP[] x;

    // Temp
    Graph GM;        // Residue Graph
    int nbOfVertex;
    int n;
    int X;
    int[] MaxMatch;
    HashMap<Integer, Integer> Map;
    HashMap<Integer, Integer> DeMap;
    HashSet<Integer> Value = new HashSet<>();

    // current free vetex in X and DX
    HashSet<Integer> V1, V2;

    public Filter(VarIntCP[] x) {
        this.x = x;
        n = x.length;
        MaxMatch = new int[n];

        // Map value to index
        nbOfVertex = n;
        Map = new HashMap<>();
        DeMap = new HashMap<>();
        int c = 0;

        Value = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (int val : x[i].getDomain()) {
                Value.add(val);
            }
        }

        for (int i : Value) {
            DeMap.put(n + c, i);
            Map.put(i, n + c);
            c++;
        }
        nbOfVertex += c;

        // Construct Graph
        V1 = new HashSet<>();       // Free vertex of left
        V2 = new HashSet<>();       // Free vertex of right
        GM = new Graph(nbOfVertex + 1);
        X = nbOfVertex;
    }

    public OutputPropagation propagate() {
        System.out.println("------------------\nCalled " + ++nbOfCall);
        for (int i = 0; i < x.length; i++) {
            System.out.print("x[" + i + "] : ");
            for (int j : x[i].getDomain()) {
                System.out.printf("%d ", j);
            }
            System.out.println();
        }
        if (findMaxMatching() == 1) {
            System.out.println("Find Matching Done !");
            printMaxiumMatching();
            removeEdge();
            return OutputPropagation.SUSPEND;
        } else {
            System.out.println("Graph not have any Matching cover whole x");
            return OutputPropagation.FAILURE;
        }
    }

    // Init MaxMatch={}
    // findMaxiumMatching of G cover whole x(0->n-1)
    // While(have augument path Path) MaxMatch=MaxMatch XOR Path
    // TODO Optimize
    public int findMaxMatching() {
        // Reconstruct Residue-Graph
        // Reuse previous MaxMatch and V1,V2
        for (int i = 0; i < x.length; i++) {
            boolean matchRemoved = true;
            for (int j : x[i].getDomain()) {
                if (MaxMatch[i] == Map.get(j)) matchRemoved = false;        // MaxMatch[i] still in x[i] domain
            }
            if (matchRemoved) {
                MaxMatch[i] = 0;        // (i,MaxMatch[i]) remove due to other constraint => Add it to V1,V2 before reset
            }
        }

        V1.clear();
        V2.clear();
        for (int i = 0; i < n; i++) {
            V1.add(i);
            for (int j : x[i].getDomain()) {
                V2.add(Map.get(j));
            }
        }
        for (int i = 0; i < n; i++) {
            if (MaxMatch[i] != 0) {
                V1.remove(i);
                V2.remove(MaxMatch[i]);
            }
        }
        if (debug == 1) printFree();


        //printMaxiumMatching();
        HashMap<Integer, Integer> Path;
        while (true) {
            Path = findAugumentPath();
            if (Path.size() > 0) xorSet(Path);
            else {
                break;
            }
        }
        for (int i = 0; i < MaxMatch.length; i++) if (MaxMatch[i] == 0) return 0;
        return 1;
    }

    // Find Augument Path from V1 -> V2
    // TODO Optimize : Increment GM
    public HashMap<Integer, Integer> findAugumentPath() {
        if (debug == 1)  printFree();
        HashMap<Integer, Integer> Path = new HashMap<>();
        if (V1.isEmpty() || V2.isEmpty()) {
            if (debug == 1)  System.out.println("V1||V2 is empty");
            return Path;
        }

        GM.clearAllEdge();
        for (int i = 0; i < x.length; i++) {
            for (int j : x[i].getDomain()) {
                GM.addEdge(i, Map.get(j));
            }
        }
        GM.adjList[X].clear();
        for (int i : V1) {
            GM.addEdge(X, i);
        }
        for (int i = 0; i < x.length; i++) {
            if (MaxMatch[i] != 0) GM.addEdge(MaxMatch[i], i);
        }
        if (debug == 1) System.out.println(GM);

        // DFS từ X. Gặp 1 đỉnh thuộc V2 thì break
        boolean[] visited = new boolean[nbOfVertex + 10];
        int[] prev = new int[nbOfVertex + 10];
        prev[X] = -1;
        int last = -1;

        Stack<Integer> S = new Stack<>();
        S.push(X);
        while (!S.isEmpty() && last == -1) {
            int top = S.pop();
            //System.out.println("Popped " + top);
            if (!visited[top]) {
                visited[top] = true;
                for (int next : GM.adjList[top])
                    if (!visited[next]) {
                        prev[next] = top;
                        if (V2.contains(next)) {
                            last = next;
                            //System.out.println("Here " + next);
                            break;
                        }
                        S.push(next);
                        //System.out.println("Pushed " + next);
                    }
            }
        }
        if (last == -1) {
            //System.out.println("Cant find any augument path\n");
        } else {
            //System.out.println("Constructing augument path");
            //int step = 0;
            while (last != -1) {
                int from = prev[last];
                if (from == X) break;
                Path.put(from, last);
                ////System.out.println(Math.min(from, last) + " --> " + Math.max(from, last));
                last = from;
                //step++;
                //if(step>10) break;
            }
            if (debug == 1)  System.out.println("Construct augument path completed " + Path.size());
            if (debug == 1)  printCurrentAugumentPath(Path);
        }
        //System.out.println(Path.size());
        return Path;
    }

    // MaxMatch = MaxMatch XOR Path
    // TODO Optimize : Increment Change GM after change MaxMatch + Optimize data structure
    public void xorSet(HashMap<Integer, Integer> Path) {
        int[] newMM = new int[x.length];    // == MaxMatch (+) P

        // In Path, Not In MaxMatch
        for (int i : Path.keySet()) {
            int j = Path.get(i);
            int A = Math.min(i, j);
            int B = Math.max(i, j);
            if (MaxMatch[A] != B) {     // (i,j) in Path and not in MaxMatch
                newMM[A] = B;
                V1.remove(A);
                V2.remove(B);

                //System.out.println(A + " -- " + B);
                //printFree();
            }
        }

        // In MaxMatch, Not In Path
        for (int i = 0; i < x.length; i++) {
            int j = MaxMatch[i];
            if (j != 0) {
                if (((Path.get(i) != null && Path.get(i) != j) || Path.get(i) == null) && (Path.get(j) != null && Path.get(j) != i) || Path.get(j) == null) {
                    newMM[i] = j;
                    V1.remove(i);
                    V2.remove(j);
                    //System.out.println(i + " --- " + j);
                    //printFree();
                }
            }
        }
        MaxMatch = newMM;

        if (debug == 1)  printMaxiumMatching();
    }

    /**
     * RemoveEdge not appear in any MaxMatching.
     */
    public void removeEdge() {
        Graph GO = new Graph(nbOfVertex + 1);
        Value.clear();
        for (int i = 0; i < x.length; i++) {
            for (int j : x[i].getDomain()) {
                Value.add(j);
                if (MaxMatch[i] == Map.get(j)) {
                    GO.addEdge(Map.get(j), i);
                } else {
                    GO.addEdge(i, Map.get(j));
                }
            }
        }

        for (int i : V2) {
            for (int j : Value) {
                if (!V2.contains(j)) {
                    GO.addEdge(i, j);
                }
            }
        }
        //System.out.println("GO : ");
        //System.out.println(GO);
        int[] map2Node = GO.findStrongConnectedComponent();
        //GO.printSCC();

        ArrayList<Integer>[] toRemove = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            toRemove[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < n; i++) {
            for (int j : x[i].getDomain()) {
                ////System.out.println(i + " " + j);
                if (map2Node[i] != map2Node[Map.get(j)] && MaxMatch[i] != Map.get(j)) {
                    toRemove[i].add(j);
                    if (debug == 1)  System.out.println("Remove arc : " + i + " => " + j);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j : toRemove[i]) {
                x[i].removeValue(j);
            }
        }
    }

    public void printValueGraph() {
        for (int i = 0; i < x.length; i++) {
            //System.out.printf("%d", i);
            //System.out.printf(" -> ");
            for (int j : x[i].getDomain()) {
                //System.out.printf("%d(%d) ", DeMap.get(j), j);
                ////System.out.printf("%d ",DeMap.get(j));
            }
            //System.out.println();
        }
    }

    public void printCurrentValueGraph() {
        for (int i = 0; i < x.length; i++) {
            //System.out.print("x[" + i + "] : ");
            for (int j : x[i].getDomain()) {
                //System.out.printf("%d ", j);
            }
            //System.out.println();
        }
    }

    public void printMaxiumMatching() {
        System.out.println("Current Maxium Matching");
        for (int i = 0; i < MaxMatch.length; i++) {
            if (DeMap.containsKey(MaxMatch[i])) {
                System.out.printf("%d -> %d(%d) \n", i, MaxMatch[i], DeMap.get(MaxMatch[i]));
            }
        }
        System.out.println();
    }

    public void printFree() {
        System.out.print("V1 : ");
        for (int i : V1) {
            System.out.print(i + " ");
        }
        System.out.print("V2 : ");
        for (int j : V2) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public void printCurrentAugumentPath(HashMap<Integer, Integer> Path) {
        System.out.println("Current Augument Path : ");
        for (int i : Path.keySet()) {
            System.out.println(i + " ~> " + Path.get(i));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // CSP Figure 6.2

        CPManager m = new CPManager();
        VarIntCP[] x = new VarIntCP[4];

//        x[0] = new VarIntCP(m, 2, 5, "x[0]");
//        x[1] = new VarIntCP(m, 2, 3, "x[1]");
//        x[2] = new VarIntCP(m, 1, 4, "x[2]");
//        x[3] = new VarIntCP(m, 2, 3, "x[3]");
        x[0] = new VarIntCP(m, 1, 1, "x[0]");
        x[1] = new VarIntCP(m, 1, 5, "x[1]");
        x[2] = new VarIntCP(m, 3, 5, "x[2]");
        x[3] = new VarIntCP(m, 5, 5, "x[3]");


        Filter myFilter = new Filter(x);
        //myFilter.printValueGraph();

        myFilter.propagate();
        myFilter.printMaxiumMatching();
        myFilter.printCurrentValueGraph();
    }
}
