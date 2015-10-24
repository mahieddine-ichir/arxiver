package com.michir.projects.arxiving.bootstrap;

import java.net.UnknownHostException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@ApplicationScoped
public class MongoStarter  {

	@Produces
    public MongoClient mongoClient()  {
    	try {
			return new MongoClient("127.0.0.1", 27017);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
    }
	
	@Produces
	public DB db(MongoClient mongoClient) {
		return mongoClient.getDB("arxives");
	}

	@Produces
	public DBCollection dbCollection(DB db) {
		return db.getCollection("arxivs");
	}
}
