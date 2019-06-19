public class MyUmlAssociationEnd {
    private String associationEndReference;
    private String associationEndMultiplicity;
    private String associationEndParent;
    private String associationEndName;
    private String associationEndVisibility;
    private String umlType;
    private String associationEndId;

    public MyUmlAssociationEnd(String parent, String name, String visibility,
                               String id, String reference, String type,
                               String multiplicity) {
        this.associationEndParent = parent;
        this.associationEndName = name;
        this.associationEndVisibility = visibility;
        this.associationEndId = id;
        this.umlType = type;
        this.associationEndReference = reference;
        this.associationEndMultiplicity = multiplicity;
    }

    public String getAssociationEndReference() {
        return this.associationEndReference;
    }

    public String getAssociationEndName() {
        return associationEndName;
    }

    public void print() {
        System.out.println("parentid: " +
                this.associationEndParent + " visibility: "
                + this.associationEndVisibility + " name: "
                + associationEndName + " id: " +
                associationEndId + " reference: " + associationEndReference
                + " multiplicity: " + this.associationEndMultiplicity
                + " type: " + this.umlType);
    }
}
