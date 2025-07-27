import java.io.Serializable;

/**
 * This class represents a BloodType and provides compatibility checks
 * according to standard transfusion rules.
 */
public class BloodType implements Serializable {
    private String type;

    /**
     * Constructs a BloodType by normalizing the given type string to uppercase.
     *
     * @param type the blood type code (e.g., "A", "O", "AB", "B")
     */
    public BloodType(String type) {
        this.type = type.toUpperCase();
    }

    /**
     * An accessor which gets the blood type code.
     *
     * @return the blood type string
     */
    public String getType() {
        return type;
    }

    /**
     * A mutator which sets the blood type code, normalizing to uppercase.
     *
     * @param type the new blood type code
     */
    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    /**
     * It checks whether the donor’s blood type is compatible with the recipient’s.
     *
     * @param recipient the recipient’s blood type
     * @param donor the donor’s blood type
     * @return true if compatible according to transfusion rules, otherwise false
     */
    public static boolean isCompatible(BloodType recipient, BloodType donor) {
        String r = recipient.getType();
        String d = donor.getType();
        switch (r) {
            case "O":  return d.equals("O");
            case "A":  return d.equals("O") || d.equals("A");
            case "B":  return d.equals("O") || d.equals("B");
            case "AB": return d.equals("O") || d.equals("A") || d.equals("B") || d.equals("AB");
            default:   return false;
        }
    }
}
