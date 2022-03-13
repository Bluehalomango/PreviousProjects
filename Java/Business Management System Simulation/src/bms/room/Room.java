package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.sensors.*;
import bms.util.Encodable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a room on a floor of a building.
 * <p>
 * Each room has a room number (unique for this floor, ie. no two rooms on the
 * same floor can have the same room number), a type to indicate its intended
 * purpose, and a total area occupied by the room in square metres.
 * <p>
 * Rooms also need to record whether a fire drill is currently taking place in
 * the room.
 * <p>
 * Rooms can have one or more sensors to monitor hazard levels
 * in the room.
 * @ass1
 */
public class    Room implements Encodable {

    /**
     * Unique room number for this floor.
     */
    private int roomNumber;

    /**
     * The type of room. Different types of rooms can be used for different
     * activities.
     */
    private RoomType type;

    /**
     * List of sensors located in the room. Rooms may only have up to one of
     * each type of sensor. Alphabetically sorted by class name.
     */
    private List<Sensor> sensors;

    /**
     * Area of the room in square metres.
     */
    private double area;

    /**
     * Minimum area of all rooms, in square metres.
     * (Note that dimensions of the room are irrelevant).
     * Defaults to 5.
     */
    private static final int MIN_AREA = 5;

    /**
     * Records whether there is currently a fire drill.
     */
    private boolean fireDrill;

    /**
     * Current state of the room
     */
    private RoomState roomState;

    /**
     * Records whether the room is set for maintenance
     */
    private boolean maintenance;

    /**
     * Variable to hold the hazard evaluator for the room
     */
    private HazardEvaluator evaluator;

    /**
     * Creates a new room with the given room number.
     *
     * @param roomNumber the unique room number of the room on this floor
     * @param type the type of room
     * @param area the area of the room in square metres
     * @ass1
     */
    public Room(int roomNumber, RoomType type, double area) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;
        this.roomState = RoomState.OPEN;
        this.maintenance = false;
        this.sensors = new ArrayList<>();
        this.fireDrill = false;
    }

    /**
     * Returns room number of the room.
     *
     * @return the room number on the floor
     * @ass1
     */
    public int getRoomNumber() {
        return this.roomNumber;
    }

    /**
     * Returns area of the room.
     *
     * @return the room area in square metres
     * @ass1
     */
    public double getArea() {
        return this.area;
    }

    /**
     * Returns the minimum area for all rooms.
     * <p>
     * Rooms must be at least 5 square metres in area.
     *
     * @return the minimum room area in square metres
     * @ass1
     */
    public static int getMinArea() {
        return MIN_AREA;
    }

    /**
     * Returns the type of the room.
     *
     * @return the room type
     * @ass1
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Returns whether there is currently a fire drill in progress.
     *
     * @return current status of fire drill
     * @ass1
     */
    public boolean fireDrillOngoing() {
        return this.fireDrill;
    }

    /**
     * Returns the list of sensors in the room.
     * <p>
     * The list of sensors stored by the room should always be in alphabetical
     * order, by the sensor's class name.
     * <p>
     * Adding or removing sensors from this list should not affect the room's
     * internal list of sensors.
     *
     * @return list of all sensors in alphabetical order of class name
     * @ass1
     */
    public List<Sensor> getSensors() {
        return new ArrayList<>(this.sensors);
    }

    /**
     * Change the status of the fire drill to the given value.
     *
     * @param fireDrill whether there is a fire drill ongoing
     * @ass1
     */
    public void setFireDrill(boolean fireDrill) {
        this.fireDrill = fireDrill;
    }

    /**
     * Return the given type of sensor if there is one in the list of sensors;
     * return null otherwise.
     *
     * @param sensorType the type of sensor which matches the class name
     *                   returned by the getSimpleName() method,
     *                   e.g. "NoiseSensor" (no quotes)
     * @return the sensor in this room of the given type; null if none found
     * @ass1
     */
    public Sensor getSensor(String sensorType) {
        for (Sensor s : this.getSensors()) {
            if (s.getClass().getSimpleName().equals(sensorType)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Adds a sensor to the room if a sensor of the same type is not
     * already in the room.
     * <p>
     * The list of sensors should be sorted after adding the new sensor, in
     * alphabetical order by simple class name ({@link Class#getSimpleName()}).
     *
     * @param sensor the sensor to add to the room
     * @throws DuplicateSensorException if the sensor to add is of the
     * same type as a sensor already in this room
     * @ass1
     */
    public void addSensor(Sensor sensor)
            throws DuplicateSensorException {
        for (Sensor s : sensors) {
            if (s.getClass().equals(sensor.getClass())) {
                throw new DuplicateSensorException(
                        "Duplicate sensor of type: "
                                + s.getClass().getSimpleName());
            }
        }
        //Removes hazard evaluator
        this.evaluator = null;
        sensors.add(sensor);
        sensors.sort(Comparator.comparing(s -> s.getClass().getSimpleName()));
    }

    /**
     * Returns whether there is currently maintenance in progress.
     * @return current status of maintenance
     */
    public boolean maintenanceOngoing() {
        return maintenance;
    }

    /**
     * Change the status of maintenance to the given value.
     * @param maintenance Maintenance value to be set
     */
    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    /**
     * Returns this room's hazard evaluator, or null if none exists.
     * @return room's hazard evaluator
     */
    public HazardEvaluator getHazardEvaluator() {
        //If hazard evaluator exists, returns it
        if (this.evaluator == null) {
            return null;
        } else {
            return this.evaluator;
        }
    }

    /**
     * Sets the room's hazard evaluator to a new hazard evaluator.
     * @param hazardEvaluator new hazard evaluator for the room to use
     */
    public void setHazardEvaluator(HazardEvaluator hazardEvaluator) {
        this.evaluator = hazardEvaluator;
    }

    /**
     * Evaluates the room status based upon current information.
     * Can change it to EVACUATE if a temperature sensor reads a hazard level of
     * 100 or there is a firedrill, MAINTENANCE if there is ongoing maintenance
     * or OPEN otherwise.
     *
     * @return current room status
     */
    public RoomState evaluateRoomState() {
        boolean fire = false;
        //Goes through each sensor in the room
        for (Sensor sensor:this.getSensors()) {
            //Checks if a temperature sensor is reading a hazard level of 100
            if (sensor.getClass() == TemperatureSensor.class &&
                    ((TemperatureSensor) sensor).getHazardLevel() == 100) {
                fire = true;
            }
        }
        //Sets the room state based on various conditions
        if (fire | this.fireDrillOngoing()) {
            this.roomState = RoomState.EVACUATE;
        } else if (maintenanceOngoing()) {
            this.roomState = RoomState.MAINTENANCE;
        } else {
            this.roomState = RoomState.OPEN;
        }
        return this.roomState;
    }


    /**
     * Returns the human-readable string representation of this room.
     * <p>
     * The format of the string to return is
     * "Room #'roomNumber': type='roomType', area='roomArea'm^2,
     * sensors='numSensors'"
     * without the single quotes, where 'roomNumber' is the room's unique
     * number, 'roomType' is the room's type, 'area' is the room's type,
     * 'numSensors' is the number of sensors in the room.
     * <p>
     * The room's area should be formatted to two (2) decimal places.
     * <p>
     * For example:
     * "Room #42: type=STUDY, area=22.50m^2, sensors=2"
     *
     * @return string representation of this room
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Room #%d: type=%s, area=%.2fm^2, sensors=%d",
                this.roomNumber,
                this.type,
                this.area,
                this.sensors.size());
    }

    /**
     * Returns the machine-readable string representation of this room and
     * all of its sensors.
     *
     * Adds the necessary room info to a StringBuilder and then appends the
     * encoded representation of each sensor, with a new line separating them.
     *
     * @return encoded string representation of this room
     */
    @Override
    public String encode() {
        StringBuilder encodedRoom = new StringBuilder();
        //Appends room properties to the StringBuilder
        encodedRoom.append(this.getRoomNumber());
        encodedRoom.append(':');
        encodedRoom.append(this.getType());
        encodedRoom.append(':');
        encodedRoom.append(String.format("%.2f", this.getArea()));
        encodedRoom.append(':');
        encodedRoom.append(this.getSensors().size());
        //Checks if the room has an hazard evaluator
        if (this.getHazardEvaluator() != null) {
            encodedRoom.append(':');
            encodedRoom.append(this.getHazardEvaluator().toString());
        }
        int i = 0;
        //Goes through the list of sensors, with if statements for each type
        for (Sensor sensors: this.getSensors()) {
            encodedRoom.append(System.lineSeparator());
            if (sensors.getClass() == OccupancySensor.class) {
                encodedRoom.append(((OccupancySensor) sensors).encode());
            }
            else if (sensors.getClass() == CarbonDioxideSensor.class) {
                encodedRoom.append(((CarbonDioxideSensor) sensors).encode());
            }
            else if (sensors.getClass() == TemperatureSensor.class) {
                encodedRoom.append(((TemperatureSensor) sensors).encode());
            }
            else if (sensors.getClass() == NoiseSensor.class) {
                encodedRoom.append(((NoiseSensor) sensors).encode());
            }
            //If the hazard evaluator is weighting based, adds weightings
            if (this.getHazardEvaluator().getClass() ==
                    WeightingBasedHazardEvaluator.class) {
                encodedRoom.append("@");
                encodedRoom.append(((WeightingBasedHazardEvaluator)
                        this.getHazardEvaluator()).getWeightings().get(i));
                i++;
                }
            }
        return encodedRoom.toString();
    }

    /**
     * Returns true if and only if this room is equal to the other given room.
     *
     * Checks the room number, room type, area, number of sensors and then
     * compares the lists of sensors using the relevant sensor equals method.
     *
     * @param obj - Object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        //Checks that the object is an instance of a room class
        if (obj instanceof Room) {
            Room roomCompare = (Room) obj;
            //Compares room number, type, area and sensor array size
            if (this.getRoomNumber() == roomCompare.getRoomNumber() &&
                    this.getType() == roomCompare.getType() &&
                    this.getArea() == roomCompare.getArea() &&
                    this.getSensors().size()==roomCompare.getSensors().size()) {
                //Goes through the list of sensors and compares them all
                for (int i = 0; i < this.getSensors().size(); i++) {
                    try {
                        Sensor compareSensor = this.getSensors().get(i);
                        if (!this.getSensor(compareSensor.getClass().
                                getSimpleName()).equals(compareSensor)) {
                            return false;
                        }
                    } catch (NullPointerException npe) {
                        return false;
                    }
                    if (!this.getSensors().get(i).
                            equals(roomCompare.getSensors().get(i))) {
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
     * Creates a unique hashcode using the room's number, area, sensor amount,
     * sensor hashcodes, class hashcode and room type hashcode.
     *
     * @return hash code of this room
     */
    @Override
    public int hashCode() {
        int hash = 13;
        hash = 31 * hash + (int) this.getArea();
        hash = 31 * hash + this.getRoomNumber();
        hash = 31 * hash + this.getSensors().size();
        for (Sensor reading: this.getSensors()) {
            hash += reading.hashCode();
        }
        hash = 31 * hash + (this.getClass() == null ? 0 :
                this.getClass() .hashCode());
        hash = 31 * hash + (this.getType() == null ? 0 :
                this.getType() .hashCode());
        return hash;
    }

}
