import java.util.ArrayList;
import java.util.Iterator;

public class MyUmlOperation {
    private String operationParent;
    private String operationVisibility;
    private String operationName;
    private String operationId;
    private ArrayList<MyUmlParameter> operationParameter;
    private String umlType;

    public MyUmlOperation(String parent, String visibility,
                          String name, String id, String umlType) {
        this.operationParent = parent;
        this.operationVisibility = visibility;
        this.operationName = name;
        this.operationId = id;
        this.umlType = umlType;
        this.operationParameter = new ArrayList<MyUmlParameter>();
    }

    public void addParameter(MyUmlParameter myUmlParameter) {
        this.operationParameter.add(myUmlParameter);
    }

    //二进制数，00，第一位表示有无返回值，第二位表示有无参数
    public int type() {
        boolean isReturn = false;
        boolean isParam = false;
        Iterator<MyUmlParameter> iterator = this.operationParameter.iterator();
        while (iterator.hasNext()) {
            switch (iterator.next().getParameterDirection()) {
                case "IN":
                    isParam = true;
                    break;
                case "OUT":
                    isParam = true;
                    break;
                case "INOUT":
                    isParam = true;
                    break;
                case "RETURN":
                    isReturn = true;
                    break;
                default:
                    break;
            }
        }
        if (!isParam && !isReturn) {
            return 1;
        }
        if (!isParam && isReturn) {
            return 2;
        }
        if (isParam && !isReturn) {
            return 3;
        }
        if (isParam && isReturn) {
            return 4;
        }
        return 0;
    }

    public String getOperationVisibility() {
        return this.operationVisibility;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public void print() {
        System.out.println("parentid: " + this.operationParent + " visibility: "
                + this.operationVisibility + " name: "
                + operationName + " id: " + operationId +
                " type: " + this.umlType);
        printParameter();
    }

    public void printParameter() {
        Iterator<MyUmlParameter> iterator = this.operationParameter.iterator();
        while (iterator.hasNext()) {
            iterator.next().print();
        }
    }
}
