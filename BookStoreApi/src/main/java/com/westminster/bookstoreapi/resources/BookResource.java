package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.Book;
import com.westminster.bookstoreapi.exception.BookNotFoundException;
import com.westminster.bookstoreapi.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
/**
 *
 * @author 
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final Map<Integer, Book> books = new HashMap<>();
    private static int nextId = 1;

    public static Map<Integer, Book> getBooks() {
    return books;
}

    @POST
    public Response createBook(Book book) {
        if (book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        book.setId(nextId++);
        books.put(book.getId(), book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    @GET
    @Path("/{id}")
    public Book getBook(@PathParam("id") int id) {
        Book book = books.get(id);
        if (book == null) throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book updated) {
        Book book = books.get(id);
        if (book == null) throw new BookNotFoundException("Book with ID " + id + " does not exist.");

        if (updated.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }

        updated.setId(id);
        books.put(id, updated);
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        if (!books.containsKey(id)) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        books.remove(id);
        return Response.noContent().build();
    }
}







//@Path("/books")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class BookResource {
//
//    private static final Map<Integer, Book> books = new HashMap<>();
//    private static int nextId = 1;
//
//    static Map<Integer, Book> getBooks() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    @POST
//    public Response createBook(Book book) {
//        if (book.getId() == 0) {
//            book.setId(nextId++);
//        }
//        books.put(book.getId(), book);
//        return Response.status(Response.Status.CREATED).entity(book).build();
//    }
//
//    @GET
//    public Collection<Book> getAllBooks() {
//        return books.values();
//    }
//
//    @GET
//    @Path("/{id}")
//    public Book getBook(@PathParam("id") int id) {
//        Book book = books.get(id);
//        if (book == null) {
//            throw new NotFoundException("Book with ID " + id + " not found.");
//        }
//        return book;
//    }
//
//    @PUT
//    @Path("/{id}")
//    public Book updateBook(@PathParam("id") int id, Book updatedBook) {
//        Book book = books.get(id);
//        if (book == null) {
//            throw new NotFoundException("Book with ID " + id + " not found.");
//        }
//        updatedBook.setId(id);
//        books.put(id, updatedBook);
//        return updatedBook;
//    }
//
//    @DELETE
//    @Path("/{id}")
//    public Response deleteBook(@PathParam("id") int id) {
//        Book book = books.remove(id);
//        if (book == null) {
//            throw new NotFoundException("Book with ID " + id + " not found.");
//        }
//        return Response.noContent().build();
//    }
//}