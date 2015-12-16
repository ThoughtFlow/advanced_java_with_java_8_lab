package lab11;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Life
{
    public static double execute(String method, Function<boolean[][], boolean[][]> function, boolean[][] firstGeneration, int generations)
    {
        long start = System.currentTimeMillis();
        View view = new View(method, firstGeneration, 4);
        boolean[][] nextGeneration = firstGeneration;
        for (int generationCount = 0; generationCount < generations; ++generationCount)
        {
            nextGeneration = function.apply(nextGeneration);
            view.displayNextGeneration(nextGeneration, generationCount);
        }

        return (System.currentTimeMillis() - start) / 1000.0;
    }
    public static void main(String[] args)
    {
        int size = Integer.parseInt(args[0]);
        int generations = Integer.parseInt(args[1]);
        boolean[][] firstGeneration = new boolean[size][size];
        Random r = new Random();
        IntStream.rangeClosed(0, size - 1).forEach(y -> IntStream.rangeClosed(0, size - 1).forEach(x -> firstGeneration[y][x] = Math.abs(r.nextInt()) % 2 == 0));

        for (int index = 0; index < 1; index++)
        {
//            System.out.println(execute("Imperative", ImperativeLife::getNextGeneration, firstGeneration, generations));
//            System.out.println(execute("Functional Serial", FunctionalLife::getNextGenerationSerial, firstGeneration, generations));
//            System.out.println(execute("Functional Parallel", FunctionalLife::getNextGenerationParallel, firstGeneration, generations));
            System.out.println(execute("Functional Parallel Plus", FunctionalLife::getNextGenerationFunctionalPlus, firstGeneration, generations));
        }
    }
}
