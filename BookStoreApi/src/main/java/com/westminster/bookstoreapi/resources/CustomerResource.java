///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.westminster.bookstoreapi.resources;
//
//import com.westminster.bookstoreapi.model.Customer;
//import com.westminster.bookstoreapi.exception.CustomerNotFoundException;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.*;
//import java.util.*;
//
///**
// *
// * @author ASUS
// */
//@Path("/customers")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class CustomerResource {
//    private static final Map<Integer, Customer> customers = new HashMap<>();
//    private static int nextId = 1;
//
//    @POST
//    public Response createCustomer(Customer customer) {
//        customer.setId(nextId++);
//        customers.put(customer.getId(), customer);
//        return Response.status(Response.Status.CREATED).entity(customer).build();
//    }
//
//    @GET
//    public Collection<Customer> getAllCustomers() {
//        return customers.values();
//    }
//
//    @GET
//    @Path("/{id}")
//    public Customer getCustomer(@PathParam("id") int id) {
//        Customer customer = customers.get(id);
//        if (customer == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
//        return customer;
//    }
//
//    @PUT
//    @Path("/{id}")
//    public Customer updateCustomer(@PathParam("id") int id, Customer updated) {
//        Customer customer = customers.get(id);
//        if (customer == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
//        updated.setId(id);
//        customers.put(id, updated);
//        return updated;
//    }
//
//    @DELETE
//    @Path("/{id}")
//    public Response deleteCustomer(@PathParam("id") int id) {
//        if (!customers.containsKey(id)) {
//            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
//        }
//        customers.remove(id);
//        return Response.noContent().build();
//    }
//
//    // Utility method to access customers (for cart and order services)
//    public static Map<Integer, Customer> getCustomers() {
//        return customers;
//    }
//}










package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.Customer;
import com.westminster.bookstoreapi.exception.CustomerNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class.getName());
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static int nextId = 1;

    @POST
    public Response createCustomer(Customer customer) {
        LOGGER.info(String.format("Creating customer: name=%s, email=%s", customer.getName(), customer.getEmail()));

        customer.setId(nextId++);
        customers.put(customer.getId(), customer);

        LOGGER.info("Customer created successfully with ID: " + customer.getId());
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Collection<Customer> getAllCustomers() {
        LOGGER.info("Retrieving all customers");
        Collection<Customer> result = customers.values();
        LOGGER.info("Retrieved " + result.size() + " customers");
        return result;
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        LOGGER.info("Retrieving customer with ID: " + id);

        Customer customer = customers.get(id);
        if (customer == null) {
            LOGGER.warning("Customer not found: " + id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }

        LOGGER.info("Customer retrieved successfully: " + customer.getName());
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer updated) {
        LOGGER.info(String.format("Updating customer with ID: %d, new name=%s, email=%s",
                id, updated.getName(), updated.getEmail()));

        Customer customer = customers.get(id);
        if (customer == null) {
            LOGGER.warning("Customer not found: " + id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        updated.setId(id);
        customers.put(id, updated);

        LOGGER.info("Customer updated successfully with ID: " + id);
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        LOGGER.info("Deleting customer with ID: " + id);

        if (!customers.containsKey(id)) {
            LOGGER.warning("Customer not found: " + id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        customers.remove(id);

        LOGGER.info("Customer deleted successfully with ID: " + id);
        return Response.noContent().build();
    }

    // Utility method to access customers (for cart and order services)
    public static Map<Integer, Customer> getCustomers() {
        LOGGER.info("Accessing customers map");
        return customers;
    }
}