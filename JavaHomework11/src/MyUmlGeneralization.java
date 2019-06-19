public class MyUmlGeneralization {
    private String generalizationParent;
    private String generalizationName;
    private String umlType;
    private String generalizationId;
    private String generalizationSource;
    private String generalizationTarget;

    public MyUmlGeneralization(String parent, String name, String id,
                               String source, String target, String type) {
        this.generalizationId = id;
        this.generalizationParent = parent;
        this.generalizationName = name;
        this.generalizationSource = source;
        this.generalizationTarget = target;
        this.umlType = type;
    }

    public String getGeneralizationSource() {
        return generalizationSource;
    }

    public String getGeneralizationTarget() {
        return generalizationTarget;
    }

    public void print() {
        System.out.println("parentid: " + this.generalizationParent + " name: "
                + generalizationName + " id: " +
                generalizationId + " source: " + generalizationSource
                + " target: " + generalizationTarget
                + " type: " + this.umlType);
    }
}
