import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        String datalake = "Datalake";
        String datamart = "Datamart";
        TreeIndexer index = new TreeIndexer(datalake, datamart);

        Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                index.indexGenerator();
            }
        };

        timer.schedule(timertask, 0, 60 * 1000);
    }
}
