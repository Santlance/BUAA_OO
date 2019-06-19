import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;
import com.oocourse.specs3.models.RailwaySystem;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class MyRailwaySystem implements RailwaySystem {
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
    private int[][] tranWeight;
    private int[][] tickWeight;
    private int[][] unplWeight;
    private int[][] tranCount;
    private int[][] tickCount;
    private int[][] unplCount;
    private int connectBlock;

    private static int pidcount = 0;
    private static int[] visited = new int[125];
    private static ArrayBlockingQueue<Integer> visitQueue =
            new ArrayBlockingQueue<>(125);

    public MyRailwaySystem() {
        this.connectBlock = 0;
        this.tickWeight = new int[125][125];
        this.tranWeight = new int[125][125];
        this.unplWeight = new int[125][125];
        this.tickCount = new int[125][125];
        this.tranCount = new int[125][125];
        this.unplCount = new int[125][125];
        this.freeNodeId = new Stack<>();
        this.distance = new int[125][125];
        this.graph = new int[125][125];
        this.nodeIdList = new HashMap<>();
        this.distinctNode = new HashMap<>();
        this.pathIdList = new HashMap<>();
        this.pathList = new HashMap<>();
        for (int i = 125; i >= 1; i--) {
            this.freeNodeId.push(i);
        }
    }
    //点和边要分开处理,在removePath的时候，先处理边，两个相邻点的边数减一，再处理点，如果这个点只在一条path上就删除

    /***************************************/
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
        int secAfterId;
        for (int i = 0; i < size - 1; i++) {
            beforeId = this.nodeIdList.get(path.getNode(i));
            afterId = this.nodeIdList.get(path.getNode(i + 1));
            this.graph[beforeId][afterId] -= 1;
            this.graph[afterId][beforeId] -= 1;
            this.tranWeight[beforeId][afterId] -= 1;
            this.tranWeight[afterId][beforeId] -= 1;
            for (int j = i + 2; j < size; j++) {
                secAfterId = this.nodeIdList.get(path.getNode(j));
                this.tranWeight[beforeId][secAfterId] -= 1;
                this.tranWeight[secAfterId][beforeId] -= 1;
            }
        }
    }

    //这个函数必须放在addDistinctNode之后，因为要用到nodeIdList,而addDistinctNode会为每个node赋予Id
    private void addPathToGraph(Path path) {
        int size = path.size();
        int beforeId;
        int afterId;
        int secAfterId;
        for (int i = 0; i < size - 1; i++) {
            beforeId = this.nodeIdList.get(path.getNode(i));
            afterId = this.nodeIdList.get(path.getNode(i + 1));
            this.graph[beforeId][afterId] += 1;
            this.graph[afterId][beforeId] += 1;
            this.tranWeight[beforeId][afterId] += 1;
            this.tranWeight[afterId][beforeId] += 1;
            for (int j = i + 2; j < size; j++) {
                secAfterId = this.nodeIdList.get(path.getNode(j));
                this.tranWeight[beforeId][secAfterId] += 1;
                this.tranWeight[secAfterId][beforeId] += 1;
            }
        }
    }

    private void getDistance() {
        int[][] newDistance = new int[300][300];
        int[] svisited = new int[125];
        this.connectBlock = 0;
        Iterator<HashMap.Entry<Integer, Integer>> iterator =
                this.nodeIdList.entrySet().iterator();
        while (iterator.hasNext()) {
            for (int i = 0; i < visited.length; i++) {
                visited[i] = 0;
            }
            int nodeId = iterator.next().getValue();
            bfs(nodeId, newDistance);
            if (svisited[nodeId] == 0) {
                bfs(nodeId, svisited);
            }
        }
        this.distance = newDistance;
        int[][] tran = new int[125][125];
        int[][] tick = new int[125][125];
        int[][] unpl = new int[125][125];
        this.updateWeight();
        for (int i = 0; i < 125; i++) {
            for (int j = 0; j < 125; j++) {
                if (tranWeight[i][j] == 0) {
                    tran[i][j] = 999999;
                    tran[j][i] = 999999;
                } else {
                    tran[i][j] = 1;
                    tran[j][i] = 1;
                }
                if (tickWeight[i][j] == 0) {
                    tick[i][j] = 999999;
                    tick[j][i] = 999999;
                } else {
                    tick[i][j] = tickWeight[i][j] + 2;
                    tick[j][i] = tickWeight[j][i] + 2;
                }
                if (unplWeight[i][j] == 0) {
                    unpl[i][j] = 999999;
                    unpl[j][i] = 999999;
                } else {
                    unpl[i][j] = unplWeight[i][j] + 32;
                    unpl[j][i] = unplWeight[j][i] + 32;
                }
            }
        }
        Floyd(tran, tick, unpl);
        //printGraph(tran);
        this.tranCount = tran;
        this.tickCount = tick;
        this.unplCount = unpl;
    }

    private void Floyd(int[][] weight1, int[][] weight2, int[][] weight3) {
        for (int k = 1; k < 125; k++) {
            for (int i = 1; i < 125; i++) {
                for (int j = 1; j < 125; j++) {
                    if (weight1[i][j] > weight1[i][k] + weight1[k][j]) {
                        weight1[i][j] = weight1[i][k] + weight1[k][j];
                    }
                    if (weight2[i][j] > weight2[i][k] + weight2[k][j]) {
                        weight2[i][j] = weight2[i][k] + weight2[k][j];
                    }
                    if (weight3[i][j] > weight3[i][k] + weight3[k][j]) {
                        weight3[i][j] = weight3[i][k] + weight3[k][j];
                    }
                }
            }
        }
    }

    private void bfs(int node, int[][] newDistance) {
        visited[node] = 1;
        visitQueue.add(node);
        while (!visitQueue.isEmpty()) {
            int vnode = visitQueue.poll();
            for (int i = 0; i < 125; i++) {
                if (graph[vnode][i] > 0 && visited[i] == 0) {
                    newDistance[node][i] = newDistance[vnode][node] + 1;
                    newDistance[i][node] = newDistance[node][i];
                    visited[i] = 1;
                    visitQueue.add(i);
                }
            }
        }
    }

    private void bfs(int node, int[] visited) {
        visited[node] = 1;
        visitQueue.add(node);
        while (!visitQueue.isEmpty()) {
            int vnode = visitQueue.poll();
            for (int i = 0; i < 125; i++) {
                if (graph[vnode][i] > 0 && visited[i] == 0) {
                    visited[i] = 1;
                    visitQueue.add(i);
                }
            }
        }
        this.connectBlock++;
    }

    private void updateWeight() { //这里要取最小
        int[][] tickWeight = new int[125][125];
        int[][] unplWeight = new int[125][125];
        Iterator<MyPath> iterator = this.pathIdList.keySet().iterator();
        while (iterator.hasNext()) {
            MyPath path = iterator.next();
            int beforeId;
            int afterId;
            int weight;
            Iterator<HashMap.Entry<Pair<Integer, Integer>, Integer>>
                    tickIter = path.getTickIter();
            while (tickIter.hasNext()) {
                HashMap.Entry<Pair<Integer, Integer>, Integer>
                        tentry = tickIter.next();
                beforeId = this.nodeIdList.get(tentry.getKey().getKey());
                afterId = this.nodeIdList.get(tentry.getKey().getValue());
                weight = tentry.getValue();
                if (tickWeight[beforeId][afterId] == 0) {
                    tickWeight[beforeId][afterId] = weight;
                    tickWeight[afterId][beforeId] = weight;
                } else {
                    int srcWeight = tickWeight[afterId][beforeId];
                    tickWeight[beforeId][afterId] = Math.min(weight, srcWeight);
                    tickWeight[afterId][beforeId] = Math.min(weight, srcWeight);
                }
            }
            Iterator<HashMap.Entry<Pair<Integer, Integer>, Integer>>
                    unplIter = path.getUnplIter();
            while (unplIter.hasNext()) {
                HashMap.Entry<Pair<Integer, Integer>, Integer>
                        uentry = unplIter.next();
                beforeId = this.nodeIdList.get(uentry.getKey().getKey());
                afterId = this.nodeIdList.get(uentry.getKey().getValue());
                weight = uentry.getValue();
                if (unplWeight[beforeId][afterId] == 0) {
                    unplWeight[beforeId][afterId] = weight;
                    unplWeight[afterId][beforeId] = weight;
                } else {
                    int srcWeight = unplWeight[afterId][beforeId];
                    unplWeight[beforeId][afterId] = Math.min(weight, srcWeight);
                    unplWeight[afterId][beforeId] = Math.min(weight, srcWeight);
                }
            }
        }
        this.tickWeight = tickWeight;
        this.unplWeight = unplWeight;
    }

    /*****************************/
    public /*@pure@*/int size() {
        return this.pathList.size();
    }

    public /*@pure@*/ boolean containsPath(Path path) {
        return this.pathIdList.containsKey((MyPath) path);
    }

    public /*@pure@*/ boolean containsPathId(int pathId) {
        return this.pathList.containsKey(pathId);
    }

    public /*@pure@*/ Path getPathById(int pathId)
            throws PathIdNotFoundException {
        if (!this.pathList.containsKey(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        return this.pathList.get(pathId);
    }

    public /*@pure@*/ int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !((MyPath) path).isValid()
                || !this.pathIdList.containsKey(path)) {
            throw new PathNotFoundException(path);
        } else {
            return this.pathIdList.get(path);
        }
    }

    public int addPath(Path path) {
        if (path == null || !((MyPath) path).isValid()) {
            return 0;
        }
        if (this.pathIdList.containsKey((MyPath) path)) {
            return this.pathIdList.get(path);
        } else {
            this.pathList.put(++pidcount, (MyPath) path);
            this.pathIdList.put((MyPath) path, pidcount);
            this.addDistinctNode(path);
            this.addPathToGraph(path);
            this.getDistance();
            return pidcount;
        }
    }

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
        return keyid;
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!this.containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        MyPath path = this.pathList.get(pathId);
        this.removePathFromGraph(path);
        this.removeDistinctNode(path);
        MyPath path1 = this.pathList.get(pathId);
        this.pathList.remove(pathId);
        this.pathIdList.remove(path1);
        this.getDistance();
    }

    public /*@pure@*/int getDistinctNodeCount() {
        return this.distinctNode.size();
    }

    /***************************************/

    @Override
    public boolean containsNode(int i) {
        return this.distinctNode.containsKey(i);
    }

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
            throw new NodeNotConnectedException(i, i1);
        }
    }

    @Override
    public int getLeastTicketPrice(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!this.distinctNode.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!this.distinctNode.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        int node1 = this.nodeIdList.get(i);
        int node2 = this.nodeIdList.get(i1);
        if (node1 == node2) {
            return 0;
        }
        return this.tickCount[node1][node2] - 2;
    }

    @Override
    public int getLeastTransferCount(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!this.distinctNode.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!this.distinctNode.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        int node1 = this.nodeIdList.get(i);
        int node2 = this.nodeIdList.get(i1);
        if (node1 == node2) {
            return 0;
        }
        return this.tranCount[node1][node2] - 1;
    }

    @Override
    public int getUnpleasantValue(Path path, int i, int i1) {
        return Math.max(path.getUnpleasantValue(i),
                path.getUnpleasantValue(i1));
    }

    @Override
    public int getLeastUnpleasantValue(int i, int i1) throws
            NodeIdNotFoundException, NodeNotConnectedException {
        if (!this.distinctNode.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!this.distinctNode.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        int node1 = this.nodeIdList.get(i);
        int node2 = this.nodeIdList.get(i1);
        if (node1 == node2) {
            return 0;
        }
        return this.unplCount[node1][node2] - 32;
    }

    @Override
    public int getConnectedBlockCount() {
        return this.connectBlock;
    }
}
