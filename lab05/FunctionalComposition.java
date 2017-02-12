package lab05;

import java.util.function.Function;

public class FunctionalComposition
{
    public static void main(String... args)
    {
       
        Function<Integer, Integer> doubleIt = i -> i * 2;
        Function<Integer, Integer> squareIt = i -> i * i;
        Function<Integer, Integer> cubeIt = i -> i * i * i;
        Function<Integer, Integer> negateIt = i -> i * -1;

        // Using andThen()
        Function<Integer, Integer> full = doubleIt.andThen(squareIt).andThen(cubeIt).andThen(negateIt);
        System.out.println(full.apply(3));

        // Using compose()
        full = negateIt.compose(cubeIt.compose(squareIt.compose(doubleIt)));
        System.out.println(full.apply(3));
        
        // Using both andThen() and compose()
        full = negateIt.compose(doubleIt.andThen(squareIt).andThen(cubeIt));
        System.out.println(full.apply(3));
    }
}
