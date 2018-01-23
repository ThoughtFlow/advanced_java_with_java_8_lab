package lab11;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FizzBuzz
{
	private static List<String> getFizzBuzzList(int start, int end) {
        return IntStream.rangeClosed(start, end).
        		mapToObj(Holder::new).
        		map(h -> {h.add(Math.floorMod(h.number, 3) == 0 ? "Fizz" : ""); return h;}).
        		map(h -> {h.add(Math.floorMod(h.number, 5) == 0 ? "Buzz" : ""); return h;}).
        		filter(h -> h.fizzBuzz.length() > 0).
        		map(h -> h.number + " " + h.fizzBuzz).
        		collect(Collectors.toList());
	}

	private static List<String> getFizzBuzzListWithCurrying(int start, int end) {
		
		// Using currying to define the modulo function once only.
		BiFunction<String, Integer, UnaryOperator<Holder>> moduloFunction = 
				(fizzBuzz, modulo) -> 
					h -> {h.add(Math.floorMod(h.number, modulo) == 0 ? fizzBuzz : ""); return h;};
		
        return IntStream.rangeClosed(start, end).
        		mapToObj(Holder::new).
        		map(h -> moduloFunction.apply("Fizz", 3).apply(h)).
        		map(h -> moduloFunction.apply("Buzz", 5).apply(h)).
        		filter(h -> h.fizzBuzz.length() > 0).
        		map(h -> h.number + " " + h.fizzBuzz).
        		collect(Collectors.toList());
	}
	
    public static void main(String... args)
    {
    		getFizzBuzzList(1, 100).forEach(System.out::println);
    		getFizzBuzzListWithCurrying(1, 100).forEach(System.out::println);
    }

    private static class Holder
    {
        public final int number;
        public String fizzBuzz = "";

        public Holder(int i)
        {
            number = i;
        }

        public void add(String toAdd)
        {
            fizzBuzz += toAdd;
        }
    }
}
