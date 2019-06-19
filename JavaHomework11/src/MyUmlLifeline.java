import java.util.ArrayList;

public class MyUmlLifeline {
    private String lifelineParent;
    private String lifelineVisibility;
    private String lifelineName;
    private String umlType;
    private String lifelineId;
    private String lifelineRepresent;
    private ArrayList<MyUmlMessage> incomingMessage;
    private ArrayList<MyUmlMessage> outputingMessage;

    public MyUmlLifeline(String parent, String visibility,
                         String name, String id, String represent,
                         String type) {
        this.lifelineParent = parent;
        this.lifelineVisibility = visibility;
        this.lifelineName = name;
        this.lifelineId = id;
        this.lifelineRepresent = represent;
        this.umlType = type;
        this.incomingMessage = new ArrayList<>();
        this.outputingMessage = new ArrayList<>();
    }

    public String getId() {
        return this.lifelineId;
    }

    public void print() {
        System.out.println("parentid: " + this.lifelineParent + " visibility: "
                + this.lifelineVisibility + " name: "
                + lifelineName + " id: " + lifelineId +
                " represent: " + this.lifelineRepresent +
                " type: " + this.umlType);
    }

    public void addIncomingMessage(MyUmlMessage umlMessage) {
        incomingMessage.add(umlMessage);
    }

    public void addOutputingMessage(MyUmlMessage umlMessage) {
        outputingMessage.add(umlMessage);
    }

    public String getName() {
        return this.lifelineName;
    }

    public int getIncomingMessageCount() {
        return incomingMessage.size();
    }

}
