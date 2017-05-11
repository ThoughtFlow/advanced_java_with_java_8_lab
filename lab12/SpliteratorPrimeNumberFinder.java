package lab12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SpliteratorPrimeNumberFinder {

	private static final int LIST_SIZE = 1000000;

	private static boolean isPrime(int primeCandidate) throws IllegalArgumentException {
		
		if (primeCandidate < 0) {
			throw new IllegalArgumentException("PrimeCandidate must be a positive number - received: " + primeCandidate);
		}
		
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

	private static class PrimeFinder implements Callable<Integer> {

		private final Spliterator<Integer> spliterator;

		public PrimeFinder(Spliterator<Integer> spliterator) {
			this.spliterator = spliterator;
		}

		@Override
		public Integer call() {
			AtomicInteger counter = new AtomicInteger(0);
			
			// Make sure the spliterator is not null
			if (spliterator != null) {
			   spliterator.forEachRemaining(nextInt -> counter.updateAndGet(containedInt -> isPrime(nextInt) ? ++containedInt : containedInt));
			}

			return counter.get();
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// Populate the list of integers
		List<Integer> integerList = new ArrayList<>(LIST_SIZE);
		for (int index = 0; index < LIST_SIZE; ++index) {
			integerList.add(index);
		}
		
		// Split in 4 equal sizes
		// Careful - the trySplit may not work and return null
		Spliterator<Integer> spliterator1 = integerList.spliterator();
		Spliterator<Integer> spliterator2 = spliterator1.trySplit();
		Spliterator<Integer> spliterator3 = spliterator1 != null ? spliterator1.trySplit() : null;
		Spliterator<Integer> spliterator4 = spliterator2 != null ? spliterator2.trySplit() : null;
		
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<PrimeFinder> primeFinders = Arrays.asList(new PrimeFinder(spliterator1), 
													   new PrimeFinder(spliterator2), 
													   new PrimeFinder(spliterator3),
													   new PrimeFinder(spliterator4));
		List<Future<Integer>> futures = pool.invokeAll(primeFinders);
		int totalPrimesFound = 0;
		for (Future<Integer> nextFuture : futures) {
			totalPrimesFound += nextFuture.get();
		}
		
		System.out.println("Total primes found: " + totalPrimesFound);
		pool.shutdown();
	}
}