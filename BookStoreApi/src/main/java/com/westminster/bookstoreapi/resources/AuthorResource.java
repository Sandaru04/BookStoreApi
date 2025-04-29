/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.Author;
import com.westminster.bookstoreapi.model.Book;
import com.westminster.bookstoreapi.exception.AuthorNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Logger LOGGER = Logger.getLogger(AuthorResource.class.getName());
    private static final Map<Integer, Author> authors = new HashMap<>();
    private static final Map<Integer, Book> books = BookResource.getBooks(); // Access books
    private static int nextId = 1;

    @POST
    public Response createAuthor(Author author) {
        LOGGER.info("Creating author: name=" + author.getName());

        author.setId(nextId++);
        authors.put(author.getId(), author);

        LOGGER.info("Author created successfully with ID: " + author.getId());
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Collection<Author> getAllAuthors() {
        LOGGER.info("Retrieving all authors");
        Collection<Author> result = authors.values();
        LOGGER.info("Retrieved " + result.size() + " authors");
        return result;
    }

    @GET
    @Path("/{id}")
    public Author getAuthor(@PathParam("id") int id) {
        LOGGER.info("Retrieving author with ID: " + id);

        Author author = authors.get(id);
        if (author == null) {
            LOGGER.warning("Author not found: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }

        LOGGER.info("Author retrieved successfully: " + author.getName());
        return author;
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author updated) {
        LOGGER.info(String.format("Updating author with ID: %d, new name=%s", id, updated.getName()));

        Author author = authors.get(id);
        if (author == null) {
            LOGGER.warning("Author not found: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        updated.setId(id);
        authors.put(id, updated);

        LOGGER.info("Author updated successfully with ID: " + id);
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        LOGGER.info("Deleting author with ID: " + id);

        if (!authors.containsKey(id)) {
            LOGGER.warning("Author not found: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        authors.remove(id);

        LOGGER.info("Author deleted successfully with ID: " + id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int id) {
        LOGGER.info("Retrieving books for author with ID: " + id);

        if (!authors.containsKey(id)) {
            LOGGER.warning("Author not found: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }

        List<Book> authoredBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId() == id) {
                authoredBooks.add(book);
            }
        }
        LOGGER.info("Retrieved " + authoredBooks.size() + " books for author ID: " + id);
        return authoredBooks;
    }

    // Utility to expose authors (for cross-access)
    public static Map<Integer, Author> getAuthors() {
        LOGGER.info("Accessing authors map");
        return authors;
    }
}









