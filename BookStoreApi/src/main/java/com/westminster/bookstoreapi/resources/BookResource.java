package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.Book;
import com.westminster.bookstoreapi.exception.BookNotFoundException;
import com.westminster.bookstoreapi.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final Logger LOGGER = Logger.getLogger(BookResource.class.getName());
    private static final Map<Integer, Book> books = new HashMap<>();
    private static int nextId = 1;

    public static Map<Integer, Book> getBooks() {
        LOGGER.info("Accessing books map");
        return books;
    }

    @POST
    public Response createBook(Book book) {
        LOGGER.info(String.format("Creating book: title=%s, authorId=%d, isbn=%s, publicationYear=%d, price=%.2f, stock=%d",
                book.getTitle(), book.getAuthorId(), book.getIsbn(), book.getPublicationYear(), book.getPrice(), book.getStock()));

        if (book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            LOGGER.warning("Invalid publication year: " + book.getPublicationYear());
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        book.setId(nextId++);
        books.put(book.getId(), book);

        LOGGER.info("Book created successfully with ID: " + book.getId());
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Collection<Book> getAllBooks() {
        LOGGER.info("Retrieving all books");
        Collection<Book> result = books.values();
        LOGGER.info("Retrieved " + result.size() + " books");
        return result;
    }

    @GET
    @Path("/{id}")
    public Book getBook(@PathParam("id") int id) {
        LOGGER.info("Retrieving book with ID: " + id);

        Book book = books.get(id);
        if (book == null) {
            LOGGER.warning("Book not found: " + id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }

        LOGGER.info("Book retrieved successfully: " + book.getTitle());
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book updated) {
        LOGGER.info(String.format("Updating book with ID: %d, new title=%s, authorId=%d, isbn=%s, publicationYear=%d, price=%.2f, stock=%d",
                id, updated.getTitle(), updated.getAuthorId(), updated.getIsbn(), updated.getPublicationYear(), updated.getPrice(), updated.getStock()));

        Book book = books.get(id);
        if (book == null) {
            LOGGER.warning("Book not found: " + id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }

        if (updated.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            LOGGER.warning("Invalid publication year: " + updated.getPublicationYear());
            throw new InvalidInputException("Publication year cannot be in the future.");
        }

        updated.setId(id);
        books.put(id, updated);

        LOGGER.info("Book updated successfully with ID: " + id);
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        LOGGER.info("Deleting book with ID: " + id);

        if (!books.containsKey(id)) {
            LOGGER.warning("Book not found: " + id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        books.remove(id);

        LOGGER.info("Book deleted successfully with ID: " + id);
        return Response.noContent().build();
    }
}







