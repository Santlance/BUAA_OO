import java.util.Stack;

public class PlusItem {
    private String deresult;

    public PlusItem(String inputThis) {
        String inputStr = inputThis;
        String result = "";
        String[] plusitem;
        inputStr = splitPlus(inputStr);
        plusitem = inputStr.split("&");//得到每项的和形式
        for (int i = 0; i < plusitem.length; i++) {
            boolean isPos = true;
            if (plusitem[i].startsWith("++") ||
                    plusitem[i].startsWith("--")) {
                isPos = true;
                plusitem[i] = plusitem[i].substring(2);
            } else if (plusitem[i].startsWith("+-") ||
                    plusitem[i].startsWith("-+")) {
                isPos = false;
                plusitem[i] = plusitem[i].substring(2);
            } else if (plusitem[i].startsWith("+")) {
                isPos = true;
                plusitem[i] = plusitem[i].substring(1);
            } else if (plusitem[i].startsWith("-")) {
                isPos = false;
                plusitem[i] = plusitem[i].substring(1);
            }
            //去前面的项的符号
            MuilItem muilty = new MuilItem(plusitem[i]);
            String middle = muilty.getDeResult();
            if (!isPos) {
                middle = "-1*(" + middle + ")";
            }
            result = result + middle + "+";
        }
        deresult = result.substring(0, result.length() - 1);
        //
    }

    public String splitPlus(String inputThis) {
        StringBuffer inputStr = new StringBuffer(inputThis);
        Stack bracketNum = new Stack();
        for (int i = 0; i < inputStr.length() - 1; i++) {
            if (inputStr.charAt(i) == '(') {
                bracketNum.push(inputStr.charAt(i));
            } else if (inputStr.charAt(i) == ')') {
                bracketNum.pop();
            }
            //算括号
            String present = inputStr.substring(i, i + 1);
            String next = inputStr.substring(i + 1, i + 2);
            if (!present.matches("[\\+\\-\\*\\^]") && bracketNum.empty()
                    && next.matches("[\\+\\-]")) {
                inputStr.insert(i + 1, "&");
                i++;
            } else {
                continue;
            }
        }
        return inputStr.toString();
    }

    public String getDeresult() {
        return this.deresult;
    }

}
