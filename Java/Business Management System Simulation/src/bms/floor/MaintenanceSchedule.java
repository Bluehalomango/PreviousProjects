package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.TimedItem;
import bms.util.TimedItemManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class used to simulate maintenance for rooms in a floor.
 */
public class MaintenanceSchedule implements TimedItem, Encodable {

    /**
     * List of rooms to receive maintenance in order
     */
    private List<Room> schedule;

    /**
     * A map to hold the three types of rooms and their modifiers
     */
    private Map<RoomType, Double> roomTypes = new HashMap<>();

    /**
     * Room currently under maintenance
     */
    private Room currentRoom;

    /**
     * Time elapsed relative for the current room
     */
    private int timeElapsedRelative;

    /**
     * Creates a new maintenance schedule for a floor's list of rooms.
     * @param roomOrder list of rooms on which to perform maintenance, in order
     * @requies roomOrder != null && roomOrder.size() > 0
     */
    public MaintenanceSchedule(List<Room> roomOrder) {
        schedule = new ArrayList<>(roomOrder);
        schedule.get(0).setMaintenance(true);
        currentRoom = schedule.get(0);
        timeElapsedRelative = 0;
        //Registers the schedule with the timer manager
        TimedItemManager.getInstance().registerTimedItem(this);
        //Adding the room types and their maintenance time modifiers to a map
        roomTypes.put(RoomType.STUDY, 1.0);
        roomTypes.put(RoomType.OFFICE, 1.5);
        roomTypes.put(RoomType.LABORATORY, 2.0);
    }

    /**
     * Calculates how long maintenance will take for a given room. Is affected
     * by room size and room type, will always be 5 minutes or greater.
     *
     * @param room Room to determine maintenance time on
     * @return How long maintenance will take in minutes
     */
    public int getMaintenanceTime(Room room) {
        //Calculates the maintenance time for the given room using area and type
        double relativeArea = room.getArea() - Room.getMinArea();
        double typeMultiplier = roomTypes.get(room.getType());
        return (int) Math.round(5 + (relativeArea * 0.2 * typeMultiplier));
    }

    /**
     * Returns the room which is currently under maintenance.
     * @return room currently in maintenance
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Returns how many minutes have passed for the current room's maintenance
     * @return time spent maintaining current room
     */
    public int getTimeElapsedCurrentRoom() {
        return this.timeElapsedRelative;
    }

    /**
     * Progresses the maintenance schedule by one minute.
     * Will not progress if rooms are being evacuated. Additionally, will move
     * to the next room when the current room has finished it's maintenance.
     */
    public void elapseOneMinute() {
        //Checks the room state is not evacuated
        if (getCurrentRoom().evaluateRoomState() != RoomState.EVACUATE) {
            //Checks if the maintenance is finishing
            if (getMaintenanceTime(getCurrentRoom())
                    == getTimeElapsedCurrentRoom()) {
                //Resets maintenance variables and changes next room
                currentRoom.setMaintenance(false);
                timeElapsedRelative = 0;
                int nextRoomIndex = (schedule.indexOf(currentRoom))+1;
                //Resets the maintenance back to the start at the end
                if (nextRoomIndex == schedule.size()) {
                    currentRoom = schedule.get(0);
                } else {
                    //Sets the maintenance room to the next in the list
                    currentRoom = schedule.get(nextRoomIndex);
                }
                currentRoom.setMaintenance(true);
            } else {
                //Increments the time elapsed if the maintenance is occurring
                timeElapsedRelative++;
            }
        }
    }

    /**
     * Stops the current maintenance and moves to the next room.
     */
    public void skipCurrentMaintenance() {
        //Moves the current room to the next in the list and resets timer
        currentRoom.setMaintenance(false);
        int nextRoomIndex = (schedule.indexOf(currentRoom))+1;
        timeElapsedRelative = 0;
        //Resets the maintenance back to the start at the end
        if (nextRoomIndex == schedule.size()) {
            currentRoom = schedule.get(0);
        } else {
            currentRoom = schedule.get(nextRoomIndex);
        }
        currentRoom.setMaintenance(true);
    }

    /**
     * Returns the machine-readable representation of this maintenance schedule.
     * Is a list of the room numbers for the maintenance order.
     * @return encoded string representation of this maintenance schedule
     */
    @Override
    public String encode() {
        StringBuilder encodedSchedule = new StringBuilder();
        encodedSchedule.append(schedule.get(0).getRoomNumber());
        //Appends each room in the schedule to the StringBuilder
        for (int i = 1; i < schedule.size(); i++) {
            encodedSchedule.append(',');
            encodedSchedule.append(schedule.get(i).getRoomNumber());
        }
        return encodedSchedule.toString();
    }

    /**
     * Returns the human-readable representation of this maintenance schedule.
     * @return string representation of this maintenance schedule
     */
    @Override
    public String toString() {
        return String.format("MaintenanceSchedule: currentRoom=#%d, " +
                        "currentElapsed=%d",
                this.getCurrentRoom().getRoomNumber(),
                this.getTimeElapsedCurrentRoom());
    }
}
