import com.oocourse.specs2.models.Graph;
import com.oocourse.specs2.models.NodeIdNotFoundException;
import com.oocourse.specs2.models.NodeNotConnectedException;
import com.oocourse.specs2.models.Path;
import com.oocourse.specs2.models.PathIdNotFoundException;
import com.oocourse.specs2.models.PathNotFoundException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class MyGraph implements Graph {
    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;
    private HashMap<Integer, MyPath> pathList;
    //here we set the key as pid and set the value as path
    private HashMap<MyPath, Integer> pathIdList;
    //here we set the key as path and set the value as pid
    private HashMap<Integer, Integer> distinctNode;
    //here we set the key as node and set the value as weigh
    private int[][] graph;
    //here we use the double dimensional array to store the graph
    private int[][] distance;
    //here we use the double dimensional array
    // to store the instance between two nodes
    private HashMap<Integer, Integer> nodeIdList;
    private Stack<Integer> freeNodeId;

    private static int pidcount = 0;
    private static int[] visited = new int[300];
    private static ArrayBlockingQueue<Integer> visitQueue =
            new ArrayBlockingQueue<>(300);
    private final boolean isTest = false;

    public MyGraph() {
        this.freeNodeId = new Stack<>();
        this.distance = new int[300][300];
        this.graph = new int[300][300];
        this.nodeIdList = new HashMap<>();
        this.distinctNode = new HashMap<>();
        this.pathIdList = new HashMap<>();
        this.pathList = new HashMap<>();
        for (int i = 299; i >= 1; i--) {
            this.freeNodeId.push(i);
        }
    }
    //点和边要分开处理,在removePath的时候，先处理边，两个相邻点的边数减一，再处理点，如果这个点只在一条path上就删除

    /***************************************/
    public void printGraph() {
        String s = new String();
        String d = new String();
        int i;
        int j;
        int order;
        for (i = 1; i <= 299 - freeNodeId.size(); i++) {
            for (j = 1; j <= 299 - freeNodeId.size(); j++) {
                s += String.valueOf(graph[i][j]);
                d += String.valueOf(distance[i][j]);
                d += '\t';
                s += '\t';
            }
            s = s + '\n';
            d = d + '\n';
        }
        System.out.println(s);
        System.out.println(d);
    }

    //this two function mark/demark the node with a id by the way
    private void addDistinctNode(Path path) {
        Iterator<Integer> iteratorP = ((MyPath) path).iterator();
        while (iteratorP.hasNext()) {
            int node = iteratorP.next();
            if (this.distinctNode.containsKey(node)) {
                int value = this.distinctNode.get(node);
                this.distinctNode.replace(node, value + 1);
            } else {
                this.distinctNode.put(node, 1);
                this.nodeIdList.put(node, this.freeNodeId.pop());
            }
        }
    }

    private void removeDistinctNode(Path path) {
        Iterator<Integer> iteratorP = ((MyPath) path).iterator();
        while (iteratorP.hasNext()) {
            int node = iteratorP.next();
            if (this.distinctNode.containsKey(node)) {
                int value = this.distinctNode.get(node);
                if (value == 1) {
                    this.distinctNode.remove(node);
                    this.freeNodeId.push(this.nodeIdList.get(node));
                    this.nodeIdList.remove(node);
                } else {
                    this.distinctNode.replace(node, value - 1);
                }
            }
        }
    }

    //这个函数必须放在removeDistinctNode之前，
    // 因为要用到nodeIdList,而removeDistinctNode会把nodeIdList清除
    private void removePathFromGraph(Path path) {
        int size = path.size();
        int beforeId;
        int afterId;
        for (int i = 0; i < size - 1; i++) {
            beforeId = this.nodeIdList.get(path.getNode(i));
            afterId = this.nodeIdList.get(path.getNode(i + 1));
            this.graph[beforeId][afterId] -= 1;
            this.graph[afterId][beforeId] -= 1;
        }
    }

    //这个函数必须放在addDistinctNode之后，因为要用到nodeIdList,而addDistinctNode会为每个node赋予Id
    private void addPathToGraph(Path path) {
        int size = path.size();
        int beforeId;
        int afterId;
        for (int i = 0; i < size - 1; i++) {
            beforeId = this.nodeIdList.get(path.getNode(i));
            afterId = this.nodeIdList.get(path.getNode(i + 1));
            this.graph[beforeId][afterId] += 1;
            this.graph[afterId][beforeId] += 1;
        }
    }

    private void getDistance() {
        int[][] newDistance = new int[300][300];
        Iterator<HashMap.Entry<Integer, Integer>> iterator =
                this.nodeIdList.entrySet().iterator();
        while (iterator.hasNext()) {
            for (int i = 0; i < visited.length; i++) {
                visited[i] = 0;
            }
            int nodeId = iterator.next().getValue();
            bfs(nodeId, newDistance);
        }
        this.distance = newDistance;
    }

    private void bfs(int node, int[][] newDistance) {
        visited[node] = 1;
        visitQueue.add(node);
        while (!visitQueue.isEmpty()) {
            int vnode = visitQueue.poll();
            for (int i = 0; i < 300; i++) {
                if (graph[vnode][i] > 0 && visited[i] == 0) {
                    //distance[node][i] = Math.min(distance[vnode][node] + 1,
                    //       distance[node][i]);
                    newDistance[node][i] = newDistance[vnode][node] + 1;
                    newDistance[i][node] = newDistance[node][i];
                    visited[i] = 1;
                    visitQueue.add(i);
                }
            }
        }
    }

    /*****************************/

    //@ ensures \result == pList.length;
    public /*@pure@*/int size() {
        return this.pathList.size();
    }

    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    public /*@pure@*/ boolean containsPath(Path path) {
        return this.pathIdList.containsKey((MyPath) path);
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    public /*@pure@*/ boolean containsPathId(int pathId) {
        return this.pathList.containsKey(pathId);
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length)
      @ && (\exists int i; 0 <= i && i < pList.length;
      @ pidList[i] == pathId && \result == pList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !containsPathId(pathId);
      @ assignable \nothing;
      @ signals_only PathIdNotFoundException;
      @*/
    public /*@pure@*/ Path getPathById(int pathId)
            throws PathIdNotFoundException {
        if (!this.pathList.containsKey(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        return this.pathList.get(pathId);
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && containsPath(path);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length) && (\exists int i;
      @ 0 <= i && i < pList.length; pList[i].equals(path)
      @ && pidList[i] == \result);
      @ also
      @ public exceptional_behavior
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) !path.isValid();
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public /*@pure@*/ int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !((MyPath) path).isValid()
                || !this.pathIdList.containsKey(path)) {
            throw new PathNotFoundException(path);
        } else {
            return this.pathIdList.get(path);
        }
    }

    /*@ normal_behavior
      @ requires path != null && path.isValid();
      @ assignable pList, pidList;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i] == path &&
      @           \result == pidList[i]);
      @ ensures !\old(containsPath(path)) ==>
      @          pList.length == (\old(pList.length) + 1) &&
      @          pidList.length == (\old(pidList.length) + 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length);
      @          containsPath(\old(pList[i]))
      @          && containsPathId(\old(pidList[i])));
      @ also
      @ normal_behavior
      @ requires path == null || path.isValid() == false;
      @ assignable \nothing;
      @ ensures \result == 0;
      @*/
    public int addPath(Path path) {
        if (path == null || !((MyPath) path).isValid()) {
            return 0;
        }
        if (this.pathIdList.containsKey((MyPath) path)) {
            if (isTest) {
                this.printGraph();
            }
            return this.pathIdList.get(path);
        } else {
            this.addDistinctNode(path);
            this.addPathToGraph(path);
            this.getDistance();
            this.pathList.put(++pidcount, (MyPath) path);
            this.pathIdList.put((MyPath) path, pidcount);
            if (isTest) {
                this.printGraph();
            }
            return pidcount;
        }
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && \old(containsPath(path));
      @ assignable pList, pidList;
      @ ensures containsPath(path) == false;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i &&
      @ i < \old(pList.length); \old(pList[i].equals(path)) &&
      @           \result == \old(pidList[i]));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) path.isValid()==false;
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !((MyPath) path).isValid()
                || !this.pathIdList.containsKey((MyPath) path)) {
            throw new PathNotFoundException(path);
        }
        this.removePathFromGraph(path);
        this.removeDistinctNode(path);
        int keyid = this.pathIdList.get(path);
        this.pathList.remove(keyid);
        this.pathIdList.remove(path);
        this.getDistance();
        if (isTest) {
            this.printGraph();
        }
        return keyid;
    }

    /*@ public normal_behavior
      @ requires \old(containsPathId(pathId));
      @ assignable pList, pidList;
      @ ensures pList.length == pidList.length;
      @ ensures (\forall int i; 0 <= i &&
      @ i < pidList.length; pidList[i] != pathId);
      @ ensures (\forall int i; 0 <= i &&
      @ i < pList.length; !pList[i].equals(\old(getPathById(pathId))));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathIdNotFoundException e) !containsPathId(pathId);
      @*/
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!this.containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        MyPath path = this.pathList.get(pathId);
        this.removePathFromGraph(path);
        this.removeDistinctNode(path);
        this.getDistance();
        MyPath path1 = this.pathList.get(pathId);
        this.pathList.remove(pathId);
        this.pathIdList.remove(path1);
        if (isTest) {
            this.printGraph();
        }
    }

    /*@ ensures (\exists int[] arr;
      @ (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
      @             (\forall int i; 0 <= i && i < arr.length; (\exists Path p;
      @             this.containsPath(p); p.containsNode(arr[i])))
      @             &&(\forall Path p; this.containsPath(p);
      @             (\forall int node; p.containsNode(node);
      @             (\exists int i; 0 <= i && i < arr.length; node == arr[i])))
      @             &&(\result == arr.length));
      @*/
    public /*@pure@*/int getDistinctNodeCount() {
        return this.distinctNode.size();
    }

    /***************************************/

    //@ ensures \result == (\exists Path path; path.isValid()
    // && containsPath(path); path.containsNode(nodeId));
    // here i is nodeId
    // 一个结点肯定在一个路径上，而加入的路径肯定合法
    @Override
    public boolean containsNode(int i) {
        return this.distinctNode.containsKey(i);
    }

    /*@ ensures \result == (\exists Path path; path.isValid()
    && containsPath(path);
      @      (\exists int i; 0 <= i && i < path.size() - 1;
      (path.getNode(i) == fromNodeId && path.getNode(i + 1) == toNodeId)||
      @        (path.getNode(i) == toNodeId
      && path.getNode(i + 1) == fromNodeId)));
      @*/
    // here i is fromNodeId,i1 is toNodeId
    @Override
    public boolean containsEdge(int i, int i1) {
        if (!distinctNode.containsKey(i) ||
                !this.distinctNode.containsKey(i1)) {
            return false;
        }
        int node1 = this.nodeIdList.get(i);
        int node2 = this.nodeIdList.get(i1);
        return (this.graph[node1][node2] > 0) && (this.graph[node2][node1] > 0);
    }

    /*@ normal_behavior
      @ requires (\exists Path path; path.isValid()
      && containsPath(path); path.containsNode(fromNodeId)) &&
      @          (\exists Path path; path.isValid()
      && containsPath(path); path.containsNode(toNodeId));
      @ assignable \nothing;
      @ ensures (fromNodeId != toNodeId) ==>
      \result == (\exists int[] npath; npath.length >= 2
      && npath[0] == fromNodeId && npath[npath.length - 1] == toNodeId;
      @                     (\forall int i; 0 <= i && (i < npath.length - 1);
      containsEdge(npath[i], npath[i + 1])));
      @ ensures (fromNodeId == toNodeId) ==> \result == true;
      @ also
      @ exceptional_behavior
      @ signals (NodeIdNotFoundException e)
      (\forall Path path; containsPath(path); !path.containsNode(fromNodeId));
      @ signals (NodeIdNotFoundException e)
      (\forall Path path; containsPath(path); !path.containsNode(toNodeId));
      @*/
    // here i is fromNodeId, i1 is toNodeId
    @Override
    public boolean isConnected(int i, int i1) throws NodeIdNotFoundException {
        if (!this.distinctNode.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!this.distinctNode.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        int node1 = this.nodeIdList.get(i);
        int node2 = this.nodeIdList.get(i1);
        if (node1 == node2) {
            return true;
        }
        return this.distance[node1][node2] != 0;
    }

    /*@ normal_behavior
      @ requires (\exists Path path; path.isValid()
      && containsPath(path); path.containsNode(fromNodeId)) &&
      @          (\exists Path path; path.isValid()
      && containsPath(path); path.containsNode(toNodeId));
      @ assignable \nothing;
      @ ensures (fromNodeId != toNodeId) ==>
      (\exists int[] spath; spath.length >= 2
      && spath[0] == fromNodeId && spath[spath.length - 1] == toNodeId;
      @             (\forall int i; 0 <= i && (i < spath.length - 1);
      containsEdge(spath[i], spath[i + 1])) &&
      @             (\forall Path p; p.isValid() && p.getNode(0) == fromNodeId
      && p.getNode(p.size() - 1) == toNodeId; p.size() >= spath.length) &&
      @             (\result == spath.length - 1));
      @ ensures (fromNodeId == toNodeId) ==> \result == 0;
      @ also
      @ exceptional_behavior
      @ signals (NodeIdNotFoundException e)
      (\forall Path path; containsPath(path); !path.containsNode(fromNodeId));
      @ signals (NodeIdNotFoundException e)
      (\forall Path path; containsPath(path); !path.containsNode(toNodeId));
      @ signals (NodeNotConnectedException e)
      !(\exists int[] npath; npath.length >= 2
      && npath[0] == fromNodeId && npath[npath.length - 1] == toNodeId;
      @
      (\forall int i; 0 <= i && (i < npath.length - 1);
       containsEdge(npath[i], npath[i + 1])));
      @*/
    // here i is fromNodeId, i1 is toNodeId
    @Override
    public int getShortestPathLength(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!this.distinctNode.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!this.distinctNode.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (i == i1) {
            return 0;
        }
        if (isConnected(i, i1)) {
            int node1 = this.nodeIdList.get(i);
            int node2 = this.nodeIdList.get(i1);
            return this.distance[node1][node2];
        } else {
            //System.out.println(i + " and " + i1 +
            // "'s distance : " + this.distance[i][i1]);
            throw new NodeNotConnectedException(i, i1);
        }
    }
}
