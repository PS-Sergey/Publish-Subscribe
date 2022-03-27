package custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomPublisher implements Flow.Publisher<Integer> {

    private List<Integer> listValues = Collections.synchronizedList(new ArrayList<Integer>());
    private List<Flow.Subscription> subscriptions = Collections.synchronizedList(new ArrayList<Flow.Subscription>());

    public void addValue(Integer value) {
        synchronized (listValues) {
            listValues.add(value);
        }
    }

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        CustomSubscription subscription = new CustomSubscription(subscriber);
        subscriptions.add(subscription);
        subscriber.onSubscribe(subscription);
    }


    private class CustomSubscription implements Flow.Subscription{

        private Flow.Subscriber<Integer> subscriber;
        private AtomicBoolean isCanceled;
        private AtomicInteger numberItem;

        public CustomSubscription(Flow.Subscriber<Integer> subscriber) {
            this.subscriber = subscriber;
            isCanceled = new AtomicBoolean(false);
            numberItem = new AtomicInteger(0);
        }

        @Override
        public void request(long n) {
            if (isCanceled.get()) {
                return;
            }
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException());
            }
            synchronized (listValues) {
                if (numberItem.get() >= listValues.size()) {
                    subscriber.onComplete();
                    cancel();
                }
                for (long i = 0; i < n && !isCanceled.get() && numberItem.get() < listValues.size(); i++) {
                    subscriber.onNext(listValues.get(numberItem.getAndIncrement()));
                }
            }
        }

        @Override
        public void cancel() {
            isCanceled.set(true);
            synchronized (subscriptions) {
                subscriptions.remove(this);
            }
        }

    }
}
