package com.example.contactmanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ContactTest {

    @Test
    public void testContactHasNameAndPhone() {
        Contact contact = new Contact("Mohamed", "+39123456789");
        assertThat(contact.getName()).isEqualTo("Mohamed");
        assertThat(contact.getPhone()).isEqualTo("+39123456789");
    }

}