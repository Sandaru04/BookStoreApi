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

/**
 *
 * @author ASUS
 */
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Map<Integer, Author> authors = new HashMap<>();
    private static final Map<Integer, Book> books = BookResource.getBooks(); // Access books
    private static int nextId = 1;

    @POST
    public Response createAuthor(Author author) {
        author.setId(nextId++);
        authors.put(author.getId(), author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Collection<Author> getAllAuthors() {
        return authors.values();
    }

    @GET
    @Path("/{id}")
    public Author getAuthor(@PathParam("id") int id) {
        Author author = authors.get(id);
        if (author == null) throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        return author;
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author updated) {
        Author author = authors.get(id);
        if (author == null) throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        updated.setId(id);
        authors.put(id, updated);
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        authors.remove(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int id) {
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }

        List<Book> authoredBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId() == id) {
                authoredBooks.add(book);
            }
        }
        return authoredBooks;
    }

    // Utility to expose authors (for cross-access)
    public static Map<Integer, Author> getAuthors() {
        return authors;
    }
}