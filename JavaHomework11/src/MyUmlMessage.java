public class MyUmlMessage {
    private String messageParent;
    private String messageVisibility;
    private String messageName;
    private String messageId;
    private String messageSource;
    private String messageTarget;
    private String umlType;
    private String messageSort;

    public MyUmlMessage(String parent, String visibility, String name,
                        String id, String source, String target,
                        String type, String sort) {
        this.messageParent = parent;
        this.messageVisibility = visibility;
        this.messageName = name;
        this.messageId = id;
        this.messageSort = sort;
        this.messageSource = source;
        this.messageTarget = target;
        this.umlType = type;
    }

    public String getId() {
        return this.messageId;
    }

    public String getSource() {
        return this.messageSource;
    }

    public String getTarget() {
        return this.messageTarget;
    }

    public void print() {
        System.out.println("parentid: " + this.messageParent + " visibility: "
                + this.messageVisibility + " name: "
                + messageName + " id: " + messageId + " source: " +
                messageSource + " target: " + messageTarget +
                " sort: " + messageSort +
                " type: " + this.umlType);
    }
}
