package lab03;

@FunctionalInterface
public interface Interface4 {

	public Double getPi();
	
	default Double defaultGetPi() {
		return Math.PI;
	}
	
}
