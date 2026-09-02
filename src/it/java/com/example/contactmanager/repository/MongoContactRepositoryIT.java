package com.example.contactmanager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
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
        for (Contact c : repository.findAll()) {
            repository.delete(c.getId());
        }
    }

    @After
    public void tearDown() {
        repository.close();
    }

    @Test
    public void testSaveAndFindAll() {
        repository.save(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        List<Contact> contacts = repository.findAll();
        assertThat(contacts)
            .containsExactly(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
    }

    @Test
    public void testFindByIdWhenContactExists() {
        repository.save(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        assertThat(repository.findById("1"))
            .isEqualTo(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
    }

    @Test
    public void testFindByIdWhenContactDoesNotExist() {
        assertThat(repository.findById("nonexistent")).isNull();
    }

    @Test
    public void testUpdate() {
        repository.save(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        repository.update(new Contact("1", "NewName", "000111222", "new@example.com"));
        assertThat(repository.findById("1"))
            .isEqualTo(new Contact("1", "NewName", "000111222", "new@example.com"));
    }

    @Test
    public void testDelete() {
        repository.save(new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com"));
        repository.delete("1");
        assertThat(repository.findAll()).isEmpty();
    }
}
