package bms.sensors;

import bms.util.Encodable;
import bms.util.TimedItem;

/**
 * A sensor that measures levels of carbon dioxide (CO2) in the air, in parts
 * per million (ppm).
 * @ass1
 */
public class CarbonDioxideSensor extends TimedSensor
        implements ComfortSensor, HazardSensor, Sensor, Encodable, TimedItem {

    /**
     * The ideal value for this sensor, where the comfort level is highest.
     */
    private int idealValue;

    /**
     * The maximum variation that is allowed from the ideal value. The comfort
     * level will be 0 when the value is this far (or further) away from the
     * ideal value.
     */
    private int variationLimit;

    /**
     * Creates a new carbon dioxide sensor with the given sensor readings,
     * update frequency, ideal CO2 value and acceptable variation limit.
     * <p>
     * Different rooms and environments may naturally have different "normal"
     * CO2 concentrations, for example, a large room with many windows may
     * have lower typical CO2 concentrations than a small room with poor
     * airflow.
     * <p>
     * To allow for these discrepancies, each CO2 sensor has an "ideal" CO2
     * concentration and a maximum acceptable variation from this value.
     * Both the ideal value and variation limit must be greater than zero.
     * These two values must be such that (idealValue - variationLimit) &gt;= 0.
     *
     * @param sensorReadings array of CO2 sensor readings <b>in ppm</b>
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @param idealValue ideal CO2 value in ppm
     * @param variationLimit acceptable range above and below ideal value in ppm
     * @throws IllegalArgumentException if idealValue &lt;= 0;
     * or if variationLimit &lt;= 0; or if (idealValue - variationLimit) &lt; 0
     * @ass1
     */
    public CarbonDioxideSensor(int[] sensorReadings, int updateFrequency,
                               int idealValue, int variationLimit)
            throws IllegalArgumentException {
        super(sensorReadings, updateFrequency);

        if (idealValue <= 0) {
            throw new IllegalArgumentException("Ideal CO2 value must be > 0");
        }
        if (variationLimit <= 0) {
            throw new IllegalArgumentException(
                    "CO2 variation limit must be > 0");
        }

        if (idealValue - variationLimit < 0) {
            throw new IllegalArgumentException("Ideal CO2 value - variation "
                    + "limit must be >= 0");
        }

        this.idealValue = idealValue;
        this.variationLimit = variationLimit;
    }

    /**
     * Returns the sensor's CO2 variation limit.
     *
     * @return variation limit in ppm
     * @ass1
     */
    public int getVariationLimit() {
        return variationLimit;
    }

    /**
     * Returns the sensor's ideal CO2 value.
     *
     * @return ideal value in ppm
     * @ass1
     */
    public int getIdealValue() {
        return idealValue;
    }

    /**
     * Returns the hazard level as detected by this sensor.
     * <p>
     * The returned hazard level is determined by the following table, and is
     * based on the current sensor reading.
     * <table border="1">
     * <caption>CO2 hazard level table</caption>
     * <tr>
     * <th>Current sensor reading</th>
     * <th>Hazard level</th>
     * <th>Associated effect</th>
     * </tr>
     * <tr><td>0-999</td><td>0</td><td>No effects</td></tr>
     * <tr><td>1000-1999</td><td>25</td><td>Drowsiness</td></tr>
     * <tr><td>2000-4999</td><td>50</td>
     * <td>Headaches, sleepiness, loss of concentration</td></tr>
     * <tr><td>5000+</td><td>100</td><td>Oxygen deprivation</td></tr>
     * </table>
     *
     * @return the current hazard level as an integer between 0 and 100
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        final int currentReading = this.getCurrentReading();
        if (currentReading < 1000) {
            return 0;
        }
        if (currentReading < 2000) {
            return 25;
        }
        if (currentReading < 5000) {
            return 50;
        }
        return 100;
    }

    /**
     *  Returns the comfort level as detected by this sensor.
     *  This uses the provided mathematical description. That is, the comfort
     *  level is found by as the percentage of the complement of the absolute
     *  difference between the ideal value and current reading, divided by
     *  the variation limit.
     *
     * @return the current comfort level as an integer between 0 and 100
     */
    @Override
    public int getComfortLevel() {
        //Calculates the absolute difference between the reading and ideal value
        int absoluteDifference = Math.abs(getIdealValue()-getCurrentReading());
        //If the difference is greater than the variation limit, return 0
        if (absoluteDifference >= getVariationLimit()) {
            return 0;
        } else {
            //Calculates the comfort level by finding
            // the compliment percentage of difference/variationLimit
            float comfortLevel = (100 * (1 -
                    ((float)absoluteDifference/(float) getVariationLimit())));
            return Math.round(comfortLevel);
        }
    }

    /**
     * Returns the human-readable string representation of this CO2 sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=CarbonDioxideSensor, idealPPM='idealValue',
     * varLimit='variationLimit'"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes), 'sensorReadings' is a comma-separated
     * list of this sensor's readings, 'idealValue' is this sensor's ideal CO2
     * concentration, and 'variationLimit' is this sensor's variation limit.
     * <p>
     * For example: "TimedSensor: freq=5, readings=702,694,655,680,711,
     * type=CarbonDioxideSensor, idealPPM=600, varLimit=250"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format(
                "%s, type=CarbonDioxideSensor, idealPPM=%d, varLimit=%d",
                super.toString(),
                this.idealValue,
                this.variationLimit);
    }

    /**
     * Returns the machine-readable string representation of this carbon
     * dioxide sensor for use in saving the files. Calls the encode method of
     * TimedSensor to append the sensor readings to this encoded sensor
     *
     * @return encoded string representation of this carbon dioxide sensor
     */
    @Override
    public String encode() {
        //Appends the TimedSensor encode string with the relevant info
        return "CarbonDioxideSensor:" +
                super.encode() +
                ":" +
                this.getUpdateFrequency() +
                ":" +
                this.getIdealValue() +
                ":" +
                this.getVariationLimit();
    }

    /**
     * Returns true if this CO2 sensor is equal to the other given sensor.
     *
     * Checks the ideal value, variation limit, update frequency and readings.
     *
     * @param obj the object to compare against
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        //Checks that given object to compare against isn't null
        if (obj == null) {
            return false;
        }
        if (obj instanceof CarbonDioxideSensor) {
            CarbonDioxideSensor sensorCompare = (CarbonDioxideSensor) obj;
            //Checks that they share the same variation limit,
            // ideal value and TimedSensor properties
            return this.getVariationLimit() == sensorCompare.getVariationLimit()
                    && this.getIdealValue() == sensorCompare.getIdealValue()
                    && super.equals(obj);
        } else {
            return false;
        }
    }

    /**
     * Returns the uniquely generated hash code of this CO2 sensor.
     * Uses the update frequency, ideal value, variation limit, TimedSensor
     * hashcode and the hashcode of the class name.
     *
     * @return hash code of this sensor
     */
    @Override
    public int hashCode() {
        int hash = 19;
        hash = 37 * hash + this.getUpdateFrequency();
        hash = 37 * hash + this.getIdealValue();
        hash = 37 * hash + this.getVariationLimit();
        hash = 37 * hash + super.hashCode();
        hash = 37 * hash + (this.getClass() == null ? 0 :
                this.getClass() .hashCode());
        return hash;
    }
}
