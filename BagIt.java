public class BagIt {
    public static void main(String[] args) {
        BaggingProblem bp = new BaggingProblem(args);
    
        if (bp.search()) {
            bp.printPacking();
        } else {
            System.out.println("failure");
        }
    }
}