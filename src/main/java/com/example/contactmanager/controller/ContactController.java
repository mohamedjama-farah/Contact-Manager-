package com.example.contactmanager.controller;

import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.ContactRepository;
import com.example.contactmanager.view.ContactView;
import java.util.List;

public class ContactController {

    private ContactRepository repository;
    private ContactView view;

    public ContactController(ContactRepository repository, ContactView view) {
        this.repository = repository;
        this.view = view;
    }

    public void allContacts() {
        List<Contact> contacts = repository.findAll();
        view.showAllContacts(contacts);
    }

    public void addContact(String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        repository.save(contact);
        view.contactAdded(contact);
    }

    public void deleteContact(String id) {
        repository.delete(id);
        view.contactDeleted(id);
    }
}
