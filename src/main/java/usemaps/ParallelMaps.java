package usemaps;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelMaps {
  public static void main(String[] args) throws Throwable {
//        Map<Integer, String> map = Collections.synchronizedMap(new HashMap<>());
    Map<Integer, String> map = new ConcurrentHashMap<>();
    Runnable r = ()->{
      for (int i = 0; i < 200_000; i++) {
        int key = ThreadLocalRandom.current().nextInt(4000);
        map.put(key, "" + key);
      }
    };
    long start = System.nanoTime();
    List<Thread> lt = new ArrayList<>();
    for (int i = 0; i < 1_000; i++) {
      Thread t = new Thread(r);
      lt.add(t);
      t.start();
    }
    for (Thread t : lt) {
      t.join();
    }
    long time = System.nanoTime() - start;
    System.out.println("Time was " + (time / 1_000_000_000.0));
  }
}
