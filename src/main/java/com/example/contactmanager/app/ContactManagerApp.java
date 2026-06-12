package com.example.contactmanager.app;

import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.repository.MongoContactRepository;
import com.example.contactmanager.view.swing.ContactFrame;
import javax.swing.SwingUtilities;

public class ContactManagerApp {

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017";
        String databaseName = "contactmanager";
        String collectionName = "contacts";

        MongoContactRepository repository = new MongoContactRepository(
            connectionString, databaseName, collectionName
        );

        SwingUtilities.invokeLater(() -> {
            ContactFrame frame = new ContactFrame(null);
            ContactController controller = new ContactController(repository, frame);
            frame.setVisible(true);
            controller.allContacts();
        });
    }
}