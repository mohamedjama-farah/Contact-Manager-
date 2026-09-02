package com.example.contactmanager.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class ContactManagerE2E {

    @ClassRule
    public static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    private FrameFixture window;

    @Before
    public void setUp() {
        try (MongoClient client = MongoClients.create(mongo.getConnectionString())) {
            client.getDatabase("contactmanager").getCollection("contacts").drop();
        }

        application("com.example.contactmanager.app.ContactManagerApp")
            .withArgs(
                "--mongo-host=" + mongo.getHost(),
                "--mongo-port=" + mongo.getFirstMappedPort())
            .start();

        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return "Contact Manager".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(BasicRobot.robotWithCurrentAwtHierarchy());
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    private void addContact(String id, String name, String phone, String email) {
        window.textBox("idField").enterText(id);
        window.textBox("nameField").enterText(name);
        window.textBox("phoneField").enterText(phone);
        window.textBox("emailField").enterText(email);
        window.button("addButton").click();
    }

    @Test
    public void testAddContact() {
        addContact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        assertThat(window.list("contactList").contents()[0]).contains("1", "Mohamed");
    }

    @Test
    public void testAddAndDeleteContact() {
        addContact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        window.list("contactList").selectItem(0);
        window.button("deleteButton").click();
        window.list("contactList").requireItemCount(0);
    }

    @Test
    public void testAddAndUpdateContact() {
        addContact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        window.list("contactList").selectItem(0);
        window.textBox("nameField").setText("Updated");
        window.textBox("phoneField").setText("000111222");
        window.textBox("emailField").setText("updated@example.com");
        window.button("updateButton").click();
        window.list("contactList").requireItemCount(1);
        assertThat(window.list("contactList").contents()[0]).contains("Updated");
    }
}
