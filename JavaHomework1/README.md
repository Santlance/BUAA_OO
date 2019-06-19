# 面向对象第一单元第一次作业

## 摘要

本次作业，用Java实现简单多项式导函数的求解。

## 类文件及说明

### PolyCal.java

PolyCal文件用来进行表达式的读入和进行相关操作。

```java
public static boolean isLegal(String inputStr)
```

isLegal函数通过若干个步骤用来判断输入是否合法。

1.空串不合法

```java
inputStr.matches("\\s*")  \\输入为空或者空白字符WRONG FORMAT
```

2.含有其他字符不合法

```java
!inputStr.matches("[\\+\\-\\*\\^x \\t\\d]+") \\输入含非合法字符WRONG FORMAT
```

3.带符号数有空格不合法

```java
 String regCheckNum = "((\\d\\s+\\d)|" +              // the num has space
                    "(\\^\\s*[\\+\\-]\\s+\\d)|" +     // the num of pow
                    "([\\+\\-]\\s*[\\+\\-]\\s+\\d))"; // the num of const coef
            Pattern patternNum = Pattern.compile(regCheckNum);
            Matcher matcherNum = patternNum.matcher(inputStr);
            if (matcherNum.find()) {
                return false;
            }
```

4.在检查所有的带符号数合法后，可以去空格再提取每一项进行检查是否合法

```java
inputStr = inputStr.replaceAll("\\s*", "");  \\删除空格
String regChkItem = "[\\+\\-]*\\d*\\**[x]*(\\^+[\\+\\-]*\\d*|\\d*)"; \\提取每一项
String regConstant = "[\\+\\-]{0,2}\\d+";
\\常数项合法
String regNumItem = "[\\+\\-]{0,2}\\d+\\*x";
\\不带幂数的项合法
String regItem = "[\\+\\-]{0,2}x";
\\不带幂数和指数的项合法
String regItemPow = "[\\+\\-]{0,2}x\\^[\\+\\-]?\\d+";
\\不带系数的项合法
String regNumItemPow = "[\\+\\-]{0,2}\\d+\\*x\\^[\\+\\-]?\\d+";
\\带系数和幂数的项合法

\\不符合上述五种情况为WRONG FORMAT
```

判断输入合法后，就用字符串生成Poly对象，然后进行运算进而输出

```java
if (isLegal(inputStr)) {
    Poly polyExpress = new Poly(inputStr);
    polyExpress.printPoly(polyExpress.derivative(polyExpress));
} else {
    System.out.println("WRONG FORMAT!");
}
```

### Poly.java

Poly文件用来构造表达式对象及提供相关方法

#### 一个Poly对象含有两个属性

```java
private ArrayList<BigInteger> coef; //系数
private ArrayList<BigInteger> pow;  //指数
```

#### 提供两个构造器

```java
public Poly() {
    coef = null;
    pow = null;
}
//默认情况下都为空
```

```java
public Poly(String inputThis) 
//通过字符串构造对象
```

若通过字符串构造对象，首先对字符串进行处理

```java
inputStr = inputThis.replaceAll("\\s*", ""); //去空格
//把两个操作符换成一个操作符
inputStr = inputStr.replaceAll("[\\+][\\+]", "+");
inputStr = inputStr.replaceAll("[\\-][\\-]", "+");
inputStr = inputStr.replaceAll("[\\+][\\-]", "-");
inputStr = inputStr.replaceAll("[\\-][\\+]", "-");
```

然后提取每一个项，放入系数和指数数组中即可完成构造

```java
String regChkItem = "[\\+\\-]*\\d*\\**[x]*(\\^+[\\+\\-]*\\d*|\\d*)";  //提取项
String regConstant = "[\\+\\-]{0,2}\\d+";
//常数项
String regNumItem = "[\\+\\-]{0,2}\\d+\\*x";
//仅含有系数的项
String regItem = "[\\+\\-]{0,2}x";
//不含系数和指数的项
String regItemPow = "[\\+\\-]{0,2}x\\^[\\+\\-]?\\d+";
//仅含指数的项
String regNumItemPow = "[\\+\\-]{0,2}\\d+\\*x\\^[\\+\\-]?\\d+";
//含系数和指数的项
```

#### 含有三个方法

```java
public void printPoly(Poly printThis)
    //用来输出表达式对象
```

```java
public Poly derivative(Poly deThis)
    //用来对表达式进行求导
```

```java
public Poly combinePoly(Poly comThis)
    //用来合并同类项
```

对表达式进行求导，遵循两个原则即可

```java
if (powNum.compareTo(zero) == 0) {
    ansCoef.add(zero);
    ansPow.add(zero);
} else {
    ansCoef.add(coefNum.multiply(powNum));
    ansPow.add(powNum.subtract(one));
}
```

对表达式进行合并，把同指数类相加即可

```java
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
```

# 修改记录

1.修改了 + + x的判定

2.修改了代码风格，注释缩进

3.修改了合并同类项的 j 的起始为 i + 1

4.优化了输出，合并同类项

5.进一步优化输出，判断前面的 + 号

6.更改了空白字符的判断界定，从\\s改为 和\t

7.更改了判断带符号数是否合法的方法

8.删除了trim操作

9.（忘记处理-1x的省略）