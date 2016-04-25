package spliterator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SpliteratorFinder {

	private static int POOL_SIZE = 8;

	private final ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

	public Map<String, List<String>> find(String searchString, Spliterator<String> firstSpliterator) {

		List<Future<FoundHolder<String>>> futures = new LinkedList<>();
		List<Callable<FoundHolder<String>>> callables = new LinkedList<>();

		// Split the list in POOL_SIZE pieces
		List<Spliterator<String>> spliterators = new LinkedList<>();
		spliterators.add(firstSpliterator);
		splitInX(firstSpliterator, spliterators, firstSpliterator.getExactSizeIfKnown() / POOL_SIZE, POOL_SIZE);
		
		// Print the spliterator data distribution
		System.out.println("Spliterator estimated size vs. known size");
		spliterators.forEach(s -> System.out.println(s.estimateSize() + ":" + s.getExactSizeIfKnown()));

		// Create the callables for the search
		for (Spliterator<String> nextSpliterator : spliterators) {
			FoundHolder<String> holder = new FoundHolder<String>();
			Consumer<String> finder = s -> {
				if (s.toLowerCase().contains(searchString.toLowerCase()))
					holder.add(s);
			};

			callables.add(() -> {
				nextSpliterator.forEachRemaining(finder);
				return holder;
			});
		}

		// Spawn the search in parallel
		for (Callable<FoundHolder<String>> next : callables) {
			futures.add(executor.submit(next));
		}

		Map<String, List<String>> mergedList = new HashMap<>();
		// Collect the results
		for (Future<FoundHolder<String>> nextFuture : futures) {
			try {
				FoundHolder<String> nextHolder = nextFuture.get();
				mergedList.computeIfAbsent(nextHolder.getId(), v -> new LinkedList<String>());
				mergedList.compute(nextHolder.getId(), (k, v) -> {
					v.addAll(nextHolder.getFoundItems());
					return v;
				});
			} catch (InterruptedException e) {
				// List may not contain all found elements
			} catch (ExecutionException e) {
				// List may not contain all found elements
			}
		}

		return mergedList;

	}

	private void shutdown() {
		executor.shutdownNow();
	}

	private static <T> void splitInX(Spliterator<T> spliterator, List<Spliterator<T>> handles, long dataSize, int maxSpliterators) {

		if (handles.size() < maxSpliterators) {
			Spliterator<T> peerSpliterator = spliterator.trySplit();
			
			if (peerSpliterator != null) {
				handles.add(peerSpliterator);
				
				if (peerSpliterator.getExactSizeIfKnown() > dataSize) {
					splitInX(peerSpliterator, handles, dataSize, maxSpliterators);
					splitInX(spliterator, handles, dataSize, maxSpliterators);
				}
			}
		}
	}

	private static class FoundHolder<T> {
		private static AtomicInteger count = new AtomicInteger(0);

		private final String id;
		private List<T> foundItems = new LinkedList<T>();

		public FoundHolder() {
			id = Integer.toString(count.incrementAndGet());
		}

		public String getId() {
			return id;
		}

		public void add(T t) {
			foundItems.add(t);
		}

		public List<T> getFoundItems() {
			return foundItems;
		}
	}

	public static void main(String... args) throws Exception {

		// Try with an unknown size
		{
			SpliteratorFinder finder = new SpliteratorFinder();
			URL url = new URL("http://www.oracle.com");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			Spliterator<String> s = reader.lines().spliterator();
			finder.find("oracle", s).entrySet().forEach(e -> {
				System.out.println("Thread " + e.getKey());
				e.getValue().forEach(n -> System.out.println("  " + n));
			});
			finder.shutdown();
		}

		
		// Now try with the a fixed size collection
		{
			List<String> list = new ArrayList<>();
			for (int index = 0; index < 4096; ++index) {
				list.add(Integer.toString(index));
			}

			SpliteratorFinder finder = new SpliteratorFinder();
			finder.find("10", list.spliterator()).entrySet().forEach(e -> {
				System.out.println("Thread " + e.getKey());
				e.getValue().forEach(n -> System.out.println("  " + n));
			});
			
			finder.shutdown();
		}
	}
}