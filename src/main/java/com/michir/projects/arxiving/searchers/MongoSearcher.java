package com.michir.projects.arxiving.searchers;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import com.michir.projects.arxiving.api.Arxiv;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@ApplicationScoped
public class MongoSearcher {

	@Inject
	Logger logger;
	
	@Inject
	private DBCollection collection;
	
	public Arxiv findById(String id) {
				
		DBObject o = new BasicDBObject();
		o.put("_id", new ObjectId(id));
		DBObject findOne = collection.findOne(o);
		logger.info("Found for "+id+" "+findOne);
		
		Arxiv arxiv = new Arxiv();
		arxiv.setId(findOne.get("_id").toString());
		arxiv.setName(findOne.get("name").toString());
		arxiv.setPath(findOne.get("path").toString());
		
		return arxiv;
	}
	
	public Collection<Arxiv> findByPath(Collection<String> paths) {
		BasicDBList basicDBList = new BasicDBList();
		basicDBList.addAll(paths);
		DBObject ref = new BasicDBObject("path", new BasicDBObject("$in", basicDBList));
		
		Collection<Arxiv> arxivs = new HashSet<>();
		collection.find(ref).forEach(e -> {
			Arxiv arxiv = new Arxiv();

			arxiv.setId(e.get("_id").toString());
			arxiv.setPath(e.get("path").toString());
			arxiv.setName(e.get("name").toString());
			arxiv.setDate((Date) e.get("date"));
			
			arxivs.add(arxiv);
		});
		return arxivs;
	}
}
