package lab11;

public class ImperativeLife
{
    public static boolean[][] getNextGeneration(boolean[][] oldGeneration)
    {
        boolean[][] newGeneration = new boolean[oldGeneration.length][oldGeneration.length];
        for (int y = 0; y < oldGeneration.length; ++y) {
            for (int x = 0; x < oldGeneration.length; ++x) {
                newGeneration[y][x] = isAlive(oldGeneration, y, x);
            }
        }

        return newGeneration;
    }

    private static int countLiveNeighborCells(boolean[][] generation, int y, int x)
    {
        int count = 0;

        for (int yIndex = y - 1; yIndex <= y + 1; ++yIndex) {
            if (yIndex >= 0 && yIndex < generation.length) {
                for (int xIndex = x - 1; xIndex <= x + 1; ++xIndex) {
                    if (xIndex >= 0 && xIndex < generation.length) {
                        if (generation[yIndex][xIndex] && !(xIndex == x && yIndex == y)) {
                            ++count;
                        }
                    }
                }
            }
        }

        return count;
    }

    private static boolean isAlive(boolean[][] generation, int y, int x)
    {
        int liveCells = countLiveNeighborCells(generation, y, x);
        return (generation[y][x] && liveCells >= 2 && liveCells <= 3) || (!generation[y][x] && liveCells == 3);
    }
}