package lab07;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedPrimeNumberFinder {

	private static final int K_SLICES = 100;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		List<PrimeFinder> primeFinders = new ArrayList<>(K_SLICES);
		List<Future<String>> futures = new ArrayList<>(K_SLICES);
		ExecutorService pool = Executors.newFixedThreadPool(5);

		for (int index = 0; index < K_SLICES; ++index) {
			primeFinders.add(new PrimeFinder(index * 1000, index * 1000 + 999));
		}

		for (Callable<String> nextCallable : primeFinders) {
			futures.add(pool.submit(nextCallable));			
		}

		for (Future<String> nextFuture : futures) {
			System.out.println("Primes found: " + nextFuture.get());			
		}
		
		pool.shutdown();
	}

	private static class PrimeFinder implements Callable<String> {

		private final int startRange;
		private final int endRange;

		private int primesFound = 0;

		public PrimeFinder(int startRange, int endRange) {
			this.startRange = startRange;
			this.endRange = endRange;
		}

		@Override
		public String call() {
			for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
				if (primeCandidate > 2) {
					boolean isNonPrimeFound = false;
					for (int testValue = 2; testValue <= Math.sqrt(primeCandidate); ++testValue) {
						if (primeCandidate % testValue == 0) {
							isNonPrimeFound = true;
							break;
						}
					}
					
					primesFound += isNonPrimeFound ? 0 : 1;
				} else if (primeCandidate == 2) {
					++primesFound;
				}
			}

			return Thread.currentThread().getName() +": Range " + startRange + " to " + endRange + ": " + primesFound;
		}
	}
}
