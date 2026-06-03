import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolArrayProcessing {

    static void main() {
        int arraySize = 1_000_000;
        int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = i + 1;
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Количество потоков: " + numThreads);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Long>> futures = new ArrayList<>();

        int chunkSize = arraySize / numThreads;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? arraySize : (i + 1) * chunkSize;

            Callable<Long> task = new ArrayPartProcessor(array, startIndex, endIndex);
            Future<Long> future = executorService.submit(task);
            futures.add(future);
        }

        long totalSum = 0;

        try {
            for (Future<Long> future : futures) {
                long partialSum = future.get();
                totalSum += partialSum;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Общая сумма (многопоточно): " + totalSum);
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");

        long expectedSum = (long) arraySize * (arraySize + 1) / 2;
        System.out.println("Ожидаемая сумма (математически): " + expectedSum);
    }
}

class ArrayPartProcessor implements Callable<Long> {
    private final int[] array;
    private final int startIndex;
    private final int endIndex;

    public ArrayPartProcessor(int[] array, int startIndex, int endIndex) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Long call() {
        long sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += array[i];
        }
        return sum;
    }
}
