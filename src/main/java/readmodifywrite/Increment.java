package readmodifywrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

class SyncLong {
  // volatile addresses "visibility" NOT transactions
  private /*volatile*/ long value;
  public long incrementAndGet() {
    synchronized (this) {
      return ++value;
    }
  }
  public long longValue() {
    synchronized (this) {
      return value;
    }
  }
}

public class Increment {
  public static void main(String[] args) throws Throwable {
//    SyncLong theValue = new SyncLong();
//    SyncLong theValue2 = new SyncLong();
    AtomicLong theValue = new AtomicLong();
    AtomicLong theValue2 = new AtomicLong();
//    LongAdder theValue = new LongAdder();
//    LongAdder theValue2 = new LongAdder();

    Runnable r = () -> {
      for (int i = 0; i < 200_000; i++) {
        theValue.incrementAndGet();
        theValue2.incrementAndGet();
      }
    };

    long start = System.nanoTime();
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      Thread t = new Thread(r);
      threads.add(t);
      t.start();
    }

    for (Thread t : threads) {
      t.join();
    }
    long time = System.nanoTime() - start;
    System.out.println("value is " + theValue.longValue()
      + " time was " + (time / 1_000_000_000.0));
  }
}
