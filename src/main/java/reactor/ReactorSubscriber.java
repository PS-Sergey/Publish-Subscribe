package reactor;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class ReactorSubscriber extends BaseSubscriber<Integer> {

    private String name;

    public ReactorSubscriber(String name) {
        this.name = name;
    }

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println(String.format("%s subscribed success", name));
        request(1);
    }

    @Override
    protected void hookOnNext(Integer value) {
        System.out.println(String.format("%s get %d", name, value));
        request(1);
    }

    @Override
    protected void hookOnComplete() {
        System.out.println(String.format("%s completed", name));
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
