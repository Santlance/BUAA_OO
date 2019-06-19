public class MyUmlOpaqueBehavior {
    private String opaqueBehaviorParent;
    private String opaqueBehaviorVisibility;
    private String opaqueBehaviorName;
    private String opaqueBehaviorId;
    private String umlType;

    public MyUmlOpaqueBehavior(String parent, String visibility,
                               String name, String id, String type) {
        this.opaqueBehaviorParent = parent;
        this.opaqueBehaviorVisibility = visibility;
        this.opaqueBehaviorName = name;
        this.opaqueBehaviorId = id;
        this.umlType = type;
    }

    public void print() {
        System.out.println("parentid: " + this.opaqueBehaviorParent +
                " visibility: " + this.opaqueBehaviorVisibility + " name: "
                + opaqueBehaviorName + " id: " + opaqueBehaviorId +
                " type: " + this.umlType);
    }

}
