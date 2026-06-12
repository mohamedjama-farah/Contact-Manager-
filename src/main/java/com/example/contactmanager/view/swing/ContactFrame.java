package com.example.contactmanager.view.swing;

import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.view.ContactView;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContactFrame extends JFrame implements ContactView {

    private volatile ContactController controller;
    private JList<String> contactList;
    private DefaultListModel<String> listModel;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton addButton;
    private JButton deleteButton;
    private List<Contact> currentContacts;

    public ContactFrame(ContactController initialController) {
        this.controller = initialController;
        setTitle("Contact Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setName("contactList");
        add(new JScrollPane(contactList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
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

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            controller.addContact(nameField.getText(), phoneField.getText(), emailField.getText());
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int index = contactList.getSelectedIndex();
            if (index != -1) {
                controller.deleteContact(currentContacts.get(index).getId());
            }
        });
    }

    public void setController(ContactController controller) {
        this.controller = controller;
    }

    public void showAllContacts(List<Contact> contacts) {
        this.currentContacts = contacts;
        listModel.clear();
        for (Contact c : contacts) {
            listModel.addElement(c.getName() + " - " + c.getPhone() + " - " + c.getEmail());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public void contactAdded(Contact contact) {
        controller.allContacts();
    }

    public void contactDeleted(String id) {
        controller.allContacts();
    }
}
