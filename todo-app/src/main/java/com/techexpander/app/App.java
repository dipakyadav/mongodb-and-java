package com.techexpander.app;

import java.net.URLEncoder;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import com.mongodb.client.MongoClient;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;


/**
 * App
 *
 */
public class App
{
    public static void main( String[] args )
    {


        // Connect to MongoDB atlas server
        MongoClient mongoClient = MongoClients.create("mongodb+srv://" + URLEncoder.encode("{usename}") + ":" + URLEncoder.encode("{password}") + "@{clusterhost}/");

        // Get ToDo database
        MongoDatabase database = mongoClient.getDatabase("ToDo");

        // Get tasks collection
        MongoCollection<Document> coll = database.getCollection("tasks");

        // Drop all documents from collection.
        coll.drop();

        // Prepare task document to be inserted
        Document doc = new Document("text", "Learn MongoDB")
            .append("owner", "Dipak")
            .append("priority", "high")
            .append("checked", true);
        
        
        // Insert one task document
        coll.insertOne(doc);

        // ArrayList to store list of task docuemnts
        ArrayList<Document> docs = new ArrayList<Document>();
        
        // Add task Document to docs array
        docs.add(
            new Document("text", "Learn Java")
                .append("owner", "Dipak")
                .append("priority", "medium")
                .append("checked", true)
        );

        // Add another task Document to docs array
        docs.add(
            new Document("text", "Release MongoDB 4.0")
                .append("owner", "MongoDB Inc.")
                .append("priority", "high")
                .append("checked", true)
        );

        // Insert multiple task documents
        coll.insertMany(docs);

        // Find first task document
        Document firstDoc = coll.find().first();
        System.out.println(firstDoc.toJson());

        
        // find all task documents.
        for (Document cur : coll.find()) {
            System.out.println(cur.toJson());
        }

        // find all task documents with high priority.
        coll.find(eq("priority", "high"));
        for (Document cur : coll.find()) {
            System.out.println(cur.toJson());
        }

        // Update priority to high for "Learn Java" task
        coll.updateOne(eq("text", "Learn Java"), set("priority", "high"));
         
        // Uncheck all task assigned to Dipak
        UpdateResult updateResult = coll.updateMany(eq("owner", "Dipak"), set("checked", false));
        System.out.println(updateResult.getModifiedCount());
        
        // Delete task assigned to MongoDB Inc.
        coll.deleteOne(eq("owner", "MongoDB Inc."));
        
        // Delete Many
        DeleteResult deleteResult = coll.deleteMany(eq("owner", "Dipak"));
        System.out.println(deleteResult.getDeletedCount());

        // drop task collection.
        coll.drop();

        // drop database
        // Note: use should have permission to dropDatabase
        database.drop();
        
        // Close monogdb connection
        mongoClient.close();
        
    }

    
}
