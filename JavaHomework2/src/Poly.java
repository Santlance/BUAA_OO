import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly {
    private ArrayList<Item> item;

    public Poly() {
        item = null;
    }

    public Poly(ArrayList<Item> items) {
        item = items;
    }

    public Poly(String inputStr) {
        String judgeThis = inputStr.replaceAll("\\s+", "");
        if (judgeThis.charAt(0) != '+' || judgeThis.charAt(0) != '-') {
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
        ArrayList<Item> thisitem = new ArrayList<Item>();
        while (matcherItem.find()) {
            thisitem.add(new Item(matcherItem.group(0)));
        }
        item = thisitem;
    }

    public String toString() {
        String printStr = "";
        Poly toPrint = this.combinePoly();
        for (int i = 0; i < toPrint.item.size(); i++) {
            Item a = toPrint.item.get(i);
            String sinStr = a.toString();
            if (sinStr.equals("")) {
                continue;
            } else if (sinStr.startsWith("-")) {
                printStr = printStr + sinStr;
            } else {
                printStr = "+" + sinStr + printStr;
            }
        }
        if (printStr.startsWith("+")) {
            printStr = printStr.substring(1);
        } else if (printStr.equals("")) {
            printStr = "0";
        }
        return printStr;
    }

    public Poly combinePoly() {
        Poly comThis = this;
        for (int i = 0; i < comThis.item.size(); i++) {
            Item a = comThis.item.get(i);
            for (int j = i + 1; j < comThis.item.size(); j++) {
                Item b = comThis.item.get(j);
                if (a.equals(b)) {
                    a = a.combineItem(b);
                    comThis.item.set(j, new Item());
                }
            }
        }
        return comThis;
    }

    public Poly derivative() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item());
        Poly deThis = new Poly(items);
        for (int i = 0; i < this.item.size(); i++) {
            deThis = deThis.add(this.item.get(i).derivative());
        }
        return deThis;
    }

    public Poly add(Poly addThis) {
        for (int i = 0; i < addThis.item.size(); i++) {
            this.item.add(addThis.item.get(i));
        }
        return this.combinePoly();
    }
}
