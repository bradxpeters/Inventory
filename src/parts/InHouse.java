package parts;

/**
 * The type In house.
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * Instantiates a new In house.
     *
     * @param id        the id
     * @param name      the name
     * @param price     the price
     * @param stock     the stock
     * @param min       the min
     * @param max       the max
     * @param machineId the machine id
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Gets machine id.
     *
     * @return the machine id
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets machine id.
     *
     * @param machineId the machine id
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
