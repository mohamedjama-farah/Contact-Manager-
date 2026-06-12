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
		ContactGroup group = new coontectGroup("Family");
		contact contact =new contact("mohamed","+399123456789");
		group.addcontact(contact);
		assertThat(group.getcontacts()).contains(contact);
		
	}
	

}
