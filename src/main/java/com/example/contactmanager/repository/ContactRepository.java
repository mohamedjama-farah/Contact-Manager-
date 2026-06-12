package com.example.contactmanager.repository;

import com.example.contactmanager.model.Contact;
import java.util.List;

public interface ContactRepository {
	
	void save(Contact contact);
	List<Contact> findAll();
	void delete(String id );
	

}
