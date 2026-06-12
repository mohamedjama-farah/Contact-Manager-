package com.example.contactmanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ContactTest {

    @Test
    public void testContactHasNamePhoneAndEmail() {
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        assertThat(contact.getName()).isEqualTo("Mohamed");
        assertThat(contact.getPhone()).isEqualTo("0039123456789");
        assertThat(contact.getEmail()).isEqualTo("mohamed@example.com");
    }
}
