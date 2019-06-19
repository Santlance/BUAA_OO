import java.util.ArrayList;

public class MyUmlEndPoint {
    private String endPointParent;
    private String endPointVisibility;
    private String endPointName;
    private String umlType;
    private String endPointId;
    private ArrayList<MyUmlMessage> incomingMessage;
    private ArrayList<MyUmlMessage> outputingMessage;

    public MyUmlEndPoint(String parent, String visibility,
                         String name, String type, String id) {
        this.endPointParent = parent;
        this.endPointVisibility = visibility;
        this.endPointName = name;
        this.umlType = type;
        this.endPointId = id;
        this.incomingMessage = new ArrayList<>();
        this.outputingMessage = new ArrayList<>();
    }

    public int getIncomingMessageCount() {
        return incomingMessage.size();
    }

    public String getId() {
        return this.endPointId;
    }

    public String getName() {
        return this.endPointName;
    }

    public void print() {
        System.out.println("parentid: " + this.endPointParent + " visibility: "
                + this.endPointVisibility + " name: "
                + endPointName + " id: " + endPointId +
                " type: " + this.umlType);
    }

    public void addIncomingMessage(MyUmlMessage umlMessage) {
        incomingMessage.add(umlMessage);
    }

    public void addOutputingMessage(MyUmlMessage umlMessage) {
        outputingMessage.add(umlMessage);
    }
}
