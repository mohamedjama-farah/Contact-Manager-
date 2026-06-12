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
}
