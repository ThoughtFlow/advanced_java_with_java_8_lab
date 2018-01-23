package lab11;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Same exercise as lab05.PredicateComposition but using streams instead.
 *
 */
public class PredicateCompositionWithStreams {

	public static void main(String... args) {

		// Need only change the predicate definitions
		Predicate<Double[]> isAllPassed = e -> Arrays.stream(e).noneMatch(g -> g < .60);
		Predicate<Double[]> isBAverage = e -> Arrays.stream(e).reduce(0d, (l, r) -> l + r) / e.length >= .80;
//		Predicate<Double[]> isBAverage = e -> Arrays.stream(e).mapToDouble(d -> d).average().orElse(0) >= .8;
		Predicate<Double[]> isLastPerfect = e -> e[e.length - 1] == 1; 
		Predicate<Double[]> isAnyMissed = e -> Arrays.stream(e).anyMatch(x -> x == 0);
		
		// The rest of the code is identical
        Predicate<Double[]> hasPassed = isAllPassed.and(isBAverage).or(isLastPerfect).and(isAnyMissed.negate());

        // True: Passed all
        Double[] scores = (Double[]) Arrays.asList(.65, .90, .90, .90, .90, .90).toArray();
        System.out.println(hasPassed.test(scores));

        // False: Not all passed
        scores = (Double[]) Arrays.asList(.59, .90, .90, .90, .90, .9).toArray();
        System.out.println(hasPassed.test(scores));

        // False: C average - fail
        scores = (Double[]) Arrays.asList(.70, .70, .70, .70, .70, .70).toArray();
        System.out.println(hasPassed.test(scores));

        // True: C average but aced last
        scores = (Double[]) Arrays.asList(.70, .70, .70, .70, .70, 1d).toArray();
        System.out.println(hasPassed.test(scores));

        // True: Failed first by scored perfect on last
        scores = (Double[]) Arrays.asList(.59, .90, .90, .90, .90, 1d).toArray();
        System.out.println(hasPassed.test(scores));
        
        // False: same as previous but missed a test
        scores = (Double[]) Arrays.asList(.59, .90, .90, .90, 0d, 1d).toArray();
        System.out.println(hasPassed.test(scores));

        // False: Perfect but missed last - fail!
        scores = (Double[]) Arrays.asList(1d, 1d, 1d, 1d, 1d, 0d).toArray();
        System.out.println(hasPassed.test(scores));
	}
}