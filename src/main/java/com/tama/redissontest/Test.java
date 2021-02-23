package com.tama.redissontest;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void test() throws InterruptedException {
        // Without locking
        final List<Integer> data = new ArrayList<>();
        data.add(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = data.get(0)+9;
                data.set(0, i);
            }
        }).start();
        Thread.sleep(20L);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Nilai tanpa locking: "+data.get(0));
            }
        }).start();

        // With locking
        final List<Integer> data2 = new ArrayList<>();
        data2.add(1);
        RLock lock = redissonClient.getLock("test");
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = data2.get(0)+9;
                data2.set(0, i);
                lock.unlock();
            }
        }).start();
        Thread.sleep(20L);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("Nilai dengan locking: "+data2.get(0));
                lock.unlock();
            }
        }).start();

        // Coba watchdog
        RLock wdLock = redissonClient.getLock("watchdog");
        System.out.println("Mulai watchdog");
        System.out.println(wdLock.remainTimeToLive());
        Thread.sleep(Long.parseLong("20000"));
        System.out.println("Checkpoint 20s watchdog");
        System.out.println(wdLock.remainTimeToLive());
        Thread.sleep(Long.parseLong("20000"));
        System.out.println("Pass 40s watchdog");
        System.out.println(wdLock.remainTimeToLive());
    }

}
