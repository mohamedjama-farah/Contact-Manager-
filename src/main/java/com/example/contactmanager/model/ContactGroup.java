package com.example.contactmanager.model;
import java.util.ArrayList;
import java.util.List;

public class ContactGroup {
	
	private String name;
	private List<Contact> contacts;
	
	public ContactGroup(String name) {
		this.name = name;
		this.contacts = new ArrayList<>();
	}
	public String getName() {
		return name;
}
	public void addContact(Contact contact) {
		contacts.add(contact);
	}
	public List<Contact> getContacts(){
		return contacts;
	}

}
