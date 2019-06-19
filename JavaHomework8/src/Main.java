import com.oocourse.specs2.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        //FileInputStream files = new FileInputStream("input.txt");
        //System.setIn(files);
        AppRunner runner = AppRunner.newInstance(
                MyPath.class, MyGraph.class);
        runner.run(args);
    }
}
