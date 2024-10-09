import java.io.FileWriter;
import java.io.IOException;

public class BagIt {
    public static void main(String[] args) {
        BaggingProblem bp = new BaggingProblem(args);
    
        if (bp.search()) {
            String printed = bp.printPacking();
            try {
                FileWriter writer = new FileWriter(args[0] + ".out");
                writer.write(printed);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("failure");
        }
    }
}