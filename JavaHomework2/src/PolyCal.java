import java.util.Scanner;

public class PolyCal {
    public static void main(String[] args) {
        try {
            Scanner src = new Scanner(System.in);
            String input = src.nextLine();
            IsLegal judgeStr = new IsLegal(input);
            if (judgeStr.judge()) {
                Poly polyExpress = new Poly(input);
                System.out.println(polyExpress.derivative().toString());
                System.out.print("CORRECT FORMAT!");
            } else {
                System.out.print("WRONG FORMAT!");
            }
        } catch (Exception e) {
            System.out.print("WRONG FORMAT!");
        }
    }
}
