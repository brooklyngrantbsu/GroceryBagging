import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GroceryProbGen {


   private static final String[] ITEMS = {"bread", "rolls", "squash", "meat", "lima_beans", "apples", "tomatoes", "milk", "cheese", "yogurt"};
   private static Random random = new Random();


   // Generates a random grocery packing problem and writes to a file.
   public static void main(String[] args) throws IOException {
       if (args.length < 1) {
           System.out.println("Usage: java GroceryProbGen <output-file>");
           System.exit(1);
       }


       String outputFile = args[0];


       // Random number of bags (between 2 and 5)
       int numBags = random.nextInt(4) + 2;


       // Random maximum bag size (between 5 and 10)
       int maxBagSize = random.nextInt(6) + 5;


       // Random number of items (between 3 and 6)
       int numItems = random.nextInt(4) + 3;


       // Randomly generate item information
       List<String> itemLines = generateItems(numItems);


       // Write to file
       try (FileWriter writer = new FileWriter(outputFile)) {
           writer.write(numBags + "\n");
           writer.write(maxBagSize + "\n");
           for (String itemLine : itemLines) {
               writer.write(itemLine + "\n");
           }
       }


       System.out.println("Grocery packing problem generated in: " + outputFile);
   }


   // Generate random items with constraints and sizes
   private static List<String> generateItems(int numItems) {
       List<String> items = new ArrayList<>();
       List<String> selectedItems = new ArrayList<>();


       for (int i = 0; i < numItems; i++) {
           String item = ITEMS[random.nextInt(ITEMS.length)];
           while (selectedItems.contains(item)) {
               item = ITEMS[random.nextInt(ITEMS.length)];
           }
           selectedItems.add(item);


           // Random size for item (between 1 and 5)
           int size = random.nextInt(5) + 1;


           // Randomly decide if the item will have constraints or not
           String constraint = generateRandomConstraint(selectedItems, item);


           items.add(item + " " + size + constraint);
       }


       return items;
   }


   // Generate random constraints in the form of + or -
   private static String generateRandomConstraint(List<String> selectedItems, String currentItem) {
       if (selectedItems.size() <= 1) {
           return "";  // No constraints for the first item
       }


       StringBuilder constraint = new StringBuilder();
       boolean isPositiveConstraint = random.nextBoolean();


       if (isPositiveConstraint) {
           // + constraint
           constraint.append(" + ");
       } else {
           // - constraint
           constraint.append(" - ");
       }


       // Pick a random other item for the constraint
       String otherItem;
       do {
           otherItem = selectedItems.get(random.nextInt(selectedItems.size()));
       } while (otherItem.equals(currentItem));


       constraint.append(otherItem);


       return constraint.toString();
   }
}



