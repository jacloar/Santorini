package utils;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Utils {

  /**
   * Calls the given function on the given obj, but makes sure that the call does not last
   * longer than timeout seconds.
   *
   * @param <T> type the function returns
   * @param obj obj to call function on
   * @param func function to call on obj
   * @param timeout length in seconds to wait for
   * @return the result of the function, or empty if timeout
   */
  public static <T, U> Optional<T> timedCall(U obj, Function<U, T> func, int timeout) {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    TimeLimiter limiter = SimpleTimeLimiter.create(executor);
    T result;
    try {
      result = limiter.callWithTimeout(() -> func.apply(obj), timeout, TimeUnit.SECONDS);
    } catch (Exception e) {
      executor.shutdown();
      return Optional.empty();
    }

    executor.shutdown();
    return Optional.of(result);
  }

  public static String createRandomName() {
    int aNum = 97; // number representing character 'a'
    int zNum = 122; // number representing character 'z'
    int length = 10;

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < length; i += 1) {
      int charInt = aNum + (int) (Math.random() * (zNum - aNum + 1));
      builder.append((char) charInt);
    }

    return builder.toString();
  }
}
