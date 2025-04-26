/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.*;
import com.westminster.bookstoreapi.exception.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

/**
 *
 * @author ASUS
 */
@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final Map<Integer, Cart> carts = new HashMap<>();
    private static final Map<Integer, Customer> customers = CustomerResource.getCustomers();
    private static final Map<Integer, Book> books = BookResource.getBooks();

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, CartItem item) {
        if (!customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Book book = books.get(item.getBookId());
        if (book == null) throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
        if (book.getStock() < item.getQuantity()) {
            throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
        }

        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        cart.addItem(item);

        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        if (!customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateItem(@PathParam("customerId") int customerId,
                           @PathParam("bookId") int bookId,
                           CartItem updatedItem) {
        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found for customer ID " + customerId);

        for (CartItem item : cart.getItems()) {
            if (item.getBookId() == bookId) {
                item.setQuantity(updatedItem.getQuantity());
                return cart;
            }
        }

        throw new BookNotFoundException("Book with ID " + bookId + " not found in cart.");
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId) {
        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found for customer ID " + customerId);

        boolean removed = cart.getItems().removeIf(item -> item.getBookId() == bookId);
        if (!removed) throw new BookNotFoundException("Book with ID " + bookId + " not found in cart.");

        return Response.noContent().build();
    }

    // Utility method for use in OrderResource
    public static Map<Integer, Cart> getCarts() {
        return carts;
    }
}