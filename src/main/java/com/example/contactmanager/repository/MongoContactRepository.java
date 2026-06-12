package com.example.contactmanager.repository;

import com.example.contactmanager.model.Contact;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class MongoContactRepository implements ContactRepository {

    private MongoCollection<Document> collection;

    public MongoContactRepository(String connectionString,
                                   String databaseName,
                                   String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(databaseName);
        this.collection = database.getCollection(collectionName);
    }

    @Override
    public void save(Contact contact) {
        Document doc = new Document("name", contact.getName())
                .append("phone", contact.getPhone());
        collection.insertOne(doc);
        contact.setId(doc.getObjectId("_id").toString());
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        for (Document doc : collection.find()) {
            Contact contact = new Contact(
                doc.getString("name"),
                doc.getString("phone")
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
