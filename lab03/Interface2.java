package lab03;

@FunctionalInterface
public interface Interface2 {

	public int getSquareOfA(int a);
	
	default int defaultSquareOfA(int a) {
		return a * a;
	}
	
}
