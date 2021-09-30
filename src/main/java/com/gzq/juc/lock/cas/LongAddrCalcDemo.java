package com.gzq.juc.lock.cas;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

class ClickNumber{
    int number = 0;
    public synchronized void add_Synchronized(){
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();
    public void add_AtomicInteger(){
        atomicInteger.incrementAndGet();
    }

    AtomicLong atomicLong = new AtomicLong();
    public void add_AtomicLong(){
        atomicLong.incrementAndGet();
    }

    LongAdder longAdder = new LongAdder();
    public void add_LongAdder(){
        longAdder.increment();
    }

    LongAccumulator longAccumulator = new LongAccumulator((x,y)->x+y,0);
    public void add_LongAccumulator(){
        longAccumulator.accumulate(1);
    }
}

/**
 * 50个线程，每个线程100w次，统计总点赞数
 */
public class LongAddrCalcDemo {
    public static final int SIZE_THREAD = 50;
    public static final int _1W = 10000;

    public static void main(String[] args) throws InterruptedException {
        ClickNumber clickNumber = new ClickNumber();
        long startTime;
        long endTime;
        CountDownLatch countDownLatch1 = new CountDownLatch(SIZE_THREAD);
        startTime = System.currentTimeMillis();
        //=========
        for (int i = 1;i<=SIZE_THREAD;i++){
            new Thread(()->{
                try{
                    for (int j = 1; j<=100*_1W;j++){
                        clickNumber.add_Synchronized();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    countDownLatch1.countDown();
                }

            },String.valueOf(i)).start();
        }
        countDownLatch1.await();
        endTime = System.currentTimeMillis();
        System.out.println("---costTime"+(endTime-startTime)+"毫秒"+"\t add_Synchronized");
    }
}
