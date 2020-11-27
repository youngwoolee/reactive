package me.joeylee.study.reactive.observer;

public class ConcreteObserverA implements Observer<String> {
    @Override
    public void observe(String event) {
        System.out.println("Observer A : " + event);
    }
}
