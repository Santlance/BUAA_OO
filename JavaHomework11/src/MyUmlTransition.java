import java.util.ArrayList;
import java.util.Iterator;

public class MyUmlTransition {
    private String transitionParent;
    private String transitionVisibility;
    private String transitionGuard;
    private String transitionName;
    private String transitionId;
    private String transitionSource;
    private String transitionTarget;
    private String umlType;
    private ArrayList<MyUmlEvent> transitionEvent;
    private ArrayList<MyUmlOpaqueBehavior> transitionOpaqueBehavior;

    public MyUmlTransition(String parent, String visibility, String guard,
                           String name, String id, String source,
                           String target, String type) {
        this.transitionParent = parent;
        this.transitionVisibility = visibility;
        this.transitionGuard = guard;
        this.transitionName = name;
        this.transitionId = id;
        this.transitionSource = source;
        this.transitionTarget = target;
        this.umlType = type;
        this.transitionEvent = new ArrayList<>();
        this.transitionOpaqueBehavior = new ArrayList<>();
    }

    public void addEvent(MyUmlEvent umlEvent) {
        this.transitionEvent.add(umlEvent);
    }

    public void addOpaqueBehavior(MyUmlOpaqueBehavior umlOpaqueBehavior) {
        this.transitionOpaqueBehavior.add(umlOpaqueBehavior);
    }

    public void print() {
        System.out.println("parentid: " + this.transitionParent +
                " visibility: "
                + this.transitionVisibility + " name: "
                + transitionName + " id: " + transitionId +
                " guard: " + transitionGuard + " source: " +
                transitionSource + " target: " + transitionTarget +
                " type: " + this.umlType);
        printEvent();
        printOpaqueBehavior();
    }

    public void printEvent() {
        Iterator<MyUmlEvent> iterator = this.transitionEvent.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

    public void printOpaqueBehavior() {
        Iterator<MyUmlOpaqueBehavior> iterator =
                this.transitionOpaqueBehavior.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

    public String getTarget() {
        return this.transitionTarget;
    }

    public String getSource() {
        return this.transitionSource;
    }
}
