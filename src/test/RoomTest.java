package test;

import main.Room;
import main.Room_Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room(1, 2, true, true, false);
    }

    @Test
    public void testInitialRoomState() {
        assertFalse(room.isOcupied());
        assertEquals(1, room.getNumber());
        assertEquals(2, room.getCapacity());
        assertEquals(Room_Type.SINGLE, room.getType());
        assertTrue(room.isBalcony());
        assertTrue(room.isClean());
        assertFalse(room.isHydromassage());
        assertEquals(Room_Type.SINGLE.getPrice(), room.getPrice_per_night());
    }

    @Test
    public void testSetOcupied() {
        room.setOcupied(true);
        assertTrue(room.isOcupied());

        room.setOcupied(false);
        assertFalse(room.isOcupied());
    }

    @Test
    public void testSetType() {
        room.setType(Room_Type.DOUBLE);
        assertEquals(Room_Type.DOUBLE, room.getType());
    }

    @Test
    public void testSetPrice_per_night() {
        room.setPrice_per_night(150.0);
        assertEquals(150.0, room.getPrice_per_night());
    }

    @Test
    public void testSetClean() {
        room.setClean(false);
        assertFalse(room.isClean());

        room.setClean(true);
        assertTrue(room.isClean());
    }

    @Test
    public void testToString() {
        String expected = """
                Room Number: 1
                \t\tOccupied: No
                \t\tCapacity 2\

                \t\tRoom Type: SINGLE
                \t\tBalcony: Yes\

                \t\tPrice per Night: 45,0\

                \t\tClean: Yes
                \t\tMydromassage: No""";
        assertEquals(expected, room.toString());
    }
}
