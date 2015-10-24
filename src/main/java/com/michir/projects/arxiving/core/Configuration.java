package com.michir.projects.arxiving.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.michir.projects.arxiving.api.ArxivDirectory;
import com.michir.projects.arxiving.api.IndexDirectory;
import com.michir.projects.arxiving.api.PublishDirectory;

@ApplicationScoped
public class Configuration {

	private Properties properties = new Properties();
	
	@PostConstruct
	public void start() {
		String filePath = System.getProperty("arxiver.configuration");
		Path path = Paths.get(filePath);
		if (!path.toFile().exists()) {
			throw new RuntimeException(path.toString()+" not found");
		}
		Logger.getLogger(Configuration.class.getName()).info("++++ loading file "+filePath);
		try (FileInputStream is = new FileInputStream(filePath)) {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Produces
	@ArxivDirectory
	public Path arxivDirectory() {
		return Paths.get(properties.getProperty("arxiv.folder"));
	}

	@Produces
	@IndexDirectory
	public Path indexDirectory() {
		return Paths.get(properties.getProperty("index.folder"));
	}

	@Produces
	@PublishDirectory
	public Path publishDirectory() {
		return Paths.get(properties.getProperty("public.folder"));
	}
}
