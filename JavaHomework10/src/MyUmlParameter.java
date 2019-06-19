public class MyUmlParameter {
    private String parameterId;
    private String parameterParent;
    private String parameterName;
    //private String parameterType;
    private String parameterDirection;
    private String umlType;

    public MyUmlParameter(String parent,
                          String name, String direction,
                          String id, String umlType) {
        this.parameterId = id;
        this.parameterParent = parent;
        this.parameterName = name;
        this.parameterDirection = direction;
        this.umlType = umlType;
        //this.parameterType = type;
    }

    public String getParameterDirection() {
        return this.parameterDirection;
    }

    public void print() {
        System.out.println("parentid: " + this.parameterParent + " name: "
                + parameterName + " direction: " +
                this.parameterDirection + " id: " + parameterId
                + " type: " + this.umlType);
    }
}
