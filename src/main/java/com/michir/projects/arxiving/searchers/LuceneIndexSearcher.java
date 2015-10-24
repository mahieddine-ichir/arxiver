package com.michir.projects.arxiving.searchers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import com.michir.projects.arxiving.api.IndexDirectory;
import com.michir.projects.arxiving.api.PhraseQueryBuilder;
import com.michir.projects.arxiving.api.SearchResult;

@ApplicationScoped
public class LuceneIndexSearcher {

	@Inject
	private Logger logger;

	@Inject
	@PhraseQueryBuilder
	private QueryBuilder builder;

	@Inject
	@IndexDirectory
	private Path indexDir;

	public Stream<SearchResult> search(String q) throws IOException {

		FSDirectory fsDirectory = null;
		IndexReader reader = null;

		try {
			fsDirectory = FSDirectory.open(indexDir);
			reader = DirectoryReader.open(fsDirectory);

			IndexSearcher indexSearcher = new IndexSearcher(reader);
			String qs = q.trim().toLowerCase();
			Query query;
			if (qs.endsWith("/and")) {
				String[] qss = qs.substring(0, q.lastIndexOf("/and")).trim().split("\\s");
				Builder builder2 = new BooleanQuery.Builder();
				for (String s : qss) {
					builder2.add(queryFor(s), Occur.MUST);
				}
				query = builder2.build();
			} else if (qs.endsWith("/or")) {
				String[] qss = qs.substring(0, q.lastIndexOf("/or")).trim().split("\\s");
				Builder builder2 = new BooleanQuery.Builder();
				for (String s : qss) {
					builder2.add(queryFor(s), Occur.SHOULD);
				}
				query = builder2.build();
			} else {
				query = queryFor(qs);
			}
			logger.info("+++ searching index for "+qs);

			TopDocs topDocs = indexSearcher.search(query, 128);
			return Arrays.stream(topDocs.scoreDocs)
					.map(e->{
						try {
							Document doc = indexSearcher.doc(e.doc);
							SearchResult result = new SearchResult();
							result.setName(doc.get("name"));
							result.setPath(doc.get("path"));
							return result;
						} catch (Exception e2) {
							return null;
						}
					})
					.filter(e -> e != null);
		} finally {
			//try {reader.close();} catch (Exception e) {};
			//try {fsDirectory.close();} catch (Exception e) {};
		}
	}

	private Query queryFor(String qs) {
		Term term = new Term("content", qs);
		Query query;
		if (qs.endsWith("*")) {
			query = new WildcardQuery(term);
		} else {
			query = builder.createPhraseQuery(term.field(), term.text());
		}
		return query;
	}
}
