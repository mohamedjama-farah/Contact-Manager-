package com.example.contactmanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ContactGroupTest {

    @Test
    public void testContactGroupHasName() {
        ContactGroup group = new ContactGroup("Family");
        assertThat(group.getName()).isEqualTo("Family");
    }

    @Test
    public void testContactGroupCanAddContact() {
        ContactGroup group = new ContactGroup("Family");
        Contact contact = new Contact("Mohamed", "0039123456789", "mohamed@example.com");
        group.addContact(contact);
        assertThat(group.getContacts()).contains(contact);
    }
}
