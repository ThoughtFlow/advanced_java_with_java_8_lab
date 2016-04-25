package threading;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExample {

	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		ExecutorService pool = Executors.newCachedThreadPool();
		
		Handler callable = new Handler();
		Future<Long> future = pool.submit(callable);
		
		System.out.println(future.get());
		System.out.println("Initiated shutdown");
		pool.shutdown();
	}
	
	private static class Handler implements Callable<Long> {

		@Override
		public Long call() throws Exception {
			Thread.sleep(10000);
			return System.currentTimeMillis();
		}
	}
}
