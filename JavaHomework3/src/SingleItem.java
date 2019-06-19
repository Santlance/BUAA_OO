import java.math.BigInteger;
import java.util.Stack;

public class SingleItem {
    private String deResult;

    public SingleItem(String inputThis) {
        String inputStr = inputThis;
        if (inputStr.startsWith("(") && inputStr.endsWith(")")) {
            //表达式因子
            PlusItem plusItem = new PlusItem(
                    inputStr.substring(1, inputStr.length() - 1));
            deResult = plusItem.getDeresult();
        } else if (inputStr.matches("x(\\^[\\+\\-]?\\d+)?")) {
            //幂函数因子
            deResult = dePow(inputStr);
        } else if (inputStr.matches("[\\+\\-]?\\d+")) {
            //常数因子
            deResult = deConst(inputStr);
        } else if (inputStr.startsWith("sin(") || inputStr.startsWith("cos(")) {
            //三角函数因子
            deResult = deTrigono(inputStr);
        } else {
            System.out.println("WRONG FORMAT! \n singleitem de error");
            System.exit(0);
        }
    }

    public String dePow(String deThis) {
        int p = deThis.indexOf("^");
        if (p < 0) {
            return "1";
        } else {
            BigInteger q = new BigInteger(deThis.substring(p + 1));
            return q.toString() + "*x^" + q.subtract(BigInteger.ONE).toString();
        }
    }

    public String deConst(String deThis) {
        return "0";
    }

    public String deTrigono(String deThis) {
        String inputStr = deThis;
        String result = "";
        String outside = "";
        int p = 0;
        Stack bracketNum = new Stack();
        for (int i = 0; i < inputStr.length(); i++) {
            if (inputStr.charAt(i) == '(') {
                bracketNum.push(inputStr.charAt(i));
            } else if (inputStr.charAt(i) == ')') {
                bracketNum.pop();
            }
            if (inputStr.charAt(i) == '^' && bracketNum.empty()) {
                p = i;
            }
        }
        if (p == 0) {
            if (inputStr.startsWith("sin")) {
                outside = "cos" + inputStr.substring(3);
            } else if (inputStr.startsWith("cos")) {
                outside = "-1*sin" + inputStr.substring(3);
            }
        } else {
            if (inputStr.startsWith("sin")) {
                BigInteger q = new BigInteger(inputStr.substring(p + 1));
                outside = q.toString() + "*sin" + inputStr.substring(3, p)
                        + "^" + q.subtract(BigInteger.ONE).toString() +
                        "*cos" + inputStr.substring(3, p);
            } else if (inputStr.startsWith("cos")) {
                BigInteger q = new BigInteger(inputStr.substring(p + 1));
                outside = q.toString() + "*cos" + inputStr.substring(3, p)
                        + "^" + q.subtract(BigInteger.ONE).toString() +
                        "*-1*sin" + inputStr.substring(3, p);
            }
        }
        //取得外层函数导数
        if (p != 0) {
            inputStr = inputStr.substring(0, p);
        }
        //取非指数部分
        if (inputStr.endsWith(")")) {
            inputStr = inputStr.substring(4, inputStr.length() - 1);
            if (inputStr.equals("x")) {
                result = outside;
            } else {
                result = outside + "*" + deNest(inputStr);
            }
        } else {
            System.out.println("WRONG FORMAT!\n no bracket");
            System.exit(0);
        }
        return result;
    }

    String deNest(String inputThis) {
        String inputStr = inputThis;
        String result = "";
        if (inputStr.startsWith("(") && inputStr.endsWith(")")) {
            inputStr = inputThis.substring(1, inputStr.length() - 1);
            PlusItem a = new PlusItem(inputStr);
            result = a.getDeresult();
        } else if (inputStr.matches("x(\\^[\\+\\-]?\\d+)?")) {
            PlusItem a = new PlusItem(inputStr);
            result = a.getDeresult();
        } else if (inputStr.matches("[\\+\\-]?\\d+")) {
            PlusItem a = new PlusItem(inputStr);
            result = a.getDeresult();
        } else if (inputStr.startsWith("sin(") || inputStr.startsWith("cos(")) {
            PlusItem a = new PlusItem(inputStr);
            result = a.getDeresult();
        } else {
            System.out.println("WRONG FORMAT!\n trigono nest error");
            System.exit(0);
        }
        return "(" + result + ")";
    }

    public String getDeResult() {
        return this.deResult;
    }

}
