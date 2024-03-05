package PT1;
import java.util.*;

public class Mage implements Comparable<Mage> {
    private String name;
    private int level;
    private double power;
    private Set<Mage> apprentices;
    public static String sortMode = "default";

    //default compareTo
    @Override
    public int compareTo(Mage o) {
        if(sortMode.equals("natural"))
        {
            int result = name.compareTo(o.name);
            if (result != 0) {
                return result;
            }
            result = Integer.compare(level, o.level);
            if (result != 0) {
                return result;
            }
            return Double.compare(power, o.power);
        }
        if(sortMode.equals("alternative"))
        {
            int result = Integer.compare(level, o.level);
            if (result != 0) {
                return result;
            }
            result = name.compareTo(o.name);
            if (result != 0) {
                return result;
            }
            return Double.compare(power, o.power);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", power=" + power +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mage mage = (Mage) o;
        return level == mage.level && Double.compare(mage.power, power) == 0 && name.equals(mage.name) && apprentices.equals(mage.apprentices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, power, apprentices);
    }

    //constructor
    public Mage(String name, int level, double power) {
        this.name = name;
        this.level = level;
        this.power = power;
        this.sortMode = sortMode;
        if (sortMode.equals("default"))
            this.apprentices = new HashSet<>();
        if(sortMode.equals("natural") || sortMode.equals("alternative"))
            this.apprentices = new TreeSet<>();
    }

    public Map getStatistics() {
        Map<Mage,Integer> statistics;
        if(sortMode.equals("default")) statistics = new HashMap<>();
            else statistics = new TreeMap<>();

        populateStatistics(statistics, this);
        return  statistics;
    }

    public int populateStatistics(Map<Mage, Integer> statistics, Mage mage) {
        int count = 0;
        for (Mage apprentice : mage.getApprentices()) {
            count += populateStatistics(statistics, apprentice);
        }
        count += mage.getApprentices().size(); // add count of all apprentices
        statistics.put(mage, count);
        return count;
    }

    public void addApprentice(Mage apprentice) {
        this.apprentices.add(apprentice);
    }

    //getters and setters

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getPower() {
        return power;
    }

    public Set<Mage> getApprentices() {
        return apprentices;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setApprentices(Set<Mage> apprentices) {
        this.apprentices = apprentices;
    }
}
