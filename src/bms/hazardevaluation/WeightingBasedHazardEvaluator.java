package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Evaluates the hazard level of a room using weightings for the sensor values.
 */
public class WeightingBasedHazardEvaluator
        implements HazardEvaluator {

    /**
     * Map of the hazard sensors and weightings
     */
    private Map<HazardSensor,Integer> hazardSensorMap;
    /**
     * List of the weightings in order given
     */
    private ArrayList<Integer> weightings;

    /**
     * Creates a new weighting-based hazard evaluator with the given sensors
     * and weightings. Checks that the weightings equal to 100 and are above 0.
     *
     * @param sensors mapping of sensors to their respective weighting
     * @throws IllegalArgumentException if weighting below 0 or total above 100
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor,Integer> sensors)
            throws IllegalArgumentException {
        int totalWeightings = 0;
        ArrayList<Integer>tempWeights = new ArrayList<>();
        //Goes through the given Map and access each entry
        for(Map.Entry<HazardSensor, Integer> entry : sensors.entrySet()) {
            //Adds each value to a total counter and to an array of weightings
            totalWeightings += entry.getValue();
            tempWeights.add(entry.getValue());
            //If the given weighting is negative, throw exception
            if (entry.getValue() < 0) {
                throw new IllegalArgumentException();
            }
        }
        //If the weightings add to 100, create evaluator. Else, throw exception
        if (totalWeightings == 100) {
            hazardSensorMap = new HashMap<>(sensors);
            weightings = tempWeights;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the weighted average of the current hazard levels of all sensors
     * in the map passed to the constructor. Goes through the local sensor map
     * and adds the hazard level of each sensor with its weighting.
     *
     * @return weighted average of current sensor hazard levels
     */
    public int evaluateHazardLevel() {
        double hazardLevel = 0;
        //Goes through the map and access each sensor
        for(Map.Entry<HazardSensor, Integer> entry:hazardSensorMap.entrySet()) {
            //Determines each sensors hazard level and adds to a total
            hazardLevel += (entry.getKey().getHazardLevel()
                    * ((float) entry.getValue()/100));
        }
        return (int) hazardLevel;
    }

    /**
     * Returns a list containing the weightings associated with all of the
     * sensors monitored by this hazard evaluator.
     * @return list of weightings
     */
    public List<Integer> getWeightings() {
        return new ArrayList<>(weightings);
    }

    /**
     * Returns the string of this hazard evaluator, which is just the name.
     * @return string representation of this hazard evaluator
     */
    @Override
    public String toString() {
        return "WeightingBased";
    }
}
