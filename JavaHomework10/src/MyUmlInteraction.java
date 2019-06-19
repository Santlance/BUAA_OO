import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {
    private MyUmlModel umlModel;
    private final boolean debug = false;

    public MyUmlInteraction(UmlElement[] elements) {
        this.umlModel = new MyUmlModel();
        ArrayList<UmlElement> elementContainer = new ArrayList<>();
        //为了避免顺序输入顺序造成的影响，先把所有的类对象构建好
        this.addClassInterface(elements, elementContainer);
        this.addAttributeOperation(elementContainer);
        this.addParameter(elementContainer);
        this.addRelations(elementContainer);
        this.addAssociationEnd(elementContainer);
        this.umlModel.putAssociation();
        if (debug) {
            this.umlModel.print();
        }
    }

    private void addClassInterface(UmlElement[] elements,
                                   ArrayList<UmlElement> elementContainer) {
        for (int i = 0; i < elements.length; i++) {
            switch (elements[i].getElementType()) {
                case UML_CLASS:
                    this.umlModel.addClass(elements[i]);
                    break;
                case UML_INTERFACE:
                    this.umlModel.addInterface(elements[i]);
                    break;
                default:
                    elementContainer.add(elements[i]);
                    break;
            }
        }
    }

    private void addAttributeOperation(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_ATTRIBUTE:
                    this.umlModel.addAttribute(elem);
                    iterator.remove();
                    break;
                case UML_OPERATION:
                    this.umlModel.addOperation(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addParameter(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_PARAMETER:
                    this.umlModel.addParameter(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addRelations(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_ASSOCIATION:
                    this.umlModel.addAssociation(elem);
                    iterator.remove();
                    break;
                case UML_GENERALIZATION:
                    this.umlModel.addGeneralization(elem);
                    iterator.remove();
                    break;
                case UML_INTERFACE_REALIZATION:
                    this.umlModel.addInterfaceRealization(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addAssociationEnd(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_ASSOCIATION_END:
                    this.umlModel.addAssociationEnd(elem);
                    iterator.remove();
                    break;
                default:
                    System.out.println("has another element "
                            + elem.getElementType().toString());
                    break;
            }
        }
    }

    @Override
    public int getClassCount() {
        return this.umlModel.getClassCount();
    }

    @Override
    public int getClassOperationCount(String s, OperationQueryType
            operationQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        return umlClass.getOperationTypeCount(
                operationQueryType);
    }

    @Override
    public int getClassAttributeCount(String s, AttributeQueryType
            attributeQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        return umlClass.getAttributeTypeCount(attributeQueryType);
    }

    @Override
    public int getClassAssociationCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        return umlClass.getAssociationCount();
    }

    @Override
    public List<String> getClassAssociatedClassList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();

        HashMap<String, MyUmlClass> classHashMap =
                umlClass.getAssociationList();
        ArrayList<String> nameList = new ArrayList<>();
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator1 = classHashMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            MyUmlClass umlClass1 = iterator1.next().getValue();
            nameList.add(umlClass1.getClassName());
        }
        return nameList;
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        HashMap<String, Integer> visihashmap =
                umlClass.getOperationVisibilityCount(s1);
        HashMap<Visibility, Integer> returnHash = new HashMap<>();
        Iterator<HashMap.Entry<String, Integer>>
                iterator1 = visihashmap.entrySet().iterator();
        int value;
        while (iterator1.hasNext()) {
            HashMap.Entry<String, Integer> entry = iterator1.next();
            switch (entry.getKey()) {
                case "PUBLIC":
                    value = entry.getValue();
                    returnHash.put(Visibility.PUBLIC, value);
                    break;
                case "PRIVATE":
                    value = entry.getValue();
                    returnHash.put(Visibility.PRIVATE, value);
                    break;
                case "PROTECTED":
                    value = entry.getValue();
                    returnHash.put(Visibility.PROTECTED, value);
                    break;
                case "PACKAGE":
                    value = entry.getValue();
                    returnHash.put(Visibility.PACKAGE, value);
                    break;
                default:
                    System.out.println("WRONG OPERATION VISIBILITY");
                    break;
            }
        }
        return returnHash;
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        ArrayList<MyUmlAttribute> attributes =
                umlClass.getAttributeVisibilityCount(s1);
        if (attributes.isEmpty()) {
            throw new AttributeNotFoundException(s, s1);
        }
        if (attributes.size() > 1) {
            throw new AttributeDuplicatedException(s, s1);
        }
        MyUmlAttribute umlAttribute = attributes.get(0);
        switch (umlAttribute.getAttributeVisibility()) {
            case "PUBLIC":
                return Visibility.PUBLIC;
            case "PRIVATE":
                return Visibility.PRIVATE;
            case "PROTECTED":
                return Visibility.PROTECTED;
            case "PACKAGE":
                return Visibility.PACKAGE;
            default:
                return null;
        }
    }

    @Override
    public String getTopParentClass(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        return umlClass.getTopClass().getClassName();
    }

    @Override
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        HashMap<String, MyUmlInterface> interfaceHashMap =
                umlClass.getInterfaceList();
        ArrayList<String> nameList = new ArrayList<>();
        Iterator<HashMap.Entry<String, MyUmlInterface>>
                iterator1 = interfaceHashMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            MyUmlInterface umlInterface = iterator1.next().getValue();
            nameList.add(umlInterface.getInterfaceName());
        }
        return nameList;
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<String, MyUmlClass> hashMap =
                this.umlModel.getClassHashMapByName(s);
        if (hashMap == null) {
            throw new ClassNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new ClassDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = hashMap.entrySet().iterator();
        MyUmlClass umlClass = iterator.next().getValue();
        HashMap<String, AttributeClassInformation>
                nameHash = umlClass.getHiddenInformationList();
        Iterator<HashMap.Entry<String, AttributeClassInformation>>
                iterator1 = nameHash.entrySet().iterator();
        ArrayList<AttributeClassInformation> nameList = new ArrayList<>();
        while (iterator1.hasNext()) {
            nameList.add(iterator1.next().getValue());
        }
        return nameList;
    }
}
