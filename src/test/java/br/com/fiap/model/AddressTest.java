package br.com.fiap.model;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import br.com.fiap.dto.AddressDTO;

class AddressTest {

    @Test
    void shouldCreateAddressAndConvertToDTO() {
        Address address = new Address();
        address.setStreet("Rua A");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setZipCode("01000-000");

        AddressDTO dto = address.toDTO();

        assertEquals("Rua A", dto.street());
        assertEquals("123", dto.number());
        assertEquals("Centro", dto.neighborhood());
        assertEquals("São Paulo", dto.city());
        assertEquals("SP", dto.state());
        assertEquals("01000-000", dto.zipCode());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Address a1 = new Address();
        a1.setId(1L);
        a1.setState("SP");
        a1.setZipCode("01000-000");

        Address a2 = new Address();
        a2.setId(1L);
        a2.setState("SP");
        a2.setZipCode("01000-000");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());

        Set<Address> set = new HashSet<>();
        set.add(a1);
        assertTrue(set.contains(a2)); 
    }

    @Test
    void shouldNotBeEqualWithDifferentData() {
        Address a1 = new Address();
        a1.setId(1L);
        a1.setState("SP");
        a1.setZipCode("01000-000");

        Address a2 = new Address();
        a2.setId(2L);
        a2.setState("RJ");
        a2.setZipCode("20000-000");

        assertNotEquals(a1, a2);
    }

    @Test
    void shouldUseToString() {
        Address address = new Address(1L, "Rua A", "123", "Centro", "São Paulo", "SP", "01000-000", null);
        String result = address.toString();
        assertTrue(result.contains("Rua A"));
        assertTrue(result.contains("SP"));
    }
}
