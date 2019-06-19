import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyUmlInteraction {
    private String interactionParent;
    private String interactionVisibility;
    private String interactionName;
    private String interactionId;
    private String umlType;
    private HashMap<String, MyUmlMessage> interactionMessage;
    private HashMap<String, MyUmlEndPoint> interactionEndPoint;
    private HashMap<String, MyUmlLifeline> interactionLifeline;
    private ArrayList<MyUmlAttribute> interactionAttribute;
    private HashMap<String, HashMap<String, MyUmlLifeline>> nameToLifeline;
    private HashMap<String, HashMap<String, MyUmlEndPoint>> nameToEndPoint;

    public MyUmlInteraction(String parent, String visibility,
                            String name, String id, String type) {
        this.interactionParent = parent;
        this.interactionVisibility = visibility;
        this.interactionName = name;
        this.interactionId = id;
        this.umlType = type;
        this.interactionMessage = new HashMap<>();
        this.interactionLifeline = new HashMap<>();
        this.interactionEndPoint = new HashMap<>();
        this.interactionAttribute = new ArrayList<>();
        this.nameToLifeline = new HashMap<>();
        this.nameToEndPoint = new HashMap<>();
    }

    public void addEndPoint(MyUmlEndPoint umlEndPoint) {
        this.interactionEndPoint.put(umlEndPoint.getId(), umlEndPoint);
        String name = umlEndPoint.getName();
        if (this.nameToEndPoint.containsKey(name)) {
            nameToEndPoint.get(name).put(umlEndPoint.getId(), umlEndPoint);
        } else {
            HashMap<String, MyUmlEndPoint> namehashmap = new HashMap<>();
            namehashmap.put(umlEndPoint.getId(), umlEndPoint);
            nameToEndPoint.put(name, namehashmap);
        }
    }

    public void addLifeline(MyUmlLifeline umlLifeline) {
        this.interactionLifeline.put(umlLifeline.getId(), umlLifeline);
        String name = umlLifeline.getName();
        if (this.nameToLifeline.containsKey(name)) {
            nameToLifeline.get(name).put(umlLifeline.getId(), umlLifeline);
        } else {
            HashMap<String, MyUmlLifeline> namehashmap = new HashMap<>();
            namehashmap.put(umlLifeline.getId(), umlLifeline);
            nameToLifeline.put(name, namehashmap);
        }
    }

    public void addMessage(MyUmlMessage umlMessage) {
        this.interactionMessage.put(umlMessage.getId(), umlMessage);
        String source = umlMessage.getSource();
        String target = umlMessage.getTarget();
        if (interactionLifeline.containsKey(source) &&
                interactionLifeline.containsKey(target)) {
            interactionLifeline.get(source).addOutputingMessage(umlMessage);
            interactionLifeline.get(target).addIncomingMessage(umlMessage);
        } else if (interactionLifeline.containsKey(source) &&
                interactionEndPoint.containsKey(target)) {
            interactionLifeline.get(source).addOutputingMessage(umlMessage);
            interactionEndPoint.get(target).addIncomingMessage(umlMessage);
        } else if (interactionEndPoint.containsKey(source) &&
                interactionLifeline.containsKey(target)) {
            interactionEndPoint.get(source).addOutputingMessage(umlMessage);
            interactionLifeline.get(target).addIncomingMessage(umlMessage);
        } else if (interactionEndPoint.containsKey(source) &&
                interactionEndPoint.containsKey(target)) {
            interactionEndPoint.get(source).addOutputingMessage(umlMessage);
            interactionEndPoint.get(target).addIncomingMessage(umlMessage);
        }
    }

    public void addAttribute(MyUmlAttribute umlAttribute) {
        this.interactionAttribute.add(umlAttribute);
    }

    public int getParticipantCount() {
        return this.interactionLifeline.size();
    }

    public int getIncomingMessageCount(String interaction, String name)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!nameToEndPoint.containsKey(name) &&
                !nameToLifeline.containsKey(name)) {
            throw new LifelineNotFoundException(interaction, name);
        }
        if (nameToLifeline.containsKey(name)) {
            HashMap<String, MyUmlLifeline> namehashmap =
                    nameToLifeline.get(name);
            if (namehashmap.size() > 1) {
                throw new LifelineDuplicatedException(interaction, name);
            }
            Iterator<HashMap.Entry<String, MyUmlLifeline>> iterator =
                    namehashmap.entrySet().iterator();
            MyUmlLifeline umlLifeline = iterator.next().getValue();
            return umlLifeline.getIncomingMessageCount();
        }
        if (nameToEndPoint.containsKey(name)) {
            HashMap<String, MyUmlEndPoint> namehashmap =
                    nameToEndPoint.get(name);
            if (namehashmap.size() > 1) {
                throw new LifelineDuplicatedException(interaction, name);
            }
            Iterator<HashMap.Entry<String, MyUmlEndPoint>> iterator =
                    namehashmap.entrySet().iterator();
            MyUmlEndPoint umlEndPoint = iterator.next().getValue();
            return umlEndPoint.getIncomingMessageCount();
        }
        return 0;
    }

    public int getMessageCount() {
        return this.interactionMessage.size();
    }

    public void print() {
        System.out.println("parentid: " + this.interactionParent +
                " visibility: "
                + this.interactionVisibility + " name: "
                + interactionName + " id: " + interactionId +
                " type: " + this.umlType);
        printAttribute();
        printLifeline();
        printMessage();
        printEndPoint();
    }

    public void printAttribute() {
        Iterator<MyUmlAttribute> iterator =
                this.interactionAttribute.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

    public void printLifeline() {
        Iterator<HashMap.Entry<String, MyUmlLifeline>> iterator =
                this.interactionLifeline.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlLifeline umlLifeline =
                    iterator.next().getValue();
            umlLifeline.print();
        }
    }

    public void printMessage() {
        Iterator<HashMap.Entry<String, MyUmlMessage>> iterator =
                this.interactionMessage.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlMessage umlMessage =
                    iterator.next().getValue();
            umlMessage.print();
        }
    }

    public void printEndPoint() {
        Iterator<HashMap.Entry<String, MyUmlEndPoint>> iterator =
                this.interactionEndPoint.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlEndPoint umlEndPoint =
                    iterator.next().getValue();
            umlEndPoint.print();
        }
    }

}
