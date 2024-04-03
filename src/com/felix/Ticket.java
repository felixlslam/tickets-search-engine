package com.felix;

public class Ticket {
    String url; // The URL of the ticket
    String summary;
    String description;

    public Ticket() {
        super();
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Ticket(String url, String summary, String description) {
        this.url = url;
        this.summary = summary;
        this.description = description;
    }
}
