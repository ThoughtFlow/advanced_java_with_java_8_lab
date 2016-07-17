package lab07;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class QueueLockCondition implements SynchronizedQueue {

	private Element first, last;
	private int curSize, maxSize;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Condition condition = lock.writeLock().newCondition();

	public QueueLockCondition(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void put(Object o) throws InterruptedException {
		try {
			lock.writeLock().lock();
			
			while (this.curSize == this.maxSize) {
				condition.await();
			}
			
			if (this.first == null) {
				this.first = (this.last = new Element(o));
			} else {
				this.last = (this.last.next = new Element(o));
			}
			
			this.curSize++;
			condition.signalAll();
		} 
		finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Object get() throws InterruptedException {

		try {
			lock.writeLock().lock();
			
			while (this.curSize == 0) {
				condition.await();
			}
			
			Object o = this.first.value;
			this.first = this.first.next;
			this.curSize--;
			condition.signalAll();
			
			return o;
		} 
		finally {
			lock.writeLock().unlock();
		}
	}

	private static class Element {
		final Object value;
		Element next;

		Element(Object value) {
			this.value = value;
		}
	}
}
