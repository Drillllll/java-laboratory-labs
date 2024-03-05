package PT1;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String modes[] = {"default", "alternative", "natural"};
        Mage.sortMode = modes[2];

        String names[] = { "Yennefer", "Stregobor", "Vilgefortz", "Keira Metz", "Alzur", "Triss Merigold", "Dethmold",
                "Artorius Vigo", "Fringilla Vigo", "Istredd", "Philippa Eilhart", "Sabrina Glevissig", "Xarthisius", "Ciri" };
        Mage headMage = new Mage("Avallac'h", 100, 100 );
        List<Mage> mages = new ArrayList<>();

        //populate mages
        Random random = new Random();
        int i=0;
        for (String n : names) {
            mages.add( new Mage(names[i], random.nextInt(100), random.nextInt(100)) );
            i++;
        }

        addApprentices(mages, headMage);
        System.out.println("\nMages printed:");
        printMages(headMage, 0);
        printStatistics(headMage);
        //test();



    }

    //------------------------------
    public static void printStatistics(Mage headMage) {
        System.out.println("\nStatistics printed:");
        Map map = headMage.getStatistics(); //HashMap (not sorted) or TreeMap (sorted)
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            System.out.println(key + " = " + value);
        }
    }


    public static void printMages(Mage mage, int depth) {
        //print current mage
        for (int i = 0; i < depth; i++) {
            System.out.print("-");
        }
        System.out.print(mage.toString() + '\n');
        //print apprentices
        Set<Mage> apprentices = mage.getApprentices();
        for (Mage apprentice : apprentices) {

            printMages(apprentice, depth+1);
        }
    }

    public static void addApprentices(List<Mage> mages, Mage headMage) {
        //add apprentices
        int apprenticesN = 3;
        //list of apprenticesN-elements lists, each mage will have apprenticesN appr.
        int groupsN = (int) Math.ceil((double) mages.size() / apprenticesN); //number of apprenticesN-elements groups (ceil - round up)

        ArrayList<ArrayList<Mage>> groups = new ArrayList<ArrayList<Mage>>();
        // Initialize sublists
        int i=0;
        for ( i = 0; i < groupsN; i++) {
            groups.add(new ArrayList<Mage>());
        }

        int g=0;
        for(i=0; i< mages.size(); i++) {
            groups.get(g).add(mages.get(i));
            if(i%apprenticesN == apprenticesN-1)g++;
        }

        //attach first group to headMage
        for (Mage apprentice : groups.get(0)) {
            headMage.addApprentice(apprentice);
        }

        g=1; //ommit the first group, they are attached to the head mage
        for(i=0; i<mages.size(); i++) {
            if(g>groupsN-1) break;
            for (Mage apprentice : groups.get(g)) {
                mages.get(i).addApprentice(apprentice);
            }
            g++;
        }
    }

    public static void test(){
        Boolean testEquals = true;
        Boolean testCompareTo = true;
        if(testEquals)
        {
            Mage m1 = new Mage("a", 1, 1);
            Mage m2 = new Mage("a", 1, 1);
            System.out.println(m1.equals(m2));
        }
        if(testCompareTo) {
            Mage m = new Mage("master", 100, 100);
            Mage a3 = new Mage("b", 1, 1);
            Mage a1 = new Mage("a", 1, 1);
            Mage a4 = new Mage("a", 1, 2);
            Mage a2 = new Mage("a", 2, 1);

            m.addApprentice(a1);
            m.addApprentice(a2);
            m.addApprentice(a3);
            m.addApprentice(a4);
            for(Mage a : m.getApprentices()){
                System.out.println(a.toString());
            }
        }

    }
}
