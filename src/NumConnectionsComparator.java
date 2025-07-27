import java.util.Comparator;

/**
 * This comparator orders Patients by their number of transplant connections
 * in a given TransplantGraph.
 */
public class NumConnectionsComparator implements Comparator<Patient> {
    private TransplantGraph graph;

    /**
     * Constructs a NumConnectionsComparator using the specified graph.
     *
     * @param graph the TransplantGraph to query for connection counts
     */
    public NumConnectionsComparator(TransplantGraph graph) {
        this.graph = graph;
    }

    /**
     * It compares two patients by their number of connections in the graph.
     *
     * @param p1 the first patient
     * @param p2 the second patient
     * @return negative if p1 has fewer connections, zero if equal, positive if more
     */
    @Override
    public int compare(Patient p1, Patient p2) {
        return Integer.compare(graph.getNumConnections(p1), graph.getNumConnections(p2));
    }
}
