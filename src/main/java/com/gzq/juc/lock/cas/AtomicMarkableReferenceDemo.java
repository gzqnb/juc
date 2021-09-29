package com.gzq.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceDemo {
    static AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference(100,false);

    public static void main(String[] args) {
        new Thread(()->{
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName()+"\t"+"---默认修改标志"+marked);
            try{
                TimeUnit.SECONDS.sleep(1);
                atomicMarkableReference.compareAndSet(100,101,marked,!marked);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        },"t1").start();

        new Thread(()->{
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName()+"\t"+"---默认修改标志"+marked);
            try{
                TimeUnit.SECONDS.sleep(2);
                atomicMarkableReference.compareAndSet(100,101,marked,!marked);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            boolean b = atomicMarkableReference.compareAndSet(100, 221212, marked, !marked);
            System.out.println(Thread.currentThread().getName()+"\t"+"操作是否成功:"+b);
            System.out.println(Thread.currentThread().getName()+"\t"+atomicMarkableReference.getReference());
        },"t2").start();
    }
}
