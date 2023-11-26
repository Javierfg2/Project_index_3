import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class TreeIndexer implements Indexer{
    private String datamart;
    private String datalake;

    private Set<String> indexedFiles;

    public TreeIndexer(String datalake, String datamart){
        this.datalake = datalake;
        this.datamart = datamart;
        this.indexedFiles = new HashSet<>();
    }

    @Override
    public void indexGenerator(){

        File datalake = new File(this.datalake);

        if (datalake.exists() && datalake.isDirectory()){
            exploreQueue();
        }
        else{
            System.out.println("Empty directory");
        }
    }

    @Override
    public String wordBrowser(String word) {
        String route = this.datamart+"/"+word.charAt(0)+"/"+word.charAt(0)+word.charAt(1)+"/"+word.charAt(0)+
                word.charAt(1)+word.charAt(2)+"/"+word+".txt";
        StringBuilder answer = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(route));

            String line;

            while ((line = br.readLine()) != null){
                answer.append(line);
            }

            br.close();

            return answer.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Word not found");
            return " ";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void exploreQueue(){
        QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        QueueConnection connection = null;

        try {
            connection = connectionFactory.createQueueConnection();
            connection.start();

            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("IndexerQueue");
            QueueReceiver receiver = session.createReceiver(queue);

            while (true) {
                Message message = receiver.receive();

                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    String filename = (String) objectMessage.getObject();

                    new FileIndexer(new File(filename), this.datamart);
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

    public boolean emptyDatamart() {
        File datamart = new File(this.datamart);

        if (!datamart.exists()){
            return true;
        } else if (datamart.isDirectory()) {
            String[] files = datamart.list();
            return files == null || files.length == 0;
        } else{
            return false;
        }
    }

    public String getDatalake(){
        return this.datalake;
    }

    public String getDatamart(){
        return this.datamart;
    }

    public void setDatalake(String newDatalake) {
        this.datalake = newDatalake;
    }

    public void setDatamart(String newDatamart){
        this.datamart = newDatamart;
    }
}