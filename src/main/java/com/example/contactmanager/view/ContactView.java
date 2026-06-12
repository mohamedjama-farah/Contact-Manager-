package com.example.contactmanager.view;

import com.example.contactmanager.model.Contact;
import java.util.List;

public interface ContactView {
	
	void showAllContacts(List<Contact> contacts);
	void showError(String message);
	void contactAdded(Contact contact);
	void contactDeleted(String id);

}
