package com.example.contactmanager.e2e;

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

    @Test
    public void testAddAndDeleteContact() {
        window.textBox("nameField").enterText("Mohamed");
        window.textBox("phoneField").enterText("0039123456789");
        window.textBox("emailField").enterText("mohamed@example.com");
        window.button("addButton").click();
        window.list("contactList").requireItemCount(1);
        window.list("contactList").selectItem(0);
        window.button("deleteButton").click();
        window.list("contactList").requireItemCount(0);
    }
}
