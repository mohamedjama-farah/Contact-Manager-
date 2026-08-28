package com.example.contactmanager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;
import com.example.contactmanager.model.Contact;
import java.util.List;

public class MongoContactRepositoryIT {

    @ClassRule
    public static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    private MongoContactRepository repository;

    @Before
    public void setUp() {
        repository = new MongoContactRepository(
            mongo.getConnectionString(), "contactmanager", "contacts");
    }

    @Test
    public void testSaveAndFindAll() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        repository.save(contact);
        List<Contact> contacts = repository.findAll();
        assertThat(contacts).hasSize(1);
        assertThat(contacts.get(0).getName()).isEqualTo("Mohamed");
        assertThat(contacts.get(0).getPhone()).isEqualTo("0039123456789");
        assertThat(contacts.get(0).getEmail()).isEqualTo("mohamed@example.com");
    }

    @Test
    public void testDeleteContact() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        repository.save(contact);
        String id = repository.findAll().get(0).getId();
        repository.delete(id);
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void testSaveAssignsGeneratedId() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        repository.save(contact);
        assertThat(contact.getId()).isNotNull();
    }

    @Test
    public void testFindByIdReturnsMatchingContact() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        repository.save(contact);
        Contact found = repository.findById(contact.getId());
        assertThat(found.getName()).isEqualTo("Mohamed");
        assertThat(found.getId()).isEqualTo(contact.getId());
    }

    @Test
    public void testFindByIdReturnsNullWhenNotFound() {
        assertThat(repository.findById(new org.bson.types.ObjectId().toString())).isNull();
    }

    @Test
    public void testUpdateChangesExistingContact() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        repository.save(contact);
        Contact edit = new Contact("NewName", "000111222", "new@example.com");
        edit.setId(contact.getId());
        repository.update(edit);
        Contact reloaded = repository.findById(contact.getId());
        assertThat(reloaded.getName()).isEqualTo("NewName");
        assertThat(reloaded.getPhone()).isEqualTo("000111222");
        assertThat(reloaded.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    public void testRepositoryCanBeClosed() {
        MongoContactRepository repo = new MongoContactRepository(
            mongo.getConnectionString(), "contactmanager", "close_test");
        assertThat(repo).isNotNull();
        repo.close();
    }
}
