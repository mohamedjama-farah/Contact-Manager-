package com.example.contactmanager.view.swing;

import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.view.ContactView;
import javax.swing.*;
import javax.swing.WindowConstants;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContactFrame extends JFrame implements ContactView {

    private static final long serialVersionUID = 1L;

    private final AtomicReference<ContactController> controllerRef = new AtomicReference<>();
    private JList<String> contactList;
    private DefaultListModel<String> listModel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private transient List<Contact> currentContacts;

    public ContactFrame(ContactController initialController) {
        controllerRef.set(initialController);
        setTitle("Contact Manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setName("contactList");
        add(new JScrollPane(contactList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        idField = new JTextField();
        idField.setName("idField");
        nameField = new JTextField();
        nameField.setName("nameField");
        phoneField = new JTextField();
        phoneField.setName("phoneField");
        emailField = new JTextField();
        emailField.setName("emailField");
        addButton = new JButton("Add");
        addButton.setName("addButton");
        deleteButton = new JButton("Delete");
        deleteButton.setName("deleteButton");
        deleteButton.setEnabled(false);
        updateButton = new JButton("Update");
        updateButton.setName("updateButton");
        updateButton.setEnabled(false);

        inputPanel.add(new JLabel("Id:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(updateButton);
        add(inputPanel, BorderLayout.SOUTH);

        contactList.addListSelectionListener(e -> {
            boolean selected = contactList.getSelectedIndex() != -1;
            deleteButton.setEnabled(selected);
            updateButton.setEnabled(selected);
        });

        addButton.addActionListener(e -> {
            controllerRef.get().addContact(new Contact(
                idField.getText(),
                nameField.getText(),
                phoneField.getText(),
                emailField.getText()));
            idField.setText("");
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
        });

        deleteButton.addActionListener(e ->
            controllerRef.get().deleteContact(
                currentContacts.get(contactList.getSelectedIndex()).getId()));

        updateButton.addActionListener(e ->
            controllerRef.get().updateContact(new Contact(
                currentContacts.get(contactList.getSelectedIndex()).getId(),
                nameField.getText(),
                phoneField.getText(),
                emailField.getText())));
    }

    public void setController(ContactController controller) {
        this.controllerRef.set(controller);
    }

    public void showAllContacts(List<Contact> contacts) {
        this.currentContacts = contacts;
        listModel.clear();
        for (Contact c : contacts) {
            listModel.addElement(
                c.getId() + " - " + c.getName() + " - " + c.getPhone() + " - " + c.getEmail());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public void contactAdded(Contact contact) {
        controllerRef.get().allContacts();
    }

    public void contactDeleted(String id) {
        controllerRef.get().allContacts();
    }

    public void contactUpdated(Contact contact) {
        controllerRef.get().allContacts();
    }
}
