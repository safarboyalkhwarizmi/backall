package uz.backall.store;

public class StoreNotCreatedException extends RuntimeException {
    public StoreNotCreatedException(String message) {
        super(message);
    }
}