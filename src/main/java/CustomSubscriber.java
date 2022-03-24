import java.util.concurrent.Flow;

public class CustomSubscriber implements Flow.Subscriber<Integer> {

    private Flow.Subscription subscription;
    private String name;

    public CustomSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.println(String.format("%s subscribed success", name));
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        System.out.println(String.format("%s get %d", name, item));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println(String.format("%s completed", name));
    }
}


