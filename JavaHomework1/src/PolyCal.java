import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolyCal {
    public static boolean isLegal(String str) {
        //String inputStr = str.trim();
        //cannot trim
        String inputStr = str;
        if (inputStr.matches("\\s*")) { //check if is null
            return false;
        } else if (!inputStr.matches("[\\+\\-\\*\\^x \\t\\d]+")) {
            //check if contains other char
            return false;
        } else { //check if the number contains space
            String regCheckNum = "((\\d\\s+\\d)|" +   // the num has space
                    "(\\^\\s*[\\+\\-]\\s+\\d)|" +     // the num of pow
                    "([\\+\\-]\\s*[\\+\\-]\\s+\\d))"; // the num of coef const
            Pattern patternNum = Pattern.compile(regCheckNum);
            Matcher matcherNum = patternNum.matcher(inputStr);
            if (matcherNum.find()) {
                return false;
            }
            // complete checking if number contains space
            inputStr = inputStr.replaceAll("\\s*", "");
            //if number doesn't contains space,then delete it
            String regChkItem = "[\\+\\-]*\\d*\\**[x]*(\\^+[\\+\\-]*\\d*|\\d*)";
            //the use of small bracket this reg has 64 + 32
            String regConstant = "[\\+\\-]{0,2}\\d+";
            String regNumItem = "[\\+\\-]{0,2}\\d+\\*x";
            String regItem = "[\\+\\-]{0,2}x";
            String regItemPow = "[\\+\\-]{0,2}x\\^[\\+\\-]?\\d+";
            String regNumItemPow = "[\\+\\-]{0,2}\\d+\\*x\\^[\\+\\-]?\\d+";
            Pattern patternItem = Pattern.compile(regChkItem);
            Matcher matcherItem = patternItem.matcher(inputStr);
            while (matcherItem.find()) {
                //System.out.println(matcherItem.group(0));
                if (matcherItem.group(0).matches("\\s*")) {
                    continue;
                } else if (matcherItem.group(0).matches(regConstant)) {
                    continue;
                } else if (matcherItem.group(0).matches(regItem)) {
                    continue;
                } else if (matcherItem.group(0).matches(regItemPow)) {
                    continue;
                } else if (matcherItem.group(0).matches(regNumItem)) {
                    continue;
                } else if (matcherItem.group(0).matches(regNumItemPow)) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner src = new Scanner(System.in);
        String inputStr = src.nextLine();
        if (isLegal(inputStr)) {
            Poly polyExpress = new Poly(inputStr);
            polyExpress.printPoly(polyExpress.derivative(polyExpress));
            //System.out.println("CORRECT FORMAT!");
        } else {
            System.out.println("WRONG FORMAT!");
        }
    }
}
