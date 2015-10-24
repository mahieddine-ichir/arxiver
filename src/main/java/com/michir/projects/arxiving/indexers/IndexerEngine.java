package com.michir.projects.arxiving.indexers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import com.michir.projects.arxiving.api.Arxiv;
import com.michir.projects.arxiving.api.IndexDirectory;
import com.michir.projects.arxiving.api.Uploaded;


@ApplicationScoped
public class IndexerEngine {

	@Inject
	private MSWordIndexer msWordIndexer;

	@Inject
	private PDFWordIndexer pdfWordIndexer;

	@Inject
	private IndexWriterConfig indexWriterConfig;

	@Inject
	@IndexDirectory Path indexDir;
	
	@Inject
	private Logger logger;

	public void onDocumentUploaded(@Observes @Uploaded Arxiv arxiv) throws InvalidFormatException, IOException, OpenXML4JException, XmlException {

		try (
				FSDirectory fsDirectory = FSDirectory.open(indexDir);
				IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
				) {

			String name = arxiv.getName();
			Path path = Paths.get(arxiv.getPath());
			logger.info("Index arxiv "+arxiv.getPath()+", "+name);

			if (name.endsWith("docx") || name.endsWith(".doc")) {
				msWordIndexer.index(path, indexWriter);
			} else if (name.endsWith("pdf")) {
				pdfWordIndexer.index(path, indexWriter);
			} else {
				throw new IllegalArgumentException("path type not yet supported "+name);
			}
		}
	}

}
