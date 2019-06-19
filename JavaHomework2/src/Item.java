import java.math.BigInteger;
import java.util.ArrayList;

public class Item {
    private BigInteger constNum;
    private BigInteger powNum;
    private BigInteger sinNum;
    private BigInteger cosNum;

    public Item() {
        constNum = BigInteger.ZERO;
        powNum = BigInteger.ZERO;
        sinNum = BigInteger.ZERO;
        cosNum = BigInteger.ZERO;
    }

    public Item(BigInteger conNum,
                BigInteger powNum,
                BigInteger sinNum,
                BigInteger cosNum) {
        this.constNum = conNum;
        this.powNum = powNum;
        this.sinNum = sinNum;
        this.cosNum = cosNum;
    }

    public Item(String inputThis) {
        String inThis = inputThis;
        if (inThis.startsWith("+++") || inThis.startsWith("+--") ||
                inThis.startsWith("-+-") || inThis.startsWith("--+")) {
            inThis = inThis.substring(3);
        } else if (inThis.startsWith("++-") || inThis.startsWith("+-+") ||
                inThis.startsWith("-++") || inThis.startsWith("---")) {
            inThis = "-1*" + inThis.substring(3);
        } else if (inThis.startsWith("++") || inThis.startsWith("--")) {
            inThis = inThis.substring(2);
        } else if (inThis.startsWith("-+") || inThis.startsWith("+-")) {
            inThis = "-1*" + inThis.substring(2);
        } else if (inThis.startsWith("+")) {
            inThis = inThis.substring(1);
        } else if (inThis.startsWith("-")) {
            inThis = "-1*" + inThis.substring(1);
        }
        String[] factors = inThis.split("\\*");
        BigInteger powNum = BigInteger.ZERO;
        BigInteger conNum = BigInteger.ONE;
        BigInteger cosNum = BigInteger.ZERO;
        BigInteger sinNum = BigInteger.ZERO;
        for (int i = 0; i < factors.length; i++) {
            if (isConst(factors[i])) {
                conNum = conNum.multiply(new BigInteger(factors[i]));
            } else if (isPower(factors[i])) {
                int x = factors[i].indexOf('^');
                if (x < 0) {
                    powNum = powNum.add(BigInteger.ONE);
                } else {
                    powNum = powNum.add(
                            new BigInteger(factors[i].substring(x + 1)));
                }
            } else if (isTrigono(factors[i])) {
                int y = factors[i].indexOf('^');
                if (factors[i].startsWith("sin")) {
                    if (y < 0) {
                        sinNum = sinNum.add(BigInteger.ONE);
                    } else {
                        sinNum = sinNum.add(
                                new BigInteger(factors[i].substring(y + 1)));
                    }
                } else if (factors[i].startsWith("cos")) {
                    if (y < 0) {
                        cosNum = cosNum.add(BigInteger.ONE);
                    } else {
                        cosNum = cosNum.add(
                                new BigInteger(factors[i].substring(y + 1)));
                    }
                }
            } else if (factors[i].matches("\\s*")) {
                continue;
            }
        }
        this.cosNum = cosNum;
        this.sinNum = sinNum;
        this.powNum = powNum;
        this.constNum = conNum;
    }

    public static boolean isTrigono(String inputStr) {
        String regTri = "((sin|cos)\\(x\\)\\^[\\+\\-]?\\d+)|((sin|cos)\\(x\\))";
        if (inputStr.matches(regTri)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPower(String inputStr) {
        String regPow = "((x\\^[\\+\\-]?\\d+)|(x))";
        if (inputStr.matches(regPow)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isConst(String inputStr) {
        String regCon = "[\\+\\-]?\\d+";
        if (inputStr.matches(regCon)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals(Item equThis) {
        boolean r1 = this.cosNum.equals(equThis.cosNum);
        boolean r2 = this.powNum.equals(equThis.powNum);
        boolean r3 = this.sinNum.equals(equThis.sinNum);
        return r1 && r2 && r3;
    }

    public Item combineItem(Item comThis) {
        this.constNum = this.constNum.add(comThis.constNum);
        return this;
    }

    public BigInteger getConstNum() {
        return this.constNum;
    }

    public BigInteger getCosNum() {
        return this.cosNum;
    }

    public BigInteger getSinNum() {
        return this.sinNum;
    }

    public BigInteger getPowNum() {
        return this.powNum;
    }

    public String toString() {
        Item a = this;
        String toStr = "";
        BigInteger one = BigInteger.ONE;
        BigInteger zero = BigInteger.ZERO;
        BigInteger mone = BigInteger.valueOf(-1);
        if (!a.getConstNum().equals(zero)) {  //const !=0
            toStr += a.getConstNum().toString();
            if (a.getPowNum().equals(one)) {
                toStr += "*x";
            } else if (!a.getPowNum().equals(zero)) {
                toStr += "*x^" + a.getPowNum().toString();
            }
            if (a.getSinNum().equals(one)) {
                toStr += "*sin(x)";
            } else if (!a.getSinNum().equals(zero)) {
                toStr += "*sin(x)^" + a.getSinNum().toString();
            }
            if (a.getCosNum().equals(one)) {
                toStr += "*cos(x)";
            } else if (!a.getCosNum().equals(zero)) {
                toStr += "*cos(x)^" + a.getCosNum().toString();
            }
        }
        if (toStr.startsWith("1*")) {
            toStr = toStr.substring(2);
        } else if (toStr.startsWith("-1*")) {
            toStr = "-" + toStr.substring(3);
        }
        return toStr;
    }

    public Poly derivative() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(
                this.getConstNum().multiply(this.getPowNum()),
                this.getPowNum().subtract(BigInteger.ONE),
                this.getSinNum(),
                this.getCosNum()));
        items.add(new Item(
                this.getConstNum().multiply(this.getSinNum()),
                this.getPowNum(),
                this.getSinNum().subtract(BigInteger.ONE),
                this.getCosNum().add(BigInteger.ONE)));
        items.add(new Item(
                this.getConstNum().multiply(this.getCosNum()
                        .multiply(new BigInteger("-1"))),
                this.getPowNum(),
                this.getSinNum().add(BigInteger.ONE),
                this.getCosNum().subtract(BigInteger.ONE)));
        Poly deThis = new Poly(items);
        return deThis;
    }
}
