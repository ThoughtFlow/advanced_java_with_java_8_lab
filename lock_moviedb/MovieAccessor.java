package lock_moviedb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MovieAccessor {

	private final MovieDB movieDb = new MovieDB();
	private AtomicInteger nextSequel = new AtomicInteger(0);

	private Runnable addCommand = new Runnable() {
		public void run() {
			for (int i = 0; i < 1000; ++i) {
				movieDb.add(Category.DRAMA, "Rocky: " + nextSequel.incrementAndGet(), 1976);
			}
		}
	};

	private Runnable findCommand = new Runnable() {
		public void run() {			
			for (int i = 0; i < 1000; ++i) {
				movieDb.find("Rocky: " + (nextSequel.get() - 1));
			}
			
		}
	};

	private Runnable deleteCommand = new Runnable() {
		public void run() {
			for (int i = 0; i < 1000; ++i) {
				movieDb.delete("Rocky: " + (nextSequel.get() - 1));

			}
		}
	};

	private ArrayList<Runnable> runnables = new ArrayList<Runnable>(Arrays.asList(addCommand, findCommand, deleteCommand));

	public void startThreads() throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(100);

		for (int i = 0; i < 10; i++) {
			threadPool.execute(runnables.get(i % 3));
		}

		threadPool.shutdown();
		threadPool.awaitTermination(5, TimeUnit.SECONDS);
	}
	
	public void checkAccess()
	{
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

		MovieAccessor movieAccessor = new MovieAccessor();

		try {
			movieAccessor.startThreads();
			movieAccessor.checkAccess();
		} catch (InterruptedException e) {

		}
	}

}
