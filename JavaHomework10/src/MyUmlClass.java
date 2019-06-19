import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyUmlClass {
    private String classParent;
    private String classVisibility;
    private String className;
    private String classId;
    private String umlType;
    private boolean initialCountOperation;
    private boolean initialCountAttribute;
    private boolean initialCountAssociation;
    private boolean initialAssociationList;
    private boolean initialInterfaceList;
    private boolean initialCountHiddenAttribute;
    private int attributeAll;
    private int associationAll;
    private ArrayList<MyUmlOperation> classOperation;
    private ArrayList<MyUmlAttribute> classAttribute;
    private ArrayList<MyUmlInterface> classInterface;
    private ArrayList<MyUmlClass> classGeneralization;
    private ArrayList<MyUmlClass> classAssociationClass;
    private ArrayList<MyUmlInterface> classAssociationInterface;
    private HashMap<String, MyUmlClass> classAssociationList;
    private HashMap<String, Integer> operationCount;
    private HashMap<String, MyUmlInterface> interfaceCount;
    private HashMap<String, AttributeClassInformation> nohiddenAttributeList;

    public MyUmlClass(String parent, String visibility,
                      String name, String id, String umlType) {
        this.initialCountOperation = false;
        this.initialCountAttribute = false;
        this.initialCountAssociation = false;
        this.initialAssociationList = false;
        this.initialInterfaceList = false;
        this.initialCountHiddenAttribute = false;
        this.attributeAll = 0;
        this.associationAll = 0;
        this.umlType = umlType;
        this.classAttribute = new ArrayList<>();
        this.classOperation = new ArrayList<>();
        this.classInterface = new ArrayList<>();
        this.classAssociationClass = new ArrayList<>();
        this.classAssociationInterface = new ArrayList<>();
        this.classGeneralization = new ArrayList<>();
        this.classParent = parent;
        this.classId = id;
        this.className = name;
        this.classVisibility = visibility;
        this.operationCount = new HashMap<>();
        this.classAssociationList = new HashMap<>();
        this.interfaceCount = new HashMap<>();
        this.nohiddenAttributeList = new HashMap<>();
        operationCount.put("NON_RETURN", 0);
        operationCount.put("RETURN", 0);
        operationCount.put("NON_PARAM", 0);
        operationCount.put("PARAM", 0);
        operationCount.put("ALL", 0);
    }

    public String getClassName() {
        return this.className;
    }

    public String getClassParent() {
        return this.classParent;
    }

    public String getClassVisibility() {
        return this.classVisibility;
    }

    public String getClassId() {
        return this.classId;
    }

    public void addOperation(MyUmlOperation umlOperation) {
        this.classOperation.add(umlOperation);
    }

    public void addAttribute(MyUmlAttribute umlAttribute) {
        this.classAttribute.add(umlAttribute);
    }

    public void addInterface(MyUmlInterface umlInterface) {
        this.classInterface.add(umlInterface);
    }

    public void addGeneralization(MyUmlClass umlClass) {
        this.classGeneralization.add(umlClass);
    }

    public void addAssociationClass(MyUmlClass umlClass) {
        this.classAssociationClass.add(umlClass);
    }

    public void addAssociationInterface(MyUmlInterface umlInterface) {
        this.classAssociationInterface.add(umlInterface);
    }

    public int getOperationTypeCount(OperationQueryType type) {
        if (!initialCountOperation) {
            countOperation();
            initialCountOperation = true;
        }
        String index = type.toString();
        return this.operationCount.get(index);
    }

    public int getAttributeTypeCount(AttributeQueryType attributeQueryType) {
        if (!initialCountAttribute) {
            countAttribute();
            initialCountAttribute = true;
        }
        String mode = attributeQueryType.toString();
        if (mode.equals("ALL")) {
            return this.attributeAll;
        } else if (mode.equals("SELF_ONLY")) {
            return this.classAttribute.size();
        }
        return 0;
    }

    private void countAttribute() {
        this.attributeAll = this.getAllAttributeCount();
    }

    private int getAllAttributeCount() {
        return this.getAttributeCount() + this.getParentAttributeCount();
    }

    private int getParentAttributeCount() {
        int ans = 0;
        Iterator<MyUmlClass> iterator = this.classGeneralization.iterator();
        while (iterator.hasNext()) {
            ans += iterator.next().getAllAttributeCount();
        }
        return ans;
    }

    private void countOperation() {
        Iterator<MyUmlOperation> iterator = this.classOperation.iterator();
        int value;
        while (iterator.hasNext()) {
            switch (iterator.next().type()) {
                case 1:
                    value = operationCount.get("NON_RETURN");
                    operationCount.replace("NON_RETURN", value + 1);
                    value = operationCount.get("NON_PARAM");
                    operationCount.replace("NON_PARAM", value + 1);
                    break;
                case 2:
                    value = operationCount.get("RETURN");
                    operationCount.replace("RETURN", value + 1);
                    value = operationCount.get("NON_PARAM");
                    operationCount.replace("NON_PARAM", value + 1);
                    break;
                case 3:
                    value = operationCount.get("NON_RETURN");
                    operationCount.replace("NON_RETURN", value + 1);
                    value = operationCount.get("PARAM");
                    operationCount.replace("PARAM", value + 1);
                    break;
                case 4:
                    value = operationCount.get("RETURN");
                    operationCount.replace("RETURN", value + 1);
                    value = operationCount.get("PARAM");
                    operationCount.replace("PARAM", value + 1);
                    break;
                default:
                    System.out.println("WRONG return in countOperation");
                    break;
            }
            value = operationCount.get("ALL");
            operationCount.replace("ALL", value + 1);
        }
    }

    public int getAttributeCount() {
        return this.classAttribute.size();
    }

    public int getAssociationCount() {
        if (!initialCountAssociation) {
            countAssociation();
            initialCountAssociation = true;
        }
        return this.associationAll;
    }

    private void countAssociation() {
        this.associationAll = this.getAllAssociationCount();
    }

    private int getAllAssociationCount() {
        return this.getSelfAssociationCount() +
                this.getParentAssociationCount();
    }

    private int getSelfAssociationCount() {
        return this.classAssociationClass.size() +
                this.classAssociationInterface.size();
    }

    private int getParentAssociationCount() {
        int ans = 0;
        Iterator<MyUmlClass> iterator = this.classGeneralization.iterator();
        while (iterator.hasNext()) {
            ans += iterator.next().getAssociationCount();
        }
        return ans;
    }

    public HashMap<String, MyUmlClass> getAssociationList() {
        if (!initialAssociationList) {
            countAssociationList();
            initialAssociationList = true;
        }
        return this.classAssociationList;
    }

    private void countAssociationList() {
        HashMap<String, MyUmlClass> selfList = this.getSelfList();
        HashMap<String, MyUmlClass> parentList = this.getParenList();
        selfList.putAll(parentList);
        this.classAssociationList = selfList;
    }

    private HashMap<String, MyUmlClass> getSelfList() {
        HashMap<String, MyUmlClass> selfList = new HashMap<>();
        Iterator<MyUmlClass> iterator = this.classAssociationClass.iterator();
        while (iterator.hasNext()) {
            MyUmlClass umlClass = iterator.next();
            selfList.put(umlClass.getClassId(), umlClass);
        }
        return selfList;
    }

    private HashMap<String, MyUmlClass> getParenList() {
        HashMap<String, MyUmlClass> parentList = new HashMap<>();
        Iterator<MyUmlClass> iterator = this.classGeneralization.iterator();
        while (iterator.hasNext()) {
            parentList.putAll(iterator.next().getAssociationList());
        }
        return parentList;
    }

    public HashMap<String, Integer> getOperationVisibilityCount(
            String operation) {
        HashMap<String, Integer> operationVisibilityCount = new HashMap<>();
        operationVisibilityCount.put("PUBLIC", 0);
        operationVisibilityCount.put("PRIVATE", 0);
        operationVisibilityCount.put("PROTECTED", 0);
        operationVisibilityCount.put("PACKAGE", 0);
        Iterator<MyUmlOperation> iterator = this.classOperation.iterator();
        int value;
        while (iterator.hasNext()) {
            MyUmlOperation umlOperation = iterator.next();
            if (umlOperation.getOperationName().equals(operation)) {
                switch (umlOperation.getOperationVisibility()) {
                    case "PUBLIC":
                        value = operationVisibilityCount.get("PUBLIC");
                        operationVisibilityCount.replace("PUBLIC", value + 1);
                        break;
                    case "PRIVATE":
                        value = operationVisibilityCount.get("PRIVATE");
                        operationVisibilityCount.replace("PRIVATE", value + 1);
                        break;
                    case "PROTECTED":
                        value = operationVisibilityCount.get("PROTECTED");
                        operationVisibilityCount.replace(
                                "PROTECTED", value + 1);
                        break;
                    case "PACKAGE":
                        value = operationVisibilityCount.get("PACKAGE");
                        operationVisibilityCount.replace("PACKAGE", value + 1);
                        break;
                    default:
                        System.out.println("WRONG OPERATION VISIBILITY");
                        break;
                }
            }
        }
        return operationVisibilityCount;
    }

    public ArrayList<MyUmlAttribute> getAttributeVisibilityCount(
            String attribute) {
        ArrayList<MyUmlAttribute> arrayList;
        arrayList = countAttributeVisibility(attribute);
        return arrayList;
    }

    public ArrayList<MyUmlAttribute> countAttributeVisibility(
            String attribute) {
        ArrayList<MyUmlAttribute> selfList = getSelfAttributeList(attribute);
        ArrayList<MyUmlAttribute> parentList =
                getParentAttributeList(attribute);
        selfList.addAll(parentList);
        return selfList;
    }

    public ArrayList<MyUmlAttribute> getSelfAttributeList(String attribute) {
        ArrayList<MyUmlAttribute> selfList = new ArrayList<>();
        Iterator<MyUmlAttribute> iterator = this.classAttribute.iterator();
        while (iterator.hasNext()) {
            MyUmlAttribute umlAttribute = iterator.next();
            if (umlAttribute.getAttributeName().equals(attribute)) {
                selfList.add(umlAttribute);
            }
        }
        return selfList;
    }

    public ArrayList<MyUmlAttribute> getParentAttributeList(String attribute) {
        ArrayList<MyUmlAttribute> parentList = new ArrayList<>();
        Iterator<MyUmlClass> iterator = this.classGeneralization.iterator();
        while (iterator.hasNext()) {
            parentList.addAll(iterator.next().
                    getAttributeVisibilityCount(attribute));
        }
        return parentList;
    }

    public MyUmlClass getTopClass() {
        if (this.classGeneralization.isEmpty()) {
            return this;
        }
        return this.classGeneralization.get(0).getTopClass();
    }

    public HashMap<String, MyUmlInterface> getInterfaceList() {
        if (!initialInterfaceList) {
            countInterfaceList();
            initialInterfaceList = true;
        }
        return this.interfaceCount;
    }

    public void countInterfaceList() {
        HashMap<String, MyUmlInterface> selfInterfaceList =
                this.getSelfInterfaceList();
        HashMap<String, MyUmlInterface> parentInterfaceList =
                this.getParentInterfaceList();
        selfInterfaceList.putAll(parentInterfaceList);
        interfaceCount = selfInterfaceList;
    }

    public HashMap<String, MyUmlInterface> getSelfInterfaceList() {
        HashMap<String, MyUmlInterface> selfInterfaceList = new HashMap<>();
        Iterator<MyUmlInterface> iterator = this.classInterface.iterator();
        while (iterator.hasNext()) {
            selfInterfaceList.putAll(iterator.next().getInterfaceList());
        }
        return selfInterfaceList;
    }

    public HashMap<String, MyUmlInterface> getParentInterfaceList() {
        HashMap<String, MyUmlInterface> parentInterfaceList = new HashMap<>();
        Iterator<MyUmlClass> iterator = this.classGeneralization.iterator();
        while (iterator.hasNext()) {
            parentInterfaceList.putAll(iterator.next().getInterfaceList());
        }
        return parentInterfaceList;
    }

    public HashMap<String,
            AttributeClassInformation> getHiddenInformationList() {
        if (!initialCountHiddenAttribute) {
            countHiddenAttribute();
            initialCountHiddenAttribute = true;
        }
        return this.nohiddenAttributeList;
    }

    public void countHiddenAttribute() {
        HashMap<String, AttributeClassInformation> selfList =
                this.getSelfNoHiddenAttribute();
        HashMap<String, AttributeClassInformation> parentList =
                this.getParentNoHiddenAttribute();
        selfList.putAll(parentList);
        this.nohiddenAttributeList = selfList;
    }

    public HashMap<String,
            AttributeClassInformation> getSelfNoHiddenAttribute() {
        Iterator<MyUmlAttribute> iterator = this.classAttribute.iterator();
        HashMap<String, AttributeClassInformation> selfList = new HashMap<>();
        while (iterator.hasNext()) {
            MyUmlAttribute umlAttribute = iterator.next();
            if (!umlAttribute.getAttributeVisibility().equals("PRIVATE")) {
                selfList.put(umlAttribute.getAttributeId(),
                        new AttributeClassInformation(
                                umlAttribute.getAttributeName(),
                                this.className));
            }
        }
        return selfList;
    }

    public HashMap<String,
            AttributeClassInformation> getParentNoHiddenAttribute() {
        Iterator<MyUmlClass> iterator = this.classGeneralization.iterator();
        HashMap<String, AttributeClassInformation> parentList = new HashMap<>();
        while (iterator.hasNext()) {
            MyUmlClass umlClass = iterator.next();
            parentList.putAll(umlClass.getHiddenInformationList());
        }
        return parentList;
    }

    public void print() {
        System.out.println("parentid: " + this.classParent + " visibility: "
                + this.classVisibility + " name: "
                + this.className + " id: " + this.classId +
                " type: " + this.umlType);
        printAttribute();
        printOperation();
    }

    public void printAttribute() {
        Iterator<MyUmlAttribute> iterator = this.classAttribute.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

    public void printOperation() {
        Iterator<MyUmlOperation> iterator = this.classOperation.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

}
