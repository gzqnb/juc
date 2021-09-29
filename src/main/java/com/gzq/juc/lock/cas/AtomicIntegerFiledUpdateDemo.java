package com.gzq.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

//不是给整个对象加锁，而是给某一个经常变化的对象进行局部加锁
class BankAccount {
    String bankName = "ccb";
    //以一种线程安全的方式操作非线程安全对象内的某些字段
    //1 更新的对象属性必须使用public volatile 修饰符
    public volatile int money = 0;
    //2因为对象的属性修改类型源自类都是抽象类，所以每次使用都必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性
    AtomicIntegerFieldUpdater fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class, "money");

    public void transfer(BankAccount bankAccount) {
        fieldUpdater.incrementAndGet(bankAccount);
    }
}

public class AtomicIntegerFiledUpdateDemo {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                bankAccount.transfer(bankAccount);
            }, String.valueOf(i)).start();
        }

        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName()+"\t"+"---bankAccount"+bankAccount.money);
    }
}
