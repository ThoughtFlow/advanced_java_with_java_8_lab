package lab09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PromisesPrimeNumberFinderExceptions {

	private static final int K_SLICES = 1000;
	private static final int K = 1000;

	private static Integer countPrimes(int startRange, int endRange) {
		int primesFound = 0;

		for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
			primesFound += isPrime(primeCandidate) ? 1 : 0;
		}

		return primesFound;
	}

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
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CompletableFuture<Integer> promise = CompletableFuture.completedFuture(0);

		for (int index = -1; index < K_SLICES; ++index) {
			int range = index;

			// Now we have an exceptionally clause - no more exception
			CompletableFuture<Integer> nextPromise =     
					CompletableFuture.supplyAsync(() -> countPrimes(range * K, range * K + K - 1)).exceptionally(i -> 0);

			 //Having no exceptionally clause will result in an exception being thrown
//			 nextPromise = CompletableFuture.supplyAsync(() -> countPrimes(range * K, range * K + K - 1)).
//			 		whenComplete((i, e) -> {if (e != null) System.err.println("Exception caught - continuing: " + e.getMessage());});

			promise = promise.thenCombine(nextPromise, (first, second) -> first + second);
		}

		System.out.println("Total primes found: " + promise.get());
	}
}
