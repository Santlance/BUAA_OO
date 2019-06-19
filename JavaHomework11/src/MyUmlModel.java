import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOpaqueBehavior;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

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
    private HashMap<String, MyUmlStateMachine> idToUmlStateMachine;
    private HashMap<String, HashMap<String, MyUmlStateMachine>>
            nameToUmlStateMachine;
    private HashMap<String, MyUmlRegion> idToUmlRegion;
    private HashMap<String, MyUmlTransition> idToUmlTransition;
    private HashMap<String, MyUmlInteraction> idToUmlInteraction;
    private HashMap<String, HashMap<String, MyUmlInteraction>>
            nameToUmlInteraction;

    public MyUmlModel() {
        this.nameToUmlClass = new HashMap<>();
        this.idToUmlClass = new HashMap<>();
        this.idToUmlOperation = new HashMap<>();
        this.idToUmlInterface = new HashMap<>();
        this.idToUmlAssociation = new HashMap<>();
        this.idToUmlGeneralization = new HashMap<>();
        this.idToUmlInterfaceRealization = new HashMap<>();
        this.idToUmlStateMachine = new HashMap<>();
        this.nameToUmlStateMachine = new HashMap<>();
        this.idToUmlRegion = new HashMap<>();
        this.idToUmlTransition = new HashMap<>();
        this.idToUmlInteraction = new HashMap<>();
        this.nameToUmlInteraction = new HashMap<>();
    }

    public void addInterface(UmlElement element) {
        UmlInterface elem = (UmlInterface) element;
        MyUmlInterface umlInterface = new MyUmlInterface(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(), elem.getElementType().toString(),
                elem);
        this.idToUmlInterface.put(elem.getId(), umlInterface);
    }

    public void addClass(UmlElement element) {
        UmlClass elem = (UmlClass) element;
        MyUmlClass umlClass = new MyUmlClass(elem.getParentId(),
                elem.getVisibility().toString(), elem.getName(),
                elem.getId(), elem.getElementType().toString(), elem);
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

    public void addInteraction(UmlElement element) {
        UmlInteraction elem = (UmlInteraction) element;
        MyUmlInteraction umlInteraction = new MyUmlInteraction(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(), elem.getElementType().toString());
        String interactionName = elem.getName();
        if (this.nameToUmlInteraction.containsKey(interactionName)) {
            this.nameToUmlInteraction.get(interactionName).put(
                    elem.getId(), umlInteraction);
        } else {
            HashMap<String, MyUmlInteraction> namehashmap = new HashMap<>();
            namehashmap.put(elem.getId(), umlInteraction);
            this.nameToUmlInteraction.put(interactionName, namehashmap);
        }
        this.idToUmlInteraction.put(elem.getId(), umlInteraction);
    }

    public void addEndPoint(UmlElement element) {
        UmlEndpoint elem = (UmlEndpoint) element;
        MyUmlEndPoint umlEndPoint = new MyUmlEndPoint(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getElementType().toString(), elem.getId());
        MyUmlInteraction parent = idToUmlInteraction.get(elem.getParentId());
        parent.addEndPoint(umlEndPoint);
    }

    public void addLifeline(UmlElement element) {
        UmlLifeline elem = (UmlLifeline) element;
        MyUmlLifeline umlLifeline = new MyUmlLifeline(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(), elem.getRepresent(),
                elem.getElementType().toString());
        MyUmlInteraction parent = idToUmlInteraction.get(elem.getParentId());
        parent.addLifeline(umlLifeline);
    }

    public void addMessage(UmlElement element) {
        UmlMessage elem = (UmlMessage) element;
        MyUmlMessage umlMessage = new MyUmlMessage(
                elem.getParentId(), elem.getVisibility().toString(),
                elem.getName(), elem.getId(),
                elem.getSource(), elem.getTarget(),
                elem.getElementType().toString(),
                elem.getMessageSort().toString());
        MyUmlInteraction parent = idToUmlInteraction.get(elem.getParentId());
        parent.addMessage(umlMessage);
    }

    public void addStateMachine(UmlElement element) {
        UmlStateMachine elem = (UmlStateMachine) element;
        MyUmlStateMachine umlStateMachine =
                new MyUmlStateMachine(elem.getParentId(),
                        elem.getName(), elem.getId(),
                        elem.getElementType().toString());
        String stateMachineName = elem.getName();
        if (this.nameToUmlStateMachine.containsKey(stateMachineName)) {
            this.nameToUmlStateMachine.get(stateMachineName).put(
                    umlStateMachine.getStateMachineId(),
                    umlStateMachine);
        } else {
            HashMap<String, MyUmlStateMachine> namehashmap = new HashMap<>();
            namehashmap.put(umlStateMachine.getStateMachineId(),
                    umlStateMachine);
            this.nameToUmlStateMachine.put(
                    umlStateMachine.getStateMachineName(), namehashmap);
        }
        this.idToUmlStateMachine.put(
                umlStateMachine.getStateMachineId(), umlStateMachine);
    }

    public void addRegion(UmlElement element) {
        UmlRegion elem = (UmlRegion) element;
        MyUmlRegion umlRegion = new MyUmlRegion(elem.getParentId(),
                elem.getVisibility().toString(),
                elem.getName(), elem.getElementType().toString(), elem.getId());
        MyUmlStateMachine parent = this.idToUmlStateMachine.get(
                elem.getParentId());
        parent.addRegion(umlRegion);
        this.idToUmlRegion.put(elem.getId(), umlRegion);
    }

    public void addPseudoState(UmlElement element) {
        UmlPseudostate elem = (UmlPseudostate) element;
        MyUmlPseudoState umlPseudoState = new MyUmlPseudoState(
                elem.getParentId(), elem.getName(),
                elem.getVisibility().toString(), elem.getId(),
                elem.getElementType().toString());
        MyUmlRegion parent = this.idToUmlRegion.get(elem.getParentId());
        parent.addPseudoState(umlPseudoState);
    }

    public void addState(UmlElement element) {
        UmlState elem = (UmlState) element;
        MyUmlState umlState = new MyUmlState(elem.getParentId(),
                elem.getVisibility().toString(), elem.getName(),
                elem.getId(), elem.getElementType().toString());
        MyUmlRegion parent = this.idToUmlRegion.get(elem.getParentId());
        parent.addState(umlState);
    }

    public void addFinalState(UmlElement element) {
        UmlFinalState elem = (UmlFinalState) element;
        MyUmlFinalState umlFinalState = new MyUmlFinalState(elem.getParentId(),
                elem.getVisibility().toString(), elem.getName(),
                elem.getId(), elem.getElementType().toString());
        MyUmlRegion parent = this.idToUmlRegion.get(elem.getParentId());
        parent.addFinalState(umlFinalState);
    }

    public void addTransition(UmlElement element) {
        UmlTransition elem = (UmlTransition) element;
        MyUmlTransition umlTransition = new MyUmlTransition(elem.getParentId(),
                elem.getVisibility().toString(), elem.getGuard(),
                elem.getName(), elem.getId(),
                elem.getSource(), elem.getTarget(),
                elem.getElementType().toString());
        MyUmlRegion parent = this.idToUmlRegion.get(elem.getParentId());
        parent.addTransition(umlTransition);
        this.idToUmlTransition.put(elem.getId(), umlTransition);
    }

    public void addEvent(UmlElement element) {
        UmlEvent elem = (UmlEvent) element;
        MyUmlEvent umlEvent = new MyUmlEvent(elem.getParentId(),
                elem.getExpression(), elem.getVisibility().toString(),
                elem.getName(), elem.getElementType().toString(), elem.getId());
        MyUmlTransition parent = this.idToUmlTransition.get(elem.getParentId());
        parent.addEvent(umlEvent);
    }

    public void addOpaqueBehavior(UmlElement element) {
        UmlOpaqueBehavior elem = (UmlOpaqueBehavior) element;
        MyUmlOpaqueBehavior umlOpaqueBehavior =
                new MyUmlOpaqueBehavior(elem.getParentId(),
                        elem.getVisibility().toString(),
                        elem.getName(), elem.getId(),
                        elem.getElementType().toString());
        MyUmlTransition parent = this.idToUmlTransition.get(elem.getParentId());
        parent.addOpaqueBehavior(umlOpaqueBehavior);
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
        } else if (this.idToUmlInteraction.containsKey(elem.getParentId())) {
            System.err.println("add interaction attribute");
            MyUmlInteraction parent = getInteractionById(elem.getParentId());
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
                umlClass1.addAssociationEnd(umlAssociationEnd2);
                umlClass2.addAssociationClass(umlClass1);
                umlClass2.addAssociationEnd(umlAssociationEnd1);
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
                umlClass1.addAssociationEnd(umlAssociationEnd2);
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
                umlClass2.addAssociationEnd(umlAssociationEnd1);
            }
        }
    }

    public HashMap<String, MyUmlClass> getClassHashMapByName(String className) {
        return this.nameToUmlClass.getOrDefault(className, null);
    }

    public HashMap<String, MyUmlStateMachine> getStateMachineHashMapByName(
            String stateMachineName) {
        return this.nameToUmlStateMachine.getOrDefault(stateMachineName, null);
    }

    public HashMap<String, MyUmlInteraction> getInteractionHashMapByName(
            String interactionName) {
        return this.nameToUmlInteraction.getOrDefault(interactionName, null);
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

    public MyUmlInteraction getInteractionById(String interactionId) {
        return this.idToUmlInteraction.get(interactionId);
    }

    public int getClassCount() {
        return this.idToUmlClass.size();
    }

    public HashMap<String, MyUmlClass> getIdToUmlClass() {
        return idToUmlClass;
    }

    public HashMap<String, MyUmlInterface> getIdToUmlInterface() {
        return idToUmlInterface;
    }

    public HashMap<String, MyUmlGeneralization> getIdToUmlGeneralization() {
        return idToUmlGeneralization;
    }

    public HashMap<String, MyUmlInterfaceRealization>
        getIdToUmlInterfaceRealization() {
        return idToUmlInterfaceRealization;
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
        Iterator<HashMap.Entry<String, MyUmlStateMachine>> iterator5 =
                this.idToUmlStateMachine.entrySet().iterator();
        while (iterator5.hasNext()) {
            MyUmlStateMachine umlStateMachine =
                    iterator5.next().getValue();
            umlStateMachine.print();
        }
        Iterator<HashMap.Entry<String, MyUmlInteraction>> iterator6 =
                this.idToUmlInteraction.entrySet().iterator();
        while (iterator6.hasNext()) {
            MyUmlInteraction umlInteraction =
                    iterator6.next().getValue();
            umlInteraction.print();
        }
    }

}
