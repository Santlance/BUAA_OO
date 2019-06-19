import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.HashMap;
import java.util.Iterator;

public class MyUmlModel {
    private HashMap<String, HashMap<String, MyUmlClass>> nameToUmlClass;
    private HashMap<String, MyUmlClass> idToUmlClass;
    private HashMap<String, MyUmlOperation> idToUmlOperation;
    private HashMap<String, MyUmlInterface> idToUmlInterface;
    private HashMap<String, MyUmlAssociation> idToUmlAssociation;
    private HashMap<String, MyUmlGeneralization> idToUmlGeneralization;
    private HashMap<String, MyUmlInterfaceRealization>
            idToUmlInterfaceRealization;

    public MyUmlModel() {
        this.nameToUmlClass = new HashMap<>();
        this.idToUmlClass = new HashMap<>();
        this.idToUmlOperation = new HashMap<>();
        this.idToUmlInterface = new HashMap<>();
        this.idToUmlAssociation = new HashMap<>();
        this.idToUmlGeneralization = new HashMap<>();
        this.idToUmlInterfaceRealization = new HashMap<>();
    }

    public void addInterface(UmlElement element) {
        UmlInterface elem = (UmlInterface) element;
        MyUmlInterface umlInterface = new MyUmlInterface(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(), elem.getElementType().toString());
        this.idToUmlInterface.put(elem.getId(), umlInterface);
    }

    public void addClass(UmlElement element) {
        UmlClass elem = (UmlClass) element;
        MyUmlClass umlClass = new MyUmlClass(elem.getParentId(),
                elem.getVisibility().toString(), elem.getName(),
                elem.getId(), elem.getElementType().toString());
        String className = umlClass.getClassName();
        if (this.nameToUmlClass.containsKey(className)) {
            this.nameToUmlClass.get(className).put(
                    umlClass.getClassId(), umlClass);
        } else {
            HashMap<String, MyUmlClass> namehashmap = new HashMap<>();
            namehashmap.put(umlClass.getClassId(), umlClass);
            this.nameToUmlClass.put(umlClass.getClassName(), namehashmap);
        }
        this.idToUmlClass.put(umlClass.getClassId(), umlClass);
    }

    public void addAttribute(UmlElement element) {
        UmlAttribute elem = (UmlAttribute) element;
        MyUmlAttribute umlAttribute = new MyUmlAttribute(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(), elem.getElementType().toString());
        if (this.idToUmlClass.containsKey(elem.getParentId())) {
            MyUmlClass parent = this.getClassById(elem.getParentId());
            parent.addAttribute(umlAttribute);
        } else if (this.idToUmlInterface.containsKey(elem.getParentId())) {
            MyUmlInterface parent = this.getInterfaceById(elem.getParentId());
            parent.addAttribute(umlAttribute);
        }
    }

    public void addOperation(UmlElement element) {
        UmlOperation elem = (UmlOperation) element;
        MyUmlOperation umlOperation = new MyUmlOperation(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(), elem.getElementType().toString());
        this.idToUmlOperation.put(elem.getId(), umlOperation);
        if (this.idToUmlClass.containsKey(elem.getParentId())) {
            MyUmlClass parent = this.getClassById(elem.getParentId());
            parent.addOperation(umlOperation);
        } else if (this.idToUmlInterface.containsKey(elem.getParentId())) {
            MyUmlInterface parent = this.getInterfaceById(elem.getParentId());
            parent.addOperation(umlOperation);
        }
    }

    public void addParameter(UmlElement element) {
        UmlParameter elem = (UmlParameter) element;
        MyUmlParameter umlParameter = new MyUmlParameter(
                elem.getParentId(), elem.getName(),
                elem.getDirection().toString(),
                elem.getId(), elem.getElementType().toString());
        MyUmlOperation parent = this.getOperationById(elem.getParentId());
        parent.addParameter(umlParameter);
    }

    public void addAssociation(UmlElement element) {
        UmlAssociation elem = (UmlAssociation) element;
        MyUmlAssociation umlAssociation = new MyUmlAssociation(
                elem.getParentId(), elem.getName(),
                elem.getId(), elem.getElementType().toString());
        this.idToUmlAssociation.put(elem.getId(), umlAssociation);
    }

    public void addAssociationEnd(UmlElement element) {
        UmlAssociationEnd elem = (UmlAssociationEnd) element;
        MyUmlAssociationEnd umlAssociationEnd = new MyUmlAssociationEnd(
                elem.getParentId(), elem.getName(),
                elem.getVisibility().toString(),
                elem.getId(), elem.getReference(),
                elem.getElementType().toString(), elem.getMultiplicity());
        MyUmlAssociation parent = this.idToUmlAssociation.get(
                elem.getParentId());
        parent.addAssociationEnd(umlAssociationEnd);
    }

    public void addGeneralization(UmlElement element) {
        UmlGeneralization elem = (UmlGeneralization) element;
        MyUmlGeneralization umlGeneralization = new MyUmlGeneralization(
                elem.getParentId(),
                elem.getName(), elem.getId(), elem.getSource(),
                elem.getTarget(), elem.getElementType().toString());
        if (this.idToUmlClass.containsKey(elem.getSource())
                && this.idToUmlClass.containsKey(elem.getTarget())) {
            MyUmlClass source = this.idToUmlClass.get(elem.getSource());
            MyUmlClass target = this.idToUmlClass.get(elem.getTarget());
            source.addGeneralization(target);
        } else if (this.idToUmlInterface.containsKey(elem.getSource())
                && this.idToUmlInterface.containsKey(elem.getTarget())) {
            MyUmlInterface source = this.getInterfaceById(elem.getSource());
            MyUmlInterface target = this.getInterfaceById(elem.getTarget());
            source.addGeneralization(target);
        }
        this.idToUmlGeneralization.put(elem.getId(), umlGeneralization);
    }

    public void addInterfaceRealization(UmlElement element) {
        UmlInterfaceRealization elem = (UmlInterfaceRealization) element;
        MyUmlInterfaceRealization umlInterfaceRealization = new
                MyUmlInterfaceRealization(elem.getParentId(),
                elem.getName(), elem.getId(), elem.getSource(),
                elem.getTarget(),
                elem.getElementType().toString());
        MyUmlClass source = this.idToUmlClass.get(elem.getSource());
        MyUmlInterface target = this.idToUmlInterface.get(elem.getTarget());
        source.addInterface(target);
        this.idToUmlInterfaceRealization.put(elem.getId(),
                umlInterfaceRealization);
    }

    public void putAssociation() {
        Iterator<HashMap.Entry<String, MyUmlAssociation>>
                iterator = this.idToUmlAssociation.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlAssociation umlAssociation = iterator.next().getValue();
            MyUmlAssociationEnd umlAssociationEnd1 =
                    umlAssociation.getAssociationEnd1();
            MyUmlAssociationEnd umlAssociationEnd2 =
                    umlAssociation.getAssociationEnd2();
            if (this.idToUmlClass.containsKey(
                    umlAssociationEnd1.getAssociationEndReference())
                    && this.idToUmlClass.containsKey(
                    umlAssociationEnd2.getAssociationEndReference())) {
                MyUmlClass umlClass1 = this.idToUmlClass.get(
                        umlAssociationEnd1.getAssociationEndReference());
                MyUmlClass umlClass2 = this.idToUmlClass.get(
                        umlAssociationEnd2.getAssociationEndReference());
                umlClass1.addAssociationClass(umlClass2);
                umlClass2.addAssociationClass(umlClass1);
            } else if (this.idToUmlInterface.containsKey(
                    umlAssociationEnd1.getAssociationEndReference())
                    && this.idToUmlInterface.containsKey(
                    umlAssociationEnd2.getAssociationEndReference())) {
                MyUmlInterface umlInterface1 = this.getInterfaceById(
                        umlAssociationEnd1.getAssociationEndReference());
                MyUmlInterface umlInterface2 = this.getInterfaceById(
                        umlAssociationEnd2.getAssociationEndReference());
                umlInterface1.addAssociationInterface(umlInterface2);
                umlInterface2.addAssociationInterface(umlInterface1);
            } else if (this.idToUmlClass.containsKey(
                    umlAssociationEnd1.getAssociationEndReference())
                    && this.idToUmlInterface.containsKey(
                    umlAssociationEnd2.getAssociationEndReference())) {
                MyUmlClass umlClass1 = this.idToUmlClass.get(
                        umlAssociationEnd1.getAssociationEndReference());
                MyUmlInterface umlInterface2 = this.getInterfaceById(
                        umlAssociationEnd2.getAssociationEndReference());
                umlClass1.addAssociationInterface(umlInterface2);
                umlInterface2.addAssociationClass(umlClass1);
            } else if (this.idToUmlClass.containsKey(
                    umlAssociationEnd2.getAssociationEndReference())
                    && this.idToUmlInterface.containsKey(
                    umlAssociationEnd1.getAssociationEndReference())) {
                MyUmlClass umlClass2 = this.idToUmlClass.get(
                        umlAssociationEnd2.getAssociationEndReference());
                MyUmlInterface umlInterface1 = this.getInterfaceById(
                        umlAssociationEnd1.getAssociationEndReference());
                umlClass2.addAssociationInterface(umlInterface1);
                umlInterface1.addAssociationClass(umlClass2);
            }
        }
    }

    public HashMap<String, MyUmlClass> getClassHashMapByName(String className) {
        return this.nameToUmlClass.getOrDefault(className, null);
    }

    public MyUmlClass getClassById(String classId) {
        return this.idToUmlClass.get(classId);
    }

    public MyUmlInterface getInterfaceById(String interfaceId) {
        return this.idToUmlInterface.get(interfaceId);
    }

    public MyUmlOperation getOperationById(String operationId) {
        return this.idToUmlOperation.get(operationId);
    }

    public int getClassCount() {
        return this.idToUmlClass.size();
    }

    public void print() {
        Iterator<HashMap.Entry<String, MyUmlClass>> iterator =
                this.idToUmlClass.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlClass umlClass = iterator.next().getValue();
            umlClass.print();
        }
        Iterator<HashMap.Entry<String, MyUmlInterface>> iterator1 =
                this.idToUmlInterface.entrySet().iterator();
        while (iterator1.hasNext()) {
            MyUmlInterface umlInterface = iterator1.next().getValue();
            umlInterface.print();
        }
        Iterator<HashMap.Entry<String, MyUmlAssociation>> iterator2 =
                this.idToUmlAssociation.entrySet().iterator();
        while (iterator2.hasNext()) {
            MyUmlAssociation umlAssociation = iterator2.next().getValue();
            umlAssociation.print();
        }
        Iterator<HashMap.Entry<String, MyUmlGeneralization>> iterator3 =
                this.idToUmlGeneralization.entrySet().iterator();
        while (iterator3.hasNext()) {
            MyUmlGeneralization umlGeneralization = iterator3.next().getValue();
            umlGeneralization.print();
        }
        Iterator<HashMap.Entry<String, MyUmlInterfaceRealization>> iterator4 =
                this.idToUmlInterfaceRealization.entrySet().iterator();
        while (iterator4.hasNext()) {
            MyUmlInterfaceRealization umlInterfaceRealization =
                    iterator4.next().getValue();
            umlInterfaceRealization.print();
        }
    }

}
