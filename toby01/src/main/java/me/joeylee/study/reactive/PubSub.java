package me.joeylee.study.reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PubSub {
    public static void main(String[] args) throws InterruptedException{
        //Publisher <- Observable
        //Subscriber <- Observer

        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);
        ExecutorService es = Executors.newCachedThreadPool();


        Publisher p = new Publisher() {
            @Override
            public void subscribe(Subscriber s) {
                Iterator<Integer> it = iter.iterator();


                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        es.execute(() -> {
                            int i= 0;
                            try {
                                while(i++ < n) {
                                    if(it.hasNext()) {
                                        s.onNext(it.next());
                                    }
                                    else {
                                        s.onComplete();
                                        break;
                                    }
                                }
                            } catch (RuntimeException e) {
                                s.onError(e);
                            }
                        });
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {

            Subscription subscription;
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println(Thread.currentThread().getName()  + " onSubscribe");
                this.subscription = s;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(Thread.currentThread().getName() + " onNext " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError" + t.getMessage());

            }

            @Override
            public void onComplete() {

                System.out.println("onComplete");
            }
        };

        p.subscribe(s);

        es.awaitTermination(10, TimeUnit.HOURS);
        es.shutdown();

    }
}
