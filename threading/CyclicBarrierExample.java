package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

	private static final int NUMBER_OF_THREADS = 3;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		CyclicBarrier barrier = new CyclicBarrier(3);
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			Thread nextThread = new Thread(new Worker("Thread: " + new Integer(index).toString(), barrier, (index * 2 + 1) * 1000));
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
		private final CyclicBarrier barrier;
		private final long delay;

		public Worker(String name, CyclicBarrier barrier, long delay) {
			this.name = name;
			this.barrier = barrier;
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

			try {
				log("Thread " + name + " is waiting...");
				barrier.await();
			} catch (InterruptedException e) {
				log("Thread " + name + " is waiting...interupted");

			} catch (BrokenBarrierException e) {
				log("Thread " + name + " is waiting...interupted");
			}
			log("Thread " + name + " is waiting...done");
		}
	}
}
