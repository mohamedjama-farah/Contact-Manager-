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
        if (name == null || name.trim().isEmpty()) {
            view.showError("Name is required");
            return;
        }
        for (Contact existing : repository.findAll()) {
            if (existing.getPhone().equals(phone)) {
                view.showError("A contact with phone " + phone + " already exists");
                return;
            }
        }
        Contact contact = new Contact(name, phone, email);
        repository.save(contact);
        view.contactAdded(contact);
    }

    public void deleteContact(String id) {
        repository.delete(id);
        view.contactDeleted(id);
    }

    public void updateContact(String id, String name, String phone, String email) {
        Contact existing = repository.findById(id);
        if (existing == null) {
            view.showError("No existing contact with id " + id);
            return;
        }
        Contact updated = new Contact(name, phone, email);
        updated.setId(id);
        repository.update(updated);
        view.contactUpdated(updated);
    }
}