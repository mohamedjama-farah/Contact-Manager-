package com.example.contactmanager.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;
import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.repository.MongoContactRepository;
import com.example.contactmanager.view.swing.ContactFrame;

public class ContactManagerE2EIT {

    @ClassRule
    public static MongoDBContainer mongo =
        new MongoDBContainer("mongo:6.0");

    private FrameFixture window;
    private MongoContactRepository repository;
    private ContactFrame frame;
    private ContactController controller;

    @Before
    public void setUp() {
        repository = new MongoContactRepository(
            mongo.getConnectionString(),
            "contactmanager",
            "contacts"
        );
        frame = new ContactFrame(null);
        controller = new ContactController(repository, frame);
        frame.setController(controller);
        window = new FrameFixture(frame);
        window.show();
        controller.allContacts();
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testAddAndDeleteContact() {
        window.textBox("nameField").enterText("Mohamed");
        window.textBox("phoneField").enterText("0039123456789");
        window.button("addButton").click();
        window.list("contactList").requireItemCount(1);

        window.list("contactList").selectItem(0);
        window.button("deleteButton").click();
        window.list("contactList").requireItemCount(0);
    }
}