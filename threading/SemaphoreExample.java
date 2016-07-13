package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * This example shows how semaphores can be used to abstract licenses. Each worker thread must first obtain a license before continuing and must wait if there are no more license remaining.
 */
public class SemaphoreExample {

	private static final int NUMBER_OF_THREADS = 5;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		Semaphore semaphore = new Semaphore(NUMBER_OF_THREADS / 2);
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			Thread nextThread = new Thread(new Worker("Thread: " + new Integer(index).toString(), semaphore, (index * 2 + 1) * 1000));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}
		
		for (Thread nextThread : threads) {
			nextThread.join();
		}
	}

	private static class Worker implements Runnable {

		private final String name;
		private final Semaphore semaphore;
		private final long delay;

		public Worker(String name, Semaphore semaphore, long delay) {
			this.name = name;
			this.semaphore = semaphore;
			this.delay = delay;
		}

		@Override
		public void run() {

			for (int index = 0; index < 3; ++ index) {
			   try {
				   log(name + " trying to acquire...");
				   semaphore.acquire();
				   log(name + " trying to acquire...acquired");
				   Thread.sleep(delay);
				   semaphore.release();
				   log(name + " trying to acquire...released");
			   } catch (InterruptedException e) {
			   }
			}
		}
	}
}
