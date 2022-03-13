package bms.sensors;

import bms.util.Encodable;
import bms.util.TimedItem;

/**
 * A sensor that measures the number of people in a room.
 * @ass1
 */
public class OccupancySensor extends TimedSensor
        implements ComfortSensor, HazardSensor, Sensor, Encodable, TimedItem {
    /**
     * Maximum capacity of the space the sensor is monitoring.
     */
    private int capacity;

    /**
     * Creates a new occupancy sensor with the given sensor readings, update
     * frequency and capacity.
     * <p>
     * The given capacity must be greater than or equal to zero.
     *
     * @param sensorReadings a non-empty array of sensor readings
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @param capacity maximum allowable number of people in the room
     * @throws IllegalArgumentException if capacity is less than zero
     * @ass1
     */
    public OccupancySensor(int[] sensorReadings, int updateFrequency,
                           int capacity) {
        super(sensorReadings, updateFrequency);

        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be >= 0");
        }

        this.capacity = capacity;
    }

    /**
     * Returns the capacity of this occupancy sensor.
     *
     * @return capacity
     * @ass1
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the hazard level based on the ratio of the current sensor reading
     * to the maximum capacity.
     * <p>
     * When the current reading is equal to or more than the capacity, the
     * hazard level is equal to 100 percent.
     * <p>
     * For example, a room with a maximum capacity of 21 people and current
     * occupancy of 8 people would have a hazard level of 38.
     * A room with a maximum capacity of 30 people and a current occupancy of
     * 34 people would have a hazard level of 100.
     * <p>
     * Floating point division should be used when performing the calculation,
     * however the resulting floating point number should be <i>rounded to the
     * nearest integer</i> before being returned.
     *
     * @return the current hazard level as an integer between 0 and 100
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        final int currentReading = this.getCurrentReading();

        if (currentReading >= this.capacity) {
            return 100;
        }
        double occupancyRatio = ((double) currentReading) / this.capacity;
        double occupancyPct = 100 * occupancyRatio;
        return (int) Math.round(occupancyPct);
    }

    /**
     * Returns the current comfort level as observed by the sensor.
     * Calculated with the complement of the percentage given by the
     * current sensor reading divided by the maximum capacity.
     *
     * @return the current comfort level as an integer between 0 and 100
     */
    @Override
    public int getComfortLevel() {
        //Gets comfort level using the compliment percent of reading/capacity
        float comfortLevel = (1 - ( (float) getCurrentReading()/
                getCapacity())) * 100;
        //If current reading is greater than capacity, return 0
        if (this.getCurrentReading() > getCapacity()) {
            return 0;
        } else {
            return (int) comfortLevel;
        }
    }

    /**
     * Returns the human-readable string representation of this occupancy
     * sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=OccupancySensor, capacity='sensorCapacity'"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes), 'sensorReadings' is a comma-separated
     * list of this sensor's readings, and 'sensorCapacity' is this sensor's
     * maximum capacity.
     * <p>
     * For example: "TimedSensor: freq=5, readings=27,28,28,25,3,1,
     * type=OccupancySensor, capacity=30"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s, type=OccupancySensor, capacity=%d",
                super.toString(),
                this.capacity);
    }

    /**
     * Returns the machine-readable representation of this occupancy sensor.
     * Calls the encode method of TimedSensor to append the sensor
     * readings to this encoded sensor
     *
     * @return encoded string representation of this occupancy sensor
     */
    @Override
    public String encode() {
        //Appends the TimedSensor encode string with the relevant info
        return "OccupancySensor:" +
                super.encode() +
                ":" +
                this.getUpdateFrequency()+
                ":" +
                this.getCapacity();
    }

    /**
     * Returns true if this occupancy sensor is equal to the other given sensor.
     * Compares update frequency, sensor readings (using TimedSensor
     * compare method) and maximum capacity.
     * @param obj the object to compare against
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        //Checks that given object to compare against isn't null
        if (obj == null) {
            return false;
        }
        if (obj instanceof OccupancySensor) {
            OccupancySensor sensorCompare = (OccupancySensor) obj;
            //Checks that they share maximum capacity and TimedSensor properties
            return this.getCapacity() == sensorCompare.getCapacity() &&
                    super.equals(obj);
        } else {
            return false;
        }
    }

    /**
     * Creates a unique hashcode for this sensor
     * Uses update frequency, capacity, TimedSensor hashcode and class name.
     *
     * @return hash code of this sensor
     */
    @Override
    public int hashCode() {
        int hash = 23;
        hash = 41 * hash + this.getUpdateFrequency();
        hash = 41 * hash + this.getCapacity();
        hash = 41 * hash + super.hashCode();
        hash = 41 * hash + (this.getClass() == null ? 0 :
                this.getClass () .hashCode());
        return hash;
    }
}
