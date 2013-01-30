package org.isfit.festival.programme.model;

public class Support {
    public static final String DEBUG = "ISFiT_d"; 
    
    public static void checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException("The object is null");
        }
    }
}
