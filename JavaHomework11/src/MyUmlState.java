public class MyUmlState {
    private String stateParent;
    private String stateVisibility;
    private String stateName;
    private String stateId;
    private String umlType;

    public MyUmlState(String parent, String visibility,
                      String name, String id, String type) {
        this.stateParent = parent;
        this.stateVisibility = visibility;
        this.stateId = id;
        this.umlType = type;
        this.stateName = name;
    }

    public void print() {
        System.out.println("parentid: " + this.stateParent + " visibility: "
                + this.stateVisibility + " name: "
                + stateName + " id: " + stateId +
                " type: " + this.umlType);
    }

    public String getId() {
        return this.stateId;
    }

    public String getName() {
        return this.stateName;
    }
}
