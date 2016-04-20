package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

	private static final int NUMBER_OF_THREADS = 3;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		CountDownLatch latch = new CountDownLatch(3);
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			Thread nextThread = new Thread(new Worker("Thread: " + new Integer(index).toString(), latch, (index * 2 + 1) * 1000));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}

		latch.await();
	}

	private static class Worker implements Runnable {

		private final String name;
		private final CountDownLatch latch;
		private final long delay;

		public Worker(String name, CountDownLatch latch, long delay) {
			this.name = name;
			this.latch = latch;
			this.delay = delay;
		}

		@Override
		public void run() {

			try {
				log("Thread " + name + " is working...");
				Thread.sleep(delay);
				log("Thread " + name + " is working...done");
			} catch (InterruptedException e) {
				log("Thread " + name + " is working...interupted");
			}

			latch.countDown();
		}
	}
}
