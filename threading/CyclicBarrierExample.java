package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This example shows how 3 worker threads can wait at a rendez-vous point before continuing.
 */
public class CyclicBarrierExample {

	private static final int NUMBER_OF_THREADS = 3;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		CyclicBarrier sharedBarrier = new CyclicBarrier(3);
		int workingSeconds = 0;
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			String workerId = "Thread: " + new Integer(index);
			Thread nextThread = new Thread(new Worker(workerId, sharedBarrier, ++workingSeconds + 2));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}
		
		log("Waiting for all threads to finish...");
		for (Thread nextThread : threads) {
			nextThread.join();
		}
		log("Waiting for all threads to finish...Done");
	}

	private static class Worker implements Runnable {

		private final String name;
		private final CyclicBarrier rendezVous;
		private final long delay;

		public Worker(String name, CyclicBarrier barrier, int delayInSeconds) {
			this.name = name;
			this.rendezVous = barrier;
			this.delay = delayInSeconds * 1000;
		}


		@Override
		public void run() {

			try {
				log(name + " is working...");
				Thread.sleep(delay);
				log(name + " is working...done");
			} catch (InterruptedException e) {
				log(name + " is working...interrupted");
			}

			try {
				log(name + " is waiting...");
				rendezVous.await();
			} catch (InterruptedException e) {
				log(name + " is waiting...interrupted");

			} catch (BrokenBarrierException e) {
				log(name + " is waiting...interrupted");
			}
			log(name + " is waiting...done");
		}
	}
}
