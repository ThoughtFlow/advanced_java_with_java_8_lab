package lab09;

public interface MessageSink<T> {

	public void put(T message);
}
