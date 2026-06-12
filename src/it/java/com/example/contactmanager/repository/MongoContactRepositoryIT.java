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
    public static MongoDBContainer mongo = 
        new MongoDBContainer("mongo:6.0");

    private MongoContactRepository repository;

    @Before
    public void setUp() {
        repository = new MongoContactRepository(
            mongo.getConnectionString(),
            "contactmanager",
            "contacts"
        );
    }

    @Test
    public void testSaveAndFindAll() {
        Contact contact = new Contact("Mohamed", "+39123456789");
        repository.save(contact);
        List<Contact> contacts = repository.findAll();
        assertThat(contacts).hasSize(1);
        assertThat(contacts.get(0).getName()).isEqualTo("Mohamed");
        assertThat(contacts.get(0).getPhone()).isEqualTo("+39123456789");
    }

    @Test
    public void testDeleteContact() {
        Contact contact = new Contact("Mohamed", "+39123456789");
        repository.save(contact);
        String id = repository.findAll().get(0).getId();
        repository.delete(id);
        assertThat(repository.findAll()).isEmpty();
    }
}