import java.util.Stack;

public class MuilItem {
    private String deResult;

    public MuilItem(String inputThis) {
        String inputStr = inputThis;
        String[] muilitem;
        inputStr = splitMuil(inputStr);
        String result = "";
        muilitem = inputStr.split("@");
        for (int i = 0; i < muilitem.length; i++) {
            SingleItem sinItem = new SingleItem(muilitem[i]);
            String ahead = sinItem.getDeResult();
            String behind = "";
            for (int j = 0; j < muilitem.length; j++) {
                if (j == i) {
                    behind = behind + "(" + ahead + ")" + "*";
                } else {
                    behind = behind + muilitem[j] + "*";
                }
            }
            result = result + behind.substring(0, behind.length() - 1) + "+";
        }
        deResult = result.substring(0, result.length() - 1);
    }

    public String splitMuil(String inputThis) {
        StringBuffer inputStr = new StringBuffer(inputThis);
        Stack bracketNum = new Stack();
        for (int i = 0; i < inputStr.length() - 1; i++) {
            if (inputStr.charAt(i) == '(') {
                bracketNum.push(inputStr.charAt(i));
            } else if (inputStr.charAt(i) == ')') {
                bracketNum.pop();
            }
            String present = inputStr.substring(i, i + 1);
            if (present.equals("*") && bracketNum.empty()) {
                inputStr.replace(i, i + 1, "@");
            } else {
                continue;
            }
        }
        return inputStr.toString();
    }

    public String getDeResult() {
        return this.deResult;
    }

}
