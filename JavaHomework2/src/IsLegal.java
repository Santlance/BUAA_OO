import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsLegal {
    private String isLegal;

    public IsLegal() {
        isLegal = null;
    }

    public IsLegal(String inputStr) {
        isLegal = inputStr;
    }

    public boolean judge() {
        String judgeThis = isLegal;
        if (judgeThis.matches("\\s*")) {
            return false;
        } else if (!judgeThis.matches("[\\+\\-\\*\\^x \\t\\dsincos\\(\\)]+")) {
            return false;
        } else {
            String regCheSpace = "((\\d\\s+\\d)|" +   // the num has space
                    "(\\^\\s*[\\+\\-]\\s+\\d)|" +     // the num of pow
                    "([\\+\\-]\\s*[\\+\\-]\\s*[\\+\\-]\\s+\\d)|" +
                    "(\\*\\s*[\\+\\-]\\s+\\d)|" +
                    // the num of coef const
                    "(s\\s*i\\s+n)|" +
                    "(c\\s*o\\s+s)|" +
                    "(s\\s+i\\s*n)|" +
                    "(c\\s+o\\s*s))";
            Pattern patternSpace = Pattern.compile(regCheSpace);
            Matcher matcherSpace = patternSpace.matcher(judgeThis);
            if (matcherSpace.find()) {
                return false;
            }
            judgeThis = judgeThis.replaceAll("\\s+", "");
            if (judgeThis.charAt(0) != '+' && judgeThis.charAt(0) != '-') {
                judgeThis = "+" + judgeThis;
            }
            String regItem = "[\\+\\-]{1,2}" +
                    "(([\\+\\-]?\\d+)|" +
                    "(x\\^[\\+\\-]?\\d+)|" +
                    "(x)|" +
                    "((sin|cos)\\(x\\)\\^[\\+\\-]?\\d+)|" +
                    "((sin|cos)\\(x\\))){1}" +
                    "((\\*[\\+\\-]?\\d+)|" +
                    "(\\*x\\^[\\+\\-]?\\d+)|" +
                    "(\\*x)|" +
                    "(\\*(sin|cos)\\(x\\)\\^[\\+\\-]?\\d+)|" +
                    "(\\*(sin|cos)\\(x\\)))*";
            //next time match the sin((.)+) then check each item spilted by *
            Pattern patternItem = Pattern.compile(regItem);
            Matcher matcherItem = patternItem.matcher(judgeThis);
            String ans = matcherItem.replaceAll("");
            //System.out.println(ans);
            if (ans.length() > 0) {
                return false;
            }
        }
        return true;
    }
}
