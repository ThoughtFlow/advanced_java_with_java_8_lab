package spliterator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class SpliteratorSpliter {

	private static final int MAX_SPLITERATORS = 8;
	
	private static <T> void splitInX(Spliterator<T> spliterator, List<Spliterator<T>> handles, long targetSpliteratorSize, int maxSpliterators) {

		// Try to create no more than maxSpliterators 
		if (handles.size() < maxSpliterators) {
			Spliterator<T> peerSpliterator = spliterator.trySplit();
			
			// Did the split work?
			if (peerSpliterator != null) {
				handles.add(peerSpliterator);
				
				// If possible, each spliterator's size should be no smaller than targetSpliteratorSize.
				// Note that this method may not know what the real value is. If it doesn't know, it will estimate it and may be inaccurate. 
				// Inaccurate estimates lead to unbalanced spliterators.
				if (peerSpliterator.getExactSizeIfKnown() > targetSpliteratorSize) {
					
					// Further split is possible. Now recursively call this method to split each leg. 
					splitInX(peerSpliterator, handles, targetSpliteratorSize, maxSpliterators);
					splitInX(spliterator, handles, targetSpliteratorSize, maxSpliterators);
				}
			}
		}
	}
	
	private static <T> List<Spliterator<T>> doSplit(Collection<T> collection) {
		
		List<Spliterator<T>> spliterators = new LinkedList<>();
		Spliterator<T> firstSpliterator = collection.spliterator();
		spliterators.add(firstSpliterator);
		splitInX(firstSpliterator, spliterators, firstSpliterator.getExactSizeIfKnown() / MAX_SPLITERATORS, MAX_SPLITERATORS);
		
		return spliterators;
	}
	
	private static <T> List<Spliterator<T>> doSplit(Spliterator<T> firstSpliterator) {
		
		List<Spliterator<T>> spliterators = new LinkedList<>();
		spliterators.add(firstSpliterator);
		splitInX(firstSpliterator, spliterators, firstSpliterator.getExactSizeIfKnown() / MAX_SPLITERATORS, MAX_SPLITERATORS);
		
		return spliterators;
	}
	
	private static <T> void printSplitResult(List<Spliterator<T>> spliterators, String sourceType) {
		
		// Print the spliterator data distribution
		System.out.println("==============");
		System.out.println("Spliterator estimated size vs. known size: "+ sourceType);
		System.out.println("Characteristics: " + getCharacteristics(spliterators.get(0)));
		
		int totalEstimatedSize = 0;
		int totalExactSize = 0;
		
		for (Spliterator<T> next : spliterators) {
			totalEstimatedSize += next.estimateSize();
			totalExactSize += next.getExactSizeIfKnown() > -1L ?  next.getExactSizeIfKnown() : 0;
		}
		
		AtomicInteger counter = new AtomicInteger(1);
		spliterators.forEach(s -> System.out.println("Slice " + counter.getAndIncrement() + ": " + s.estimateSize() + ":" + s.getExactSizeIfKnown()));
		System.out.println("Total estimated size: " + totalEstimatedSize);
		System.out.println("Total exact size: " + totalExactSize);
	}

	private static String getCharacteristics(Spliterator<?> spliterator) {
		StringBuilder characteristics = new StringBuilder();
		characteristics.append(spliterator.hasCharacteristics(Spliterator.ORDERED) ? "ORDERED, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.DISTINCT) ? "DISTINCT, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.SORTED) ? "SORTED, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.SIZED) ? "SIZED, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.NONNULL) ? "NONNULL, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.IMMUTABLE) ? "IMMUTABLE, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.CONCURRENT) ? "CONCURRENT, " : "");
		characteristics.append(spliterator.hasCharacteristics(Spliterator.SUBSIZED) ? "SUBSIZED, " : "");
		
		int lastComma = characteristics.lastIndexOf(", ");
		return lastComma > 0 ? characteristics.substring(0, lastComma) : characteristics.toString();
	}
	
	private static <T extends Collection<String>> T populate(T collection, int size) {
		for (int index = 0; index < size; ++index) {
			collection.add(Integer.toString(index));
		}
		
		return collection;
	}
	
	public static void main(String... args) throws Exception {

		{
			// ArrayList
		    List<String> list = populate(new ArrayList<String>(), 4096);
			printSplitResult(doSplit(list), "ArrayList");
		}

		{
			// A small LinkedList
		    List<String> list = populate(new LinkedList<String>(), 4096);
			printSplitResult(doSplit(list), "Small LinkedList");
		}
		
		{
			// A big LinkedList 
		    List<String> list = populate(new LinkedList<String>(), 1000000);
			printSplitResult(doSplit(list), "Big LinkedList");
		}
		
		{
			// A HashSet
			Set<String> set = populate(new HashSet<String>(), 4096);
			printSplitResult(doSplit(set), "HashSet");
		}
		
		{
			// A LinkedHashSet
			Set<String> set = populate(new LinkedHashSet<String>(), 4096);
			printSplitResult(doSplit(set), "LinkedHashSet");
		}

		{
			// An infinite source using streams
		    Spliterator<Integer> s = Stream.iterate(0, i -> ++i).spliterator();
		    printSplitResult(doSplit(s), "Infinite");
		}
				
		{
			// A BufferedReader
			URL url = new URL("http://www.oracle.com");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			Spliterator<String> firstSpliterator = reader.lines().spliterator();
		
			List<Spliterator<String>> spliterators = new LinkedList<>();
			spliterators.add(firstSpliterator);
			splitInX(firstSpliterator, spliterators, firstSpliterator.getExactSizeIfKnown() / 8, 8);
			printSplitResult(spliterators, "BufferedReader");
		}
	}
}