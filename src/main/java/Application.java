import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> values = Arrays.asList(1,2,3,4,5);
        CustomPublisher publisher = new CustomPublisher(values);
        CustomSubscriber subscriber1 = new CustomSubscriber("First subs");
        CustomSubscriber subscriber2 = new CustomSubscriber("Second subs");
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
    }
}
