package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.OccupancySensor;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates the hazard level of a location using a rule based system.
 */
public class RuleBasedHazardEvaluator
        implements HazardEvaluator{

    /**
     * List of the hazard sensors this evaluator evaluates
     */
    private List<HazardSensor> hazardSensorList;

    /**
     * Creates a new rule-based hazard evaluator with the given list of sensors.
     * @param sensors Sensors that the evaluator uses to calculate hazard level
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        hazardSensorList = new ArrayList<>(sensors);
    }

    /**
     * Returns a calculated hazard level based on applying a set of rules to
     * the list of sensors passed to the constructor.
     *
     * @return - Calculated hazard level
     */
    public int evaluateHazardLevel() {
        //Checks if there are more than one sensor in the room/evaluator
        if (hazardSensorList.size() > 1) {
            int averageTotal = 0;
            int averageCount = 0;
            double averageCalculated;
            double occupancyModifier = 1;
            //Goes through each sensor in the list
            for (HazardSensor sensor: hazardSensorList) {
                //Checks if the sensor is not an occupancy type
                if (sensor.getClass() == OccupancySensor.class) {
                    //Applies occupancy modifier
                    occupancyModifier = (float) sensor.getHazardLevel()/100;
                } else {
                    if (sensor.getHazardLevel() == 100) {
                        return 100;
                    } else {
                        //Adds the hazard level to a total counter
                        averageTotal += sensor.getHazardLevel();
                        averageCount++;
                    }
                }
            }
            //Calculates the average hazard level using the total and modifier
            averageCalculated = ( (float) averageTotal/averageCount)
                    * occupancyModifier;
            return (int) (Math.round(averageCalculated));
        } else if (hazardSensorList.size() == 1) {
            //If there is only one sensor, returns the hazard level of that one
            return hazardSensorList.get(0).getHazardLevel();
        } else  {
            return 0;
        }
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * @return string representation of this room
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}
