import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;

public class MyUmlStateMachine {
    private String stateMachineParent;
    private String stateMachineName;
    private String stateMachineId;
    private String umlType;
    private ArrayList<MyUmlRegion> stateMachineRegion;

    public MyUmlStateMachine(String parent, String name,
                             String id, String type) {
        this.stateMachineParent = parent;
        this.stateMachineName = name;
        this.stateMachineId = id;
        this.umlType = type;
        this.stateMachineRegion = new ArrayList<>();
    }

    public String getStateMachineId() {
        return this.stateMachineId;
    }

    public String getStateMachineName() {
        return this.stateMachineName;
    }

    public void addRegion(MyUmlRegion umlRegion) {
        this.stateMachineRegion.add(umlRegion);
    }

    public void print() {
        System.out.println("parentid: " + this.stateMachineParent
                + " name: "
                + stateMachineName + " id: " + stateMachineId +
                " type: " + this.umlType);
        printRegion();
    }

    public void printRegion() {
        Iterator<MyUmlRegion> iterator = this.stateMachineRegion.iterator();
        while (iterator.hasNext()) {
            MyUmlRegion umlRegion = iterator.next();
            umlRegion.print();
        }
    }

    public int getStateCount() {
        int ans = 0;
        Iterator<MyUmlRegion> iterator = this.stateMachineRegion.iterator();
        while (iterator.hasNext()) {
            MyUmlRegion umlRegion = iterator.next();
            ans += umlRegion.getStateCount();
        }
        return ans;
    }

    public int getTransitionCount() {
        int ans = 0;
        Iterator<MyUmlRegion> iterator = this.stateMachineRegion.iterator();
        while (iterator.hasNext()) {
            MyUmlRegion umlRegion = iterator.next();
            ans += umlRegion.getTransitionCount();
        }
        return ans;
    }

    public int getSubsequentStateCount(String machine, String state)
            throws StateDuplicatedException, StateNotFoundException {
        int ans = 0;
        Iterator<MyUmlRegion> iterator = this.stateMachineRegion.iterator();
        while (iterator.hasNext()) {
            MyUmlRegion umlRegion = iterator.next();
            ans += umlRegion.getSubsequentStateCount(machine, state);
        }
        return ans;
    }

}
