import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Cleaner {

    public static void cleanBooks(){
        QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        QueueConnection connection = null;

        try {
            connection = connectionFactory.createQueueConnection();
            connection.start();

            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("CleanerQueue");
            QueueReceiver receiver = session.createReceiver(queue);

            if (receiver != null) {
                try {
                Message message = receiver.receive();

                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    String filename = (String) objectMessage.getObject();

                    ContentExtractor cont = new ContentExtractor();
                    MetadataExtractor meta = new MetadataExtractor();

                    String content = cont.extractData(filename);
                    cont.storeData(content, filename);

                    String metadata = meta.extractData(filename);
                    meta.storeData(metadata, filename);

                    message.acknowledge();
                }
                } catch (Exception e) {
                    System.out.println("Moving to the next file.\n");
                }

            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
