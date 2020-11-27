package me.joeylee.study.reactive.observer;

public interface Observer<T> {
    void observe(T event);
}
