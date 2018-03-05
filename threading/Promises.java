package threading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Promises {

	private static final Integer RETURN_VALUE = 2;
	private static int testCount = 0;
	
	public static Integer doTask(boolean succeed) {
		sleep();
		if (!succeed) {
			throw new IllegalArgumentException("Synthesized error");
		}
		
		return RETURN_VALUE;
	}
	
	public static void doTask(CompletableFuture<Integer> promise, boolean succeed) {
		sleep();
		if (!succeed) {
			promise.completeExceptionally(new IllegalArgumentException("Synthesized error"));
		}
		else
		{
		   promise.complete(RETURN_VALUE);
		}
	}
	
	public static void sleep() {
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException("Error occurred", e);
		}
	}
	
	public static void printTestHeader(String testName) {
		System.out.println("===============");
		System.out.println("Test " + ++testCount + ": " + testName);
	}
	
	public static void main(String... args) {

	try {
			{
				printTestHeader("All tasks run under same thread sequentially using the common fork-join thread pool");
				CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> doTask(true));
				CompletableFuture<Integer> task2 = task1.thenApply(i -> i * 2);
				CompletableFuture<Integer> task3 = task2.thenApply(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task4 = task3.thenApply(i -> (int) Math.pow(i, 3));
	
				System.out.println("Result: " + task1.get());
				System.out.println("Result: " + task2.get());
				System.out.println("Result: " + task3.get());
				System.out.println("Result: " + task4.get());
			}


			
			{
				printTestHeader("All tasks run under same thread sequentially using this thread");
				CompletableFuture<Integer> task1 = new CompletableFuture<>();
				CompletableFuture<Integer> task2 = task1.thenApply(i -> i * 2);
				CompletableFuture<Integer> task3 = task2.thenApply(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task4 = task3.thenApply(i -> (int) Math.pow(i, 3));
	
				task1.complete(doTask(true));
				System.out.println("Result: " + task4.get());
			}
			
			{
				printTestHeader("Task 1 & 3 run in parallel on multiple threads, 2 and 4 run sequentially on any previous task thread");
				CompletableFuture<Integer> task1a = CompletableFuture.supplyAsync(() -> doTask(true));
				CompletableFuture<Integer> task1b = CompletableFuture.supplyAsync(() -> doTask(true));
				CompletableFuture<Integer> task1c = CompletableFuture.supplyAsync(() -> doTask(true));
				
				CompletableFuture<Integer> task2 = 
					task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
						thenCombine(task1c, (iab, ic) -> iab + ic).thenApply(i -> i * 2);
				
				CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				
				CompletableFuture<Integer> task4 = 
					task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
						thenCombine(task3c, (iab, ic) -> iab + ic).
							thenApply(i -> (int) Math.pow(i, 3));
	
				System.out.println("Result: " + task4.get());
			}
			
			{
				printTestHeader("Task 1 & 3 run in parallel on multiple threads, 2 and 4 run sequentially on any previous task thread - " +
							    "with exception handling");
				CompletableFuture<Integer> task1a = CompletableFuture.supplyAsync(() -> doTask(false)).exceptionally(e -> 1);
				CompletableFuture<Integer> task1b = CompletableFuture.supplyAsync(() -> doTask(true)).exceptionally(e -> 1);
				CompletableFuture<Integer> task1c = CompletableFuture.supplyAsync(() -> doTask(true)).exceptionally(e -> 1);
				
				CompletableFuture<Integer> task2 = 
					task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
						thenCombine(task1c, (iab, ic) -> iab + ic).
							thenApply(i -> i * 2);
				
				CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				
				CompletableFuture<Integer> task4 = 
					task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
						thenCombine(task3c, (iab, ic) -> iab + ic).
							thenApply(i -> (int) Math.pow(i, 3));
	
				System.out.println("Result: " + task4.get());
			}
			
			{
				printTestHeader("Task 1 and 2 run on the main thread sequentially, 3 runs in parallel on multiple threads, " +
								"and 4 runs sequentially on any previous task thread - with manual exception handling");
				CompletableFuture<Integer> task1a = new CompletableFuture<>();
				CompletableFuture<Integer> task1b = new CompletableFuture<>();
				CompletableFuture<Integer> task1c = new CompletableFuture<>();
	
				task1a.completeExceptionally(new IllegalArgumentException("Synthesized error"));
				task1b.completeExceptionally(new IllegalArgumentException("Synthesized error"));
				task1c.completeExceptionally(new IllegalArgumentException("Synthesized error"));
				
				// Must be added AFTER the task has executed
				task1a = task1a.exceptionally(e -> 1);
				task1b = task1b.exceptionally(e -> 1);
				task1c = task1c.exceptionally(e -> 1);
				
				CompletableFuture<Integer> task2 = 
					task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
						thenCombine(task1c, (iab, ic) -> iab + ic).
							thenApply(i -> i * 2);
				
				CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				
				CompletableFuture<Integer> task4 = 
					task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
						thenCombine(task3c, (iab, ic) -> iab + ic).
							thenApply(i -> (int) Math.pow(i, 3));
	
				System.out.println("Result: " + task4.get());
			}
			
			{
				printTestHeader("Task 1 and 2 run on the main thread sequentially, 3 runs in parallel on multiple threads, " +
						        "and 4 runs sequentially on any previous task thread - using when complete");
				CompletableFuture<Integer> task1a = new CompletableFuture<>();
				CompletableFuture<Integer> task1b = new CompletableFuture<>();
				CompletableFuture<Integer> task1c = new CompletableFuture<>();
				
				task1a.completeExceptionally(new IllegalArgumentException("Synthesized error"));
				doTask(task1b, true);
				doTask(task1c, true);
				
				CompletableFuture<Integer> task2 = 
					task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
						thenCombine(task1c, (iab, ic) -> iab + ic).
							thenApply(i -> i * 2);
				
				CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
				
				CompletableFuture<Integer> task4 = 
					task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
						thenCombine(task3c, (iab, ic) -> iab + ic).thenApply(i -> (int) Math.pow(i, 3)).
							whenComplete((i, e) -> {
								if (i != null) {
									System.out.println(i);
								}
								else {
									System.err.println("An error occured");
								}
							});
	
				if (!task4.isCompletedExceptionally()) {
				   System.out.println("Result: " + task4.get());
				}
			}
		}
		catch (InterruptedException | ExecutionException e) {
			System.err.println("Exception caught");
			e.printStackTrace();
		}
	}
}
