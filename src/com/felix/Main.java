package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    static String indexPath = "index";
    public static void main(String[] args) throws IOException {
        Indexer indexer = new Indexer(indexPath);
        Searcher searcher = new Searcher(indexPath);
        MuServer server = MuServerBuilder.httpServer()
                .withHttpPort(8080)
                .addHandler(Method.POST, "/", (request, response, paramMap) -> {
                    String payload = request.readBodyAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Ticket ticket = mapper.readValue(payload, Ticket.class);
                        indexer.index(ticket);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                    response.write("Done");
                })
                .addHandler(Method.POST, "/findsimilar", (request, response, paramMap) -> {
                    String payload = request.readBodyAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    Ticket ticket = mapper.readValue(payload, Ticket.class);
                    List<String> result = searcher.findSimilarTickets(ticket);
                    response.write(String.join(";", result));
                })
                .start();
    }
}