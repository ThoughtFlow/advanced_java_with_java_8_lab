package lab11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunctionalLife
{
    public static boolean[][] getNextGenerationSerial(boolean[][] oldGeneration)
    {
        boolean[][] newGeneration = new boolean[oldGeneration.length][oldGeneration.length];
        IntStream.range(0, oldGeneration.length).
            forEach(nextY ->
                        IntStream.range(0, oldGeneration.length).forEach((nextX -> newGeneration[nextY][nextX] = isAlive(oldGeneration, nextY, nextX))));

        return newGeneration;
    }

    public static boolean[][] getNextGenerationParallel(boolean[][] oldGeneration)
    {
        boolean[][] newGeneration = new boolean[oldGeneration.length][oldGeneration.length];
        IntStream.range(0, oldGeneration.length).parallel().
            forEach(nextY ->
                        IntStream.range(0, oldGeneration.length).parallel().forEach((nextX -> newGeneration[nextY][nextX] = isAlive(oldGeneration, nextY, nextX))));

        return newGeneration;
    }

    public static boolean[][] getNextGenerationFunctionalPlus(boolean[][] oldGeneration)
    {
        boolean[][] newGeneration = new boolean[oldGeneration.length][oldGeneration.length];
        List<Coordinates> list = new ArrayList<>();
        IntStream.range(0, oldGeneration.length).
            forEach(nextY ->
                    {
                        list.addAll(IntStream.range(0, oldGeneration.length).
                            parallel().
                                mapToObj(nextX -> new Coordinates(nextX, nextY)).
                                    filter(coordinates -> isAlive(oldGeneration, coordinates.getY(), coordinates.getX())).
                                        collect(Collectors.toList()));
                    });

        list.parallelStream().forEach(nextCoordinate -> newGeneration[nextCoordinate.getY()][nextCoordinate.getX()] = true);

        return newGeneration;
    }

    private static int countLiveNeighborCells(boolean[][] generation, int y, int x)
    {
        return IntStream.rangeClosed(y - 1, y + 1).
            filter(yInner -> yInner >= 0 && yInner < generation.length).
                reduce(0, (yLeft, yRight) -> yLeft +
                    IntStream.rangeClosed(x - 1, x + 1).
                        filter(xInner -> xInner >= 0 && xInner < generation.length && generation[yRight][xInner] && !(xInner == x && yRight == y)).
                            reduce(0, (xLeft, xRight) -> xLeft + 1));
    }

    private static boolean isAlive(boolean[][] generation, int y, int x)
    {
        int liveCells = countLiveNeighborCells(generation, y, x);
        return (generation[y][x] && liveCells >= 2 && liveCells <= 3) || (!generation[y][x] && liveCells == 3);
    }

    private static class Coordinates
    {
        private final int x;
        private final int y;

        public Coordinates(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }
    }

}
