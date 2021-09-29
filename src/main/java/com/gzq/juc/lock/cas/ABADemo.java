package com.gzq.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ABADemo {
    static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        new Thread(()->{
            atomicInteger.compareAndSet(100,101);
            atomicInteger.compareAndSet(101,100);
        },"t1").start();
        try{
            TimeUnit.MILLISECONDS.sleep(10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        new Thread(()->{
            boolean b = atomicInteger.compareAndSet(100,20210000);
            System.out.println(Thread.currentThread().getName()+"\t"+"修改成功是否"+b+"\t"+atomicInteger.get());
        },"t2").start();
    }
}
