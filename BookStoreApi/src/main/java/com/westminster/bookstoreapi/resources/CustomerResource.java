/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.westminster.bookstoreapi.resources;

import com.westminster.bookstoreapi.model.Customer;
import com.westminster.bookstoreapi.exception.CustomerNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

/**
 *
 * @author ASUS
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static int nextId = 1;

    @POST
    public Response createCustomer(Customer customer) {
        customer.setId(nextId++);
        customers.put(customer.getId(), customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        Customer customer = customers.get(id);
        if (customer == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer updated) {
        Customer customer = customers.get(id);
        if (customer == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        updated.setId(id);
        customers.put(id, updated);
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        customers.remove(id);
        return Response.noContent().build();
    }

    // Utility method to access customers (for cart and order services)
    public static Map<Integer, Customer> getCustomers() {
        return customers;
    }
}