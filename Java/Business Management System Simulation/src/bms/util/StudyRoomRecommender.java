package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.ComfortSensor;
import bms.sensors.Sensor;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that provides a recommendation for a study room in a building.
 */
public class StudyRoomRecommender {


    /**
     * Returns a room in the given building that is most suitable for study.
     * Checks several criteria, including:
     *      - Room's type
     *      - Room's status
     *      - Room's comfort level
     *      - Floor level of room
     *
     * Makes use of several private methods to compartmentalise the process/
     * This component goes through the floors in the building and finds rooms
     * that meet the first two criteria. It also oversees the best room manager.
     *
     * @param building - The building to search for a study room
     * @return the room best suited to study in
     */
    public static Room recommendStudyRoom(Building building) {
        //Checks if the building given is null
        if (building == null) {
            return null;
        } else {
            Room bestRoom = null;
            List<Room> candidateRooms;
            //Sets up a loop to go through each floor but doesn't always finish
            for (Floor floor: building.getFloors()) {
                candidateRooms = new ArrayList<>();
                //Goes through the entire floor and compares the rooms
                for (Room currentRoom : floor.getRooms()) {
                    //If the room is open and a study room, adds it to a list
                    if (currentRoom.evaluateRoomState() == RoomState.OPEN
                            && currentRoom.getType() == RoomType.STUDY) {
                        candidateRooms.add(currentRoom);
                    }
                }
                // If there are no possible candidate rooms on this floor,
                // returns previous best room
                if (candidateRooms.isEmpty()) {
                    return bestRoom;
                } else if (bestRoom !=null && calculateComfortLevel
                        (calculateBestRoom(candidateRooms)) <=
                        calculateComfortLevel(bestRoom)) {
                    //Checks if the floor has any better rooms than the last
                    //selected best room. If not, returns last selection
                    return bestRoom;
                } else {
                    //Sets the current best room to the best of the floor
                    bestRoom = calculateBestRoom(candidateRooms);
                }
            }
            return bestRoom;
        }
    }

    /**
     * This helper function is designed to calculate the best room after
     * searching through a floor in a building, as it compares the previous
     * best room to each candidate room in the current floor. To do this, it
     * uses another private function.
     *
     * @param candidateRooms The rooms in the floor that fulfil criteria 1 and 2
     * @return The best room on a floor or the previous best room
     */
    private static Room calculateBestRoom (List<Room> candidateRooms) {
        Room bestRoom = null;
        //Goes through each of the possible rooms from a floor
        for (Room currentRoom: candidateRooms) {
            //If there is no current best room, sets the first room to best
            if (bestRoom == null) {
                bestRoom = currentRoom;
            } else if (calculateComfortLevel(currentRoom)
                    > calculateComfortLevel(bestRoom)){
                //Checks if the current room is better than the best room
                bestRoom = currentRoom;
            }
        }
        return bestRoom;
    }

    /**
     * This private function is used to calculate the comfort level of a
     * specific room. It goes through each sensor and complies the info.
     *
     * @param room To room to calculate the comfort level for
     * @return The comfort level for that room
     */
    private static double calculateComfortLevel (Room room) {
        int comfortAverage = 0;
        int comfortCount = 0;
        //Goes through each sensor in a room and determines their comfort value
        for (Sensor sensors : room.getSensors()) {
            ComfortSensor setSensor = (ComfortSensor) sensors;
            //Adds the comfort value to the average and increments the counter
            comfortAverage += setSensor.getComfortLevel();
            comfortCount ++;
        }
        return (double) comfortAverage/comfortCount;
    }
}
