package org.example;
import java.util.Optional;

public class MageController {

    private final MageRepository repository;

    public MageController(MageRepository repository) {
        this.repository = repository;
    }
    public String find(String name) {
        //próba pobrania nieistniejącego obiektu powoduje zwrócenie obiektu String
        //o wartości not found
        //próba pobrania istniejącego obiektu zwraca obiekt String reprezentujący
        //znaleziony obiekt encyjny,

        Optional<Mage> mage = repository.find(name);
        if (mage.isPresent()){
            Mage m = mage.orElseThrow();
            return m.toString();
        }
        return "not found";
    }
    public String delete(String name) {
        //próba usunięcia nieistniejącego obiektu powoduje zwrócenie obiektu String
        //o wartości done
        //próba usunięcia nieistniejącego obiektu powoduje zwrócenie obiektu String
        //o wartości not found

        try{
            repository.delete(name);
            return "done";
        } catch (IllegalArgumentException e){
            return "not found";
        }

    }
    public String save(String name, String level) {
        //próba zapisania nowego obiektu skutkuje wywołaniem metody z serwisu
        //z poprawnym parametrem i zwróceniem obiektu String o wartości done
        //próba zapisania nowego obiektu, którego klucz główny znajduje się już
        //w repozytorium powoduje zwrócenie obiektu String o wartości bad request.
        try {
            repository.save(new Mage(name,Integer.parseInt(level)));
            return "done";
        }
        catch (IllegalArgumentException e){
            return "bad request";
        }
    }
}
