package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.exceptions.FloorTooSmallException;
import bms.exceptions.InsufficientSpaceException;
import bms.room.Room;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.FireDrill;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a floor of a building.
 * <p>
 * All floors have a floor number (ground floor is floor 1), a list of rooms,
 * and a width and length.
 * <p>
 * A floor can be evacuated, which causes all rooms on the floor to be
 * evacuated.
 * @ass1
 */
public class Floor implements FireDrill, Encodable {
    /**
     * Unique floor number for this floor. Corresponds to how many floors above
     * ground floor (inclusive).
     */
    private int floorNumber;

    /**
     * List of rooms on the floor level.
     */
    private List<Room> rooms;

    /**
     * Width of the floor in metres.
     */
    private double width;

    /**
     * Length of the floor in metres.
     */
    private double length;

    /**
     * Minimum width of all floors, in metres.
     */
    private static final int MIN_WIDTH = 5;

    /**
     * Minimum length of all floors, in metres.
     */
    private static final int MIN_LENGTH = 5;

    /**
     * The maintenance schedule for this floor
     */
    private MaintenanceSchedule schedule;

    /**
     * Creates a new floor with the given floor number.
     *
     * @param floorNumber a unique floor number, corresponds to how many floors
     * above ground floor (inclusive)
     * @param width the width of the floor in metres
     * @param length the length of the floor in metres
     * @ass1
     */
    public Floor(int floorNumber, double width, double length) {
        this.floorNumber = floorNumber;
        this.width = width;
        this.length = length;
        this.rooms = new ArrayList<>();
    }

    /**
     * Returns the floor number of this floor.
     *
     * @return floor number
     * @ass1
     */
    public int getFloorNumber() {
        return this.floorNumber;
    }

    /**
     * Returns the minimum width for all floors.
     *
     * @return 5
     * @ass1
     */
    public static int getMinWidth() {
        return MIN_WIDTH;
    }

    /**
     * Returns the minimum length for all floors.
     *
     * @return 5
     * @ass1
     */
    public static int getMinLength() {
        return MIN_LENGTH;
    }

    /**
     * Returns a new list containing all the rooms on this floor.
     * <p>
     * Adding or removing rooms from this list should not affect the
     * floor's internal list of rooms.
     *
     * @return new list containing all rooms on the floor
     * @ass1
     */
    public List<Room> getRooms() {
        return new ArrayList<>(this.rooms);
    }

    /**
     * Returns width of the floor.
     *
     * @return floor width
     * @ass1
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Returns length of the floor.
     *
     * @return floor length
     * @ass1
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Search for the room with the specified room number.
     * <p>
     * Returns the corresponding Room object, or null if the room was not
     * found.
     *
     * @param roomNumber room number of room to search for
     * @return room with the given number if found; null if not found
     * @ass1
     */
    public Room getRoomByNumber(int roomNumber) {
        for (Room room : this.rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    /**
     * Calculates the area of the floor in square metres.
     * <p>
     * The area should be calculated as {@code getWidth()} multiplied by
     * {@code getLength()}.
     * <p>
     * For example, a floor with a length of 20.5 and width of 35.2, would be
     * 721.6 square metres.
     *
     * @return area of the floor in square metres
     * @ass1
     */
    public double calculateArea() {
        return this.getWidth() * this.getLength();
    }

    /**
     * Calculates the area of the floor which is currently occupied by all the
     * rooms on the floor.
     *
     * @return area of the floor that is currently occupied, in square metres
     * @ass1
     */
    public float occupiedArea() {
        float area = 0;
        for (Room room : rooms) {
            area += room.getArea();
        }
        return area;
    }

    /**
     * Adds a room to the floor.
     * <p>
     * The dimensions of the room are managed automatically. The length and
     * width of the room do not need to be specified, only the required space.
     *
     * @param newRoom object representing the new room
     * @throws IllegalArgumentException if area is less than Room.getMinArea()
     * @throws DuplicateRoomException if the room number on this floor is
     * already taken
     * @throws InsufficientSpaceException if there is insufficient space
     * available on the floor to be able to add the room
     * @ass1
     */
    // check that there is enough space available left on the floor
    public void addRoom(Room newRoom)
            throws DuplicateRoomException, InsufficientSpaceException {
        if (newRoom.getArea() < Room.getMinArea()) {
            throw new IllegalArgumentException(
                    "Area cannot be less than " + Room.getMinArea());
        }

        if (this.getRoomByNumber(newRoom.getRoomNumber()) != null) {
            throw new DuplicateRoomException(
                    "The room number " + newRoom.getRoomNumber()
                            + " is already taken on this floor.");
        }

        if ((this.occupiedArea() + newRoom.getArea()) > this.calculateArea()) {
            throw new InsufficientSpaceException("Insufficient space to add "
                    + "room. Floor area:" + this.calculateArea()
                    + "m^2, Occupied area: " + this.occupiedArea()
                    + "m^2, This room: " + newRoom.getArea() + "m^2");
        }

        // No problems, so add room to the list of rooms
        rooms.add(newRoom);
    }

    /**
     * Starts a fire drill in all rooms of the given type on the floor.
     * <p>
     * Only rooms of the given type must start a fire drill.
     * Rooms other than the given type must not start a fire drill.
     * <p>
     * If the room type given is null, then <b>all</b> rooms on the floor
     * must start a fire drill.
     *
     * @param roomType the type of room to carry out fire drills on; null if
     *                 fire drills are to be carried out in all rooms
     * @ass1
     */
    public void fireDrill(RoomType roomType) {
        for (Room r : this.rooms) {
            if (roomType == null || roomType == r.getType()) {
                r.setFireDrill(true);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in rooms on the floor.
     * <p>
     * All rooms must have their fire alarm cancelled regardless of room type.
     *
     * @ass1
     */
    public void cancelFireDrill() {
        for (Room r : this.rooms) {
            r.setFireDrill(false);
        }
    }

    /**
     * Function to change the dimensions of a floor. Has several checks to
     * ensure the new room is valid.
     *
     * @param newWidth new width dimension for the floor
     * @param newLength new length dimension for the floor
     * @throws IllegalArgumentException if the new dimensions are below the min
     * @throws FloorTooSmallException if the floor is too small for the rooms
     */
    public void changeDimensions(double newWidth, double newLength)
            throws IllegalArgumentException, FloorTooSmallException {
        //Checks that given dimensions are within the floors min specifics
        if (newWidth >= Floor.getMinWidth() &&
                newLength >= Floor.getMinLength()) {
            //Checks that the floors new area allows for all the rooms
            if (this.occupiedArea() <= (newLength * newWidth)) {
                this.length = newLength;
                this.width = newWidth;
            } else {
                throw new FloorTooSmallException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the floor's maintenance schedule, or null if it does not exist.
     * @return the floor's maintenance schedule
     */
    public MaintenanceSchedule getMaintenanceSchedule() {
        return schedule;
    }

    /**
     * Adds a maintenance schedule to this floor with the given room order.
     * Goes through the room order and loops back to start when finished.
     * Given order must not be null or empty and must not have rooms that don't
     * exist and rooms can't be in any form of consecutive order.
     * Will replace the previous maintenance schedule.
     *
     * @param roomOrder The order of rooms for maintenance
     * @throws IllegalArgumentException If the order is null or empty, one of
     * the rooms doesn't exist on this floor or a room is in the order
     * consecutively
     */
    public void createMaintenanceSchedule(List<Room> roomOrder)
            throws IllegalArgumentException {
        //Checks that the given room order isn't empty
        if (roomOrder == null) {
            throw new IllegalArgumentException();
        } else {
            //If there is only one room in the order, it must be on the floor
            if (roomOrder.size() == 1) {
                if (!(this.getRooms().contains(roomOrder.get(0)))) {
                    throw new IllegalArgumentException();
                }
            } else {
                int roomIndex = 0;
                //Goes through each room in the given order
                for (Room currentRoom : roomOrder) {
                    //Keep a variable to track the index of the current room
                    roomIndex++;
                    if (roomIndex == roomOrder.size()) {
                        roomIndex = 0;
                    }
                    //Checks if the current room in the order is equal to the
                    // previous room or, for the last room, the first room
                    //And if the room exists in the floor
                    if (!(this.getRooms().contains(currentRoom))
                            | currentRoom.equals(roomOrder.get(roomIndex))) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            //If a schedule already exists, wipes the previous maintenance
            if (schedule != null) {
                this.getMaintenanceSchedule().skipCurrentMaintenance();
                this.getMaintenanceSchedule().getCurrentRoom()
                        .setMaintenance(false);
            }
            schedule = new MaintenanceSchedule(roomOrder);
        }

    }

    /**
     * Returns the human-readable string representation of this floor.
     * <p>
     * The format of the string to return is
     * "Floor #'floorNumber': width='floorWidth'm, length='floorLength'm,
     * rooms='numRooms'"
     * without the single quotes, where 'floorNumber' is the floor's unique
     * number in the building, 'floorWidth' is the floor's width, 'floorLength'
     * is the floor's length, 'numRooms' is the number of rooms in the floor.
     * <p>
     * The floor's length and width should be formatted to two (2)
     * decimal places.
     * <p>
     * For example:
     * "Floor #6: width=12.80m, length=10.25m, rooms=15"
     *
     * @return string representation of this floor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Floor #%d: width=%.2fm, length=%.2fm, rooms=%d",
                this.floorNumber,
                this.width,
                this.length,
                this.rooms.size());
    }

    /**
     * Returns the machine-readable string representation of this floor and
     * all of its rooms and sensors.
     *
     * Appends the floor number, width, length and room size. After which, it
     * appends the schedule if it exists and each room using it's encode.
     * @return encoded string representation of this floor
     */
    @Override
    public String encode() {
        StringBuilder encodedFloor = new StringBuilder();
        //Adds the floors properties to the StringBuilder
        encodedFloor.append(this.getFloorNumber());
        encodedFloor.append(':');
        encodedFloor.append(String.format("%.2f", this.getWidth()));
        encodedFloor.append(':');
        encodedFloor.append(String.format("%.2f", this.getLength()));
        encodedFloor.append(':');
        encodedFloor.append(this.getRooms().size());
        //If there is a schedule, adds it to the encode copy
        if (this.getMaintenanceSchedule() != null) {
            encodedFloor.append(':');
            encodedFloor.append(this.getMaintenanceSchedule().encode());
        }
        //Goes through each room in the floor and adds its encoded version
        for (Room rooms: this.getRooms()) {
            encodedFloor.append(System.lineSeparator());
            encodedFloor.append(rooms.encode());
        }
        return encodedFloor.toString();
    }

    /**
     * Returns true if and only if this floor is equal to the other given floor.
     * Compares the floor number, width, length, number of floors and each room
     * using the room's appropriate equals method.
     *
     * @param obj - Object to compare with
     * @return - True if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        //Checks that object given is of class Floor
        if (obj instanceof Floor) {
            Floor floorCompare = (Floor) obj;
            //Compares floor number, width, length and number of rooms
            if (this.getFloorNumber() == floorCompare.getFloorNumber() &&
                    this.getWidth() == floorCompare.getWidth() &&
                    this.getLength() == floorCompare.getLength() &&
                    this.getRooms().size() == floorCompare.getRooms().size()) {
                //Goes through and compares each room with each other
                for (int i = 0; i < this.getRooms().size(); i++) {
                    try {
                        Room compareRoom = floorCompare.getRoomByNumber
                                (this.getRooms().get(i).getRoomNumber());
                        if (!this.getRooms().get(i).equals(compareRoom)) {
                            //Rooms that match room number don't equal
                            return false;
                        }
                    } catch (NullPointerException npe) {
                        //Room doesn't exist in compareFloor
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Generatres a unique hashcode using the floor number, length, width,
     * amount of rooms, each room's hashcode and class hashcode
     * @return hash code of this floor
     */
    @Override
    public int hashCode() {
        int hash = 11;
        hash = 31 * hash + (int) this.getLength();
        hash = 31 * hash + (int) this.getWidth();
        hash = 31 * hash + this.getFloorNumber();
        hash = 31 * hash + this.getRooms().size();
        for (Room reading: this.getRooms()) {
            hash  += reading.hashCode();
        }
        hash = 31 * hash + (this.getClass() == null ? 0 :
                this.getClass() .hashCode());
        return hash;
    }
}
