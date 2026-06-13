package com.example.contactmanager.repository;

import com.example.contactmanager.model.Contact;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class MongoContactRepository implements ContactRepository, Closeable {

    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public MongoContactRepository(String connectionString,
                                   String databaseName,
                                   String collectionName) {
        this.client = MongoClients.create(connectionString);
        MongoDatabase database = this.client.getDatabase(databaseName);
        this.collection = database.getCollection(collectionName);
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public void save(Contact contact) {
        Document doc = new Document("name", contact.getName())
                .append("phone", contact.getPhone())
                .append("email", contact.getEmail());
        collection.insertOne(doc);
        contact.setId(doc.getObjectId("_id").toString());
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        for (Document doc : collection.find()) {
            Contact contact = new Contact(
                doc.getString("name"),
                doc.getString("phone"),
                doc.getString("email")
            );
            contact.setId(doc.getObjectId("_id").toString());
            contacts.add(contact);
        }
        return contacts;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
}
