package lab08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedPrimeNumberFinder {

	private static final int K_SLICES = 1000;
	private static final int K = 1000;

	private static class PrimeFinder implements Callable<Integer> {

		private final int startRange;
		private final int endRange;

		public PrimeFinder(int startRange, int endRange) {
			this.startRange = startRange;
			this.endRange = endRange;
		}

		@Override
		public Integer call() {
			int primesFound = 0;
			
			for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
				primesFound += isPrime(primeCandidate) ? 1 : 0;
			}

			return primesFound;
		}
		
		private boolean isPrime(int primeCandidate) {
			boolean isPrime = primeCandidate == 2;

			if (primeCandidate > 2) {
				isPrime = true;
				for (int testValue = 2; testValue <= Math.sqrt(primeCandidate); ++testValue) {
					if (primeCandidate % testValue == 0) {
						isPrime = false;
						break;
					}
				}
			} 

			return isPrime;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		List<PrimeFinder> primeFinders = new ArrayList<>(K_SLICES);
		ExecutorService pool = Executors.newFixedThreadPool(5);
		int totalPrimesFound = 0;

		for (int index = 0; index < K_SLICES; ++index) {
			primeFinders.add(new PrimeFinder(index * K, index * K + K - 1));
		}
		
		List<Future<Integer>> futures = pool.invokeAll(primeFinders);

		for (Future<Integer> nextFuture : futures) {
			totalPrimesFound += nextFuture.get();
		}
		
		System.out.println("Total primes found: " + totalPrimesFound);
		pool.shutdown();
	}
}
