package com.example.contactmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.model.Contact;
import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.util.List;

public class ContactFrameTest {

    private FrameFixture window;
    private ContactFrame frame;
    @Mock private ContactController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        frame = new ContactFrame(controller);
        window = new FrameFixture(frame);
        window.show();
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testShowAllContactsDisplaysContactsInList() {
        List<Contact> contacts = Arrays.asList(new Contact("Mohamed", "0039123456789", "mohamed@example.com"));
        frame.showAllContacts(contacts);
        window.list("contactList").requireItemCount(1);
        assertThat(window.list("contactList").contents()[0]).contains("Mohamed");
    }

    @Test
    public void testShowErrorDisplaysDialog() {
        SwingUtilities.invokeLater(() -> frame.showError("Test error"));
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane()
            .withTimeout(2000)
            .using(window.robot());
        assertThat(optionPane).isNotNull();
        optionPane.requireMessage("Test error").okButton().click();
    }

    @Test
    public void testAddButtonCallsController() {
        window.textBox("nameField").enterText("Mohamed");
        window.textBox("phoneField").enterText("0039123456789");
        window.textBox("emailField").enterText("mohamedexample.com");
        window.button("addButton").click();
        verify(controller).addContact("Mohamed", "0039123456789", "mohamedexample.com");
    }

    @Test
    public void testDeleteButtonCallsController() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        contact.setId("1");
        frame.showAllContacts(Arrays.asList(contact));
        window.list("contactList").selectItem(0);
        window.button("deleteButton").click();
        verify(controller).deleteContact("1");
    }

    @Test
    public void testDeleteButtonDoesNothingWhenNoItemSelected() {
        window.button("deleteButton").click();
        verify(controller, never()).deleteContact(anyString());
    }
}
