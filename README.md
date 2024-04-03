# What is this ?

A Lucene-powered simple tickets search engine to find similar tickets indexed

# Why ?

This is to compliment a missing feature in some ticketing systems to show similar tickets raised before, 
which could be a very useful piece of information to speed up the handling of the ticket.

# How to use ?

As of now, only two rest apis. 

- POST /
   - Submit a ticket data for indexing 
- POST /findsimilar
   - Submit a ticket data for searching similar tickets   