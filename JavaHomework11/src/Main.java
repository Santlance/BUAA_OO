import com.oocourse.uml2.interact.AppRunner;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        //long begintime = System.currentTimeMillis();
        //FileInputStream inputStream = new FileInputStream("uml_2_8.txt");
        //System.setIn(inputStream);
        //PrintStream printStream = new PrintStream("out.txt");
        //System.setOut(printStream);
        AppRunner appRunner = AppRunner.newInstance(MyGeneralInteraction.class);
        appRunner.run(args);
        //long endtime = System.currentTimeMillis();
        //System.out.println(endtime - begintime);
    }
}
