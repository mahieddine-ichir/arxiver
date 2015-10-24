package com.michir.projects.arxiving.indexers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import com.snowtide.PDF;
import com.snowtide.pdf.lucene.LucenePDFConfiguration;
import com.snowtide.pdf.lucene.LucenePDFDocumentFactory;

@ApplicationScoped
public class PDFWordIndexer {

	@Inject
	private Logger logger;

	public void index(Path file, IndexWriter indexWriter) throws IOException, InvalidFormatException, OpenXML4JException, XmlException {

		logger.info("Indexing PDF document "+file);
		try (com.snowtide.pdf.Document pdf = PDF.open(file.toFile())) {
			
			LucenePDFConfiguration configuration = new LucenePDFConfiguration();
			configuration.setBodyTextFieldName("content");
			
			Document pdfDocument = LucenePDFDocumentFactory.buildPDFDocument(pdf, configuration);
			pdfDocument.add(new StoredField("name", file.toFile().getName()));
			pdfDocument.add(new StoredField("path", file.toFile().getAbsolutePath()));
			
			indexWriter.addDocument(pdfDocument);
		}
	}
}
