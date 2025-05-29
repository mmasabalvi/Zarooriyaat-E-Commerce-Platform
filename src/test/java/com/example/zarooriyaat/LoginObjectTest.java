package com.example.zarooriyaat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.example.zarooriyaat.signupobj.SignupEntity;

class SignupEntityTest {

    @Test
    void testAllGettersAndSetters() {
        SignupEntity e = new SignupEntity();

        // id
        e.setId(100L);
        assertEquals(100L, e.getId());

        // fullName
        e.setFullName("Alice Wonderland");
        assertEquals("Alice Wonderland", e.getFullName());

        // email
        e.setEmail("alice@example.com");
        assertEquals("alice@example.com", e.getEmail());

        // phoneNumber
        e.setPhoneNumber("1234567890");
        assertEquals("1234567890", e.getPhoneNumber());

        // password
        e.setPassword("secretPass");
        assertEquals("secretPass", e.getPassword());

        // confirmPassword (transient)
        e.setConfirmPassword("secretPass");
        assertEquals("secretPass", e.getConfirmPassword());
    }
}
