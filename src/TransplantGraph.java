import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages donor and recipient Patient lists and a compatibility matrix
 * for organ transplants, supporting up to MAX_PATIENTS of each type.
 */
public class TransplantGraph implements Serializable {
    public static final int MAX_PATIENTS = 100;
    private ArrayList<Patient> donors;
    private ArrayList<Patient> recipients;
    private boolean[][] connections;

    /**
     * Default constructor which initializes empty donor and recipient lists
     * and the compatibility matrix.
     */
    public TransplantGraph() {
        donors = new ArrayList<>();
        recipients = new ArrayList<>();
        connections = new boolean[MAX_PATIENTS][MAX_PATIENTS];
    }

    /**
     * Builds a TransplantGraph by reading donor and recipient data from txt files.
     *
     * @param donorFile     the path to the donor txt file
     * @param recipientFile the path to the recipient txt file
     * @return the populated TransplantGraph
     * @throws IOException if an I/O error occurs reading either file
     */
    public static TransplantGraph buildFromFiles(String donorFile, String recipientFile) throws IOException {
        TransplantGraph graph = new TransplantGraph();

        BufferedReader dReader = new BufferedReader(new InputStreamReader(new FileInputStream(donorFile)));
        String line;
        while ((line = dReader.readLine()) != null) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            String organ = parts[3].trim();
            BloodType bt = new BloodType(parts[4].trim());
            graph.addDonor(new Patient(id, name, age, organ, bt, true));
        }
        dReader.close();

        BufferedReader rReader = new BufferedReader(new InputStreamReader(new FileInputStream(recipientFile)));
        while ((line = rReader.readLine()) != null) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            String organ = parts[3].trim();
            BloodType bt = new BloodType(parts[4].trim());
            graph.addRecipient(new Patient(id, name, age, organ, bt, false));
        }
        rReader.close();

        return graph;
    }

    /**
     * It adds a donor Patient to the graph, assigns an ID, and updates compatibility.
     *
     * @param p the Patient to add as donor
     */
    public void addDonor(Patient p) {
        int i = donors.size();
        p.setID(i);
        donors.add(p);
        for (int j = 0; j < recipients.size(); j++) {
            Patient rec = recipients.get(j);
            connections[i][j] = p.getOrgan().equalsIgnoreCase(rec.getOrgan())
                    && BloodType.isCompatible(rec.getBloodType(), p.getBloodType());
        }
    }

    /**
     * It adds a recipient Patient to the graph, assigns an ID, and updates compatibility.
     *
     * @param p the Patient to add as recipient
     */
    public void addRecipient(Patient p) {
        int j = recipients.size();
        p.setID(j);
        recipients.add(p);
        for (int i = 0; i < donors.size(); i++) {
            Patient donor = donors.get(i);
            connections[i][j] = p.getOrgan().equalsIgnoreCase(donor.getOrgan())
                    && BloodType.isCompatible(p.getBloodType(), donor.getBloodType());
        }
    }

    /**
     * It removes the donor with the given name, re-indexes remaining donors,
     * and shifts the compatibility matrix accordingly.
     *
     * @param name the donor name to remove
     */
    public void removeDonor(String name) {
        int idx = -1;
        for (int i = 0; i < donors.size(); i++) {
            if (donors.get(i).getName().equalsIgnoreCase(name)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            System.out.println("Failed to remove donor: No such patient named " + name + " in list of donors.");
            return;
        }
        donors.remove(idx);
        for (int i = idx; i < donors.size(); i++) {
            donors.get(i).setID(i);
        }
        for (int i = idx; i < donors.size() + 1; i++) {
            for (int j = 0; j < recipients.size(); j++) {
                if (i < donors.size())
                    connections[i][j] = connections[i + 1][j];
                else
                    connections[i][j] = false;
            }
        }
    }

    /**
     * It removes the recipient with the given name, re-indexes remaining recipients,
     * and shifts the compatibility matrix accordingly.
     *
     * @param name the recipient name to remove
     */
    public void removeRecipient(String name) {
        int idx = -1;
        for (int j = 0; j < recipients.size(); j++) {
            if (recipients.get(j).getName().equalsIgnoreCase(name)) {
                idx = j;
                break;
            }
        }
        if (idx == -1) {
            System.out.println("Failed to remove recipient: No such patient named " + name + " in list of recipients.");
            return;
        }
        recipients.remove(idx);
        for (int j = idx; j < recipients.size(); j++) {
            recipients.get(j).setID(j);
        }
        for (int j = idx; j < recipients.size() + 1; j++) {
            for (int i = 0; i < donors.size(); i++) {
                if (j < recipients.size())
                    connections[i][j] = connections[i][j + 1];
                else
                    connections[i][j] = false;
            }
        }
    }

    /**
     * An accessor which gets the list of donor patients.
     *
     * @return the list of donors
     */
    public ArrayList<Patient> getDonors() {
        return donors;
    }

    /**
     * An accessor which gets the list of recipient patients.
     *
     * @return the list of recipients
     */
    public ArrayList<Patient> getRecipients() {
        return recipients;
    }

    /**
     * It checks if the donor with the given ID is compatible with
     * the recipient with the given ID.
     *
     * @param donorID the donor’s ID
     * @param recipientID the recipient’s ID
     * @return true if compatible, otherwise false
     */
    public boolean isConnected(int donorID, int recipientID) {
        return connections[donorID][recipientID];
    }

    /**
     * It returns the number of transplant connections for the given patient,
     * counting matches with donors or recipients based on donor status.
     *
     * @param p the Patient whose connections are counted
     * @return the number of connections
     */
    public int getNumConnections(Patient p) {
        int count = 0;
        if (p.isDonor()) {
            int i = p.getID();
            for (int j = 0; j < recipients.size(); j++) {
                if (connections[i][j])
                    count++;
            }
        } else {
            int j = p.getID();
            for (int i = 0; i < donors.size(); i++) {
                if (connections[i][j])
                    count++;
            }
        }
        return count;
    }

    /**
     * It prints a formatted table of all donors, including compatible recipient IDs.
     */
    public void printAllDonors() {
        System.out.printf("Index | Donor Name         | Age | Organ Donated | Blood Type | Recipient IDs%n");
        System.out.println("=============================================================================");
        for (Patient p : donors) {
            int i = p.getID();
            System.out.printf("   %d  | %-18s | %2d  | %-13s |     %-6s | ",
                    i, p.getName(), p.getAge(), p.getOrgan(), p.getBloodType().getType());
            List<Integer> ids = new ArrayList<>();
            for (int j = 0; j < recipients.size(); j++) {
                if (connections[i][j]) ids.add(j);
            }
            for (int k = 0; k < ids.size(); k++) {
                System.out.print(ids.get(k));
                if (k < ids.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();
        }
    }

    /**
     * It prints a formatted table of all recipients, including compatible donor IDs.
     */
    public void printAllRecipients() {
        System.out.printf("Index | Recipient Name     | Age | Organ Needed  | Blood Type | Donor IDs%n");
        System.out.println("==========================================================================");
        for (Patient p : recipients) {
            int j = p.getID();
            System.out.printf("   %d  | %-18s | %2d  | %-13s |     %-6s | ",
                    j, p.getName(), p.getAge(), p.getOrgan(), p.getBloodType().getType());
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < donors.size(); i++) {
                if (connections[i][j]) ids.add(i);
            }
            for (int k = 0; k < ids.size(); k++) {
                System.out.print(ids.get(k));
                if (k < ids.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }
}
