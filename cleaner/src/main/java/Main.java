import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

        Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                Cleaner.cleanBooks();
            }
        };

        timer.schedule(timertask, 0, 1000);
    }
}
