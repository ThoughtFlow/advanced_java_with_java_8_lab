package lab03;

public class LambdaTest {

	public static void main(String... args) {

		Interface1 i1 = x -> {System.out.println(x * x);};
		i1.defaultPrintSquareOfA(3);

		Interface2 i2 = x -> x * x;
		System.out.println(i2.defaultSquareOfA(3));

		Interface3 i3 = Math::multiplyExact;
		System.out.println(i3.defaultGetAxB(3, 3));

		Interface4 i4 = () -> Math.PI;
		System.out.println(i4.defaultGetPi());
	}
}
