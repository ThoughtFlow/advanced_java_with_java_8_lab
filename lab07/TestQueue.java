package lab07;

public class TestQueue {

	public static void testQueue(SynchronizedQueue queue) {

		System.out.println("=============");
		System.out.println("Testing with: " + queue.getClass());
		Thread getterThread = new Thread(() -> {
			Object object = null;
			do {
				try {
					Thread.sleep(1000);
					if ((object = queue.get()) != null) {
						System.out.println(System.currentTimeMillis() + ": Getting: " + object);
					}
				} catch (InterruptedException exception) {
					System.err.println("Caught exception: " + exception);
				}
			} while (object != null);
		});

		getterThread.start();

		try {
			Thread.sleep(5000);
			for (int index = 0; index < 10; ++index) {
				System.out.println(System.currentTimeMillis() + ": Putting: " + index);
				queue.put(index);
			}
			queue.put(null);
			getterThread.join();
		} catch (InterruptedException exception) {
			System.err.println("Caught exception: " + exception);
		}
	}

	public static void main(String... args) throws Exception {
		testQueue(new QueueNotifyWait(5));
		testQueue(new QueueLockCondition(5));
	}
}
