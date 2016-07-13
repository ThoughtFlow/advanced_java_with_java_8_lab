package lab02;

public class LambdaTest {

	private static int squareIt(int x) {
		return x * x;
	}
	
	public static void main(String... args) {

		Interface1 i1 = x -> {System.out.println(x * x);};
		i1.printSquareOfA(3);

		Interface2 i2 = LambdaTest::squareIt;
		System.out.println(i2.getSquareOfA(3));

		Interface3 i3 = Math::multiplyExact;
		System.out.println(i3.getAxB(3,  3));

		Interface4 i4 = () -> Math.PI;
		System.out.println(i4.getPi());

		// Interface 4 cannot be converted into a constructor reference - need a new interface
		Interface5 i5 = Double::new;
		System.out.println(i5.getPi(Math.PI));
	}
}
