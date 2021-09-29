package com.gzq.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABAStampDemo {
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t" + "---默认版本号" + stamp);
            //让后面的t2获得和t1一样的版本号，都是1，好比较
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedReference.compareAndSet(100, 101, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t" + "---1次版本号" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t" + "---2次版本号" + atomicStampedReference.getStamp());
        }, "t1").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t" + "---默认版本号" + stamp);
            //让前面的t3完成ABA问题
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean res = atomicStampedReference.compareAndSet(100, 20211111, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t" + "操作成功否:" + res + "\t" + atomicStampedReference.getStamp());
        }, "t2").start();
    }
}
