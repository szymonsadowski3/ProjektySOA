package serv.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import serv.constants.Constants;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Singleton
@Startup
public class BookXmlParser {

    public List<Book> getBookList() {
        Document doc = null;
        try {
            doc = getDocumentFromFile(new File("C:\\Users\\Szymon\\IdeaProjects\\lab5\\LibraryRemote\\src\\main\\resources\\library.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getBookListFromDocument(doc);
    }

    private Document getDocumentFromFile(File file) throws Exception {
        Document document = prepareDocument(file);
        document.getDocumentElement().normalize();

        return document;
    }

    private Document prepareDocument(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder.parse(file);
    }

    private List<Book> getBookListFromDocument(Document doc) {
        NodeList nodes = doc.getElementsByTagName(Constants.BOOK);

        List<Book> bookList = new ArrayList<>();

        for (Node node : iterable(nodes)) {
            Element element = (Element) node;

            String bookAuthor = getElementByKey(element, Constants.AUTHOR);
            String bookTitle = getElementByKey(element, Constants.TITLE);
            String bookId = getElementByKey(element, Constants.ID);
            String isbn = getElementByKey(element, Constants.ISBN);

            bookList.add(new Book(Integer.parseInt(bookId), bookAuthor, bookTitle, isbn));
        }

        return bookList;
    }

    private String getElementByKey(Element element, String author) {
        return element
                .getElementsByTagName(author)
                .item(0)
                .getTextContent();
    }

    public Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }
}
