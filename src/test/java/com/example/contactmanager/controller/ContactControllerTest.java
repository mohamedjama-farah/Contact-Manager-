package com.example.contactmanager.controller;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.ContactRepository;
import com.example.contactmanager.view.ContactView;
import java.util.Arrays;
import java.util.List;

public class ContactControllerTest {

    @Mock private ContactRepository repository;
    @Mock private ContactView view;
    private ContactController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ContactController(repository, view);
    }

    @Test
    public void testAllContactsShouldShowAllContactsFromRepository() {
        List<Contact> contacts = Arrays.asList(
            new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        when(repository.findAll()).thenReturn(contacts);
        controller.allContacts();
        verify(view).showAllContacts(contacts);
    }

    @Test
    public void testAddContactWhenIdDoesNotExistShouldSaveAndNotify() {
        Contact contact = new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        when(repository.findById("1")).thenReturn(null);
        controller.addContact(contact);
        InOrder inOrder = inOrder(repository, view);
        inOrder.verify(repository).save(contact);
        inOrder.verify(view).contactAdded(contact);
    }

    @Test
    public void testAddContactWhenIdAlreadyExistsShouldShowErrorAndNotSave() {
        Contact existing = new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        Contact toAdd = new Contact("1", "Ali", "000111222", "ali@example.com");
        when(repository.findById("1")).thenReturn(existing);
        controller.addContact(toAdd);
        verify(view).showError("Already existing contact with id 1");
        verify(repository, never()).save(any(Contact.class));
        verify(view, never()).contactAdded(any(Contact.class));
    }

    @Test
    public void testUpdateContactWhenIdExistsShouldUpdateAndNotify() {
        Contact existing = new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        Contact updated = new Contact("1", "NewName", "000111222", "new@example.com");
        when(repository.findById("1")).thenReturn(existing);
        controller.updateContact(updated);
        InOrder inOrder = inOrder(repository, view);
        inOrder.verify(repository).update(updated);
        inOrder.verify(view).contactUpdated(updated);
    }

    @Test
    public void testUpdateContactWhenIdDoesNotExistShouldShowErrorAndNotUpdate() {
        Contact updated = new Contact("99", "X", "Y", "Z");
        when(repository.findById("99")).thenReturn(null);
        controller.updateContact(updated);
        verify(view).showError("No existing contact with id 99");
        verify(repository, never()).update(any(Contact.class));
        verify(view, never()).contactUpdated(any(Contact.class));
    }

    @Test
    public void testDeleteContactWhenIdExistsShouldDeleteAndNotify() {
        Contact existing = new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        when(repository.findById("1")).thenReturn(existing);
        controller.deleteContact("1");
        InOrder inOrder = inOrder(repository, view);
        inOrder.verify(repository).delete("1");
        inOrder.verify(view).contactDeleted("1");
    }

    @Test
    public void testDeleteContactWhenIdDoesNotExistShouldShowErrorAndNotDelete() {
        when(repository.findById("99")).thenReturn(null);
        controller.deleteContact("99");
        verify(view).showError("No existing contact with id 99");
        verify(repository, never()).delete(anyString());
        verify(view, never()).contactDeleted(anyString());
    }
}
