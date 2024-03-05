package org.example;
import java.util.*;


public class MageRepository {
    private List<Mage> collection = new LinkedList<>();

    public void initialize(Mage[] initializedMages){
        for(Mage m : initializedMages){
            collection.add(m);
        }

    }

    public Optional<Mage> find(String name) {
        //próba pobrania nieistniejącego obiektu zwraca pusty obiekt Optional
        //próba pobrania istniejącego obiektu zwraca obiekt Optional z zawartością
        Mage result = null;
        for(Mage m : collection){
            if(m.getName().equals(name)){
                result = m;
                break;
            }
        }
        return Optional.ofNullable(result);
    }
    public void delete(String name) {
        //próba usunięcia nieistniejącego obiektu powoduje IllegalArgumentException
        for(Mage m : collection){
            if(m.getName().equals(name)){
                collection.remove(m);
                return;
            }
        }
        throw new IllegalArgumentException();
    }
    public void save(Mage mage) {
        //próba zapisania obiektu, którego klucz główny już znajduje się w repozytorium
        //powoduje IllegalArgumentException.

        if (find(mage.getName()).isPresent()){
            throw new IllegalArgumentException();
        }
        else {
            collection.add(mage);
        }
    }

}