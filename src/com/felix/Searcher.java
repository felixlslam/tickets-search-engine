package com.felix;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Searcher {
    private String indexPath;
    final String[] searchFields = new String[]{"summary", "description"};
    final int MAX_RESULTS=3;

    public Searcher(String indexPath) throws IOException {
        this.indexPath = indexPath;
    }

    public List<String> findSimilarTickets(Ticket ticket) throws IOException {
        Directory indexDir = FSDirectory.open(Path.of(indexPath));
        DirectoryReader reader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        MoreLikeThis mlt = new MoreLikeThis(reader);
        mlt.setMinTermFreq(0);
        mlt.setMinDocFreq(0);
        mlt.setMaxDocFreqPct(80);
        mlt.setAnalyzer(new StandardAnalyzer());
        mlt.setFieldNames(searchFields);

        List<String> result = new ArrayList<>();
        try {
            Map<String, Collection<Object>> queryDoc = Map.of("summary", List.of(ticket.getSummary()), "description", List.of(ticket.getDescription()));
            Query query = mlt.like(queryDoc);
            BooleanQuery bq = new BooleanQuery.Builder()
                    .add(query, BooleanClause.Occur.MUST)
                    .add(new TermQuery(new Term("url", ticket.getUrl())), BooleanClause.Occur.MUST_NOT)
                    .build();

            TopDocs topDocs = indexSearcher.search(bq, MAX_RESULTS);
            for (ScoreDoc doc : topDocs.scoreDocs) {
                Document originalDoc = indexSearcher.storedFields().document(doc.doc);
                System.out.println(originalDoc);
                result.add(originalDoc.get("url"));
            }
        } catch (IOException e) {
            System.err.println("Failed to search index. Error" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
