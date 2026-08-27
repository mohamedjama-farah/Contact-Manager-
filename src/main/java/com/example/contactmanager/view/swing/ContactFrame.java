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
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton addButton;
    private JButton deleteButton;
    private transient List<Contact> currentContacts;

    public ContactFrame(ContactController initialController) {
        controllerRef.set(initialController);
        setTitle("Contact Manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
            controllerRef.get().addContact(nameField.getText(), phoneField.getText(), emailField.getText());
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int index = contactList.getSelectedIndex();
            if (index != -1) {
                controllerRef.get().deleteContact(currentContacts.get(index).getId());
            }
        });
    }

    public void setController(ContactController controller) {
        this.controllerRef.set(controller);
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
        controllerRef.get().allContacts();
    }

    public void contactDeleted(String id) {
        controllerRef.get().allContacts();
    }
}
