package me.saro.sap.jco.test;

import me.saro.sap.jco.SapField;
import org.junit.jupiter.api.Test;

public class SapUtilsTest {
    
    @Test
    public void convertField() {
        
    }
    
    
    public static class User {
        
        @SapField("FIRST_NAME")
        String firstName;
        
        @SapField("LAST_NAME")
        String lastName;
        
        @SapField("BIRTH")
        String birth;
        
        @SapField("JOIN_DATE")
        String joinDate;
    }
}
