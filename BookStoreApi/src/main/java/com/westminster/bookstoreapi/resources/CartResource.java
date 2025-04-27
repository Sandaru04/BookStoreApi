///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.westminster.bookstoreapi.resources;
//
//import com.westminster.bookstoreapi.model.*;
//import com.westminster.bookstoreapi.exception.*;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.*;
//import java.util.*;
//
///**
// *
// * @author ASUS
// */
//@Path("/customers/{customerId}/cart")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class CartResource {
//    private static final Map<Integer, Cart> carts = new HashMap<>();
//    private static final Map<Integer, Customer> customers = CustomerResource.getCustomers();
//    private static final Map<Integer, Book> books = BookResource.getBooks();
//
//    @POST
//    @Path("/items")
//    public Response addItem(@PathParam("customerId") int customerId, CartItem item) {
//        if (!customers.containsKey(customerId)) {
//            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
//        }
//
//        Book book = books.get(item.getBookId());
//        if (book == null) throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
//        if (book.getStock() < item.getQuantity()) {
//            throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
//        }
//
//        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
//        cart.addItem(item);
//
//        return Response.status(Response.Status.CREATED).entity(cart).build();
//    }
//
//    @GET
//    public Cart getCart(@PathParam("customerId") int customerId) {
//        if (!customers.containsKey(customerId)) {
//            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
//        }
//
//        Cart cart = carts.get(customerId);
//        if (cart == null) throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
//        return cart;
//    }
//
//    @PUT
//    @Path("/items/{bookId}")
//    public Cart updateItem(@PathParam("customerId") int customerId,
//                           @PathParam("bookId") int bookId,
//                           CartItem updatedItem) {
//        Cart cart = carts.get(customerId);
//        if (cart == null) throw new CartNotFoundException("Cart not found for customer ID " + customerId);
//
//        for (CartItem item : cart.getItems()) {
//            if (item.getBookId() == bookId) {
//                item.setQuantity(updatedItem.getQuantity());
//                return cart;
//            }
//        }
//
//        throw new BookNotFoundException("Book with ID " + bookId + " not found in cart.");
//    }
//
//    @DELETE
//    @Path("/items/{bookId}")
//    public Response removeItem(@PathParam("customerId") int customerId,
//                               @PathParam("bookId") int bookId) {
//        Cart cart = carts.get(customerId);
//        if (cart == null) throw new CartNotFoundException("Cart not found for customer ID " + customerId);
//
//        boolean removed = cart.getItems().removeIf(item -> item.getBookId() == bookId);
//        if (!removed) throw new BookNotFoundException("Book with ID " + bookId + " not found in cart.");
//
//        return Response.noContent().build();
//    }
//
//    // Utility method for use in OrderResource
//    public static Map<Integer, Cart> getCarts() {
//        return carts;
//    }
//}








package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.*;
import com.westminster.bookstoreapi.exception.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final Logger LOGGER = Logger.getLogger(CartResource.class.getName());
    private static final Map<Integer, Cart> carts = new HashMap<>();
    private static final Map<Integer, Customer> customers = CustomerResource.getCustomers();
    private static final Map<Integer, Book> books = BookResource.getBooks();

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, CartItem item) {
        LOGGER.info(String.format("Adding item to cart for customerId: %d, bookId: %d, quantity: %d",
                customerId, item.getBookId(), item.getQuantity()));

        if (!customers.containsKey(customerId)) {
            LOGGER.warning("Customer not found: " + customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Book book = books.get(item.getBookId());
        if (book == null) {
            LOGGER.warning("Book not found: " + item.getBookId());
            throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
        }
        if (book.getStock() < item.getQuantity()) {
            LOGGER.warning("Insufficient stock for book: " + book.getTitle());
            throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
        }

        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        cart.addItem(item);

        LOGGER.info("Item added successfully to cart for customerId: " + customerId);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        LOGGER.info("Retrieving cart for customerId: " + customerId);

        if (!customers.containsKey(customerId)) {
            LOGGER.warning("Customer not found: " + customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOGGER.warning("Cart not found for customerId: " + customerId);
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }

        LOGGER.info("Cart retrieved successfully for customerId: " + customerId);
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateItem(@PathParam("customerId") int customerId,
                           @PathParam("bookId") int bookId,
                           CartItem updatedItem) {
        LOGGER.info(String.format("Updating item in cart for customerId: %d, bookId: %d, new quantity: %d",
                customerId, bookId, updatedItem.getQuantity()));

        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOGGER.warning("Cart not found for customerId: " + customerId);
            throw new CartNotFoundException("Cart not found for customer ID " + customerId);
        }

        for (CartItem item : cart.getItems()) {
            if (item.getBookId() == bookId) {
                item.setQuantity(updatedItem.getQuantity());
                LOGGER.info("Item updated successfully in cart for customerId: " + customerId);
                return cart;
            }
        }

        LOGGER.warning("Book not found in cart: " + bookId);
        throw new BookNotFoundException("Book with ID " + bookId + " not found in cart.");
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId) {
        LOGGER.info(String.format("Removing item from cart for customerId: %d, bookId: %d", customerId, bookId));

        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOGGER.warning("Cart not found for customerId: " + customerId);
            throw new CartNotFoundException("Cart not found for customer ID " + customerId);
        }

        boolean removed = cart.getItems().removeIf(item -> item.getBookId() == bookId);
        if (!removed) {
            LOGGER.warning("Book not found in cart: " + bookId);
            throw new BookNotFoundException("Book with ID " + bookId + " not found in cart.");
        }

        LOGGER.info("Item removed successfully from cart for customerId: " + customerId);
        return Response.noContent().build();
    }

    // Utility method for use in OrderResource
    public static Map<Integer, Cart> getCarts() {
        LOGGER.info("Accessing carts map");
        return carts;
    }
}