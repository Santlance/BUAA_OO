import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class MyUmlRegion {
    private String regionParent;
    private String regionVisibility;
    private String regionName;
    private String regionId;
    private String umlType;
    private MyUmlFinalState uniqueFinalState;
    private MyUmlPseudoState uniquePseudoState;
    private HashMap<String, MyUmlPseudoState> regionPseudoState;
    private HashMap<String, MyUmlState> regionState;
    private HashMap<String, MyUmlFinalState> regionFinalState;
    private ArrayList<MyUmlTransition> regionTransition;
    private HashMap<String, Integer> idToStateIndex;
    private HashMap<String, ArrayList<String>> nameToState;
    private int idcount;
    private int[][] stateGraph;

    public MyUmlRegion(String parent, String visibility,
                       String name, String type, String id) {
        this.regionParent = parent;
        this.regionVisibility = visibility;
        this.umlType = type;
        this.regionId = id;
        this.regionName = name;
        this.regionPseudoState = new HashMap<>();
        this.regionState = new HashMap<>();
        this.regionTransition = new ArrayList<>();
        this.regionFinalState = new HashMap<>();
        this.idToStateIndex = new HashMap<>();
        this.nameToState = new HashMap<>();
        this.idcount = 0;
        this.stateGraph = new int[205][205];
        this.uniqueFinalState = null;
        this.uniquePseudoState = null;
    }

    public void addPseudoState(MyUmlPseudoState umlPseudoState) {
        if (uniquePseudoState == null) {
            uniquePseudoState = new MyUmlPseudoState(regionParent,
                    "uniquePseudoState", "PUBLIC",
                    "1926-08-17", "UML_PSEUDO_STATE");
            this.idToStateIndex.put(uniquePseudoState.getId(), idcount++);
            putState(uniquePseudoState.getName(), uniquePseudoState.getId());
        }
        this.regionPseudoState.put(umlPseudoState.getId(), umlPseudoState);
        putState(umlPseudoState.getName(), umlPseudoState.getId());
    }

    public void addState(MyUmlState umlState) {
        this.regionState.put(umlState.getId(), umlState);
        this.idToStateIndex.put(umlState.getId(), idcount++);
        putState(umlState.getName(), umlState.getId());
    }

    public void addFinalState(MyUmlFinalState umlFinalState) {
        if (uniqueFinalState == null) {
            this.uniqueFinalState = new MyUmlFinalState(regionParent,
                    "PUBLIC", "uniqueFinalState",
                    "19260817", "UML_FINAL_STATE");
            this.idToStateIndex.put(uniqueFinalState.getId(), idcount++);
            putState(uniqueFinalState.getName(), uniqueFinalState.getId());
        }
        this.regionFinalState.put(umlFinalState.getId(), umlFinalState);
        putState(umlFinalState.getName(), umlFinalState.getId());
    }

    public void addTransition(MyUmlTransition umlTransition) {
        this.regionTransition.add(umlTransition);
        int source = getStateIndex(umlTransition.getSource());
        int target = getStateIndex(umlTransition.getTarget());
        this.stateGraph[source][target]++;
    }

    private int getStateIndex(String id) {
        if (regionFinalState.containsKey(id)) {
            return this.idToStateIndex.get("19260817");
        } else if (regionPseudoState.containsKey(id)) {
            return this.idToStateIndex.get("1926-08-17");
        } else if (regionState.containsKey(id)) {
            return this.idToStateIndex.get(id);
        }
        return this.idToStateIndex.get(id);
    }

    private void putState(String name, String id) {
        if (!this.nameToState.containsKey(name)) {
            ArrayList<String> namelist = new ArrayList<>();
            namelist.add(id);
            nameToState.put(name, namelist);
        } else {
            nameToState.get(name).add(id);
        }
    }

    public void print() {
        System.out.println("parentid: " + this.regionParent + " visibility: "
                + this.regionVisibility + " name: "
                + regionName + " id: " + regionId +
                " type: " + this.umlType);
        printPseudoState();
        printState();
        printFinalState();
        printTransition();
    }

    public void printPseudoState() {
        Iterator<HashMap.Entry<String, MyUmlPseudoState>>
                iterator = this.regionPseudoState.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().print();
        }
    }

    public void printState() {
        Iterator<HashMap.Entry<String, MyUmlState>>
                iterator = this.regionState.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().print();
        }
    }

    public void printFinalState() {
        Iterator<HashMap.Entry<String, MyUmlFinalState>>
                iterator = this.regionFinalState.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().print();
        }
    }

    public void printTransition() {
        Iterator<MyUmlTransition> iterator = this.regionTransition.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

    public int getStateCount() {
        int ans = regionState.size();
        if (uniquePseudoState != null) {
            ans += 1;
        }
        if (uniqueFinalState != null) {
            ans += 1;
        }
        return ans;
    }

    public int getTransitionCount() {
        return regionTransition.size();
    }

    public int getSubsequentStateCount(String machine, String state)
            throws StateNotFoundException, StateDuplicatedException {
        ArrayList<String> namelist = this.nameToState.getOrDefault(state, null);
        if (namelist == null) {
            throw new StateNotFoundException(machine, state);
        }
        if (namelist.size() > 1) {
            throw new StateDuplicatedException(machine, state);
        }
        return bfs(state);
    }

    public int bfs(String state) {
        String stateId = this.nameToState.get(state).get(0);
        int start = getStateIndex(stateId);
        int[] visited = new int[idcount];
        int ans = 0;
        ArrayBlockingQueue<Integer> visitQueue =
                new ArrayBlockingQueue<Integer>(idcount);
        visitQueue.add(start);
        while (!visitQueue.isEmpty()) {
            int vnode = visitQueue.poll();
            for (int i = 0; i < idcount; i++) {
                if (stateGraph[vnode][i] > 0 && visited[i] == 0) {
                    visited[i] = 1;
                    ans++;
                    visitQueue.add(i);
                }
            }
        }
        return ans;
    }

}
