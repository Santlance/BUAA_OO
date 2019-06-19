public class MyUmlEvent {
    private String eventParent;
    private String eventExpression;
    private String eventVisibility;
    private String eventName;
    private String umlType;
    private String eventId;

    public MyUmlEvent(String parent, String expression,
                      String visibility, String name, String type, String id) {
        this.eventParent = parent;
        this.eventExpression = expression;
        this.eventVisibility = visibility;
        this.eventName = name;
        this.umlType = type;
        this.eventId = id;
    }

    public void print() {
        System.out.println("parentid: " + this.eventParent + " visibility: "
                + this.eventVisibility + " name: "
                + eventName + " id: " + eventId +
                " expression: " + eventExpression +
                " type: " + this.umlType);
    }
}
