package org.example;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Scanner;


public class Main {
    static EntityManagerFactory entityManagerFactory;
    static EntityManager entityManager;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)  {
        entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();

        initializeNewEntities();
        showAllEntities();
        readCommands();

        entityManager.close();
        entityManagerFactory.close();


    }

    public static void readCommands() {
        System.out.println("Enter \"help\" to see avaiable commands");
        while(true){
            String command = scanner.nextLine();
            if(command.equals("help")){
                System.out.println("commands:");
                System.out.println("+mage name level tower_name");
                System.out.println("+tower name height");

                System.out.println("? mage > level");
                System.out.println("? tower < height");
                System.out.println("? tower_name mage > level");

                System.out.println("show");
                System.out.println("q");
            }
            else if(command.equals("show")){
                showAllEntities();
            }
            else if(command.equals("q")){
                break;
            }
            else {
                String[] words = command.split("\\s+");
                if (words.length > 0) {
                    String subCommand = words[0];
                    scanAddRemoveCommands(subCommand, words);

                    if(subCommand.equals("?")){
                        scanQueryCommabds(words);
                    }
                }
            }

        }
        scanner.close();
    }


    public static void scanQueryCommabds(String[] words){
        if(words.length == 4) {
            if(words[1].equals("mage")){
                //? mage > level
                int level = Integer.parseInt(words[3]);
                showMagesWithGreaterLevel(level);
            }
            if(words[1].equals("tower")){
                //? tower < height
                int height = Integer.parseInt(words[3]);
                showTowersWithSmallerHight(height);
            }
        }
        else  if(words.length == 5) {
            //? tower_name mage > level
            int level = Integer.parseInt(words[4]);
            String towerName = words[1];
            showMagesWithGreaterLevelFromTower(level, towerName);
        }
    }

    public static void showMagesWithGreaterLevelFromTower(int level, String towerName){
        Query query = entityManager.createQuery("SELECT e FROM Mage e WHERE e.level > :level AND e.tower.name = :towerName");
        query.setParameter("level", level);
        query.setParameter("towerName", towerName);
        List<Mage> entities = query.getResultList();
        for (Mage entity : entities) {
            System.out.println("Name: " + entity.getName() + ", level: " + entity.getLevel() + ", tower: " + entity.getTower().getName());
        }
    }

    public static void showMagesWithGreaterLevel(int level) {
        Query query = entityManager.createQuery("SELECT e FROM Mage e WHERE e.level > :level");
        query.setParameter("level", level);
        List<Mage> entities = query.getResultList();
        for (Mage entity : entities) {
            System.out.println("Name: " + entity.getName() + ", level: " + entity.getLevel() + ", tower: " + entity.getTower().getName());
        }
    }

    public static void showTowersWithSmallerHight(int height) {
        Query query = entityManager.createQuery("SELECT e FROM Tower e WHERE e.height < :height");
        query.setParameter("height", height);
        List<Tower> entities = query.getResultList();
        for (Tower entity : entities) {
            System.out.println("Name: " + entity.getName() + ", height: " + entity.getHeight());
        }
    }

    public static void scanAddRemoveCommands(String subCommand, String[] words){
        if(subCommand.equals("+mage")){
            String name = words[1];
            int level = Integer.parseInt(words[2]);
            String tower_name = words[3];
            addNewMage(name, level, tower_name);
        }
        else if(subCommand.equals("+tower")){
            String name = words[1];
            int height = Integer.parseInt(words[2]);
            addNewTower(name, height);
        }
        else if(subCommand.equals("-mage")){
            String name = words[1];
            removeMage(name);
        }
        else if(subCommand.equals("-tower")){
            String name = words[1];
            removeTower(name);
        }
    }

    public static void showAllEntities() {
       showAllMages();
       showAllTowers();

    }

    public static void showAllMages() {
        System.out.println("Mages:");
        Query query = entityManager.createQuery("SELECT e FROM Mage e");
        List<Mage> entities = query.getResultList();

        for (Mage entity : entities) {
            System.out.println("Name: " + entity.getName() + ", level: " + entity.getLevel() + ", tower: " + entity.getTower().getName());
        }
    }

    public static void showAllTowers() {
        System.out.println("Towers:");
        Query query = entityManager.createQuery("SELECT e FROM Tower e");
        List<Tower> entities = query.getResultList();

        for (Tower entity : entities) {
            System.out.println("Name: " + entity.getName() + ", height: " + entity.getHeight() + ", mages: ");
            //show tower's mages
            for(Mage mage : entity.getMages()) {
                System.out.println("    Name: " + mage.getName());
            }
        }
    }

    public static void initializeNewEntities() {
        addNewTower("t1", 5);
        addNewMage("m1", 6, "t1");
        addNewMage("m2", 7, "t1");
        addNewMage("m3", 8, "t1");

        addNewTower("t2", 9);
        addNewMage("m4", 10, "t2");
        addNewMage("m5", 11, "t2");
        addNewMage("m6", 12, "t2");

    }

    public static void addNewEntity(Object o) {
        //start the transaction, each transaction ends with commit
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(o);

        transaction.commit();
    }

    public static void removeEntity(Object o){
        entityManager.getTransaction().begin();
        entityManager.remove(o);
        entityManager.getTransaction().commit();
    }

    public static void addNewTower(String name, Integer height) {
        Tower tower = new Tower(name, height);
        addNewEntity(tower);
    }

    public static void addNewMage(String name, Integer level , String towerName) {
        //find tower in db
        Tower tower = entityManager.find(Tower.class, towerName);
        if(tower != null) {
            //create mage
            Mage mage = new Mage(name, level, tower);
            //add mage to tower's list of mages
            tower.getMages().add(mage);
            //add mage to db
            addNewEntity(mage);
        }
        else
            System.out.println("error");
    }

    public static void removeMage(String name){
        Mage mage = entityManager.find(Mage.class, name);
        if (mage != null) {
            //remove mage from his tower
            Tower tower = mage.getTower();
            tower.getMages().remove(mage);
            //remove mage from db
            removeEntity(mage);

        }
        else
            System.out.println("error");

    }

    public static  void removeTower(String name) {
        Tower tower = entityManager.find(Tower.class, name);
        if(tower != null){
            //remove all tower's mages from db
            var mages = List.copyOf(tower.getMages());
            for(Mage mage : mages ){
                tower.getMages().remove(mage);
                removeEntity(mage);
            }
            //remove tower from db
            removeEntity(tower);
        }
        else
            System.out.println("error");

    }



}