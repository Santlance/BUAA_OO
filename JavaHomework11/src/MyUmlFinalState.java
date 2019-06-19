public class MyUmlFinalState {
    private String finalStateParent;
    private String finalStateVisibility;
    private String finalStateName;
    private String finalStateId;
    private String umlType;

    public MyUmlFinalState(String parent, String visibility,
                           String name, String id, String type) {
        this.finalStateParent = parent;
        this.finalStateVisibility = visibility;
        this.finalStateName = name;
        this.finalStateId = id;
        this.umlType = type;
    }

    public void print() {
        System.out.println("parentid: " +
                this.finalStateParent + " visibility: "
                + this.finalStateVisibility + " name: "
                + finalStateName + " id: " + finalStateId +
                " type: " + this.umlType);
    }

    public String getId() {
        return this.finalStateId;
    }

    public String getName() {
        return this.finalStateName;
    }
}
