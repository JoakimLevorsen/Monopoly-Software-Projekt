package designpatterns;

import java.util.Set;

public interface Subject {
    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public Set<Observer> getObservers();

    public default void updated() {
        for (Observer o : this.getObservers())
            o.update(this);
    }
}
