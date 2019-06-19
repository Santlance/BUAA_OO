import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyUmlInterface {
    private String interfaceParent;
    private String interfaceVisibility;
    private String interfaceName;
    private String interfaceId;
    private String umlType;
    private boolean initialInterfaceCount;
    private ArrayList<MyUmlOperation> interfaceOperation;
    private ArrayList<MyUmlAttribute> interfaceAttribute;
    private ArrayList<MyUmlInterface> interfaceGeneralization;
    private ArrayList<MyUmlInterface> interfaceAssociationInterface;
    private ArrayList<MyUmlClass> interfaceAssociationClass;
    private HashMap<String, MyUmlInterface> interfaceCount;

    public MyUmlInterface(String parent, String visibility,
                          String name, String id, String umlType) {
        this.initialInterfaceCount = false;
        this.umlType = umlType;
        this.interfaceAttribute = new ArrayList<>();
        this.interfaceOperation = new ArrayList<>();
        this.interfaceAssociationClass = new ArrayList<>();
        this.interfaceGeneralization = new ArrayList<>();
        this.interfaceAssociationInterface = new ArrayList<>();
        this.interfaceCount = new HashMap<>();
        this.interfaceParent = parent;
        this.interfaceId = id;
        this.interfaceName = name;
        this.interfaceVisibility = visibility;
    }

    public String getInterfaceName() {
        return this.interfaceName;
    }

    public String getInterfaceParent() {
        return this.interfaceParent;
    }

    public String getInterfaceVisibility() {
        return this.interfaceVisibility;
    }

    public String getInterfaceId() {
        return this.interfaceId;
    }

    public void addOperation(MyUmlOperation umlOperation) {
        this.interfaceOperation.add(umlOperation);
    }

    public void addAttribute(MyUmlAttribute umlAttribute) {
        this.interfaceAttribute.add(umlAttribute);
    }

    public void print() {
        System.out.println("parentid: " + this.interfaceParent + " visibility: "
                + this.interfaceVisibility + " name: "
                + this.interfaceName + " id: " + this.interfaceId +
                " type: " + this.umlType);
        printAttribute();
        printOperation();
    }

    public void addGeneralization(MyUmlInterface umlInterface) {
        this.interfaceGeneralization.add(umlInterface);
    }

    public void addAssociationInterface(MyUmlInterface umlInterface) {
        this.interfaceAssociationInterface.add(umlInterface);
    }

    public void addAssociationClass(MyUmlClass umlClass) {
        this.interfaceAssociationClass.add(umlClass);
    }

    public HashMap<String, MyUmlInterface> getInterfaceList() {
        if (!initialInterfaceCount) {
            countInterface();
            initialInterfaceCount = true;
        }
        return this.interfaceCount;
    }

    public void countInterface() {
        HashMap<String, MyUmlInterface> selfList = this.getSelfList();
        HashMap<String, MyUmlInterface> parentList = this.getParentList();
        selfList.putAll(parentList);
        this.interfaceCount = selfList;
    }

    public HashMap<String, MyUmlInterface> getSelfList() {
        HashMap<String, MyUmlInterface> selfList = new HashMap<>();
        selfList.put(this.interfaceId, this);
        return selfList;
    }

    public HashMap<String, MyUmlInterface> getParentList() {
        HashMap<String, MyUmlInterface> parentList = new HashMap<>();
        Iterator<MyUmlInterface> iterator =
                this.interfaceGeneralization.iterator();
        while (iterator.hasNext()) {
            parentList.putAll(iterator.next().getInterfaceList());
        }
        return parentList;
    }

    public void printAttribute() {
        Iterator<MyUmlAttribute> iterator = this.interfaceAttribute.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

    public void printOperation() {
        Iterator<MyUmlOperation> iterator = this.interfaceOperation.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }
}
