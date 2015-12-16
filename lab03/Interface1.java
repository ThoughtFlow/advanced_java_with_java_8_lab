package lab03;

@FunctionalInterface
public interface Interface1 {

	public void printSquareOfA(int a);
	
	default void defaultPrintSquareOfA(int a) {
		System.out.println(a * a);
	}
	
}
