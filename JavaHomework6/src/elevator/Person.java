package elevator;

public class Person {
    private int personId;
    private int firstFromFloor;
    private int firstToFloor;
    private int secondFromFloor;
    private int secondToFloor;

    public Person(int personId, int firstFromFloor,
                  int firstToFloor, int secondFromFloor, int secondToFloor) {
        this.personId = personId;
        this.firstFromFloor = firstFromFloor;
        this.secondFromFloor = secondFromFloor;
        this.firstToFloor = firstToFloor;
        this.secondToFloor = secondToFloor;
    }

    public synchronized int getPersonId() {
        return this.personId;
    }

    public synchronized int getFirstFromFloor() {
        return this.firstFromFloor;
    }

    public synchronized int getSecondFromFloor() {
        return this.secondFromFloor;
    }

    public synchronized int getSecondToFloor() {
        return this.secondToFloor;
    }

    public synchronized int getFirstToFloor() {
        return this.firstToFloor;
    }
}
