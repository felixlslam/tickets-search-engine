package com.felix;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KeywordField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

public class Indexer {
    private IndexWriter indexWriter;
    public Indexer(String indexPath) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        Directory indexDir = FSDirectory.open(Path.of(indexPath));
        indexWriter = new IndexWriter(indexDir, iwc);
    }

    public void index(Ticket ticket) throws IOException {
        Document doc = new Document();
        doc.add(new KeywordField("url", ticket.url, Field.Store.YES));
        doc.add(new TextField("summary", ticket.summary, Field.Store.NO));
        doc.add(new TextField("description", ticket.description, Field.Store.NO));
        indexWriter.updateDocument(new Term("url", ticket.url), doc);
        indexWriter.commit();
    }
}
