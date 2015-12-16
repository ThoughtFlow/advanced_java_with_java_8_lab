package lab03;

@FunctionalInterface
public interface Interface3 {

	public int getAxB(int a, int b);
	
	default int defaultGetAxB(int a, int b) {
		return a * b;
	}
	
}
