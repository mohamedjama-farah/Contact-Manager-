package com.example.contactmanager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.MongoContactRepository;
import com.example.contactmanager.view.ContactView;

public class ContactControllerIT {

    @ClassRule
    public static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @Mock
    private ContactView view;

    private AutoCloseable closeable;
    private MongoContactRepository repository;
    private ContactController controller;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        repository = new MongoContactRepository(
            mongo.getConnectionString(), "contactmanager", "contacts");
        for (Contact c : repository.findAll()) {
            repository.delete(c.getId());
        }
        controller = new ContactController(repository, view);
    }

    @After
    public void tearDown() throws Exception {
        repository.close();
        closeable.close();
    }

    @Test
    public void testAddContactPersistsToRepository() {
        controller.addContact(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        assertThat(repository.findById("1"))
            .isEqualTo(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
    }

    @Test
    public void testDeleteContactRemovesFromRepository() {
        repository.save(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        controller.deleteContact("1");
        assertThat(repository.findById("1")).isNull();
    }

    @Test
    public void testUpdateContactModifiesRepository() {
        repository.save(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        controller.updateContact(new Contact("1", "NewName", "000111222", "new@example.com"));
        assertThat(repository.findById("1"))
            .isEqualTo(new Contact("1", "NewName", "000111222", "new@example.com"));
    }
}
