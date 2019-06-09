import com.google.common.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MINUTES;

public class TestCase {

  private final ExecutorService executorService = newFixedThreadPool(4);

  public void updateData() {
    try {
      final CompletableFuture<List<String>> completableFuture = supplyAsync(() -> asyncSupplier(() -> new MyRepository().getData(), new TypeToken<>() {}), executorService);

      CompletableFuture.allOf(completableFuture).get(3, MINUTES);
    }
    catch (Exception e) {
    }
  }

  private <T> List<T> asyncSupplier(final Supplier<List<T>> supplier, final TypeToken<T> typeToken) {
    final long start = System.currentTimeMillis();
    final List<T> ts = supplier.get();
    final long stop = System.currentTimeMillis();
    System.out.println("Took: " + (stop - start) + "ms for " + typeToken.getType().getTypeName());

    return ts;
  }

  private static final class MyRepository {

    public List<String> getData() {
      return List.of("");
    }
  }
}
