package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class which manages the initialisation and saving of buildings.
 */
public class BuildingInitialiser {

    /**
     * Loads a list of buildings from a save file with the given filename.
     * Makes use of several private methods to compartmentalise the function.
     * Goes through a large amount of checks to ensure validity of the file.
     * This function specifically creates the buildings and then runs through
     * each buildings floor to call the readFloor function.
     *
     * @param filename path of the file from which to load a list of buildings
     * @return a list containing all the buildings loaded from the file
     * @throws IOException if an IOException is encountered with any IO methods
     * @throws FileFormatException - if the format of the given file is invalid.
     * This includes several other exceptions that represent an invalid file,
     * including NumberFormatException, DuplicateFloorException,
     * NoFloorBelowException, FloorTooSmallException, IllegalArgumentException,
     * ArrayIndexOutOfBoundsException, NullPointerException,
     * InsufficientSpaceException, DuplicateRoomException and
     * DuplicateSensorException, as these apply to exceptions that can be
     * thrown from the methods called
     */
    public static List<Building> loadBuildings(String filename)
            throws IOException,  FileFormatException {
        //Creates a new File and Buffered reader
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        List<Building> buildings = new ArrayList<>();
        String buildingName;
        int numFloors;
        //Boolean value to determine if the file is valid
        boolean validFile = false;
        //Only reads file when there is content to be read. If not, validFile
        //boolean is kept false, resulting in the appropriate exception thrown
        while ((buildingName = br.readLine()) != null) {
            validFile = true;
            //Tries to parse the number of floors associated with the building
            try {
                numFloors = Integer.parseInt(br.readLine());
                if (numFloors < 0) {
                    throw new FileFormatException();
                }
            } catch (NumberFormatException n) {
                throw new FileFormatException();
            }
            Building compiledBuilding = new Building(buildingName);
            //Goes through each of the floors in the building and reads them
            for (int i = 0; i < numFloors; i++) {
                try {
                    compiledBuilding.addFloor(readFloor(br));
                }
                //Catch statement to convert the relevant exceptions into
                //FileFormatExceptions instead
                catch (DuplicateFloorException | NoFloorBelowException |
                        FloorTooSmallException | IllegalArgumentException |
                        ArrayIndexOutOfBoundsException |
                        NullPointerException e) {
                    throw new FileFormatException();
                }
            }
            //Checks if the number of floors specified and actual are different
            if (compiledBuilding.getFloors().size() != numFloors) {
                throw new FileFormatException();
            }
            buildings.add(compiledBuilding);
        }
        //If invalid (Empty file), throw exception
        if (!validFile) {
            throw new FileFormatException();
        }
        return buildings;
    }

    /**
     * This function is used to read the floors for the buildings. It will go
     * through to check the floor properties and then run a loop to read the
     * rooms of the floor with readRoom.
     *
     * @param br - BufferedReader for file reading
     * @return - A complied and decoded floor with rooms and sensors
     * @throws IOException if an IOException is encountered with any IO methods
     * @throws FileFormatException If the file is invalid
     */
    private static Floor readFloor(BufferedReader br)
            throws IOException, FileFormatException {
        int floorNumber, numRooms;
        double floorWidth, floorLength;
        List<Integer> scheduleOrder = new ArrayList<>();
        List<Room> schedule = new ArrayList<>();
        boolean maintenance = false;
        //Splits the floor string up into readable segments
        String [] splitFloor = br.readLine().split(":", 5);
        //Tries to parse the Floor's properties
        try {
            floorNumber = Integer.parseInt(splitFloor[0]);
            floorWidth = Double.parseDouble(splitFloor[1]);
            floorLength = Double.parseDouble(splitFloor[2]);
            numRooms = Integer.parseInt(splitFloor[3]);
            //If the number of rooms is negative or Floor number is less than 0
            if (numRooms < 0 | floorNumber <= 0) {
                throw new FileFormatException();
            }
            //If the floor has been split to the limit, then either there are
            //more colons (exception) or the floor has a schedule
            if (splitFloor.length > 4) {
                //Checks for more colons and throws error if it finds them
                if (splitFloor[4].contains(":")) {
                    throw new FileFormatException();
                }
                //Otherwise, splits the element into order for the schedule
                String [] scheduleTemp = splitFloor[4].split(",");
                for (String maintenanceRoom: scheduleTemp) {
                    scheduleOrder.add(Integer.parseInt(maintenanceRoom));
                }
                maintenance = true;
            }
        }
        catch (NumberFormatException n){
            throw new FileFormatException();
        }
        //Creates a new floor with the relevant parsed info
        Floor compiledFloor = new Floor(floorNumber, floorWidth, floorLength);
        //Goes through each of the rooms in the floor and attempts to read them
        for (int i = 0; i < numRooms; i++) {
            try {
                compiledFloor.addRoom(readRoom(br));
            }
            //Catches room exceptions that can occur and converts them
            catch (InsufficientSpaceException | DuplicateRoomException fe) {
                throw new FileFormatException();
            }
        }
        //If the floor has a schedule, attempts to add the schedule to the floor
        if (maintenance) {
            try {
                //For the schedule order, finds and adds the appropriate room
                for (Integer roomNum : scheduleOrder) {
                    try {
                        schedule.add(compiledFloor.getRoomByNumber(roomNum));
                    }catch (IllegalArgumentException iae) {
                        throw new FileFormatException();
                    }
                }
                compiledFloor.createMaintenanceSchedule(schedule);
            } catch (IllegalArgumentException iae) {
                throw new FileFormatException();
            }
        }
        //Check if the specified room amount and actual room amount is different
        if (compiledFloor.getRooms().size() != numRooms) {
            throw new FileFormatException();
        }
        return compiledFloor;
    }

    /**
     * Takes a room from a floor and parses it's info. It then runs through
     * the list of sensors and runs the readSensor function to parse the sensor.
     *
     * @param br - BufferedReader for file reading
     * @return - A complied and decoded room with sensors
     * @throws IOException if an IOException is encountered with any IO methods
     * @throws FileFormatException If the file is invalid
     */
    private static Room readRoom(BufferedReader br)
            throws IOException, FileFormatException {
        List<HazardSensor> sensors = new ArrayList<>();
        Map<HazardSensor,Integer> sensorMap = new LinkedHashMap<>();
        RoomType roomType;
        String evaluator = null;
        boolean weightingBased = false;
        int roomNum, numSensors;
        double roomArea;
        //Splits the room string up into readable segments
        String [] splitRoom = br.readLine().split(":", 5);
        //Tries to parse the room's properties
        try {
            roomNum = Integer.parseInt(splitRoom[0]);
            roomType = RoomType.valueOf(splitRoom[1]);
            roomArea = Double.parseDouble(splitRoom[2]);
            numSensors = Integer.parseInt(splitRoom[3]);
            //Checks if negative number of sensors
            if (numSensors < 0) {
                throw new FileFormatException();
            }
            //If the split length is max, either additional colons or evaluator
            if (splitRoom.length > 4) {
                if (splitRoom[4].contains(":")) {
                    throw new FileFormatException();
                }
                evaluator = splitRoom[4];
                //If weighting based, sets variable checker to true
                if (evaluator.equals("WeightingBased")) {
                    weightingBased = true;
                }
            }
        } catch (IllegalArgumentException n){
            throw new FileFormatException();
        }
        Room compiledRoom = new Room(roomNum, roomType, roomArea);
        //For each sensor in the room, attempts to read sensor
        for (int i = 0; i < numSensors; i++) {
            //The pair returns the parsed sensor and weighting if relevant
            Pair<Sensor, Integer> sensor = (readSensor(br, weightingBased));
            try {
                compiledRoom.addSensor(sensor.getKey());
            }
            catch (DuplicateSensorException dse) {
                throw new FileFormatException();
            }
            //Checks what type of evaluator it is and adds sensor to evaluator
            if (evaluator != null) {
                if (weightingBased) {
                    sensorMap.put((HazardSensor) sensor.getKey(), sensor.getValue());
                } else {
                    sensors.add((HazardSensor) sensor.getKey());
                }
            }
        }
        //Checks evaluator and sets the evaluator to the room
        if (evaluator!=null) {
            if (evaluator.equals("WeightingBased")) {
                try {
                    compiledRoom.setHazardEvaluator(new WeightingBasedHazardEvaluator(sensorMap));
                }
                catch (IllegalArgumentException iae) {
                    throw new FileFormatException();
                }
            } else if (evaluator.equals("RuleBased")) {
                compiledRoom.setHazardEvaluator(new RuleBasedHazardEvaluator(sensors));
            } else {
                throw new FileFormatException();
            }
        }
        //If the number of sensors given and actual are different, exception
        if (compiledRoom.getSensors().size() != numSensors) {
            throw new FileFormatException();
        }
        return compiledRoom;
    }

    /**
     * Takes a sensor from a room and decodes it for the room. Checks what kind
     * of sensor it is and creates a sensor of that specific type. Also creates
     * a hazard evaluator if the room has an evaluator.
     *
     * @param br - BufferedReader for file reading
     * @return - A complied and decoded sensor
     * @throws IOException if an IOException is encountered with any IO methods
     * @throws FileFormatException If the file is invalid
     */
    private static Pair<Sensor, Integer> readSensor(BufferedReader br,
                                                    boolean weightingBased)
            throws IOException, FileFormatException {
        String sensorType;
        int [] sensorReadings;
        int weighting = -1;
        Sensor compiledSensor;
        //Splits sensor into readable segments and parses info
        String [] splitSensor = br.readLine().split("[:|@]", 5);
        try {
            sensorType = splitSensor[0];
            //Splits the sensor readings into a new list
            String[] readingList = splitSensor[1].split(",");
            sensorReadings = new int[readingList.length];
            //Reads and parses each value in the sensor reading array
            for (int i = 0; i < readingList.length; i++) {
                sensorReadings[i] = Integer.parseInt(readingList[i]);
                if (sensorReadings[i] < 0) {
                    throw new FileFormatException();
                }
            }
        }
        catch (NumberFormatException n){
            throw new FileFormatException();
        }
        //Checks if the sensor type is OccupancySensor
        if (sensorType.equals(OccupancySensor.class.getSimpleName())) {
            //If the split sensor is too long (too many colons), exception
            if ((splitSensor.length > 5) |
                    (splitSensor.length > 4 && !weightingBased)) {
                throw new FileFormatException();
            }
            int frequency, capacity;
            //Parses and creates the occupancy sensor with relevant info
            try {
                frequency = Integer.parseInt(splitSensor[2]);
                capacity = Integer.parseInt(splitSensor[3]);
                //If capacity is less than 0, throw exception
                if (capacity < 0) {
                    throw new FileFormatException();
                }
                compiledSensor = new OccupancySensor
                        (sensorReadings,frequency, capacity);
            }
            catch (IllegalArgumentException iae){
                throw new FileFormatException();
            }
        }
        //Checks if the sensor type is CarbonDioxideSensor
        else if (sensorType.equals(CarbonDioxideSensor.class.getSimpleName())) {
            //If the split sensor is too long (too many colons), exception
            if ((splitSensor.length > 6) |
                    (splitSensor.length > 5 && !weightingBased)) {
                throw new FileFormatException();
            }
            int frequency, ideal, limit;
            //Parses and creates the CO2 sensor with relevant info
            try {
                frequency = Integer.parseInt(splitSensor[2]);
                ideal = Integer.parseInt(splitSensor[3]);
                limit = Integer.parseInt(splitSensor[4]);
                //Checks if limit is greater than ideal or either are below 0
                if (limit > ideal | ideal < 0 | limit < 0) {
                    throw new FileFormatException();
                }
                compiledSensor = new CarbonDioxideSensor
                        (sensorReadings, frequency, ideal, limit);
            }
            catch (IllegalArgumentException iae){
                throw new FileFormatException();
            }
        }
        //Checks if the sensor type is NoiseSensor
        else if (sensorType.equals(NoiseSensor.class.getSimpleName())) {
            //If the split sensor is too long (too many colons), exception
            if ((splitSensor.length > 4) |
                    (splitSensor.length > 3 && !weightingBased)) {
                throw new FileFormatException();
            }
            int frequency;
            //Parses and creates the noise sensor with relevant info
            try {
                frequency = Integer.parseInt(splitSensor[2]);
                compiledSensor = new NoiseSensor(sensorReadings,frequency);
            }
            catch (IllegalArgumentException iae){
                throw new FileFormatException();
            }
        }
        //Checks if the sensor type is TemperatureSensor
        else if (sensorType.equals(TemperatureSensor.class.getSimpleName())) {
            //If the split sensor is too long (too many colons), exception
            if ((splitSensor.length > 3) |
                    (splitSensor.length > 2 && !weightingBased)) {
                throw new FileFormatException();
            }
            //Creates the temperature sensor with sensor readings
            try {
                compiledSensor = new TemperatureSensor(sensorReadings);
            }
            catch (IllegalArgumentException iae) {
                throw new FileFormatException();
            }
        } else {
            throw new FileFormatException();
        }
        //If the room has a weightingBased evaluator, incorporates the weighting
        if (weightingBased) {
            try {
                weighting=Integer.parseInt(splitSensor[splitSensor.length - 1]);
            }
            catch (NumberFormatException n) {
                throw new FileFormatException();
            }
        }
        return new Pair<>(compiledSensor, weighting);
    }

}
