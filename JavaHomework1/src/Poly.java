import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly {
    private ArrayList<BigInteger> coef;
    private ArrayList<BigInteger> pow;

    public Poly() {
        coef = null;
        pow = null;
    }

    public Poly(String inputThis) {
        ArrayList<BigInteger> coef1 = new ArrayList();
        ArrayList<BigInteger> pow1 = new ArrayList();
        String inputStr;
        inputStr = inputThis.replaceAll("\\s*", "");
        inputStr = inputStr.replaceAll("[\\+][\\+]", "+");
        inputStr = inputStr.replaceAll("[\\-][\\-]", "+");
        inputStr = inputStr.replaceAll("[\\+][\\-]", "-");
        inputStr = inputStr.replaceAll("[\\-][\\+]", "-");
        String regChkItem = "[\\+\\-]*\\d*\\**[x]*(\\^+[\\+\\-]*\\d*|\\d*)";
        Pattern patternItem = Pattern.compile(regChkItem);
        Matcher matcherItem = patternItem.matcher(inputStr);
        String regConstant = "[\\+\\-]{0,2}\\d+";
        String regNumItem = "[\\+\\-]{0,2}\\d+\\*x";
        String regItem = "[\\+\\-]{0,2}x";
        String regItemPow = "[\\+\\-]{0,2}x\\^[\\+\\-]?\\d+";
        String regNumItemPow = "[\\+\\-]{0,2}\\d+\\*x\\^[\\+\\-]?\\d+";
        while (matcherItem.find()) {
            //System.out.println(matcherItem.group(0));
            if (matcherItem.group(0).matches("\\s*")) {
                continue;
            } else if (matcherItem.group(0).matches(regConstant)) {
                coef1.add(new BigInteger(matcherItem.group(0)));
                pow1.add(new BigInteger("0"));
            } else if (matcherItem.group(0).matches(regItem)) {
                if (matcherItem.group(0).startsWith("+")) {
                    coef1.add(new BigInteger("1"));
                } else if (matcherItem.group(0).startsWith("x")) {
                    coef1.add(new BigInteger("1"));
                } else if (matcherItem.group(0).startsWith("-")) {
                    coef1.add(new BigInteger("-1"));
                }
                pow1.add(new BigInteger("1"));
            } else if (matcherItem.group(0).matches(regItemPow)) {
                if (matcherItem.group(0).startsWith("+")) {
                    coef1.add(new BigInteger("1"));
                } else if (matcherItem.group(0).startsWith("x")) {
                    coef1.add(new BigInteger("1"));
                } else if (matcherItem.group(0).startsWith("-")) {
                    coef1.add(new BigInteger("-1"));
                }
                int p = matcherItem.group(0).indexOf("^");
                pow1.add(new BigInteger(matcherItem.group(0).substring(p + 1)));
            } else if (matcherItem.group(0).matches(regNumItem)) {
                int p = matcherItem.group(0).indexOf("*");
                coef1.add(new BigInteger(matcherItem.group(0).substring(0, p)));
                pow1.add(new BigInteger("1"));
            } else if (matcherItem.group(0).matches(regNumItemPow)) {
                int p = matcherItem.group(0).indexOf("*");
                int q = matcherItem.group(0).indexOf("^");
                coef1.add(new BigInteger(matcherItem.group(0).substring(0, p)));
                pow1.add(new BigInteger(matcherItem.group(0).substring(q + 1)));
            } else {
                System.out.println("WRONG FORMAT!");
            }
        }
        coef = coef1;
        pow = pow1;
    }

    public void printPoly(Poly printThis) {
        String printStr = "";
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        Poly toPrint = printThis.combinePoly(printThis);
        for (int i = 0; i < toPrint.coef.size(); i++) {
            BigInteger coefNum = toPrint.coef.get(i);
            BigInteger powNum = toPrint.pow.get(i);
            if (coefNum.compareTo(zero) == 0) { // coef == 0
                continue;
                //coef equals to zero not print
            } else if (coefNum.compareTo(one) == 0) { //coef == 1
                if (powNum.compareTo(zero) == 0) { // pow ==0
                    printStr = "+" + coefNum.toString() + printStr;
                } else if (powNum.compareTo(one) == 0) { // pow == 1
                    printStr = "+x" + printStr;
                } else { // pow < 0 or pow > 1
                    printStr = "+x^" + powNum.toString() + printStr;
                }
            } else if (coefNum.compareTo(one) > 0) { // coef > 1
                if (powNum.compareTo(zero) == 0) { // pow == 0
                    printStr = "+" + coefNum.toString() + printStr;
                    // System.out.print("+" + coefNum + "*x^" + powNum);
                } else if (powNum.compareTo(one) == 0) { // pow == 1
                    printStr = "+" + coefNum.toString() + "*x" + printStr;
                    // System.out.print("+" + coefNum);
                } else { // pow < 0 or pow > 1
                    printStr = "+" + coefNum.toString()
                            + "*x^" + powNum.toString() + printStr;
                }
            } else if (coefNum.compareTo(zero) < 0) {  // coef < 0
                if (powNum.compareTo(zero) == 0) { // pow == 0
                    printStr += coefNum.toString();
                    // System.out.print("+" + coefNum + "*x^" + powNum);
                } else if (powNum.compareTo(one) == 0) { // pow == 1
                    printStr += coefNum.toString() + "*x";
                    // System.out.print("+" + coefNum);
                } else { // pow < 0 or pow > 1
                    printStr += coefNum.toString()
                            + "*x^" + powNum.toString();
                }
            }
        }
        if (printStr.equals("")) {
            System.out.print("0");
        } else if (printStr.startsWith("+")) {
            System.out.print(printStr.substring(1));
        } else {
            System.out.print(printStr);
        }
    }

    public Poly derivative(Poly deThis) {
        ArrayList<BigInteger> ansCoef = new ArrayList();
        ArrayList<BigInteger> ansPow = new ArrayList();
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        for (int i = 0; i < deThis.coef.size(); i++) {
            BigInteger coefNum = deThis.coef.get(i);
            BigInteger powNum = deThis.pow.get(i);
            if (powNum.compareTo(zero) == 0) {
                ansCoef.add(zero);
                ansPow.add(zero);
            } else {
                ansCoef.add(coefNum.multiply(powNum));
                ansPow.add(powNum.subtract(one));
            }
        }
        deThis.pow = ansPow;
        deThis.coef = ansCoef;
        return deThis;
    }

    public Poly combinePoly(Poly comThis) {
        ArrayList<BigInteger> ansCoef = new ArrayList();
        ArrayList<BigInteger> ansPow = new ArrayList();
        BigInteger zero = new BigInteger("0");
        for (int i = 0; i < comThis.coef.size(); i++) {
            BigInteger coefNum = comThis.coef.get(i);
            BigInteger powNum = comThis.pow.get(i);
            for (int j = i + 1; j < comThis.coef.size(); j++) {
                if (powNum.compareTo(comThis.pow.get(j)) == 0) {
                    coefNum = coefNum.add(comThis.coef.get(j));
                    comThis.coef.set(j, zero);
                }
            }
            ansCoef.add(coefNum);
            ansPow.add(powNum);
        }
        comThis.coef = ansCoef;
        comThis.pow = ansPow;
        return comThis;
    }
}
