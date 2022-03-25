import java.util.ArrayList;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        CustomPublisher publisher = new CustomPublisher();
        for (int i = 0; i < 5; i++) {
            publisher.addValue(i);
        }
        CustomSubscriber subscriber1 = new CustomSubscriber("First subs");
        CustomSubscriber subscriber2 = new CustomSubscriber("Second subs");
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        for (int i = 0; i < 5; i++) {
            publisher.addValue(5 + i);
        }
    }
}
