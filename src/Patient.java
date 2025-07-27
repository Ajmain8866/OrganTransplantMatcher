import java.io.Serializable;

/**
 * This class represents a Patient with identifying information, organ need or donation,
 * and blood type. It implements Comparable to allow ordering by patient ID.
 */
public class Patient implements Comparable<Patient>, Serializable {
    private String name;
    private String organ;
    private int age;
    private BloodType bloodType;
    private int ID;
    private boolean isDonor;

    /**
     * Constructs a Patient with the specified attributes.
     *
     * @param ID         the unique patient identifier
     * @param name       the patient’s name
     * @param age        the patient’s age
     * @param organ      the organ needed or donated
     * @param bloodType  the patient’s blood type
     * @param isDonor    true if the patient is a donor, false if recipient
     */
    public Patient(int ID, String name, int age, String organ, BloodType bloodType, boolean isDonor) {
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.organ = organ;
        this.bloodType = bloodType;
        this.isDonor = isDonor;
    }

    /**
     * An accessor which gets the patient’s unique identifier.
     *
     * @return the patient ID
     */
    public int getID() {
        return ID;
    }

    /**
     * A mutator which sets the patient’s unique identifier.
     *
     * @param ID the new patient ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * An accessor which gets the patient’s name.
     *
     * @return the patient’s name
     */
    public String getName() {
        return name;
    }

    /**
     * A mutator which sets the patient’s name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * An accessor which gets the organ needed or donated.
     *
     * @return the organ
     */
    public String getOrgan() {
        return organ;
    }

    /**
     * A mutator which sets the organ needed or donated.
     *
     * @param organ the new organ
     */
    public void setOrgan(String organ) {
        this.organ = organ;
    }

    /**
     * An accessor which gets the patient’s age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * A mutator which sets the patient’s age.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * An accessor which gets the patient’s blood type.
     *
     * @return the blood type
     */
    public BloodType getBloodType() {
        return bloodType;
    }

    /**
     * A mutator which sets the patient’s blood type.
     *
     * @param bloodType the new blood type
     */
    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * An accessor which checks if the patient is a donor.
     *
     * @return true if donor, false if recipient
     */
    public boolean isDonor() {
        return isDonor;
    }

    /**
     * A mutator which sets the donor status of the patient.
     *
     * @param donor true to mark as donor, false to mark as recipient
     */
    public void setDonor(boolean donor) {
        isDonor = donor;
    }

    /**
     * It compares this patient’s ID with another patient’s ID.
     *
     * @param o the other Patient to compare to
     * @return negative if less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(Patient o) {
        return Integer.compare(this.ID, o.ID);
    }

    /**
     * It returns the string representation of the patient,
     * including name, age, organ, and blood type.
     *
     * @return a formatted string of patient details
     */
    @Override
    public String toString() {
        return name + " | " + age + " | " + organ + " | " + bloodType.getType();
    }
}
