package lab07;

public interface SynchronizedQueue {
	
	/**
	 * Puts an object in the queue.
	 * 
	 * @param object The object to put in the queue.
	 * @throws InterruptedException Thrown if a threading error has occurred.
	 */
	public void put(Object object) throws InterruptedException; 
	
	/**
	 * Gets the next object from the queue.
	 * 
	 * @return The next object from the queue.
	 * @throws InterruptedException Thrown if a threading error has occurred.
	 */
	public Object get() throws InterruptedException;
}