package utils;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class Utils {

  private static final ExecutorService executor = Executors.newSingleThreadExecutor();

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
    TimeLimiter limiter = SimpleTimeLimiter.create(executor);
    T result;
    try {
      result = limiter.callWithTimeout(() -> func.apply(obj), timeout, TimeUnit.SECONDS);
    } catch (TimeoutException | InterruptedException | ExecutionException e) {
      return Optional.empty();
    }

    return Optional.of(result);
  }
}
