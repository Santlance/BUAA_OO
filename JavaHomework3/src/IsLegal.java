import java.math.BigInteger;
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
                    "(\\d\\s*\\^)|" +             //数字作为底数
                    // the num of coef const
                    "(s\\s*i\\s+n)|" +
                    "(c\\s*o\\s+s)|" +
                    "(s\\s+i\\s*n)|" +
                    "(c\\s+o\\s*s)|" +
                    "((sin|cos)\\s*\\(\\s*[+\\-]\\s+\\d+\\)))";
            Pattern patternSpace = Pattern.compile(regCheSpace);
            Matcher matcherSpace = patternSpace.matcher(judgeThis);
            if (matcherSpace.find()) {
                return false;
            }
            judgeThis = judgeThis.replaceAll("\\s+", "");
            String pow = "\\^\\d+";
            Pattern patternPow = Pattern.compile(pow);
            Matcher matcherPow = patternPow.matcher(judgeThis);
            while (matcherPow.find()) {
                BigInteger a = new BigInteger(matcherPow.group().substring(1));
                if (a.compareTo(new BigInteger("10000")) > 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
