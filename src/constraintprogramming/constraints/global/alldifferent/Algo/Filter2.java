package constraintprogramming.constraints.global.alldifferent.Algo;

import java.util.*;

/**
 * -> Dựng đồ thị giá trị tương ứng với mảng các biến GV(C)
 * GV(C) là arc-consistent khi mỗi cạnh e của nó thuộc một cặp ghép cực đại nào đó
 * Ta cần loại bỏ khỏi dồ thị các cạnh mà không thuộc một cặp ghép cực đại nào.
 * -> Từ một cặp ghép cực đại M của GV, một cạnh e thuộc một cặp ghép cực
 * đại nào đó khi và chỉ khi nó thuộc M hoặc nó thuộc về một đường đi
 * luân phiên chẵn bắt đầu từ 1 đỉnh không thuộc M hoặc một chu trình
 * chẵn. (Berge 1970)
 * --> Do đó, trước hết ta tìm một gặp ghép cực đại của M (findMaxiumMatching())
 * bằng thuật toán Kuhn - Tìm đường tăng luồng (~ Ford-Fulkenson trong Flow)
 * Sau đó tìm các cạnh không thuộc path/cycle nói trên và tỉa cạnh đó đi.
 */

public class Filter2 {
    // Iput
    Var[] x;

    // Temp
    Graph G;
    int nbOfVetex;
    int[] MaxMatch;
    HashMap<Integer, Integer> Map;
    HashMap<Integer, Integer> DeMap;

    // current free vetex in X and DX
    HashSet<Integer> V1, V2;

    public Filter2(Var[] x) {
        this.x = x;
        int n = x.length;
        MaxMatch = new int[n];
        //for(int i=0;i<n;i++) MaxMatch[i]=-1;

        // Map value to index
        nbOfVetex = n;
        HashSet<Integer> Value = new HashSet<>();
        Map = new HashMap<>();
        DeMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            for (int val : x[i].domain) {
                Value.add(val);
            }
        }
        int c = 1;
        for (int i : Value) {
            DeMap.put(n - 1 + c, i);
            Map.put(i, n - 1 + c);
            c++;
        }
        nbOfVetex += c;

        // Construct Graph
        V1 = new HashSet<>();
        V2 = new HashSet<>();
        G = new Graph(nbOfVetex);
        for (int i = 0; i < x.length; i++) {
            V1.add(i);
            for (int j : x[i].domain) {
                G.addEdge(i, Map.get(j));
                V2.add(Map.get(j));
            }
        }

    }

    // findMaxiumMatching of G, and return 1 if size == x.length()
    // Init MaxMatch={}
    // While(have augument path P) MaxMatch=MaxMatch (+) P
    public int findMaxMatching() {
        while (findAugumentPath().size() > 0) {
            HashMap<Integer, Integer> Path = findAugumentPath();
            xorSet(Path);
        }
        for (int i = 0; i < MaxMatch.length; i++) if (MaxMatch[i] == -1) return 0;
        return 1;
    }

    // Path ~ (1,2)+(2,3)+(3,4), lưu cạnh (u,v) vào HashMap
    public HashMap<Integer, Integer> findAugumentPath() {
        // Chỗ này có thể tối ưu bằng cách update GM thay vì dựng lại
        HashMap<Integer, Integer> Path = new HashMap<>();

        if (V1.isEmpty()) {
            System.out.println("V1 is empty");
            return Path;
        }
        // Super node
        int X = nbOfVetex;
        System.out.println(X);

        // Đồ thị tăng luồng
        Graph GM = new Graph(nbOfVetex + 1);
        for (int i = 0; i < x.length; i++) {
            for (int j : G.adjList[i]) {
                GM.addEdge(i, j);
            }
        }
        for (int i = 0; i < x.length; i++) {
            if (MaxMatch[i] != 0) GM.addEdge(MaxMatch[i], i);
        }
        for (int i : V1) {
            GM.addEdge(X, i);
        }
        System.out.print("GM - ");
        System.out.println(GM);

        // DFS từ X. Gặp 1 đỉnh thuộc V2 thì break
        boolean[] visited = new boolean[nbOfVetex + 10];
        int[] prev = new int[nbOfVetex + 10];
        prev[X] = -1;
        int last = -1;

        Stack<Integer> S = new Stack<>();
        S.push(X);
        while (!S.isEmpty() && last == -1) {
            int top = S.pop();
            System.out.println("Popped " + top);
            if (!visited[top]) {
                visited[top] = true;
                for (int next : GM.adjList[top])
                    if (!visited[next]) {
                        prev[next] = top;
                        if (V2.contains(next)) {
                            last = next;
                            break;
                        }
                        S.push(next);
                        System.out.println("Pushed " + next);
                    }
            }
        }
        if (last == -1) {
            System.out.println("Cant find any augument path\n");
        } else {
            System.out.println("Constructing augument path");
            //int step = 0;
            while (last != -1) {
                int from = prev[last];
                if (from == X) break;
                Path.put(from, last);
                System.out.println(Math.min(from, last) + " --> " + Math.max(from, last));
                last = from;
                //step++;
                //if(step>10) break;
            }
            System.out.println("Construct augument path completed");
            printCurrentAugumentPath(Path);
        }
        return Path;
    }

    // MaxMatch (+) Path
    public void xorSet(HashMap<Integer, Integer> Path) {
        int[] newMM = new int[x.length];    // == MaxMatch (+) P
        HashMap<Integer, Integer> temp = new HashMap<>();
        int cnt = 0;

        for (int i : Path.keySet()) {
            int j = Path.get(i);
            int A = Math.min(i, j);
            int B = Math.max(i, j);
            if (MaxMatch[A] != B) {     // (i,j) in Path and not in MaxMatch
                newMM[A] = B;
                temp.put(A, B);
                cnt++;
                V1.remove(A);
                V2.remove(B);
            }
        }

        for (int i = 0; i < x.length; i++) {
            int j = MaxMatch[i];
            if (j != 0) {
                if (((Path.get(i) != null && Path.get(i) != j) || Path.get(i) == null) && (Path.get(j) != null && Path.get(j) != i) || Path.get(j) == null) {
                    newMM[i] = j;
                    temp.put(i, j);
                    cnt++;
                    V1.remove(i);
                    V2.remove(j);
                }
            }
        }
        for (int i : temp.keySet()) {
            //newMM[i]=temp.get(i);
        }
        MaxMatch = newMM;
        System.out.println("Current MaxMatch : " + cnt);
        printMaxiumMatching();
    }

    /**
     * Untested
     */
    // RemoveEdge not appear in any MaxMatching.
    public void removeEdge() {
        //TODO Thay RE = HashMap
        int[] RE = new int[nbOfVetex+1];      // Danh sách tạm các cạnh cần xóa. RE[i]=j -> xóa cạnh (i,j)
        for (int i = 0; i < RE.length; i++) {
            RE[i] = -1;
        }

        Graph GO = new Graph(nbOfVetex + 1);
        // GO, với cạnh (x,y) của G, nếu (x,y) thuộc M thì add(x,y), ngược lại thêm (y,x)
        int X = nbOfVetex;

        for (int i : V2) {
            GO.addEdge(X, i);
        }
        for (int i = 0; i < x.length; i++) {
            for (int j : G.adjList[i]) {
                if (MaxMatch[i] == j) {
                    GO.addEdge(i, j);
                } else {
                    GO.addEdge(j, i);
                }
            }
        }
        //System.out.println("Oriented Graph GO : ");
        //System.out.println(GO);

        // Tìm các đường đi đơn từ X trong GO bằng BFS, đánh dấu các cạnh là used
        Queue<Integer> Q = new LinkedList<>();
        boolean[] visited = new boolean[nbOfVetex + 1];
        int[] prev = new int[nbOfVetex + 1];

        Q.add(X);
        prev[X] = -1;
        visited[X] = true;
        while (!Q.isEmpty()) {
            int head = Q.remove();
            for (int next : GO.adjList[head]) {
                if (!visited[next]) {
                    Q.add(next);
                    visited[next] = true;
                    RE[head]=next;
                    RE[next]=head;
                    //RE[Math.min(head, next)] = Math.max(head, next);
                    System.out.println(head + " --- " + next);
                }
            }
        }

        // Tìm thành phần liên thông của GO, đánh dấu các cạnh trong 1 tp liên thông là used
        GO.findStrongConnectedComponent();
        GO.printSCC();
        for (int i = 0; i < GO.SCC.size(); i++) {
            if (GO.SCC.get(i).size() > 1) {
                ArrayList<Integer> Component = GO.SCC.get(i);
                for(int j=0;j<Component.size();j++){
                    for(int k=j+1;k<Component.size();k++){
                        RE[Component.get(j)]=Component.get(k);
                        RE[Component.get(k)]=Component.get(j);
                        System.out.println(Component.get(j) + " ---- " + Component.get(k));
                    }
                }
            }
        }

        // -> Các cạnh (u,v) unused không thuộc MaxMatch chính là các cạnh cần xóa (RE[u]=-1 && MaxMatch[u]!=v)
        for (int i = 0; i < x.length; i++) {
            for (int j : G.adjList[i]) {
                if (RE[i] != j && MaxMatch[i] != j) {
                    System.out.println("RE = RE + " + i + " -> " + j);
                }
            }
        }

    }

    public void filter() {
        if (findMaxMatching() == 1) {
            System.out.println("Find Matching Done !");
            removeEdge();
        } else {
            System.out.println("Graph not have any Matching cover whole x");
        }
    }

    public void printValueGraph() {
        for (int i = 0; i < x.length; i++) {
            System.out.printf("%d", i);
            System.out.printf(" -> ");
            for (int j : G.adjList[i]) {
                System.out.printf("%d(%d) ", DeMap.get(j), j);
                //System.out.printf("%d ",DeMap.get(j));
            }
            System.out.println();
        }
    }

    public void printMaxiumMatching() {
        for (int i = 0; i < MaxMatch.length; i++) {
            if (MaxMatch[i] != 1000) {
                //System.out.println(i + " -> " + MaxMatch[i] + "\n");
                System.out.printf("%d -> %d(%d) \n", i, MaxMatch[i], DeMap.get(MaxMatch[i]));
            }
        }
        System.out.println();////////////////////////////////////
    }

    public void printCurrentAugumentPath(HashMap<Integer, Integer> Path) {
        for (int i : Path.keySet()) {
            System.out.println(i + " ~> " + Path.get(i));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // CSP Figure 6.2
        Var[] x = new Var[4];
        x[0] = new Var(new int[]{2, 3, 4, 5});
        x[1] = new Var(new int[]{2, 3});
        x[2] = new Var(new int[]{1, 2, 3, 4});
        x[3] = new Var(new int[]{2, 3});

        /*
        Var[] x = new Var[5];
        x[0] = new Var(new int[]{1, 2, 3, 4, 5});
        x[1] = new Var(new int[]{1, 2, 3, 4, 5});
        x[2] = new Var(new int[]{3, 4});
        x[3] = new Var(new int[]{3});
        x[4] = new Var(new int[]{4, 10});
        */

        Filter2 myFilter = new Filter2(x);
        myFilter.printValueGraph();

        myFilter.filter();
        myFilter.printMaxiumMatching();
        //-> Graph after filter
        //myFilter.printValueGraph();

    }
}
