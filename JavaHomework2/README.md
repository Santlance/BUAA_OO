# 面向对象第一单元第二次作业

## 摘要

本次作业，用Java实现简单多项式导函数的求解。

## 类文件及说明

### PolyCal.java

PolyCal文件用来进行读入和输出操作。

```java
Scanner src = new Scanner(System.in);
String input = src.nextLine();
```

### IsLegal.java

IsLegal类用来判断输入的字符串是否合法。

#### 含有一个属性

```java
private String isLegal;
```

#### 提供两个构造器

```java
public IsLegal() {
    isLegal = null;
}

public IsLegal(String inputStr) {
    isLegal = inputStr;
}
```

#### 含有一个方法

```java
public boolean judge();
```

judge方法用来判断输入字符串是否合法

1.空串不合法

```java
judgeThis.matches("\\s*");
```

2.含有其他字符不合法

```java
!judgeThis.matches("[\\+\\-\\*\\^x \\t\\dsincos\\(\\)]+");
```

3.含有非法空格不合法

```java
String regCheSpace = "((\\d\\s+\\d)|" +    			 	// space between num
        "(\\^\\s*[\\+\\-]\\s+\\d)|" +      				// space between op and num in pow
        "([\\+\\-]\\s*[\\+\\-]\\s*[\\+\\-]\\s+\\d)|" +	 // space between op and num in coef
    	"(\\*\\s*\\[\\+\\-]\\s+\\d)|"				   // space between op and num in factor
        "(s\\s*i\\s+n)|" +							   
        "(c\\s*o\\s+s)|" +
        "(s\\s+i\\s*n)|" +
        "(c\\s+o\\s*s))";							   // space between the keyword like sin cos
```

4.无法匹配完正确的项不合法

```java
String regItem = "[\\+\\-]{1,2}" +                        
        "(([\\+\\-]?\\d+)|" +
        "(x\\^[\\+\\-]?\\d+)|" +
        "(x)|" +
        "((sin|cos)\\(x\\)\\^[\\+\\-]?\\d+)|" +
        "((sin|cos)\\(x\\))){1}" +                       // first item
        "((\\*[\\+\\-]?\\d+)|" +
        "(\\*x\\^[\\+\\-]?\\d+)|" +
        "(\\*x)|" +
        "(\\*(sin|cos)\\(x\\)\\^[\\+\\-]?\\d+)|" +
        "(\\*(sin|cos)\\(x\\)))*";                       // the folllowing items
Pattern patternItem = Pattern.compile(regItem);
Matcher matcherItem = patternItem.matcher(judgeThis);
String ans = matcherItem.replaceAll("");
if (ans.length() > 0) {
	return false;
}   
```

#### 

### Poly.java

Poly文件用来构造表达式对象及提供相关方法

#### 含有一个属性

```java
private ArrayList<Item> item;  // provide an array to store the item
```

#### 提供三个构造器

```java
public Poly() {
    item = null;
}
// item is null at the default condition
public Poly(ArrayList<Item> items) {
    item = items;
}
// input items arrays ,copy it directly
public Poly(String inputStr) ;
// input String,spilt it and construct the Item array
```

当用字符串构造Poly对象时

预处理

```java
String judgeThis = inputStr.replaceAll("\\s+", "");
if (judgeThis.charAt(0) != '+' || judgeThis.charAt(0) != '-') {
    judgeThis = "+" + judgeThis;
}
```

提取每一项

```java
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
Pattern patternItem = Pattern.compile(regItem);
Matcher matcherItem = patternItem.matcher(judgeThis);
```

构造数组

```java
while (matcherItem.find()) {
    thisitem.add(new Item(matcherItem.group(0)));
}
```

#### 含有三个方法

```java
public String toString() ; // print the Poly object
```

打印表达式调用Item类的toString方法先把每一个Item转换成String，再合并即可

```java
Item a = toPrint.item.get(i);
String sinStr = a.toString();
```

```java
public Poly combinePoly(); // combine the Poly object
```

合并表达式，先判断每一项是否相同，然后相加即可

```java
public Poly derivative();  // derivative the Poly object	
```

对表达式求导调用Item类的derivative方法求导，再相加即可

```java
for (int i = 0; i < this.item.size(); i++) {
    deThis = deThis.add(this.item.get(i).derivative());
}
```

### Item.java

#### 含有四个属性

```java
private BigInteger constNum;  // the num of constant
private BigInteger powNum;	  // the num of power of power function
private BigInteger sinNum;	  // the num of power of sin function
private BigInteger cosNum;	  // the num of power of cos function
```

#### 提供三个构造器

```java
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

public Item(String inputThis) ;
```

用字符串构造Item对象时

预处理

```java
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
```

然后合并相同的因子的指数

#### 含有十一个方法

在用字符串构造Item对象时，判断字符串属于哪一种因子的方法

```java
public static boolean isTrigono(String inputStr);
public static boolean isPower(String inputStr);
public static boolean isConst(String inputStr);
```

判断两个项相等的方法

```java
public boolean equals(Item equThis) ;
```

合并两个项的方法

```java
public Item combineItem(Item comThis) ;
```

访问Item对象的属性的方法

```java
public BigInteger getConstNum() ;
public BigInteger getCosNum() ;
public BigInteger getSinNum() ;
public BigInteger getPowNum() ;
```

打印Item对象的方法

```java
public String toString() ;
```

对Item对象求导的方法

```java
public Poly derivative();
```

# 修改记录

1.更改了作为带符号数因子时，空格的判断方法