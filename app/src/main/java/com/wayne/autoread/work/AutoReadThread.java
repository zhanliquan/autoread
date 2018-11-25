package com.wayne.autoread.work;

import java.util.LinkedList;

public class AutoReadThread extends Thread {
    private final LinkedList<Worker> workerList = new LinkedList<Worker>();
    private int workNum = 0;
    public boolean running = true;

    public void addWorker(Worker worker) {
        synchronized (workerList) {
            System.out.println("addWorker:" + worker);
            workerList.add(worker);
            workNum ++ ;
            workerList.notify();
        }
    }

    public void removeWorker(int num) {
        synchronized (workerList) {
            if (num >= this.workNum) {
                throw new IllegalArgumentException("超过了已有的线程数量");
            }
            for (int i = 0; i < num; i++) {
                Worker worker = workerList.get(i);
                if (worker != null) {
                    worker.shutdown();
                    workerList.remove(i);
                }
            }
            this.workNum -= num;
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (workerList) {
                if (workerList.isEmpty()) {
                    try {
                        System.out.println("workerList.wait");
                        workerList.wait();
                        Thread.sleep(5000);
                        System.out.println("workerList.wait 2");
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                Worker worker = workerList.removeFirst();
                System.out.println("start work " + worker);
                if (worker != null) {
                    worker.run();
                }
            }
        }
    }
}