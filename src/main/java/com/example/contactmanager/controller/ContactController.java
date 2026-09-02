package com.example.contactmanager.controller;

import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.ContactRepository;
import com.example.contactmanager.view.ContactView;

public class ContactController {

    private ContactRepository repository;
    private ContactView view;

    public ContactController(ContactRepository repository, ContactView view) {
        this.repository = repository;
        this.view = view;
    }

    public void allContacts() {
        view.showAllContacts(repository.findAll());
    }

    public void addContact(Contact contact) {
        Contact existing = repository.findById(contact.getId());
        if (existing != null) {
            view.showError("Already existing contact with id " + contact.getId());
            return;
        }
        repository.save(contact);
        view.contactAdded(contact);
    }

    public void updateContact(Contact contact) {
        Contact existing = repository.findById(contact.getId());
        if (existing == null) {
            view.showError("No existing contact with id " + contact.getId());
            return;
        }
        repository.update(contact);
        view.contactUpdated(contact);
    }

    public void deleteContact(String id) {
        Contact existing = repository.findById(id);
        if (existing == null) {
            view.showError("No existing contact with id " + id);
            return;
        }
        repository.delete(id);
        view.contactDeleted(id);
    }
}
