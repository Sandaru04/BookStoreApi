package com.westminster.bookstoreapi;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
@ApplicationPath("api")
public class Main extends Application {
    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>>resources = new HashSet<>();
        resources.add(com.westminster.bookstoreapi.resources.BookResource.class);
        return resources;
    }
    
    
    
    
}
