package com.michir.projects.arxiving.core;

import java.nio.file.Path;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

@ApplicationScoped
public class Arxiver {

	@Inject
	private DBCollection dbCollection;
	
	@Inject
	private Logger logger;
	
	public void arxiv(Path path, String filename, Collection<String> tags) {
		
		logger.info("Saving arxiv into Mongo with path "+path+", filename "+filename);
		
		BasicDBObject basicDBObject = new BasicDBObject()
			.append("name", filename)
			.append("path", path.toFile().getAbsolutePath())
			.append("date", Calendar.getInstance().getTime());
		
		dbCollection.insert(basicDBObject);
	}
	
}
