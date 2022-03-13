package bms.util;

/**
 * A type that can be encoded to a machine-readable string representation
 */
public interface Encodable {
    /**
     * Returns the String representation of the current state of this object.
     * @return encoded String representation
     */
    String encode();
}
