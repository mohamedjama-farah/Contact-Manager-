package com.example.contactmanager.repository;

import com.example.contactmanager.model.Contact;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.io.Closeable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        collection.insertOne(new Document()
            .append("id", contact.getId())
            .append("name", contact.getName())
            .append("phone", contact.getPhone())
            .append("email", contact.getEmail()));
    }

    @Override
    public List<Contact> findAll() {
        return StreamSupport
            .stream(collection.find().spliterator(), false)
            .map(this::fromDocumentToContact)
            .collect(Collectors.toList());
    }

    @Override
    public Contact findById(String id) {
        Document doc = collection.find(Filters.eq("id", id)).first();
        if (doc != null) {
            return fromDocumentToContact(doc);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("id", id));
    }

    @Override
    public void update(Contact contact) {
        collection.replaceOne(
            Filters.eq("id", contact.getId()),
            new Document()
                .append("id", contact.getId())
                .append("name", contact.getName())
                .append("phone", contact.getPhone())
                .append("email", contact.getEmail()));
    }

    private Contact fromDocumentToContact(Document doc) {
        return new Contact(
            "" + doc.get("id"),
            "" + doc.get("name"),
            "" + doc.get("phone"),
            "" + doc.get("email"));
    }
}
