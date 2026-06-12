package com.example.contactmanager.view.swing;

import com.example.contactmanager.controller.ContactController;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.view.ContactView;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContactFrame extends JFrame implements ContactView {

    private ContactController controller;
    private JList<String> contactList;
    private DefaultListModel<String> listModel;
    private JTextField nameField;
    private JTextField phoneField;
    private JButton addButton;
    private JButton deleteButton;
    private List<Contact> currentContacts;

    public ContactFrame(ContactController controller) {
        this.controller = controller;
        setTitle("Contact Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setName("contactList");
        add(new JScrollPane(contactList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        nameField = new JTextField();
        nameField.setName("nameField");
        phoneField = new JTextField();
        phoneField.setName("phoneField");
        addButton = new JButton("Add");
        addButton.setName("addButton");
        deleteButton = new JButton("Delete");
        deleteButton.setName("deleteButton");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            controller.addContact(nameField.getText(), phoneField.getText());
            nameField.setText("");
            phoneField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int index = contactList.getSelectedIndex();
            if (index != -1) {
                controller.deleteContact(currentContacts.get(index).getId());
            }
        });
    }

    @Override
    public void showAllContacts(List<Contact> contacts) {
        this.currentContacts = contacts;
        listModel.clear();
        for (Contact c : contacts) {
            listModel.addElement(c.getName() + " - " + c.getPhone());
        }
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void contactAdded(Contact contact) {
        controller.allContacts();
    }

    @Override
    public void contactDeleted(String id) {
        controller.allContacts();
    }
}