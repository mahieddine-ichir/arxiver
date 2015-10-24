package com.michir.projects.arxiving.endpoints;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.michir.projects.arxiving.api.Arxiv;
import com.michir.projects.arxiving.core.Organizer;
import com.michir.projects.arxiving.searchers.MongoSearcher;

@Path("/arxiv")
public class Download {

	@Inject
	private Organizer organizer;
	
	@Inject
	private MongoSearcher mongo;

	@Context
	private HttpServletResponse response;
	
	@GET
	@Path("/{id}")
	public InputStream arxiv(@PathParam("id") String id) throws FileNotFoundException {
		Arxiv findById = mongo.findById(id);
		java.nio.file.Path path = Paths.get(findById.getPath());
		
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+findById.getName()+"\"");
		return new FileInputStream(path.toFile());
	}
	
}
