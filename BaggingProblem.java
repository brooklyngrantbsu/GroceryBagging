import java.util.*;
import java.io.*;

public class BaggingProblem {

    boolean[][] canPackWith;
    Vector<Bag> bags = new Vector<Bag>();

    HashMap<String, Item> items = new HashMap<String, Item>(); 

    public class Item implements Comparable<Item> {
        int id = items.size();
        String name;
        int size;
        Bag bag = null;

        StringTokenizer st_constraints;
        Vector<Integer> domain; // bag ids I could possibly pack it into

        int numConstraints = 0;

        public Item(String name, int size, StringTokenizer st_constraints) {
            this.name = name;
            this.size = size;
            this.st_constraints = st_constraints;
        }

        @Override
        public int compareTo(BaggingProblem.Item i) {
            int sizeDiff = this.size - i.size;
            if (sizeDiff == 0) { // if equal in size, pick by constraint
                return this.numConstraints - i.numConstraints;
            }

            return i.size - this.size; // if this is wrong then it is slower
            //return this.size - i.size;
        }

        
    }

    public class Bag {
        int id = bags.size();
        int maxSize;
        int currSize = 0;
        HashMap<String, Item> packedInMe = new HashMap<String, Item>();

        public Bag(int maxBagSize) {
            this.maxSize = maxBagSize;
        }

        public boolean pack(Item i) {
            if (i.size > maxSize-currSize) {
                return false; // does not fit
            }

            // check constraints
            for (Item j : packedInMe.values()) {
                if (!canPackWith[i.id][j.id]) {
                    return false; // cannot pack with item already inside bag
                }
            }

            // no problems add to bag
            currSize += i.size;
            packedInMe.put(i.name, i);
            i.bag = this;
            return true;
        }

        public void unpack(Item i) {
            // remove item from bag
            packedInMe.remove(i.name);
            currSize -= i.size;
            i.bag = null;
        }
    }


    public BaggingProblem(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            int nb = Integer.parseInt(br.readLine());
            int maxBagSize = Integer.parseInt(br.readLine());
            for (int x=0; x < nb; x++) bags.add(new Bag(maxBagSize));

            String line;

            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                String name = st.nextToken();
                int size = Integer.parseInt(st.nextToken());
                if(items.get(name)!=null) throw new RuntimeException("Duplicate item name in file.");
                items.put(name, new Item(name, size, st));
            }

            canPackWith = new boolean[items.size()][items.size()];
            for (int x=0; x < canPackWith.length; x++) Arrays.fill(canPackWith[x], true);
            
            for (Item i : items.values()) {
                if (i.st_constraints.hasMoreTokens()) { // has a '+' or '-'
                    if (i.st_constraints.nextToken().equals("+")) { // is '+'
                        Vector<String> goodStuff = new Vector<String>();
                        while (i.st_constraints.hasMoreTokens()) {
                            goodStuff.add(i.st_constraints.nextToken());
                        }
                        for (String itemName : items.keySet()) { // others cannot be with
                            if (!goodStuff.contains(itemName)) {
                                var j = items.get(itemName);
                                if (canPackWith[i.id][j.id]) { // only set if true
                                    i.numConstraints++;
                                    j.numConstraints++;
                                    canPackWith[i.id][j.id] = false; // cannot pack i and j
                                    canPackWith[j.id][i.id] = false; // cannot pack j and i
                                }
                            }
                        }

                    } else {  // is "-", cant pack with
                        while (i.st_constraints.hasMoreTokens()) {
                            Item j = items.get(i.st_constraints.nextToken());
                            if (canPackWith[i.id][j.id]) { // only set if true
                                i.numConstraints++;
                                j.numConstraints++;
                                canPackWith[i.id][j.id] = false; // cannot pack i and j
                                canPackWith[j.id][i.id] = false; // cannot pack j and i
                            }
                        }
                    }

                }
            }

        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Please enter a valid file path.");
        } catch (IOException ex) {
            System.out.println("File format not correct. Here is the error: " + ex);
        }


    }

    public boolean search(PriorityQueue<Item> pq) {

        if (pq.isEmpty()) { // BASE CASE: packed all the items!
            return true;
        }

        Item i = pq.remove();

        for (Bag b : bags) {
            if (b.pack(i)) {
                if(search(pq)) {
                    return true; // worked in this bag!
                } else {
                    // that item didn't work in the bag
                    // try in another bag
                    b.unpack(i);
                }
            }
        }


        return false; // failed, item cannot go in any bag

    }

    public boolean search() {
        
        PriorityQueue<Item> pq = new PriorityQueue<Item>();

        for (Item i : items.values()) {
            pq.add(i);
        }

        return search(pq);
    }

    public void printPacking() {
        // This method will only be called on success
        System.out.println("success");
    
        for (Bag b : bags) {
            boolean firstItem = true;
            for (Item i : b.packedInMe.values()) {
                if (!firstItem) { // don't put tab before first item
                    System.out.print("\t");
                }
                System.out.print(i.name);
                firstItem = false;
            }
            System.out.println(); // next line
        }
    }       

}
