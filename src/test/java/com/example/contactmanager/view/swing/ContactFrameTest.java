package com.example.contactmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
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
        frame = GuiActionRunner.execute(() -> new ContactFrame(controller));
        window = new FrameFixture(frame);
        window.show();
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    private Contact contact(String id) {
        return new Contact(id, "Mohamed", "0039123456789", "mohamed@example.com");
    }

    private void showContact(Contact c) {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() {
                frame.showAllContacts(Arrays.asList(c));
            }
        });
    }

    @Test
    public void testShowAllContactsDisplaysContactsInList() {
        List<Contact> contacts = Arrays.asList(contact("1"));
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() {
                frame.showAllContacts(contacts);
            }
        });
        window.list("contactList").requireItemCount(1);
        assertThat(window.list("contactList").contents()[0]).contains("1", "Mohamed");
    }

    @Test
    public void testShowErrorDisplaysDialog() {
        SwingUtilities.invokeLater(() -> frame.showError("Test error"));
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane()
            .withTimeout(2000)
            .using(window.robot());
        optionPane.requireMessage("Test error").okButton().click();
    }

    @Test
    public void testAddButtonDelegatesToController() {
        window.textBox("idField").enterText("1");
        window.textBox("nameField").enterText("Mohamed");
        window.textBox("phoneField").enterText("0039123456789");
        window.textBox("emailField").enterText("mohamed@example.com");
        window.button("addButton").click();
        verify(controller).addContact(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
    }

    @Test
    public void testDeleteButtonDelegatesToController() {
        showContact(contact("1"));
        window.list("contactList").selectItem(0);
        window.button("deleteButton").click();
        verify(controller).deleteContact("1");
    }

    @Test
    public void testUpdateButtonDelegatesToController() {
        showContact(contact("1"));
        window.list("contactList").selectItem(0);
        window.textBox("nameField").setText("NewName");
        window.textBox("phoneField").setText("000111222");
        window.textBox("emailField").setText("new@example.com");
        window.button("updateButton").click();
        verify(controller).updateContact(new Contact("1", "NewName", "000111222", "new@example.com"));
    }

    @Test
    public void testDeleteAndUpdateButtonsAreDisabledInitially() {
        window.button("deleteButton").requireDisabled();
        window.button("updateButton").requireDisabled();
    }

    @Test
    public void testDeleteAndUpdateButtonsAreEnabledWhenAContactIsSelected() {
        showContact(contact("1"));
        window.list("contactList").selectItem(0);
        window.button("deleteButton").requireEnabled();
        window.button("updateButton").requireEnabled();
    }

    @Test
    public void testContactAddedRefreshesContacts() {
        frame.contactAdded(contact("1"));
        verify(controller).allContacts();
    }

    @Test
    public void testContactDeletedRefreshesContacts() {
        frame.contactDeleted("1");
        verify(controller).allContacts();
    }

    @Test
    public void testContactUpdatedRefreshesContacts() {
        frame.contactUpdated(contact("1"));
        verify(controller).allContacts();
    }
}
