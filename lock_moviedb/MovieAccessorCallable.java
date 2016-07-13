package lock_moviedb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MovieAccessorCallable {

	private final MovieDB movieDb = new MovieDB();
	private AtomicInteger nextSequel = new AtomicInteger(0);
	List<Future<Integer>> futures = new ArrayList<>();

	private Callable<Integer> addCommand = new Callable<Integer>() {
	    public Integer call() {
			for (int i = 0; i < 1000; ++i) {
				movieDb.add(Category.DRAMA, "Rocky: " + nextSequel.incrementAndGet(), 1976);
			}
			
			return 1000;
	    }
	};
			
	private Callable<Integer> findCommand = new Callable<Integer>() {
	    public Integer call() {
	    	for (int i = 0; i < 1000; ++i) {
	    		movieDb.find("Rocky: " + (nextSequel.get() - 1));
	    	}
		
		    return 1000;
	    }
	};
			
	private Callable<Integer> deleteCommand = new Callable<Integer>() {
	    public Integer call() {
			for (int i = 0; i < 1000; ++i) {
				movieDb.delete("Rocky: " + (nextSequel.get() - 1));
			}
			
			return 1000;
	    }
	};
	
	private ArrayList<Callable<Integer>> runnables = new ArrayList<>(Arrays.asList(addCommand, findCommand, deleteCommand));

	public void startThreads() throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(100);
 
		for (int i = 0; i < 10; i++) {
			futures.add(threadPool.submit(runnables.get(i % 3)));
		}


		
		threadPool.shutdown();
		threadPool.awaitTermination(5, TimeUnit.SECONDS);
	}
	
	public void checkAccess() throws ExecutionException, InterruptedException
	{
		boolean isAll1000= true;
		for (Future<Integer> nextFuture : futures) {
			if (nextFuture.get() != 1000)
			{
				isAll1000 = false;
			}
		}
		
		if (isAll1000 == false) {
			System.out.println("Not all 1000");
		}
		
		ArrayList<AccessRecord> records = new ArrayList<AccessRecord>(movieDb.getRecords());
		for (int i= 0; i < records.size(); ++i) {
			for (int j = i + 1; j < records.size(); ++j)
			{
				if (records.get(i).isOverlap(records.get(j))) {
					System.out.println(records.get(i) + " vs. " + records.get(j));
				}
			}
		}
	}

	public static void main(String... args) {

		MovieAccessorCallable movieAccessor = new MovieAccessorCallable();

		try {
			movieAccessor.startThreads();
			movieAccessor.checkAccess();
		} catch (InterruptedException e) {

		} catch (ExecutionException e) {

	}
	}

}
