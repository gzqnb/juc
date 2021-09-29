package com.gzq.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void myLock() {
        System.out.println(Thread.currentThread().getName() + "---come in");
        while (!atomicReference.compareAndSet(null, Thread.currentThread())) {

        }
        System.out.println(Thread.currentThread().getName() + "\t持有锁成功");
    }

    public void myUnLock() {
        atomicReference.compareAndSet( Thread.currentThread(),null);
        System.out.println(Thread.currentThread().getName() + "\t释放锁成功");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(()->{
            spinLockDemo.myLock();
            try{
                TimeUnit.SECONDS.sleep(5);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            spinLockDemo.myUnLock();
        },"t1").start();
        new Thread(()->{
            spinLockDemo.myLock();
            spinLockDemo.myUnLock();
        },"t2").start();
    }
}
