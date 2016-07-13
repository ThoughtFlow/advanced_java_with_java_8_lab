package lab12;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class StreamFun {

	public static void main(String... args) {

		{
			// Print only even numbers
			IntStream.rangeClosed(0, 100).filter(i -> i % 2 == 0).forEach(System.out::println);
		}

		{
			// Print the sum of odd numbers from 0 to 100
			System.out.println("The sum of odd numbers from 0 to 100 is: "
					+ IntStream.rangeClosed(0, 100).filter(i -> i % 2 == 1).reduce(0, (l, r) -> l + r));
		}

		{
			// Print the sum of non-prime odd numbers from 0 to 100
			IntPredicate isIndivisible = i -> {
				for (int n = 2; n <= Math.sqrt(i); ++n)
					if (i % n == 0)
						return false;

				return true;
			};
			IntPredicate isGreaterThanOne = i -> i > 1;
			IntPredicate isNotPrime = isGreaterThanOne.and(isIndivisible).negate();

			System.out.println("The sum of non-prime odd numbers from 0 to 100 is: "
					+ IntStream.rangeClosed(0, 100).filter(i -> i % 2 == 1).filter(isNotPrime)
							.reduce(0, (l, r) -> l + r));
		}
		
		{
			// Print the sum of non-prime odd numbers from 0 to 100 - using an inner stream
			System.out.println("The sum of non-prime odd numbers from 0 to 100 is: " +
					IntStream.rangeClosed(1, 100).filter(i -> i % 2 == 1).
					filter(i -> i == 1 || !IntStream.rangeClosed(2, (int) Math.sqrt(i)).noneMatch(innerI -> i % innerI == 0)).
					reduce(0, (l, r) -> l + r));
		}
	}
}
