package test;

import main.Reserva;
import main.Room;
import main.Room_Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaTest {

    private Reserva reserva;
    private Room room;

    @BeforeEach
    public void setUp() {
        // Configurando um quarto de exemplo
        room = new Room(); // Presume-se que a classe Room tenha um construtor padrão

        room.setType(Room_Type.SINGLE);

        // Configurando uma reserva de exemplo
        reserva = new Reserva("Joao", 123456789, 3,
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 9, 4),
                2, Room_Type.DOUBLE,
                2, 0, 0.0,
                true, 1, 10.0, 100.0,
                true, false, room, 1);
    }

    @Test
    public void testReservaConstructor() {
        assertEquals("Joao", reserva.getName());
        assertEquals(123456789, reserva.getNif());
        assertEquals(3, reserva.getNights());
        assertEquals(LocalDate.of(2024, 9, 1), reserva.getCheck_In());
        assertEquals(LocalDate.of(2024, 9, 4), reserva.getCheck_Out());
        assertEquals(2, reserva.getNumber_Persons());
        assertEquals(Room_Type.DOUBLE, reserva.getType());
        assertEquals(2, reserva.getAdults());
        assertEquals(0, reserva.getChildren());
        assertEquals(0.0, reserva.getChildren_Description());
        assertTrue(reserva.isPets());
        assertEquals(1, reserva.getNumber_of_Pets());
        assertEquals(10.0, reserva.getPet_Description());
        assertEquals(100.0, reserva.getPrice_per_Night());
        assertTrue(reserva.isHydromassage());
        assertFalse(reserva.isRomantic_Night());
        assertEquals(room, reserva.getQuarto());
        assertEquals(1, reserva.getN_Beds());
        assertFalse(reserva.isCheck_In_Made());
        assertFalse(reserva.isCheck_Out_Made());
        assertFalse(reserva.isPaid());
        assertFalse(reserva.isCanceled());
    }

    @Test
    public void testSettersAndGetters() {
        reserva.setName("Jane Doe");
        assertEquals("Jane Doe", reserva.getName());

        reserva.setNif(987654321);
        assertEquals(987654321, reserva.getNif());

        reserva.setNights(5);
        assertEquals(5, reserva.getNights());

        reserva.setCheck_In(LocalDate.of(2024, 9, 10));
        assertEquals(LocalDate.of(2024, 9, 10), reserva.getCheck_In());

        reserva.setCheck_Out(LocalDate.of(2024, 9, 15));
        assertEquals(LocalDate.of(2024, 9, 15), reserva.getCheck_Out());

        reserva.setNumber_Persons(4);
        assertEquals(4, reserva.getNumber_Persons());

        reserva.setAdults(3);
        assertEquals(3, reserva.getAdults());

        reserva.setChildren(1);
        assertEquals(1, reserva.getChildren());

        reserva.setChildren_Description(5.0);
        assertEquals(5.0, reserva.getChildren_Description());

        reserva.setPets(false);
        assertFalse(reserva.isPets());

        reserva.setNumber_of_Pets(0);
        assertEquals(0, reserva.getNumber_of_Pets());

        reserva.setPet_Description(0.0);
        assertEquals(0.0, reserva.getPet_Description());

        reserva.setPrice_per_Night(150.0);
        assertEquals(150.0, reserva.getPrice_per_Night());

        reserva.setHydromassage(false);
        assertFalse(reserva.isHydromassage());

        reserva.setRomantic_Night(true);
        assertTrue(reserva.isRomantic_Night());

        reserva.setCheck_In_Made(true);
        assertTrue(reserva.isCheck_In_Made());

        reserva.setCheck_Out_Made(true);
        assertTrue(reserva.isCheck_Out_Made());

        reserva.setPaid(true);
        assertTrue(reserva.isPaid());

        reserva.setCanceled(true);
        assertTrue(reserva.isCanceled());


    }

    @Test
    public void testToString() {
        String expected = "\n*** Reserve Number: " + reserva.getNumber() + " ***" +
                "\n\tNClient Name: Joao" +
                "\n\tIs Canceled: false" +
                "\n\tTotal Price: 0.0" +
                "\n\tPrice per Night: 100.0" +
                "\n\tNights: 3" +
                "\n\tCheck-In: 2024-09-01" +
                "\n\tCheck-Out:2024-09-04" +
                "\n\tIs Paid: false" +
                "\n\tAdults: 2" +
                "\n\tChildren: 0" +
                "\n\tPets: 1 -- Price: 10.0" +
                "\n\tNNumber of Beds: 1" +
                "\n\tHydromassage: true" +
                "\n\tRomantic: false" +
                "\n\t\t" + room.toString() +
                "\n\nCheck-In: false\nCheck-Out: false\n";

        assertEquals(expected, reserva.toString());
    }
}
