package com.michir.projects.arxiving.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.michir.projects.arxiving.api.Arxiv;
import com.michir.projects.arxiving.searchers.LuceneIndexSearcher;
import com.michir.projects.arxiving.searchers.MongoSearcher;


@Path("/search")
public class Search {

	@Inject
	private LuceneIndexSearcher luceneSearcher;
	
	@Inject
	private MongoSearcher mongoSearcher;
	
	@Inject
	private Logger logger;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Arxiv> doGet(@NotNull @QueryParam("tags") String tags)
			throws ServletException, IOException {

		// get Paths from lucene
		Collection<String> paths = new ArrayList<>();
		luceneSearcher.search(tags)
			.forEach(e -> paths.add(e.getPath()));
		
		logger.info("found "+paths);
		
		// search on Mongo
		return mongoSearcher.findByPath(paths);
	}
}
