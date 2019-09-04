package designpatterns

interface Subject {

    val observers: Set<Observer>
    fun addObserver(observer: Observer) {
        this.observers.plus(observer)
    }

    fun removeObserver(observer: Observer) {
        this.observers.minus(observer)
    }

    fun updated() {
        for (o in this.observers)
            o.update(this)
    }
}
