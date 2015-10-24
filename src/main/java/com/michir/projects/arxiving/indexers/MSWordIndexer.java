package com.michir.projects.arxiving.indexers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

@ApplicationScoped
public class MSWordIndexer {

	@Inject
	private Logger logger;

	public void index(Path file, IndexWriter indexWriter) throws IOException, InvalidFormatException, OpenXML4JException, XmlException {

		logger.info("Indexing MS Word document "+file);
		String text = ExtractorFactory.createExtractor(file.toFile()).getText();

		Document document = new Document();
		document.add(new StoredField("name", file.toFile().getName()));
		document.add(new StoredField("path", file.toFile().getAbsolutePath()));
		document.add(new Field("content", text, org.apache.lucene.document.TextField.TYPE_NOT_STORED));

		indexWriter.addDocument(document);
	}
}
