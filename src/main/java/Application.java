import custom.CustomPublisher;
import custom.CustomSubscriber;
import reactor.ReactorSubscriber;
import reactor.core.publisher.Flux;

public class Application {
    public static void main(String[] args) {
        runCustomImpl();
        runReactorImpl();
    }

    /**
     * Реализация паттерна Publish-Subscribe с использованием Java Flow API.
     */
    public static void runCustomImpl() {
        CustomPublisher publisher = new CustomPublisher();
        for (int i = 0; i < 5; i++) {
            publisher.addValue(i);
        }
        CustomSubscriber subscriber = new CustomSubscriber("Custom subscriber");;
        publisher.subscribe(subscriber);
    }

    /**
     * Реализация паттерна Publish-Subscribe с использованием Project Reactor.
     */
    public static void runReactorImpl() {
        ReactorSubscriber subscriber = new ReactorSubscriber("Reactor subscriber");
        Flux.range(1,5).subscribe(subscriber);
    }
}
