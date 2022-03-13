package bms.building;

import bms.exceptions.DuplicateFloorException;
import bms.exceptions.FireDrillException;
import bms.exceptions.FloorTooSmallException;
import bms.exceptions.NoFloorBelowException;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.FireDrill;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a building of floors, which in turn, contain rooms.
 * <p>
 * A building needs to manage and keep track of the floors that make up the
 * building.
 * <p>
 * A building can be evacuated, which causes all rooms on all floors within
 * the building to be evacuated.
 * @ass1
 */
public class Building implements FireDrill, Encodable {

    /**
     * The name of the building.
     */
    private String name;

    /**
     * List of floors tracked by the building.
     */
    private List<Floor> floors;

    /**
     * Creates a new empty building with no rooms.
     *
     * @param name name of this building, eg. "General Purpose South"
     * @ass1
     */
    public Building(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
    }

    /**
     * Returns the name of the building.
     *
     * @return name of this building
     * @ass1
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a new list containing all the floors in this building.
     * <p>
     * Adding or removing floors from this list should not affect the
     * building's internal list of floors.
     *
     * @return new list containing all floors in the building
     * @ass1
     */
    public List<Floor> getFloors() {
        return new ArrayList<>(this.floors);
    }

    /**
     * Searches for the floor with the specified floor number.
     * <p>
     * Returns the corresponding Floor object, or null if the floor was not
     * found.
     *
     * @param floorNumber floor number of floor to search for
     * @return floor with the given number if found; null if not found
     * @ass1
     */
    public Floor getFloorByNumber(int floorNumber) {
        for (Floor floor : this.floors) {
            if (floor.getFloorNumber() == floorNumber) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Adds a floor to the building.
     * <p>
     * If the given arguments are invalid, the floor already exists,
     * there is no floor below, or the floor below does not have enough area
     * to support this floor, an exception should be thrown and no action
     * should be taken.
     *
     * @param newFloor object representing the new floor
     * @throws IllegalArgumentException if floor number is &lt;= 0,
     * width &lt; Floor.getMinWidth(), or length &lt; Floor.getMinLength()
     * @throws DuplicateFloorException if a floor at this level already exists
     * in the building
     * @throws NoFloorBelowException if this is at level 2 or above and there
     * is no floor below to support this new floor
     * @throws FloorTooSmallException if this is at level 2 or above and
     * the floor below is not big enough to support this new floor
     *
     * @ass1
     */
    public void addFloor(Floor newFloor) throws
            IllegalArgumentException, DuplicateFloorException,
            NoFloorBelowException, FloorTooSmallException {

        int newFloorNumber = newFloor.getFloorNumber();
        if (newFloorNumber < 1) {
            throw new IllegalArgumentException(
                    "Floor number must be 1 or higher.");
        } else if (newFloor.getWidth() < Floor.getMinWidth())  {
            throw new IllegalArgumentException(
                    "Width cannot be less than " + Floor.getMinWidth());
        } else if (newFloor.getLength() < Floor.getMinLength()) {
            throw new IllegalArgumentException(
                    "Length cannot be less than " + Floor.getMinLength());
        }
        if (this.getFloorByNumber(newFloorNumber) != null) {
            throw new DuplicateFloorException(
                    "This floor level already exists in the building.");
        }

        Floor floorBelow = this.getFloorByNumber(newFloorNumber - 1);
        if (newFloorNumber >= 2 && floorBelow == null) {
            throw new NoFloorBelowException("There is no floor below to "
                    + "support this new floor.");
        }
        if (newFloorNumber >= 2 && (newFloor.getWidth() > floorBelow.getWidth()
                || newFloor.getLength() > floorBelow.getLength())) {
            throw new FloorTooSmallException("The floor below does not "
                    + "have enough area to support this floor. ");
        }

        // No problems, so add floor to the list of floors
        floors.add(newFloor);
    }

    /**
     * Start a fire drill in all rooms of the given type in the building.
     * Only rooms of the given type must start a fire alarm.
     * Rooms other than the given type must not start a fire alarm.
     * * <p>
     * If the room type given is null, then <b>all</b> rooms in the building
     * must start a fire drill.
     * <p>
     * If there are no rooms (of any type) in the building, a
     * FireDrillException must be thrown. Note that floors may be in the
     * building, but the floors may not contain rooms yet.
     *
     * @param roomType the type of room to carry out fire drills on; null if
     *                 fire drills are to be carried out in all rooms
     * @throws FireDrillException if there are no floors in the building, or
     * there are floors but no rooms in the building
     * @ass1
     */
    public void fireDrill(RoomType roomType) throws FireDrillException {
        if (this.floors.size() < 1) {
            throw new FireDrillException("Cannot conduct fire drill because "
                    + "there are no floors in the building yet!");
        }
        boolean hasRooms = false;
        for (Floor floor : this.floors) {
            if (!floor.getRooms().isEmpty()) {
                hasRooms = true;
            }
        }
        if (!hasRooms) {
            throw new FireDrillException("Cannot conduct fire drill because "
                    + "there are no rooms in the building yet!");
        } else {
            for (Floor floor : this.floors) {
                floor.fireDrill(roomType);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in the building.
     * <p>
     * All rooms must have their fire alarm cancelled regardless of room type.
     *
     * @ass1
     */
    public void cancelFireDrill() {
        for (Floor floor : this.floors) {
            floor.cancelFireDrill();
        }
    }

    /**
     * Renovate the given floor by changing the width and length using the given
     * values. Proceeds through a number of checks to determine validity.
     *
     * @param floorNumber - Floor to renovate
     * @param newWidth - New width of floor
     * @param newLength - New length of floor
     * @throws IllegalArgumentException - If the floor does not exist or the
     * new dimensions (length and width) are below their associated minimum
     * @throws FloorTooSmallException - If the floor below is too small, floor
     * above is too large or the new floor does not have enough space for rooms
     */
    public void renovateFloor (int floorNumber, double newWidth,
                               double newLength)
            throws IllegalArgumentException, FloorTooSmallException {
        Floor oldFloor = getFloorByNumber(floorNumber);
        //Checks that the floor to be changed exists and that its new
        //measurements are above the minimum width and length for floors
        if (oldFloor == null || newWidth < Floor.getMinWidth() ||
                newLength < Floor.getMinLength()) {
            throw new IllegalArgumentException();
        }
        //Calculates the new area with the given length and width
        double newArea = newLength * newWidth;
        //Checks if the floor is to be made larger
        if (oldFloor.calculateArea() < newArea) {
            //Checks if the floor is above the first floor
            if (floorNumber > 1) {
                Floor belowFloor = this.getFloorByNumber(floorNumber - 1);
                //Checks that the new floor allows for the floor below
                if (belowFloor.calculateArea() < newArea) {
                    throw new FloorTooSmallException();
                } else {
                    oldFloor.changeDimensions(newWidth, newLength);
                }
            } else {
                oldFloor.changeDimensions(newWidth, newLength);
            }
        } else if (oldFloor.calculateArea() > newArea) {
            //Checks if the floor is to be made smaller after renovation
            Floor aboveFloor = this.getFloorByNumber(floorNumber + 1);
            //Checks if there exists a floor above the current floor
            if (aboveFloor != null) {
                //Checks if the new floor will allow for the floor above
                if (aboveFloor.calculateArea() > newArea) {
                    throw new FloorTooSmallException();
                }
            }
            //Checks that the new and smaller floor can fit all its rooms
            double totalOccupiedSpace = 0;
            for (Room currentRoom: oldFloor.getRooms()) {
                totalOccupiedSpace += currentRoom.getArea();
            }
            //If the rooms take up more space than available, throw exception
            if (totalOccupiedSpace <= newArea) {
                oldFloor.changeDimensions(newWidth, newLength);
            } else {
                throw new FloorTooSmallException();
            }
        } else {
            //Assumes that the new floors area matches the original area
            oldFloor.changeDimensions(newWidth, newLength);
        }
    }

    /**
     * Returns the human-readable string representation of this building.
     * <p>
     * The format of the string to return is
     * "Building: name="'buildingName'", floors='numFloors'"
     * without the single quotes, where 'buildingName' is the building's name,
     * and 'numFloors' is the number of floors in the building.
     * <p>
     * For example:
     * "Building: name="GP South", floors=7"
     *
     * @return string representation of this building
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Building: name=\"%s\", floors=%d",
                this.name, this.floors.size());
    }

    /**
     * Returns the machine-readable string representation of this building and
     * all of its floors, rooms and sensors. Appends the building's info and
     * then appends the info returned from the floors encode function.
     *
     * @return encoded string representation of this building
     */
    @Override
    public String encode() {
        //Creates new StringBuilder and appends relevant info to it
        StringBuilder encodedBuilding = new StringBuilder();
        encodedBuilding.append(this.getName());
        encodedBuilding.append(System.lineSeparator());
        encodedBuilding.append(this.getFloors().size());
        //Goes through each floor in the building and appends each encoded info
        for (Floor floors: this.getFloors()) {
            encodedBuilding.append(System.lineSeparator());
            encodedBuilding.append(floors.encode());
        }
        return encodedBuilding.toString();
    }

    /**
     * Returns true if this building is equal to the other given building.
     * Compares building name, number of floors and floor's equal each other.
     *
     * @param obj The object to compare against
     * @return - True if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        //Checks that given object to compare against isn't null
        if (obj == null) {
            return false;
        }
        //Checks that given objects class matches the building class
        if (obj instanceof Building) {
            //Casts the given object to the appropriate object
            Building buildingCompare = (Building) obj;
            //Checks that the two objects share their name and size
            if (this.getName().equals(buildingCompare.getName()) && this.
                    getFloors().size() == buildingCompare.getFloors().size()) {
                //Checks that each floor in the buildings is equal
                for (int i = 0; i < this.getFloors().size(); i++) {
                    if (!this.getFloors().get(i)
                            .equals(buildingCompare.getFloors().get(i))) {
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
     * Generates the unique hashcode made using the floor array length, the
     * hashcodes of the floors and the building name and class hashcode.
     *
     * @return hash code of this building
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.getName().hashCode();
        hash = 31 * hash + this.getFloors().size();
        //Applies unique hashcode of each floor in building to this hashcode
        for (Floor reading: this.getFloors()) {
            hash = 31 * hash + reading.hashCode();
        }
        //Adds the hashcode of the class
        hash = 31 * hash +
                (this.getClass() == null ? 0 : this.getClass().hashCode());
        return hash;
    }
}
