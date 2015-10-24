package com.michir.projects.arxiving.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.MethodNotSupportedException;
import javax.naming.OperationNotSupportedException;

import com.michir.projects.arxiving.api.ArxivDirectory;
import com.michir.projects.arxiving.api.PublishDirectory;

@ApplicationScoped
public class Organizer {

	@Inject
	private Logger logger;

	@Inject @ArxivDirectory
	private Path arxivDir;
	
	@Inject @PublishDirectory
	private Path publishDir;

	public Path save(InputStream is) throws IOException {
		Path out = Paths.get(arxivDir.toString(), UUID.randomUUID().toString());
		logger.info("saving archive into "+out);
		Files.copy(is, out);
		return out;
	}
	
	public Path publish(String path) {
		// TODO
		throw new RuntimeException();
//		try (OutputStream os = new FileOutputStream()) {
//			Files.copy(path, os);
//		}
	}
}
