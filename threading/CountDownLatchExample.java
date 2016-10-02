package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This example shows how a master thread can wait for 3 worker threads to each complete their work until the master thread continues.
 */
public class CountDownLatchExample {

	private static final int NUMBER_OF_THREADS = 3;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		CountDownLatch latch = new CountDownLatch(3);
		int workingSeconds = 0;
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			String workerId = "Thread: " + new Integer(index);
			Thread nextThread = new Thread(new Worker(workerId, latch, ++workingSeconds + 2));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}

		log("Waiting for all threads to finish...");
		latch.await();
		log("Waiting for all threads to finish...Done");
	}

	private static class Worker implements Runnable {

		private final String name;
		private final CountDownLatch latch;
		private final long delay;

		public Worker(String name, CountDownLatch latch, int delayInSeconds) {
			this.name = name;
			this.latch = latch;
			this.delay = delayInSeconds * 1000;
		}

		@Override
		public void run() {

			try {
				log(name + " is working...");
				Thread.sleep(delay);
				log(name + " is working...done");
			} catch (InterruptedException e) {
				log(name + " is working...interupted");
			}

			latch.countDown();
		}
	}
}
