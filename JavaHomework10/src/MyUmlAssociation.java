import java.util.ArrayList;
import java.util.Iterator;

public class MyUmlAssociation {
    private String associationParent;
    private String associationName;
    private String umlType;
    private String associationId;
    private ArrayList<MyUmlAssociationEnd> associationEnd;

    public MyUmlAssociation(String parent, String name,
                            String id, String type) {
        this.associationEnd = new ArrayList<>();
        this.associationName = name;
        this.associationId = id;
        this.associationParent = parent;
        this.umlType = type;
    }

    public void addAssociationEnd(MyUmlAssociationEnd myUmlAssociationEnd) {
        this.associationEnd.add(myUmlAssociationEnd);
    }

    public MyUmlAssociationEnd getAssociationEnd1() {
        return this.associationEnd.get(0);
    }

    public MyUmlAssociationEnd getAssociationEnd2() {
        return this.associationEnd.get(1);
    }

    public void print() {
        System.out.println("parentid: " + this.associationParent + " name: "
                + associationName + " id: " + associationId
                + " type: " + this.umlType);
        printEnd();
    }

    public void printEnd() {
        Iterator<MyUmlAssociationEnd> iterator = associationEnd.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }

}
