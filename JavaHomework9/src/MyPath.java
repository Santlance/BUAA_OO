import com.oocourse.specs3.models.Path;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.min;

public class MyPath implements Path {
    // Iterable<Integer>和Comparable<Path>接口的规格请参阅JDK
    //@ public instance model non_null int[] nodes;
    private ArrayList<Integer> nodes;
    private HashMap<Integer, Integer> disNode;
    private HashMap<Integer, Integer> disNodeId;
    //here disNode is the node id map as well
    private int[][] ticket;
    private int[][] unpleasent;
    private int nodeid = 0;
    private HashMap<Pair<Integer, Integer>, Integer> ticketMap;
    private HashMap<Pair<Integer, Integer>, Integer> unpleaMap;

    public MyPath(int[] nodeList) {
        this.unpleasent = new int[125][125];
        this.ticket = new int[125][125];
        this.nodes = new ArrayList<>();
        this.disNode = new HashMap<>();
        this.disNodeId = new HashMap<>();
        this.ticketMap = new HashMap<>();
        this.unpleaMap = new HashMap<>();
        for (int index = 0; index < nodeList.length; index++) {
            this.nodes.add(index, nodeList[index]);
            int node = nodeList[index];
            if (this.disNode.containsKey(node)) {
                continue;
            } else {
                this.disNode.put(node, ++nodeid);
                this.disNodeId.put(nodeid, node);
            }
        }
        int beforeId;
        int afterId;
        for (int i = 0; i < nodeList.length - 1; i++) {
            beforeId = this.disNode.get(nodeList[i]);
            afterId = this.disNode.get(nodeList[i + 1]);
            //两个相邻节点之间的票价均为1
            this.ticket[beforeId][afterId] = 1;
            this.ticket[afterId][beforeId] = 1;
            this.unpleasent[beforeId][afterId] =
                    Math.max(getUnpleasantValue(nodeList[i]),
                            getUnpleasantValue(nodeList[i + 1]));
            this.unpleasent[afterId][beforeId] =
                    this.unpleasent[beforeId][afterId];
        }
        for (int i = 1; i <= disNode.size(); i++) {
            for (int j = 1; j <= disNode.size(); j++) {
                if (ticket[i][j] == 0) {
                    ticket[i][j] = 9999999;
                    ticket[j][i] = 9999999;
                }
                if (unpleasent[i][j] == 0) {
                    unpleasent[i][j] = 9999999;
                    unpleasent[j][i] = 9999999;
                }
            }
        }
        Floyd(ticket, unpleasent);
        int beforeNode;
        int afterNode;
        for (int i = 1; i <= disNode.size(); i++) {
            for (int j = 1; j <= disNode.size(); j++) {
                beforeNode = disNodeId.get(i);
                afterNode = disNodeId.get(j);
                if (ticket[i][j] < 9999999) {
                    ticketMap.put(new Pair<>(beforeNode, afterNode),
                            ticket[i][j]);
                }
                if (unpleasent[i][j] < 9999999) {
                    unpleaMap.put(new Pair<>(beforeNode, afterNode),
                            unpleasent[i][j]);
                }
            }
        }
        //System.out.println("print path :");
        //printGraph();
    }

    private void Floyd(int[][] weight1, int[][] weight2) {
        for (int k = 1; k <= disNode.size(); k++) {
            for (int i = 1; i <= disNode.size(); i++) {
                for (int j = 1; j <= disNode.size(); j++) {
                    if (weight1[i][j] > weight1[i][k] + weight1[k][j]) {
                        weight1[i][j] = weight1[i][k] + weight1[k][j];
                    }
                    if (weight2[i][j] > weight2[i][k] + weight2[k][j]) {
                        weight2[i][j] = weight2[i][k] + weight2[k][j];
                    }
                }
            }
        }
    }

    public Iterator<HashMap.Entry<Pair<Integer, Integer>, Integer>>
        getTickIter() {
        return this.ticketMap.entrySet().iterator();
    }

    public Iterator<HashMap.Entry<Pair<Integer, Integer>, Integer>>
        getUnplIter() {
        return this.unpleaMap.entrySet().iterator();
    }

    public void printGraph() {
        String s = new String();
        String d = new String();
        int i;
        int j;
        int order;
        for (i = 1; i <= disNode.size(); i++) {
            for (j = 1; j <= disNode.size(); j++) {
                s += String.valueOf(ticket[i][j]);
                d += String.valueOf(unpleasent[i][j]);
                d += '\t';
                s += '\t';
            }
            s = s + '\n';
            d = d + '\n';
        }
        System.out.println(s);
        System.out.println(d);
    }

    //@ ensures \result == nodes.length;
    public /*@pure@*/int size() {
        return this.nodes.size();
    }

    /*@ requires index >= 0 && index < size();
      @ assignable \nothing;
      @ ensures \result == nodes[index];
      @*/
    public /*@pure@*/ int getNode(int index) {
        return this.nodes.get(index);
    }

    //@ ensures \result == (\exists int i; 0 <= i
    //@ && i < nodes.length; nodes[i] == node);
    public /*@pure@*/ boolean containsNode(int node) {
        return this.nodes.contains(node);
    }

    /*@ ensures (\exists int[] arr; (\forall int i, j; 0 <= i && i < j
      @  && j < arr.length; arr[i] != arr[j]);
      @             (\forall int i; 0 <= i
      @           && i < arr.length;this.containsNode(arr[i]))
      @           && (\forall int node; this.containsNode(node);
      @              (\exists int j; 0 <= j && j < arr.length; arr[j] == node))
      @           && (\result == arr.length));
      @*/
    public /*pure*/ int getDistinctNodeCount() {
        return this.disNode.size();
    }

    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Path;
      @ assignable \nothing;
      @ ensures \result == ((Path) obj).nodes.length == nodes.length) &&
      @                      (\forall int i; 0 <= i && i < nodes.length;
      @                       nodes[i] == ((Path) obj).nodes[i]);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Path);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MyPath)) {
            return false;
        }
        if (this.nodes.size() != ((MyPath) obj).size()) {
            return false;
        }
        for (int index = 0; index < this.nodes.size(); index++) {
            if (this.nodes.get(index) != ((MyPath) obj).getNode(index)) {
                return false;
            }
        }
        return true;
    }

    //@ ensures \result == (nodes.length >= 2);
    public /*@pure@*/ boolean isValid() {
        return (this.nodes.size()) >= 2;
    }

    //@ ensures containsNode(nodeId) ==>
    // \result == Math.pow(4, (nodeId % 5 + 5) % 5);
    //@ ensures !containsNode(nodeId) ==> \result == 0;
    @Override
    public int getUnpleasantValue(int i) {
        if (!containsNode(i)) {
            return 0;
        } else {
            return (int) Math.pow(4, (i % 5 + 5) % 5);
        }
    }

    @Override
    public int compareTo(Path o) {
        int sizeA = this.nodes.size();
        int sizeB = ((MyPath) o).size();
        int size = min(sizeA, sizeB);
        for (int index = 0; index < size; index++) {
            if (this.nodes.get(index) < ((MyPath) o).getNode(index)) {
                return -1;
            } else if (this.nodes.get(index) > ((MyPath) o).getNode(index)) {
                return 1;
            }
        }

        if (sizeA < sizeB) {
            return -1;
        } else if (sizeA > sizeB) {
            return 1;
        }
        return 0;
    }

    @Override
    public Iterator<Integer> iterator() {
        return this.nodes.iterator();
    }

    @Override
    public int hashCode() {
        return this.nodes.toString().hashCode();
    }
}
