package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This example shows how threads can establish a rendez-vous point to exchange data. 
 */
public class ExchangerExample {

	// If you choose an odd number, one of the worker threads will timeout waiting for the exchange.
	private static final int NUMBER_OF_THREADS = 9;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		Exchanger<String> exchanger = new Exchanger<>();
		int workingSeconds = 0;
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			String workerId = "Thread: " + new Integer(index);
			Thread nextThread = new Thread(new Worker(workerId, exchanger, ++workingSeconds + 2));
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
		private final Exchanger<String> exchanger;
		private final long delay;

		public Worker(String name, Exchanger<String> exchanger, int delayInSeconds) {
			this.name = name;
			this.exchanger = exchanger;
			this.delay = delayInSeconds * 1000;
		}

		@Override
		public void run() {

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				log("Thread " + name + " is working...interupted");
			}

			try {
				String exchangedString = exchanger.exchange(name, 10, TimeUnit.SECONDS);
				log("Exchanged " + name + " with " + exchangedString);
			} catch (InterruptedException e) {
				
			} catch (TimeoutException exception) {
				log("Exchange timed out " + name);
			}
		}
	}
}
