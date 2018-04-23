package serv.bean;

import serv.components.Book;
import serv.components.Books;
import serv.constants.Messages;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//@JMSDestinationDefinitions(
//        value = {
//                @JMSDestinationDefinition(
//                        name = "java:/queue/HELLOWORLDMDBQueue",
//                        interfaceName = "javax.jms.Queue",
//                        destinationName = "HelloWorldMDBQueue"
//                ),
//                @JMSDestinationDefinition(
//                        name = "java:/topic/HELLOWORLDMDBTopic",
//                        interfaceName = "javax.jms.Topic",
//                        destinationName = "HelloWorldMDBTopic"
//                )
//        }
//)

@MessageDriven(messageListenerInterface=MessageListener.class, name = "HelloWorldQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/HELLOWORLDMDBQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
@Stateful
@Remote(Library.class)
@SessionScoped
public class LibraryBean implements Library, Serializable, MessageListener {

    @EJB
    private Books books;

    private String resultMessage;
    private String jmsMessage;

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/HELLOWORLDMDBQueue")
    private Queue queue;

    @Resource(lookup = "java:/topic/HELLOWORLDMDBTopic")
    private Topic topic;

    @Override
    public void borrowBook(int bookId) {
        List<Book> matchingBooks = getBookById(bookId);

        if (matchingBooks.size() > 0) {
            Book matchedBook = matchingBooks.get(0);

            if (!matchedBook.isBorrowed()) {
                matchingBooks.get(0).setBorrowed(true);
                resultMessage = Messages.BORROW_SUCCESS;
                context.createProducer().send(queue, Messages.BORROW_SUCCESS);
            } else {
                resultMessage = Messages.ALREADY_BORROWED;
                context.createProducer().send(queue, Messages.ALREADY_BORROWED);
            }
        } else {
            resultMessage = Messages.BOOK_NOT_FOUND;
            context.createProducer().send(queue, Messages.BOOK_NOT_FOUND);
        }
    }

    private List<Book> getBookById(int bookId) {
        return books.getBookList().stream()
                    .filter(book -> book.getId() == bookId)
                    .collect(Collectors.toList());
    }

    @Override
    public void returnBook(int bookId) {
        List<Book> matchingBooks = getBookById(bookId);

        if (matchingBooks.size() > 0) {
            Book matchedBook = matchingBooks.get(0);

            if (matchedBook.isBorrowed()) {
                matchingBooks.get(0).setBorrowed(false);
                resultMessage = Messages.BOOK_RETURNED;
                context.createProducer().send(queue, Messages.BOOK_RETURNED);
            } else {
                resultMessage = Messages.BOOK_AVAILABLE;
                context.createProducer().send(queue, Messages.BOOK_AVAILABLE);
            }
        } else {
            resultMessage = Messages.BOOK_NOT_FOUND;
            context.createProducer().send(queue, Messages.BOOK_NOT_FOUND);
        }
    }

    @Override
    public void reserveBook(int bookId) {
        List<Book> matchingBooks = getBookById(bookId);

        if (matchingBooks.size() > 0) {
            Book matchedBook = matchingBooks.get(0);

            if (!matchedBook.isBorrowed() && !matchedBook.isReserved()) {
                matchingBooks.get(0).setReserved(true);
                matchingBooks.get(0).setReserved(true);
                resultMessage = Messages.SUCCESS_RESERVED;
                context.createProducer().send(queue, Messages.SUCCESS_RESERVED);
            } else if (matchedBook.isReserved()) {
                resultMessage = Messages.ALREADY_RESERVED;
                context.createProducer().send(queue, Messages.ALREADY_RESERVED);
            } else {
                resultMessage = Messages.ALREADY_BORROWED;
                context.createProducer().send(queue, Messages.ALREADY_BORROWED);
            }
        } else {
            resultMessage = Messages.BOOK_NOT_FOUND;
            context.createProducer().send(queue, Messages.BOOK_NOT_FOUND);
        }
    }

    @Override
    public Books getBooks() {
        return books;
    }

    @Override
    public String getResultMessage() {
        return resultMessage;
    }

    @Override
    public String getConfirmation() {
        return null;
    }

    public void onMessage(Message rcvMessage) {
        System.out.println("on message!");

        TextMessage msg = null;
        if (rcvMessage instanceof TextMessage) {
            msg = (TextMessage) rcvMessage;
            try {
                setNewMessage(msg);
            } catch (JMSException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public void setJmsMessage(String jmsMessage) {
        System.out.println("setting jmsmsg: " + jmsMessage);
        this.jmsMessage = jmsMessage;
    }

    private void setNewMessage(TextMessage msg) throws JMSException, IOException {
        this.jmsMessage = msg.getText();
        Files.write(Paths.get("C:\\tmp\\tmp.txt"), this.jmsMessage.getBytes());
        System.out.println("set message to!" + this.jmsMessage);
    }

    public String getJmsMessage() {
        System.out.println("getting jmsmsg: " + jmsMessage);

        try {
            jmsMessage = new String(Files.readAllBytes(Paths.get("C:\\tmp\\tmp.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jmsMessage;
    }
}
