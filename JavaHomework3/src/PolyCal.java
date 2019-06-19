import java.util.Scanner;

public class PolyCal {
    public static void main(String[] args) {
        try {
            Scanner src = new Scanner(System.in);
            String input = src.nextLine();
            IsLegal judgeStr = new IsLegal(input);
            if (judgeStr.judge()) {
                input = input.replaceAll("\\s+", "");
                PlusItem result = new PlusItem(input);
                System.out.println(result.getDeresult());
                System.out.println("CORRECT FORMAT!");
            } else {
                System.out.println("WRONG FORMAT!");
            }
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
