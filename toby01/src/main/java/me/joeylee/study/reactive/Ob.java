package me.joeylee.study.reactive;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ob {
    //Source -> Event/Data -> Observer
    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for(int i=1; i<=10; i++) {
                setChanged();
                notifyObservers(i); //push
            }
        }
    }

    public static void main(String[] args) {
        //Iterable <---> Observable
        //pull            push
        Iterable<Integer> iter = () ->
            new Iterator() {

                int i = 0;
                final static int MAX = 10;

                @Override
                public boolean hasNext() {
                    return i < MAX;
                }

                @Override
                public Object next() {
                    return ++i;
                }
            };


        for(Integer i : iter) {
            System.out.println(i);
        }

        for(Iterator<Integer> it = iter.iterator(); it.hasNext();) {
            System.out.println(it.next()); //pull
        }

        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {

                System.out.println(Thread.currentThread().getName() + " " + arg);
            }
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob);


        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() + " EXIT");
        es.shutdown();
    }
}
