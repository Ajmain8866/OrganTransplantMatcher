import java.util.Comparator;

/**
 * This comparator orders Patients by their blood type string lexicographically.
 */
public class BloodTypeComparator implements Comparator<Patient> {

    /**
     * It compares two patients by their blood type.
     *
     * @param p1 the first patient
     * @param p2 the second patient
     * @return negative if p1’s blood type precedes p2’s, zero if equal, positive if follows
     */
    @Override
    public int compare(Patient p1, Patient p2) {
        return p1.getBloodType().getType().compareTo(p2.getBloodType().getType());
    }
}
