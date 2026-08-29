package com.example.contactmanager.app;

import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.repository.MongoContactRepository;
import com.example.contactmanager.view.swing.ContactFrame;
import java.util.concurrent.Callable;
import javax.swing.SwingUtilities;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@CommandLine.Command(name = "contact-manager", mixinStandardHelpOptions = true)
public class ContactManagerApp implements Callable<Void> {

    @Option(names = { "--mongo-host" }, description = "MongoDB host")
    private String mongoHost = "localhost";

    @Option(names = { "--mongo-port" }, description = "MongoDB port")
    private int mongoPort = 27017;

    @Option(names = { "--db-name" }, description = "Database name")
    private String databaseName = "contactmanager";

    @Option(names = { "--collection-name" }, description = "Collection name")
    private String collectionName = "contacts";

    public static void main(String[] args) {
        new CommandLine(new ContactManagerApp()).execute(args);
    }

    @Override
    public Void call() throws Exception {
        String connectionString = "mongodb://" + mongoHost + ":" + mongoPort;
        MongoContactRepository repository = new MongoContactRepository(
            connectionString, databaseName, collectionName);

        SwingUtilities.invokeLater(() -> {
            ContactFrame frame = new ContactFrame(null);
            ContactController controller = new ContactController(repository, frame);
            frame.setController(controller);
            frame.setVisible(true);
            controller.allContacts();
        });
        return null;
    }
}
