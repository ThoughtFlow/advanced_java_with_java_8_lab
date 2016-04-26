package lab07;

public class QueueNotifyWait {

	private Element first, last;
	private int curSize, maxSize;

	public QueueNotifyWait(int maxSize) {
		this.maxSize = maxSize;
	}

	public synchronized void put(Object o) throws InterruptedException {
		while (this.curSize == this.maxSize) {
			this.wait();
		}
		
		if (this.first == null) {
			this.first = (this.last = new Element(o));
		} 
		else {
			this.last = (this.last.next = new Element(o));
		}
		
		this.curSize++;
		this.notifyAll();
	}

	public synchronized Object get() throws InterruptedException {
		while (this.curSize == 0) {
			this.wait();
		}
		
		Object o = this.first.value;
		this.first = this.first.next;
		this.curSize--;
		this.notifyAll();
		
		return o;
	}

	private static class Element {
		final Object value;
		Element next;

		Element(Object value) {
			this.value = value;
		}
	}
	
	public static void main(String... args) throws Exception {
		QueueLockCondition q = new QueueLockCondition(10);
		Thread getterThread = new Thread(() -> 
			{ 
				try {
					System.out.println("Got the value: " + q.get());
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		
		getterThread.start();
		Thread.sleep(5000);
		q.put("Hello");
	}
}
