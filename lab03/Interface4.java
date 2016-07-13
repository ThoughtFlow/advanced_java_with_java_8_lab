package lab03;

@FunctionalInterface
public interface Interface4 {

	public Double getPi();
	
	static Double defaultGetPi() {
		return Math.PI;
	}
	
}
