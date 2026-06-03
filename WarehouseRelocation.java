import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

interface Грузчик extends Runnable {
}

public class WarehouseRelocation {

    static void main() {
        Queue<Integer> warehouse = new ConcurrentLinkedQueue<>();
        System.out.println("--- Генерация товаров на складе ---");
        for (int i = 0; i < 20; i++) {
            int weight = 10 + (int) (Math.random() * 41);
            warehouse.add(weight);
            System.out.print(weight + " ");
        }
        System.out.println("\nВсего товаров на складе: " + warehouse.size() + "\n");

        SharedVan van = new SharedVan();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 3; i++) {
            executorService.submit(new LoaderRealization("Грузчик " + i, warehouse, van));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        van.deliverRemaining();

        System.out.println("--- Все товары успешно перенесены на новый склад! ---");
    }
}

class SharedVan {
    private int currentWeight = 0;
    private final int MAX_WEIGHT = 150;

    public synchronized void loadItem(int itemWeight, String loaderName) {
        if (currentWeight + itemWeight > MAX_WEIGHT) {
            driveToOtherWarehouse();
        }

        currentWeight += itemWeight;
        System.out.println(loaderName + " погрузил товар [" + itemWeight + " кг]. Сейчас в фургоне: " + currentWeight + " кг.");

        if (currentWeight == MAX_WEIGHT) {
            driveToOtherWarehouse();
        }
    }

    private void driveToOtherWarehouse() {
        System.out.println(">>> Фургон загружен (" + currentWeight + " кг). Грузчики едут на другой склад... <<<");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("<<< Фургон разгружен и вернулся. Можно грузить снова. <<<\n");
        currentWeight = 0;
    }

    public void deliverRemaining() {
        if (currentWeight > 0) {
            System.out.println("\n--- На складе кончились товары. Отвозим остатки... ---");
            driveToOtherWarehouse();
        }
    }
}

class LoaderRealization implements Грузчик {
    private final String name;
    private final Queue<Integer> warehouse;
    private final SharedVan van;

    public LoaderRealization(String name, Queue<Integer> warehouse, SharedVan van) {
        this.name = name;
        this.warehouse = warehouse;
        this.van = van;
    }

    @Override
    public void run() {
        while (!warehouse.isEmpty()) {
            Integer itemWeight = warehouse.poll();

            if (itemWeight != null) {
                van.loadItem(itemWeight, name);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println(name + " закончил работу (на складе пусто).");
    }
}
