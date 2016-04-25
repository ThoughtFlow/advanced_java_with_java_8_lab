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

public class SpliteratorSpliter {

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
	
	private static <T> List<Spliterator<T>> doSplit(Collection<T> collection) {
		
		List<Spliterator<T>> spliterators = new LinkedList<>();
		Spliterator<T> firstSpliterator = collection.spliterator();
		spliterators.add(firstSpliterator);
		splitInX(firstSpliterator, spliterators, firstSpliterator.getExactSizeIfKnown() / 8, 8);
		
		return spliterators;
	}
	
	private static <T> void printSplitResult(List<Spliterator<T>> spliterators, String sourceType) {
		
		// Print the spliterator data distribution
		System.out.println("==============");
		System.out.println("Spliterator estimated size vs. known size: "+ sourceType);
		
		int totalEstimatedSize = 0;
		int totalExactSize = 0;
		
		for (Spliterator<T> next : spliterators) {
			totalEstimatedSize += next.estimateSize();
			totalExactSize += next.getExactSizeIfKnown() > -1L ?  next.getExactSizeIfKnown() : 0;
		}
		
		spliterators.forEach(s -> System.out.println(s.estimateSize() + ":" + s.getExactSizeIfKnown()));
		System.out.println("Total estimated size: " + totalEstimatedSize);
		System.out.println("Total exact size: " + totalExactSize);
	}

	public static void main(String... args) throws Exception {

		{
			List<String> list = new ArrayList<>();
			for (int index = 0; index < 4096; ++index) {
				list.add(Integer.toString(index));
			}
			
			printSplitResult(doSplit(list), "ArrayList");
		}

		{
			List<String> list = new LinkedList<>();
			for (int index = 0; index < 4096; ++index) {
				list.add(Integer.toString(index));
			}
			
			printSplitResult(doSplit(list), "LinkedList");
		}
		
		{
			List<String> list = new LinkedList<>();
			for (int index = 0; index < 1000000; ++index) {
				list.add(Integer.toString(index));
			}
			
			printSplitResult(doSplit(list), "LinkedList");
		}
		
		{
			Set<String> list = new HashSet<>();
			for (int index = 0; index < 4096; ++index) {
				list.add(Integer.toString(index));
			}
			
			printSplitResult(doSplit(list), "HashSet");
		}
		
		
		{
			Set<String> list = new LinkedHashSet<>();
			for (int index = 0; index < 4096; ++index) {
				list.add(Integer.toString(index));
			}
			
			printSplitResult(doSplit(list), "LinkedHashSet");
		}

		{
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