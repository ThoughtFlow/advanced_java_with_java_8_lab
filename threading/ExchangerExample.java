package threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerExample {

	// If you choose an odd number, one of the worker threads will timeout waiting for the exchange.
	private static final int NUMBER_OF_THREADS = 9;
	
	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": " + message);
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		Exchanger<String> barrier = new Exchanger<>();
		
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
		private final Exchanger<String> exchanger;
		private final long delay;

		public Worker(String name, Exchanger<String> exchanger, long delay) {
			this.name = name;
			this.exchanger = exchanger;
			this.delay = delay;
		}

		@Override
		public void run() {

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {

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
