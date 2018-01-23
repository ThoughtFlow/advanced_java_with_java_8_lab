package lab11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class StreamGrep {

	private static long grepDashCImperative(BufferedReader in, String upperCaseSearchWord) throws IOException {

		String nextLine = in.readLine();
		int count = 0;

		while (nextLine != null) {
			String upperCaseLine = nextLine.toUpperCase();

			if (upperCaseLine.contains(upperCaseSearchWord)) {
				count++;
			}

			nextLine = in.readLine();
		}

		return count;
	}

	private static long grepDashCWithReduce(BufferedReader in, String upperCaseSearchWord) {

		// The long way
		return in.lines().map(String::toUpperCase).filter(s -> s.contains(upperCaseSearchWord)).mapToLong(count -> 1).reduce(0, (l, r) -> l + r);
	}

	private static long grepDashCWithCount(BufferedReader in, String upperCaseSearchWord) {

		// Using the built-in function count
		return in.lines().map(String::toUpperCase).filter(s -> s.contains(upperCaseSearchWord)).count();
	}

	private static List<String> grepCollect(BufferedReader in, String upperCaseSearchWord) {

		// Accumulate the strings via collect
		return in.lines().map(String::toUpperCase).filter(s -> s.contains(upperCaseSearchWord))
				.collect(ArrayList<String>::new, ArrayList<String>::add, ArrayList<String>::addAll);
	}

	private static List<String> grepCollectWithCollectors(BufferedReader in, String upperCaseSearchWord) {

		// Accumulate the strings via collect
		return in.lines().map(String::toUpperCase).filter(s -> s.contains(upperCaseSearchWord)).collect(Collectors.toList());
	}
	
	public static void main(String... args) {

		final String wordSearch = "JAVA";
		final String url = "http://www.oracle.com/technetwork/java/index.html";
		
//		try (BufferedReader bufferedReader = getReader(url)) {
//			System.out.println("Using grepDashCImperative - Found: " + grepDashCImperative(bufferedReader, wordSearch));
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try (BufferedReader bufferedReader = getReader(url)) {
//			System.out.println("Using grepDashCWithCount - Found: " + grepDashCWithCount(bufferedReader, wordSearch));
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
		
		try (BufferedReader bufferedReader = Util.getReader(url)) {
			System.out.println("Using grepDashCWithReduce - Found: " + grepDashCWithReduce(bufferedReader, wordSearch));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try (BufferedReader bufferedReader = Util.getReader(url)) {
			System.out.println("Using grepCollect - found :");
			grepCollect(bufferedReader, wordSearch).forEach(System.out::println);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
//		try (BufferedReader bufferedReader = getReader(url)) {
//			System.out.println("Using grepCollect2 - found :");
//			grepCollect2(bufferedReader, wordSearch).forEach(System.out::println);
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}