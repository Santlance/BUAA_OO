public class MyUmlPseudoState {
    private String pseudoStateParent;
    private String pseudoStateName;
    private String pseudoStateVisibility;
    private String pseudoStateId;
    private String umlType;

    public MyUmlPseudoState(String parent, String name,
                            String visibility, String id, String type) {
        this.pseudoStateParent = parent;
        this.pseudoStateName = name;
        this.pseudoStateVisibility = visibility;
        this.pseudoStateId = id;
        this.umlType = type;
    }

    public void print() {
        System.out.println("parentid: " +
                this.pseudoStateParent + " visibility: "
                + this.pseudoStateVisibility + " name: "
                + pseudoStateName + " id: " + pseudoStateId +
                " type: " + this.umlType);
    }

    public String getId() {
        return this.pseudoStateId;
    }

    public String getName() {
        return this.pseudoStateName;
    }
}
