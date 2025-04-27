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
//
///**
// *
// * @author ASUS
// */
//@Path("/customers/{customerId}/orders")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class OrderResource {
//    private static final Map<Integer, Order> orders = new HashMap<>();
//    private static final Map<Integer, List<Order>> customerOrders = new HashMap<>();
//    private static final Map<Integer, Customer> customers = CustomerResource.getCustomers();
//    private static final Map<Integer, Book> books = BookResource.getBooks();
//    private static final Map<Integer, Cart> carts = CartResource.getCarts();
//    private static int nextOrderId = 1;
//
//    @POST
//    public Response createOrder(@PathParam("customerId") int customerId) {
//        if (!customers.containsKey(customerId)) {
//            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
//        }
//
//        Cart cart = carts.get(customerId);
//        if (cart == null || cart.getItems().isEmpty()) {
//            throw new CartNotFoundException("Cart is empty or does not exist.");
//        }
//
//        List<CartItem> cartItems = new ArrayList<>(cart.getItems());
//        double total = 0;
//
//        // Validate stock and calculate total
//        for (CartItem item : cartItems) {
//            Book book = books.get(item.getBookId());
//            if (book == null) throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
//            if (book.getStock() < item.getQuantity()) {
//                throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
//            }
//            total += book.getPrice() * item.getQuantity();
//        }
//
//        // Deduct stock
//        for (CartItem item : cartItems) {
//            Book book = books.get(item.getBookId());
//            book.setStock(book.getStock() - item.getQuantity());
//        }
//
//        Order order = new Order(nextOrderId++, customerId, cartItems, total);
//        orders.put(order.getId(), order);
//
//        customerOrders.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
//
//        // Clear cart
//        cart.getItems().clear();
//
//        return Response.status(Response.Status.CREATED).entity(order).build();
//    }
//
//    @GET
//    public List<Order> getAllOrders(@PathParam("customerId") int customerId) {
//        if (!customers.containsKey(customerId)) {
//            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
//        }
//        return customerOrders.getOrDefault(customerId, Collections.emptyList());
//    }
//
//    @GET
//    @Path("/{orderId}")
//    public Order getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
//        if (!customers.containsKey(customerId)) {
//            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
//        }
//        Order order = orders.get(orderId);
//        if (order == null || order.getCustomerId() != customerId) {
//            throw new InvalidInputException("Order not found for customer ID " + customerId);
//        }
//        return order;
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
@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Logger LOGGER = Logger.getLogger(OrderResource.class.getName());
    private static final Map<Integer, Order> orders = new HashMap<>();
    private static final Map<Integer, List<Order>> customerOrders = new HashMap<>();
    private static final Map<Integer, Customer> customers = CustomerResource.getCustomers();
    private static final Map<Integer, Book> books = BookResource.getBooks();
    private static final Map<Integer, Cart> carts = CartResource.getCarts();
    private static int nextOrderId = 1;

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        LOGGER.info("Creating order for customerId: " + customerId);

        if (!customers.containsKey(customerId)) {
            LOGGER.warning("Customer not found: " + customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Cart cart = carts.get(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            LOGGER.warning("Cart is empty or does not exist for customerId: " + customerId);
            throw new CartNotFoundException("Cart is empty or does not exist.");
        }

        List<CartItem> cartItems = new ArrayList<>(cart.getItems());
        double total = 0;

        // Validate stock and calculate total
        for (CartItem item : cartItems) {
            Book book = books.get(item.getBookId());
            if (book == null) {
                LOGGER.warning("Book not found: " + item.getBookId());
                throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
            }
            if (book.getStock() < item.getQuantity()) {
                LOGGER.warning("Insufficient stock for book: " + book.getTitle());
                throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
            }
            total += book.getPrice() * item.getQuantity();
        }

        // Deduct stock
        for (CartItem item : cartItems) {
            Book book = books.get(item.getBookId());
            book.setStock(book.getStock() - item.getQuantity());
            LOGGER.info(String.format("Deducted stock for bookId: %d, new stock: %d", book.getId(), book.getStock()));
        }

        Order order = new Order(nextOrderId++, customerId, cartItems, total);
        orders.put(order.getId(), order);

        customerOrders.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);

        // Clear cart
        cart.getItems().clear();
        LOGGER.info("Cart cleared for customerId: " + customerId);

        LOGGER.info("Order created successfully with ID: " + order.getId());
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public List<Order> getAllOrders(@PathParam("customerId") int customerId) {
        LOGGER.info("Retrieving all orders for customerId: " + customerId);

        if (!customers.containsKey(customerId)) {
            LOGGER.warning("Customer not found: " + customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        List<Order> result = customerOrders.getOrDefault(customerId, Collections.emptyList());
        LOGGER.info("Retrieved " + result.size() + " orders for customerId: " + customerId);
        return result;
    }

    @GET
    @Path("/{orderId}")
    public Order getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        LOGGER.info(String.format("Retrieving order with ID: %d for customerId: %d", orderId, customerId));

        if (!customers.containsKey(customerId)) {
            LOGGER.warning("Customer not found: " + customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Order order = orders.get(orderId);
        if (order == null || order.getCustomerId() != customerId) {
            LOGGER.warning("Order not found or does not belong to customerId: " + customerId);
            throw new InvalidInputException("Order not found for customer ID " + customerId);
        }

        LOGGER.info("Order retrieved successfully with ID: " + orderId);
        return order;
    }
}