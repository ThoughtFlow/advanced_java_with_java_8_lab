package lab11;

import java.util.stream.IntStream;

public class FunctionalMatrix
{
    private static Coordinates getCoordinatesForHighestSum(int[][] matrix, int y, int subMatrixSize)
    {
        return IntStream.rangeClosed(0, matrix.length - subMatrixSize).
            mapToObj(x -> new Coordinates(y, x, IntStream.range(y, y + subMatrixSize).map(innerY -> IntStream.range(x, x + subMatrixSize).reduce(0, (innerL, innerR) -> matrix[innerY][innerR] + innerL)).
                reduce(0, (l, r) -> l + r))).
                    reduce(new Coordinates(), (l, r) -> r.compareTo(l) > 0 ? r : l);
    }

    private static Coordinates findHighestSubMatrix(int[][] matrix, int subMatrixSize)
    {
        return IntStream.rangeClosed(0, matrix.length - subMatrixSize).
            mapToObj(y -> getCoordinatesForHighestSum(matrix, y, subMatrixSize)).
                reduce(new Coordinates(), (l, r) -> r.compareTo(l) > 0 ? r : l);
    }

    private static class Coordinates implements Comparable<Coordinates>
    {
        int x = 0;
        int y = 0;
        int sum = 0;

        public Coordinates()
        {

        }

        public Coordinates(int y, int x, int sum)
        {
            this.x = x;
            this.y = y;
            this.sum = sum;
        }

        @Override
        public int compareTo(Coordinates comparedObject)
        {
            return Integer.compare(sum, comparedObject.sum);
        }

        @Override
        public String toString()
        {
            return "Coordinates: " + y + " " + x + " " + sum;
        }
    }

    public static void main(String[] args)
    {
        int[][] matrix =
            {{1, 2, 5, 7, 8},
             {1, 5, 2, 5, 6},
             {10, 2, 1, 2, 4},
             {8, 3, 1, 0, 4},
             {1, 6, 2, 4, 3}};

        System.out.println(findHighestSubMatrix(matrix, 2));
    }
}