package com.wei.common.service;

import java.util.Vector;

public class VectorTest {

    private static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args){
        while (true){
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }

            Thread removeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }
            });

            Thread printThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        System.out.println(vector.get(i) + "-------------" + vector.size());
                    }
                }
            });

            printThread.start();
            removeThread.start();

            System.out.println("------------------------------" + Thread.activeCount());

            if(Thread.activeCount() > 20){
                break;
            }
        }
    }

}
