package shpp.mentor;

import com.google.gson.Gson;
import jakarta.validation.ConstraintViolation;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class App {

    public  static void main(String[] args)  {
        try {
            Properties myProp = PropertyFileOpen.openPropertyFile();
            FileWriter errorFileWriter = new FileWriter(myProp.getProperty("errorFileName"),true);
            FileWriter validFileWriter = new FileWriter(myProp.getProperty("validFileName"), true);
            ActiveMQConnectionFactory myFactory = new ActiveMQConnectionFactory(myProp.getProperty("userName")
                    ,myProp.getProperty("password"),myProp.getProperty("brokerURL"));
            Connection consumerConnection = myFactory.createConnection();
            consumerConnection.start();
            Session consumerSession = consumerConnection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Destination queue = consumerSession.createQueue(myProp.getProperty("queue"));
            MessageConsumer consumer = consumerSession.createConsumer(queue);
            while (true) {
                Message message = consumer.receive(10);
                //Do while message queue is not empty
                if(message !=null) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    //Check for POISON PILL message
                    if (!text.equals("Stop reading")) {
                        //Convert json from message to POJO class for check validation
                        Pojo data = new Gson().fromJson(text, Pojo.class);
                        Set<ConstraintViolation<Pojo>> resultValidation = GetValidator.getValidateAge(data);
                        //Check for NonValid messages collection
                        if (!resultValidation.isEmpty()) {
                            String errLog = data.getCount() + ";" + data.getName() + "; {“errors”:[";
                            //Add all NonValid messages to total json
                            for (ConstraintViolation<Pojo> warning : resultValidation) {
                                errLog += warning.getMessage() + ":";
                            }
                            //Add log to error.csv file
                            errorFileWriter.write(errJsonBuilder.errLogPrep(errLog) + "\n");
                        } else {
                            //Add log to valid.csv file
                            validFileWriter.write(data.getCount() + ";" + data.getName() + "\n");
                        }

                    } else {
                        //If receive POISON PILL - Stop reading queue
                        System.out.println("----------Magic pill-----------------------");
                        closeConnection(errorFileWriter, validFileWriter,
                                        consumer, consumerSession, consumerConnection);
                    }
                } else {
                    //If message queue is empty exit from reading loop
                    break;
                }
            }
            //Closing connection after reading procedure
            System.out.println(closeConnection(errorFileWriter, validFileWriter,
                    consumer, consumerSession, consumerConnection));

        }catch (JMSException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Closing all open instances function
    public static String closeConnection(FileWriter errorFileWriter,
                                        FileWriter validFileWriter,
                                        MessageConsumer consumer,
                                        Session consumerSession,
                                        Connection customerConnection) throws IOException, JMSException {
        errorFileWriter.close();
        validFileWriter.close();
        consumer.close();
        consumerSession.close();
        customerConnection.close();
        return "All close";
    }
}