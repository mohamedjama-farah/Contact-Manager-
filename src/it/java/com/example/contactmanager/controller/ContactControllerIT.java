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
        // start each test with an empty collection
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
        controller.addContact("Mohamed", "0039123456789", "mohamed@example.com");

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findAll().get(0).getName()).isEqualTo("Mohamed");
    }

    @Test
    public void testDeleteContactRemovesFromRepository() {
        controller.addContact("Mohamed", "0039123456789", "mohamed@example.com");
        String id = repository.findAll().get(0).getId();

        controller.deleteContact(id);

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void testUpdateContactModifiesRepository() {
        controller.addContact("Mohamed", "0039123456789", "mohamed@example.com");
        String id = repository.findAll().get(0).getId();

        controller.updateContact(id, "NewName", "000111222", "new@example.com");

        Contact reloaded = repository.findById(id);
        assertThat(reloaded.getName()).isEqualTo("NewName");
        assertThat(reloaded.getPhone()).isEqualTo("000111222");
        assertThat(reloaded.getEmail()).isEqualTo("new@example.com");
    }
}
