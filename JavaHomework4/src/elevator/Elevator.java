package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class Elevator extends Thread {
    private PerQueue perQueue;
    private int curFloor;

    private static final long openTime = 250;
    private static final long closeTime = 250;
    private static final long runTime = 500;

    public Elevator() {
        this.perQueue = null;
        this.curFloor = 1;
    }

    public Elevator(PerQueue thisQueue) {
        this.perQueue = thisQueue;
        this.curFloor = 1;
    }

    public void personIn(int personId) {
        TimableOutput.println(String.format("IN-%d-%d",
                personId, this.curFloor));
    }

    public void personOut(int personId) {
        TimableOutput.println(String.format("OUT-%d-%d",
                personId, this.curFloor));
    }

    public void closeDoor() throws InterruptedException {
        this.sleep(closeTime);
        TimableOutput.println(String.format("CLOSE-%d", this.curFloor));
    }

    public void openDoor() throws InterruptedException {
        TimableOutput.println(String.format("OPEN-%d", this.curFloor));
        this.sleep(openTime);
    }

    public void moveDes(int desFloor) throws InterruptedException {
        if (this.curFloor != desFloor) {
            this.sleep(runTime * Math.abs(curFloor - desFloor));
            this.curFloor = desFloor;
        }
    }

    public void run(PersonRequest request) throws InterruptedException {
        int personId = request.getPersonId();
        int fromFloor = request.getFromFloor();
        int toFloor = request.getToFloor();
        this.moveDes(fromFloor);
        this.openDoor();
        this.personIn(personId);
        this.closeDoor();
        this.moveDes(toFloor);
        this.openDoor();
        this.personOut(personId);
        this.closeDoor();
    }

    public void run() {
        while (true) {
            //用true
            if (!this.perQueue.isEmpty()) {
                // TimableOutput.println(this.perQueue.getHasrequest()
                // + "queue empty");
                /*//进else保证不空
                TimableOutput.println(this.perQueue.getHasrequest()
                 + "Elevator start");
                try {
                    PersonRequest request = this.perQueue.take();//如果空的话一直卡在这里
                    TimableOutput.println("E-" + this.perQueue.isEmpty()
                            + "-" + request.toString());
                    this.sleep(5000);
                } catch (Exception e) {
                    TimableOutput.println("Elevator run error");
                }*/
                try {
                    PersonRequest request = this.perQueue.take();
                    this.run(request);
                } catch (Exception e) {
                    continue;
                }
            }
            if (!this.perQueue.getHasrequest() && this.perQueue.isEmpty()) {
                // TimableOutput.println(this.perQueue.getHasrequest()
                // + "Elevator stop");
                break;
            }
        }
    }

}
