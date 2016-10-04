package threading;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExample {

	public static void log(String message) {
		System.out.println(System.currentTimeMillis() + ": [" + Thread.currentThread().getName() + "] " + message);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		ExecutorService pool = Executors.newCachedThreadPool();
		
		Handler callable = new Handler();
		log("Submitting handler...");
		Future<Long> future = pool.submit(callable);
		log("Submitting handler...Done");
		
		log("Obtained value: " + future.get());
		pool.shutdown();
	}
	
	private static class Handler implements Callable<Long> {

		@Override
		public Long call() throws Exception {
			log("Handler is executing...");
			Thread.sleep(10000);
			log("Handler is executing...Done");
			return System.currentTimeMillis();
		}
	}
}
