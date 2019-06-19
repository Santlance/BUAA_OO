public class MyUmlAttribute {
    private String attributeParent;
    private String attributeVisibility;
    private String attributeName;
    //private String attributeType;
    private String attributeId;
    private String umlType;

    public MyUmlAttribute(String parent, String visibility,
                          String name, String id, String umlType) {
        this.attributeId = id;
        this.attributeVisibility = visibility;
        this.attributeName = name;
        //this.attributeType = type;
        this.attributeParent = parent;
        this.umlType = umlType;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public String getAttributeVisibility() {
        return this.attributeVisibility;
    }

    public String getAttributeId() {
        return this.attributeId;
    }

    public void print() {
        System.out.println("parentid: " + this.attributeParent + " visibility: "
                + this.attributeVisibility + " name: "
                + attributeName + " id: " + attributeId
                + " type: " + this.umlType);
    }
}
