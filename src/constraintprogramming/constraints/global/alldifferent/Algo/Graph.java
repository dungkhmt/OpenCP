package constraintprogramming.constraints.global.alldifferent.Algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Đồ thị có hướng biểu diễn qua danh sách kề
 */
public class Graph {
    //////////////////////////////////
    // Graph elements
    /////////////////////////////////
    int V;
    public ArrayList<Integer>[] adjList;
    public ArrayList<ArrayList<Integer>> SCC;
    public int[] mapNodeToSCC;

    //////////////////////////////////
    // Constructor
    /////////////////////////////////
    public Graph(int V) {
        this.V = V;
        adjList = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new ArrayList<Integer>();
        }
        //SCC=new ArrayList<ArrayList<Integer>>();
    }

    public Graph(ArrayList<Integer>[] adjList) {
        this.adjList = adjList;
    }

    //////////////////////////////////
    // Add directed Edge to graph
    /////////////////////////////////
    public void addEdge(int from, int to) {
        adjList[from].add(to);
    }

    public void addEdgeUndirect(int from, int to) {
        adjList[from].add(to);
        adjList[to].add(from);
    }

    public void removeEdge(int from, int to) {
        adjList[from].remove(to);
    }

    public void clearAllEdge(){
        for(int i=0;i<V;i++){
            adjList[i].clear();
        }
    }

    //////////////////////////////////
    // Traversal using DFS
    /////////////////////////////////
    public ArrayList<Integer> DFS() {
        ArrayList<Integer> dfsOrder;
        dfsOrder = new ArrayList<Integer>();
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfsUtil(dfsOrder, visited, i);
            }
        }
        return dfsOrder;
    }

    public void dfsUtil(ArrayList<Integer> dfsOrder, boolean[] visited, int v) {
        visited[v] = true;
        dfsOrder.add(v);
        for (int i = 0; i < adjList[v].size(); i++) {
            int j = adjList[v].get(i);
            if (!visited[j]) {
                dfsUtil(dfsOrder, visited, j);
            }
        }

    }

    void printDFS() {
        if (V == 0) {
            System.out.println("Graph empty!");
            return;
        }
        ArrayList<Integer> dfsOrder = DFS();
        for (int i = 0; i < dfsOrder.size(); i++) {
            System.out.printf("%d ", dfsOrder.get(i));
        }
        System.out.println();

    }

    //////////////////////////////////
    // Topology sort
    /////////////////////////////////
    public ArrayList<Integer> topoSort() {
        ArrayList<Integer> topoOrder = new ArrayList<Integer>();
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topoUtil(topoOrder, visited, i);
            }
        }
        Collections.reverse(topoOrder);
        return topoOrder;
    }

    public void topoUtil(ArrayList<Integer> topoOrder, boolean[] visited, int v) {
        visited[v] = true;
        for (int i = 0; i < adjList[v].size(); i++) {
            int j = adjList[v].get(i);
            if (!visited[j]) {
                topoUtil(topoOrder, visited, j);
            }
        }
        topoOrder.add(v);
    }

    public void printTopo() {
        if (V == 0) {
            System.out.println("Graph empty!");
            return;
        }
        ArrayList<Integer> topoOrder = topoSort();

        for (int i = 0; i < topoOrder.size(); i++) {
            System.out.printf("%d ", topoOrder.get(i));
        }
        System.out.println();
    }

    //////////////////////////////////
    // Reverse Graph
    /////////////////////////////////
    public Graph getReverse() {
        Graph Gr = new Graph(V);
        for (int from = 0; from < V; from++) {
            for (int j = 0; j < adjList[from].size(); j++) {
                int to = adjList[from].get(j);
                Gr.adjList[to].add(from);
            }
        }
        return Gr;
    }

    //////////////////////////////////
    // Korasaju algorithm for SCC
    /////////////////////////////////
    // DFS in original graph
    public void fillOrder(int v, boolean[] visited, Stack<Integer> S) {
        visited[v] = true;
        for (int i : adjList[v]) {
            if (!visited[i]) {
                fillOrder(i, visited, S);
            }
        }
        S.push(v);
    }

    // DFS in reverse graph
    public void findSCCUtil(int v, boolean[] visited, ArrayList<Integer> Component) {
        visited[v] = true;
        Component.add(v);
        for (int i : adjList[v]) {
            if (!visited[i]) {
                findSCCUtil(i, visited, Component);
            }
        }
    }

    public int[] findStrongConnectedComponent() {
        if (V == 0) return null;
        SCC = new ArrayList<>();
        mapNodeToSCC = new int[V + 1];

        boolean[] visited = new boolean[V];
        Stack<Integer> S = new Stack<>();

        for (int i = 0; i < V; i++) visited[i] = false;

        for (int i = 0; i < V; i++) if (!visited[i]) fillOrder(i, visited, S);

        Graph GR = getReverse();

        for (int i = 0; i < V; i++) visited[i] = false;
        while (!S.isEmpty()) {
            int v = S.pop();
            if (!visited[v]) {
                ArrayList<Integer> Component = new ArrayList<>();
                GR.findSCCUtil(v, visited, Component);
                SCC.add(Component);
            }
        }
        for (int i = 0; i < SCC.size(); i++) {
            for (int j : SCC.get(i)) {
                mapNodeToSCC[j] = i;
            }
        }
        return mapNodeToSCC;
    }

    public void printSCC() {
        for (int i = 0; i < SCC.size(); i++) {
            System.out.printf("Component " + (i + 1) + " : ");
            for (int j = 0; j < SCC.get(i).size(); j++) {
                System.out.printf("%d ", SCC.get(i).get(j));
            }
            System.out.println();
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Graph content : \n");
        for (int i = 0; i < V; i++) {
            s.append(i);
            s.append("->");
            for (int j : adjList[i]) {
                s.append(j);
                s.append(" ");
            }
            s.append("\n");
        }
        s.append("\n");

        return s.toString();
    }

    public static void main(String[] args) {

        Graph G = new Graph(8);
        G.addEdge(0, 1);

        G.addEdge(1, 3);
        G.addEdge(3, 2);
        G.addEdge(2, 1);

        G.addEdge(3, 4);
        G.addEdge(4, 5);
        G.addEdge(5, 7);
        G.addEdge(7, 6);
        G.addEdge(6, 4);

        /*
        Graph G = new Graph(8);
        G.addEdge(0,1);
        G.addEdge(0,2);
        G.addEdge(1,2);
        G.addEdge(1,3);
        G.addEdge(2,3);
        G.addEdge(3,4);
        G.addEdge(2,5);
        G.addEdge(7,6);
        */

        System.out.println(G);

        G.printDFS();
        G.printTopo();

        G.findStrongConnectedComponent();
        G.printSCC();

    }

}
