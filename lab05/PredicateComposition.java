package lab05;

import java.util.Arrays;
import java.util.function.Predicate;

public class PredicateComposition
{
    public static void main(String[] args)
    {
        Predicate<Double[]> isAllPassed = l -> {
            for (double next : l)
            {
                if (next < .60)
                {
                    return false;
                }
            }
            return true;
        };
        
        Predicate<Double[]> isBAverage = l -> {
            double sum = 0;
            for (double next : l)
            {
                sum += next;
            }

            return sum / l.length >= .80;
        };

        Predicate<Double[]> isLastPerfect = l -> l[l.length - 1] == 1;
        
        Predicate<Double[]> isAnyMissed = l ->
        {
            for (double next : l)
            {
                if (next == 0)
                {
                    return true;
                }
            }

            return false;
        };

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
