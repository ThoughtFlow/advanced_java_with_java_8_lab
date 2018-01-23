package lab11;

public class ImperativeMatrix
{
    private static int sum(int[][] subMatrix, int startY, int startX, int n)
    {
        int sum = 0;
        for (int y = startY; y < startY + n; ++y)
        {
            for (int x = startX; x < startX + n; ++x)
            {
                sum += subMatrix[y][x];
            }
        }

        return sum;
    }

    private static Coordinates findHighestMatrix(int[][] matrix, int n)
    {
        int highestX = 0;
        int highestY = 0;
        int highestSum = 0;

        for (int y = 0; y <= matrix.length - n; ++y)
        {
            for (int x = 0; x <= matrix.length - n; ++x)
            {
                int sum = sum(matrix, y, x, n);
                if (sum > highestSum)
                {
                    highestX = x;
                    highestY = y;
                    highestSum = sum > highestSum ? sum : highestSum;
                }
            }
        }

        return new Coordinates(highestY, highestX, highestSum);
    }

    private static class Coordinates implements Comparable<Coordinates>
    {
        int x = 0;
        int y = 0;
        int sum = 0;

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

        System.out.println(findHighestMatrix(matrix, 2));
    }
}