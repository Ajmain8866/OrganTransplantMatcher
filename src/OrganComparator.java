import java.util.Comparator;

/**
 * This comparator orders Patients by their organ string, ignoring case.
 */
public class OrganComparator implements Comparator<Patient> {

    /**
     * It compares two patients by organ needed or donated, case-insensitive.
     *
     * @param p1 the first patient
     * @param p2 the second patient
     * @return negative if p1’s organ precedes p2’s, zero if equal, positive if follows
     */
    @Override
    public int compare(Patient p1, Patient p2) {
        return p1.getOrgan().compareToIgnoreCase(p2.getOrgan());
    }
}
