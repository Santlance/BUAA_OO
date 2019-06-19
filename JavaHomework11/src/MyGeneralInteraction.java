import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyGeneralInteraction extends MyUmlModelInteraction
        implements UmlGeneralInteraction {
    private final boolean debug = false;
    private HashMap<String, Integer> idToIndex;
    private HashMap<Integer, String> indexToId;
    private HashMap<String, UmlClassOrInterface> idToUmlClassOrInterface;
    private HashSet<UmlClassOrInterface> duplicated;
    private int[][] graph;
    private int idcount;
    private final int maxNum = 99999999;

    public MyGeneralInteraction(UmlElement[] elements) {
        super();
        this.idToIndex = new HashMap<>();
        this.indexToId = new HashMap<>();
        this.idToUmlClassOrInterface = new HashMap<>();
        this.duplicated = new HashSet<>();
        this.graph = new int[205][205];
        this.idcount = 0;
        ArrayList<UmlElement> elementArrayList = new ArrayList<>();
        this.initialModelInteraction(elements, elementArrayList);
        this.addStateMachine(elementArrayList);
        this.addRegion(elementArrayList);
        this.addStateElment(elementArrayList);
        this.addTransition(elementArrayList);
        this.addTransitionElement(elementArrayList);
        this.addInteractionElement(elementArrayList);
        this.addMessage(elementArrayList);
        if (debug) {
            this.getUmlModel().print();
        }
        initialIndex();
        initialGraph();
        floyd();
    }

    private void addStateMachine(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_STATE_MACHINE:
                    this.getUmlModel().addStateMachine(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addRegion(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_REGION:
                    this.getUmlModel().addRegion(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addStateElment(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_PSEUDOSTATE:
                    this.getUmlModel().addPseudoState(elem);
                    iterator.remove();
                    break;
                case UML_STATE:
                    this.getUmlModel().addState(elem);
                    iterator.remove();
                    break;
                case UML_FINAL_STATE:
                    this.getUmlModel().addFinalState(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addTransition(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_TRANSITION:
                    this.getUmlModel().addTransition(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addTransitionElement(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_EVENT:
                    this.getUmlModel().addEvent(elem);
                    iterator.remove();
                    break;
                case UML_OPAQUE_BEHAVIOR:
                    this.getUmlModel().addOpaqueBehavior(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addInteractionElement(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_LIFELINE:
                    this.getUmlModel().addLifeline(elem);
                    iterator.remove();
                    break;
                case UML_ENDPOINT:
                    this.getUmlModel().addEndPoint(elem);
                    iterator.remove();
                    break;
                default:
                    break;
            }
        }
    }

    private void addMessage(ArrayList<UmlElement> elementContainer) {
        Iterator<UmlElement> iterator = elementContainer.iterator();
        while (iterator.hasNext()) {
            UmlElement elem = iterator.next();
            switch (elem.getElementType()) {
                case UML_MESSAGE:
                    this.getUmlModel().addMessage(elem);
                    iterator.remove();
                    break;
                default:
                    System.err.println("has no use element "
                            + elem.getElementType().toString());
                    break;
            }
        }
    }

    private void initialIndex() {
        HashMap<String, MyUmlClass> umlClassesHashMap =
                this.getUmlModel().getIdToUmlClass();
        HashMap<String, MyUmlInterface> umlInterfaceHashMap =
                this.getUmlModel().getIdToUmlInterface();
        Iterator<HashMap.Entry<String, MyUmlClass>>
                iterator = umlClassesHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlClass umlClass = iterator.next().getValue();
            idToIndex.put(umlClass.getClassId(), idcount);
            indexToId.put(idcount++, umlClass.getClassId());
            idToUmlClassOrInterface.put(umlClass.getClassId(),
                    umlClass.getUmlClass());
        }
        Iterator<HashMap.Entry<String, MyUmlInterface>>
                iterator1 = umlInterfaceHashMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            MyUmlInterface umlInterface = iterator1.next().getValue();
            idToIndex.put(umlInterface.getInterfaceId(), idcount);
            indexToId.put(idcount++, umlInterface.getInterfaceId());
            idToUmlClassOrInterface.put(umlInterface.getInterfaceId(),
                    umlInterface.getUmlInterface());
        }
    }

    private void initialGraph() {
        HashMap<String, MyUmlGeneralization> umlGeneralizationHashMap =
                this.getUmlModel().getIdToUmlGeneralization();
        HashMap<String, MyUmlInterfaceRealization> umlInterfaceRealizationMap =
                this.getUmlModel().getIdToUmlInterfaceRealization();
        Iterator<HashMap.Entry<String, MyUmlGeneralization>>
                iterator = umlGeneralizationHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            MyUmlGeneralization umlGeneralization = iterator.next().getValue();
            String source = umlGeneralization.getGeneralizationSource();
            String target = umlGeneralization.getGeneralizationTarget();
            int sourceindex = idToIndex.get(source);
            int targetindex = idToIndex.get(target);
            if (graph[sourceindex][targetindex] == 0) {
                graph[sourceindex][targetindex] = 1;
            } else if (graph[sourceindex][targetindex] == 1) {
                duplicated.add(
                        idToUmlClassOrInterface.get(
                                indexToId.get(sourceindex)));
            }
        }
        Iterator<HashMap.Entry<String, MyUmlInterfaceRealization>>
                iterator1 = umlInterfaceRealizationMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            MyUmlInterfaceRealization umlRealization =
                    iterator1.next().getValue();
            String source = umlRealization.getInterfaceRealizationSource();
            String target = umlRealization.getInterfaceRealizationTarget();
            int sourceindex = idToIndex.get(source);
            int targetindex = idToIndex.get(target);
            if (graph[sourceindex][targetindex] == 1) {
                duplicated.add(
                        idToUmlClassOrInterface.get(
                                indexToId.get(sourceindex)));
            } else if (graph[sourceindex][targetindex] == 0) {
                graph[sourceindex][targetindex] = 1;
            }
        }
    }

    private void floyd() {
        ArrayList<Integer> duplicatedList = new ArrayList<>();
        for (int i = 0; i < idcount; i++) {
            for (int j = 0; j < idcount; j++) {
                if (graph[i][j] == 0) {
                    graph[i][j] = maxNum;
                }
            }
        }
        for (int k = 0; k < idcount; k++) {
            for (int i = 0; i < idcount; i++) {
                for (int j = 0; j < idcount; j++) {
                    if (graph[i][j] < maxNum &&
                            graph[i][k] + graph[k][j] < maxNum) {
                        duplicatedList.add(i);
                        duplicated.add(
                                idToUmlClassOrInterface.get(indexToId.get(i)));
                    }
                    if (graph[i][j] > graph[i][k] + graph[k][j]) {
                        graph[i][j] = graph[i][k] + graph[k][j];
                    }
                }
            }
        }
        for (int i = 0; i < duplicatedList.size(); i++) {
            int index = duplicatedList.get(i);
            for (int j = 0; j < idcount; j++) {
                if (graph[j][index] < maxNum) {
                    duplicated.add(
                            idToUmlClassOrInterface.get(indexToId.get(j)));
                }
            }
        }
    }

    @Override
    public int getParticipantCount(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        HashMap<String, MyUmlInteraction> hashMap =
                this.getUmlModel().getInteractionHashMapByName(s);
        if (hashMap == null) {
            throw new InteractionNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new InteractionDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlInteraction>>
                iterator = hashMap.entrySet().iterator();
        MyUmlInteraction umlInteraction = iterator.next().getValue();
        return umlInteraction.getParticipantCount();
    }

    @Override
    public int getMessageCount(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        HashMap<String, MyUmlInteraction> hashMap =
                this.getUmlModel().getInteractionHashMapByName(s);
        if (hashMap == null) {
            throw new InteractionNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new InteractionDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlInteraction>>
                iterator = hashMap.entrySet().iterator();
        MyUmlInteraction umlInteraction = iterator.next().getValue();
        return umlInteraction.getMessageCount();
    }

    @Override
    public int getIncomingMessageCount(String s, String s1)
            throws InteractionNotFoundException,
            InteractionDuplicatedException,
            LifelineNotFoundException,
            LifelineDuplicatedException {
        HashMap<String, MyUmlInteraction> hashMap =
                this.getUmlModel().getInteractionHashMapByName(s);
        if (hashMap == null) {
            throw new InteractionNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new InteractionDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlInteraction>>
                iterator = hashMap.entrySet().iterator();
        MyUmlInteraction umlInteraction = iterator.next().getValue();
        return umlInteraction.getIncomingMessageCount(s, s1);
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        HashMap<String, MyUmlClass> classHashMap =
                this.getUmlModel().getIdToUmlClass();
        Iterator<HashMap.Entry<String, MyUmlClass>> iterator =
                classHashMap.entrySet().iterator();
        HashSet<AttributeClassInformation> ansList = new HashSet<>();
        while (iterator.hasNext()) {
            MyUmlClass umlClass = iterator.next().getValue();
            String className = umlClass.getClassName();
            ArrayList<MyUmlAttribute> umlAttributes =
                    umlClass.getClassAttribute();
            ArrayList<MyUmlAssociationEnd> umlAssociationEnds =
                    umlClass.getClassAssociationEnd();
            Iterator<MyUmlAttribute> iterator1 = umlAttributes.iterator();
            while (iterator1.hasNext()) {
                MyUmlAttribute umlAttribute = iterator1.next();
                String attributeName = umlAttribute.getAttributeName();
                Iterator<MyUmlAssociationEnd> iterator2 =
                        umlAssociationEnds.iterator();
                while (iterator2.hasNext()) {
                    MyUmlAssociationEnd umlAssociationEnd = iterator2.next();
                    String associationEndName =
                            umlAssociationEnd.getAssociationEndName();
                    if (attributeName.equals(associationEndName)) {
                        ansList.add(new AttributeClassInformation(
                                attributeName, className));
                    }
                }
            }
            for (int i = 0; i < umlAssociationEnds.size(); i++) {
                for (int j = i + 1; j < umlAssociationEnds.size(); j++) {
                    if (umlAssociationEnds.get(i).getAssociationEndName()
                            != null &&
                            umlAssociationEnds.get(j).getAssociationEndName()
                                    != null) {
                        if (umlAssociationEnds.get(i).
                                getAssociationEndName().equals(
                                umlAssociationEnds.get(j).
                                        getAssociationEndName())) {
                            ansList.add(new AttributeClassInformation(
                                    umlAssociationEnds.get(i).
                                            getAssociationEndName(),
                                    className));
                        }
                    }
                }
            }
        }
        if (!ansList.isEmpty()) {
            throw new UmlRule002Exception(ansList);
        }
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        HashSet<UmlClassOrInterface> ans = new HashSet<>();
        for (int i = 0; i < idcount; i++) {
            if (graph[i][i] < maxNum) {
                ans.add(
                        idToUmlClassOrInterface.get(indexToId.get(i)));
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule008Exception(ans);
        }
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        if (!duplicated.isEmpty()) {
            throw new UmlRule009Exception(duplicated);
        }
    }

    @Override
    public int getStateCount(String s)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        HashMap<String, MyUmlStateMachine> hashMap =
                this.getUmlModel().getStateMachineHashMapByName(s);
        if (hashMap == null) {
            throw new StateMachineNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new StateMachineDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlStateMachine>>
                iterator = hashMap.entrySet().iterator();
        MyUmlStateMachine umlStateMachine = iterator.next().getValue();
        return umlStateMachine.getStateCount();
    }

    @Override
    public int getTransitionCount(String s)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        HashMap<String, MyUmlStateMachine> hashMap =
                this.getUmlModel().getStateMachineHashMapByName(s);
        if (hashMap == null) {
            throw new StateMachineNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new StateMachineDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlStateMachine>>
                iterator = hashMap.entrySet().iterator();
        MyUmlStateMachine umlStateMachine = iterator.next().getValue();
        return umlStateMachine.getTransitionCount();
    }

    @Override
    public int getSubsequentStateCount(String s, String s1)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        HashMap<String, MyUmlStateMachine> hashMap =
                this.getUmlModel().getStateMachineHashMapByName(s);
        if (hashMap == null) {
            throw new StateMachineNotFoundException(s);
        }
        if (hashMap.size() > 1) {
            throw new StateMachineDuplicatedException(s);
        }
        Iterator<HashMap.Entry<String, MyUmlStateMachine>>
                iterator = hashMap.entrySet().iterator();
        MyUmlStateMachine umlStateMachine = iterator.next().getValue();
        return umlStateMachine.getSubsequentStateCount(s, s1);
    }
}
