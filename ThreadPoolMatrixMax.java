import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolMatrixMax {

    static void main() {
        int rows = 1000;
        int cols = 1000;
        int[][] matrix = new int[rows][cols];
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(1_000_000);
            }
        }

        matrix[750][500] = 5_000_000;

        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Используется потоков: " + numThreads);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();

        int rowsPerThread = rows / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? rows : (i + 1) * rowsPerThread;

            Callable<Integer> task = new MatrixMaxProcessor(matrix, startRow, endRow);

            futures.add(executorService.submit(task));
        }

        int globalMax = Integer.MIN_VALUE;

        try {
            for (Future<Integer> future : futures) {
                int localMax = future.get();

                if (localMax > globalMax) {
                    globalMax = localMax;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        System.out.println("Наибольший элемент в матрице: " + globalMax);
    }
}

class MatrixMaxProcessor implements Callable<Integer> {
    private final int[][] matrix;
    private final int startRow;
    private final int endRow;

    public MatrixMaxProcessor(int[][] matrix, int startRow, int endRow) {
        this.matrix = matrix;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public Integer call() {
        int max = Integer.MIN_VALUE;
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] > max) {
                    max = matrix[i][j];
                }
            }
        }
        return max;
    }
}
