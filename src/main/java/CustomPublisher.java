import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomPublisher implements Flow.Publisher<Integer> {

    final ExecutorService executor = Executors.newFixedThreadPool(4);
    private CopyOnWriteArrayList<Integer> listValues;
    private List subscriptions = Collections.synchronizedList(new ArrayList());

    public CustomPublisher(List<Integer> values) {
        listValues = new CopyOnWriteArrayList<Integer>(values);
    }

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        CustomSubscription subscription = new CustomSubscription(subscriber, executor, listValues.iterator());
        subscriptions.add(subscription);
        executor.execute(() -> subscriber.onSubscribe(subscription));
    }


    private class CustomSubscription implements Flow.Subscription{

        private Flow.Subscriber<Integer> subscriber;
        private AtomicBoolean isCanceled;
        private Iterator<Integer> iterator;
        private ExecutorService executor;

        public CustomSubscription(Flow.Subscriber<Integer> subscriber, ExecutorService executor, Iterator<Integer> iterator) {
            this.subscriber = subscriber;
            this.executor = executor;
            this.iterator = iterator;
            isCanceled = new AtomicBoolean(false);
        }

        @Override
        public void request(long n) {
            if (isCanceled.get()) {
                return;
            }
            if (n <= 0) {
                executor.execute(() -> subscriber.onError(new IllegalArgumentException()));
            }
            for (long i = n; iterator.hasNext() && !isCanceled.get() && i > 0; i--) {
                executor.execute(() -> subscriber.onNext(iterator.next()));
            }
            if (!iterator.hasNext() && !isCanceled.get()) {
                subscriber.onComplete();
                isCanceled.set(true);
                cancel();
            }
        }

        @Override
        public void cancel() {
            isCanceled.set(true);
            synchronized (subscriptions) {
                subscriptions.remove(this);
                if (subscriptions.size() == 0) {
                    executor.shutdown();
                }
            }
        }

    }
}
