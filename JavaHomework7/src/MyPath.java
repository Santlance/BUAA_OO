import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.min;

public class MyPath implements Path {
    // Iterable<Integer>和Comparable<Path>接口的规格请参阅JDK
    //@ public instance model non_null int[] nodes;
    private ArrayList<Integer> nodes;
    private HashMap<Integer, Integer> disNode;

    public MyPath(int[] nodeList) {
        this.nodes = new ArrayList<>();
        this.disNode = new HashMap<>();
        for (int index = 0; index < nodeList.length; index++) {
            this.nodes.add(index, nodeList[index]);
            int node = nodeList[index];
            if (this.disNode.containsKey(node)) {
                int value = this.disNode.get(node);
                this.disNode.replace(node, value + 1);
            } else {
                this.disNode.put(node, 1);
            }
        }
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
