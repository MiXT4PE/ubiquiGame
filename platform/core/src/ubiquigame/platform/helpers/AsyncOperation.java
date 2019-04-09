package ubiquigame.platform.helpers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import ubiquigame.platform.PlatformImpl;

public class AsyncOperation<T> {
	private Future<T> resultFuture;
	private Callable<T> asyncCallable;
	private Consumer<T> afterAsync;

	private AsyncOperation() {

	}

	public AsyncOperation<T> onResult(Consumer<T> consumer) {
		afterAsync = consumer;
		return this;
	}

	public void execute() {
		resultFuture = PlatformImpl.getInstance().getExecutorService().submit(asyncCallable);
	}
	
	public boolean isDone() {
		return resultFuture.isDone();
	}
	
	public void doLast() {
		try {
			afterAsync.accept(resultFuture.get());
		} catch (InterruptedException|ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public Future<T> getFuture() {
		return resultFuture;
	}
	
	public static <T> AsyncOperation<T> create(Callable<T> asyncCallable) {
		AsyncOperation<T> asyncOperation = new AsyncOperation<>();
		asyncOperation.asyncCallable = asyncCallable;
		return asyncOperation;
	}

}
