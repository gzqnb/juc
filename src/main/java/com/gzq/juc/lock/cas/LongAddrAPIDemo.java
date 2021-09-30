package com.gzq.juc.lock.cas;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

public class LongAddrAPIDemo {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();//只能做加法
        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.out.println(longAdder.longValue());

        LongAccumulator longAccumulator = new LongAccumulator((x,y)->x+y,0);//也可以做减法
        longAccumulator.accumulate(1);
        longAccumulator.accumulate(2);
        System.out.println(longAccumulator.longValue());
    }
}
