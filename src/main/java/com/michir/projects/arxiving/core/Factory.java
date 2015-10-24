package com.michir.projects.arxiving.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import com.michir.projects.arxiving.api.IndexDirectory;
import com.michir.projects.arxiving.api.PhraseQueryBuilder;

@ApplicationScoped
public class Factory {

	@Produces
	public Logger logger() {
		return Logger.getLogger("Application");
	}

	@Produces
	public Analyzer analyze() {
		return new StandardAnalyzer();
	}
	
	@Produces
	@PhraseQueryBuilder
	public QueryBuilder luceneQueryBuilder(Analyzer analyzer) throws IOException {
		return new QueryBuilder(analyzer);
	}

	@Produces
	public IndexWriterConfig indexWriterConfig(Analyzer analyzer) {
		return new IndexWriterConfig(analyzer);
	}
}
