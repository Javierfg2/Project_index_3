import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Timer;
import java.util.TimerTask;

public class CrawlerTimer {
    private static int currentBookId = 1;
    public static int maxBookId = 2000;
    static Timer timer = new Timer();

    public static void scheduleDownloadTask() {
        timer.scheduleAtFixedRate(new DownloadTask(), 0, 1000);
    }

    public static class DownloadTask extends TimerTask {

        public void run() {

            if (currentBookId <= maxBookId) {
                String txtFileUrl = "https://www.gutenberg.org/cache/epub/" + currentBookId + "/pg" + currentBookId + ".txt";
                DownloadBook.downloadTextFile(txtFileUrl, Integer.toString(currentBookId));
                currentBookId++;
            } else {
                timer.purge();
                timer.cancel();
                return;
            }

            QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            QueueConnection connection = null;
            try {
                connection = connectionFactory.createQueueConnection();
                connection.start();
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

                Queue indexerQueue = session.createQueue("IndexerQueue");
                ObjectMessage indexerMessage = session.createObjectMessage();
                indexerMessage.setObject(FolderCreator.getFilename(String.valueOf(currentBookId - 1)));
                QueueSender indexerSender = session.createSender(indexerQueue);
                indexerSender.send(indexerMessage);

                Queue cleanerQueue = session.createQueue("CleanerQueue");
                ObjectMessage cleanerMessage = session.createObjectMessage();
                cleanerMessage.setObject(FolderCreator.getFilename(String.valueOf(currentBookId - 1)));
                QueueSender cleanerSender = session.createSender(cleanerQueue);
                cleanerSender.send(cleanerMessage);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
