package lab04;

import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;

public class LambdaTest {

	public static void main(String... args) {

		IntConsumer i1 = x -> { System.out.println(x * x); };
		i1.accept(3);

		Function<Integer, Integer> i2 = x -> x * x;
		System.out.println(i2.apply(3));

		BiFunction<Integer, Integer, Integer> i3 = (x, y) -> x * y;
		System.out.println(i3.apply(3, 3));

		DoubleSupplier i4 = () -> Math.PI;
		System.out.println(i4.getAsDouble());
	}
}
