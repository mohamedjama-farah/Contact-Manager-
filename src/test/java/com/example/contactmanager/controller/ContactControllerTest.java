package com.example.contactmanager.controller;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
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
        List<Contact> contacts = Arrays.asList(new Contact("Mohamed", "0039123456789", "mohamed@example.com"));
        when(repository.findAll()).thenReturn(contacts);
        controller.allContacts();
        verify(view).showAllContacts(contacts);
    }

    @Test
    public void testAddContactShouldSaveToRepositoryAndUpdateView() {
        controller.addContact("Mohamed", "0039123456789", "mohamed@example.com");
        verify(repository).save(any(Contact.class));
        verify(view).contactAdded(any(Contact.class));
    }

    @Test
    public void testDeleteContactShouldDeleteFromRepositoryAndUpdateView() {
        controller.deleteContact("1");
        verify(repository).delete("1");
        verify(view).contactDeleted("1");
    }
    @Test
    public void testUpdateContactShouldSaveToRepositoryAndUpdateView() {
        Contact existing = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        existing.setId("1");
        when(repository.findById("1")).thenReturn(existing);

        controller.updateContact("1", "NewName", "000111222", "new@example.com");

        verify(repository).update(any(Contact.class));
        verify(view).contactUpdated(any(Contact.class));
    }

    @Test
    public void testUpdateContactWhenNotFoundShouldShowError() {
        when(repository.findById("99")).thenReturn(null);

        controller.updateContact("99", "X", "Y", "Z");

        verify(view).showError("No existing contact with id 99");
        verify(repository, never()).update(any(Contact.class));
        verify(view, never()).contactUpdated(any(Contact.class));
    }
}
