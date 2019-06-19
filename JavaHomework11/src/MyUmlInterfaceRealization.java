public class MyUmlInterfaceRealization {
    private String interfaceRealizationParent;
    private String interfaceRealizationName;
    private String umlType;
    private String interfaceRealizationId;
    private String interfaceRealizationSource;
    private String interfaceRealizationTarget;

    public MyUmlInterfaceRealization(String parent, String name, String id,
                                     String source, String target,
                                     String type) {
        this.interfaceRealizationParent = parent;
        this.interfaceRealizationId = id;
        this.interfaceRealizationSource = source;
        this.interfaceRealizationTarget = target;
        this.interfaceRealizationName = name;
        this.umlType = type;
    }

    public String getInterfaceRealizationSource() {
        return interfaceRealizationSource;
    }

    public String getInterfaceRealizationTarget() {
        return interfaceRealizationTarget;
    }

    public void print() {
        System.out.println("parentid: " + this.interfaceRealizationParent +
                " name: "
                + interfaceRealizationName + " id: " +
                interfaceRealizationId + " source: "
                + interfaceRealizationSource + " target: "
                + interfaceRealizationTarget
                + " type: " + this.umlType);
    }

}
