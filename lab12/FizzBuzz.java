package lab12;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FizzBuzz
{
    public static void main(String... args)
    {
        IntStream.rangeClosed(1, 100).
            mapToObj(Holder::new).
            map(h -> {h.add(Math.floorMod(h.number, 3) == 0 ? "Fizz" : ""); return h;}).
            map(h -> {h.add(Math.floorMod(h.number, 5) == 0 ? "Buzz" : ""); return h;}).
            filter(h -> h.fizzBuzz.length() > 0).
            map(h -> h.number + " " + h.fizzBuzz).
            collect(Collectors.toList()).
            forEach(System.out::println);
    }

    private static class Holder
    {
        public int number;
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
