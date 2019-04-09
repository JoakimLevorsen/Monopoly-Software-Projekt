package designpatterns;

import java.util.List;

public interface Subject {
    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public List<Observer> getObservers();

    public default void updated() {
        for (Observer o : this.getObservers())
            o.update(this);
    }
}