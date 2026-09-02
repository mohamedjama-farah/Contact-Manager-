package com.example.contactmanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

public class ContactTest {

    @Test
    public void testConstructorAndGetters() {
        Contact contact = new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        assertThat(contact.getId()).isEqualTo("1");
        assertThat(contact.getName()).isEqualTo("Mohamed");
        assertThat(contact.getPhone()).isEqualTo("0039123456789");
        assertThat(contact.getEmail()).isEqualTo("mohamed@example.com");
    }

    @Test
    public void testToString() {
        Contact contact = new Contact("1", "Mohamed", "0039123456789", "mohamed@example.com");
        assertThat(contact.toString())
            .isEqualTo("Contact [id=1, name=Mohamed, phone=0039123456789, email=mohamed@example.com]");
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(Contact.class).verify();
    }
}
