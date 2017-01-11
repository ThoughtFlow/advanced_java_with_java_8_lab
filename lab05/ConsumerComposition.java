package lab05;

import java.util.function.Consumer;

public class ConsumerComposition {

	public static void main(String... args) {
		
		// Note that changing to upperCase in printUpperCase lambda does NOT change the input to printToErr
		Consumer<String> printUpperCase = s -> System.out.println(s.toUpperCase());
		Consumer<String> printToErr = s -> {if (s.toUpperCase().contains("EXCEPTION")) System.err.println("Error: " + s);};
		
		Consumer<String> superPrint = printUpperCase.andThen(printToErr);
		
		superPrint.accept("This is a normal line and will only be printed to stdout");
		superPrint.accept("Exception occurred - line will be printed to stdout and stderr");
		
	}
}
