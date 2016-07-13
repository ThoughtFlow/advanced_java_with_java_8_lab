package lab03;

public class LambdaTest {

	public static void main(String... args) {

		// Need to instantiate the lambda to access the default instance method.
		Interface1 i1 = x -> {};
		i1.defaultPrintSquareOfA(3);

		// Need to instantiate the lambda to access the default instance method.
		Interface2 i2 = x -> 0;
		System.out.println(i2.defaultSquareOfA(3));

		// No need to instantiate the lambda to access the static instance method.
		System.out.println(Interface3.defaultGetAxB(3, 3));

		// No need to instantiate the lambda to access the static instance method.
		System.out.println(Interface4.defaultGetPi());
	}
}
