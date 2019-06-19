package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import java.util.Iterator;
import java.util.Vector;

public class Elevator extends Thread {
    private PerQueue perQueue;
    private int curFloor;
    private int status;
    private Vector<PersonRequest> passenger;
    private boolean doorStatus;

    private static final long openTime = 200;
    private static final long closeTime = 200;
    private static final long runTime = 400;
    private static final int Up = 1;
    private static final int Down = -1;
    private static final int Static = 0;

    public Elevator(PerQueue thisQueue) {
        this.perQueue = thisQueue;
        this.curFloor = 1;
        this.passenger = new Vector<>();
        this.status = Static;
        this.doorStatus = false;
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
        if (this.doorStatus) {
            this.sleep(closeTime);
            this.doorStatus = false;
            TimableOutput.println(String.format("CLOSE-%d", this.curFloor));
        }
    }

    public void openDoor() throws InterruptedException {
        if (!this.doorStatus) {
            TimableOutput.println(String.format("OPEN-%d", this.curFloor));
            this.sleep(openTime);
            this.doorStatus = true;
        }
    }

    public void moveFloor() throws InterruptedException {
        if (this.curFloor == 16 && this.status == Up) {
            this.status = Down;
        }
        if (this.curFloor == -3 && this.status == Down) {
            this.status = Up;
        }
        if (this.status == Up) {
            if (this.curFloor == -1) {
                this.curFloor += 2;
            } else {
                this.curFloor++;
            }
        } else if (this.status == Down) {
            if (this.curFloor == 1) {
                this.curFloor -= 2;
            } else {
                this.curFloor--;
            }
        }
        this.sleep(runTime);
        TimableOutput.println(String.format("ARRIVE-%d", this.curFloor));
    }

    public void setStatus(int from, int to) {
        if (from < to) {
            this.status = Up;
        } else if (from > to) {
            this.status = Down;
        } else {
            this.status = Static;
        }
    }

    public void pickUp() throws InterruptedException {
        PersonRequest inperson;
        while ((inperson =
                this.perQueue.getPerson(this.curFloor, this.status))
                != null) {
            openDoor();
            personIn(inperson.getPersonId());
            this.passenger.add(inperson);
        }
    }

    public void run(PersonRequest request) throws InterruptedException {
        // TimableOutput.println("run ele");
        int from = request.getFromFloor();
        int to = request.getToFloor();
        this.setStatus(this.curFloor, from);
        boolean getMain = false;
        while (true) {
            this.pickUp();//接到主请求之前和之后的pick
            if (this.curFloor == request.getFromFloor() && !getMain) {
                openDoor();
                getMain = true;
                personIn(request.getPersonId());
                this.setStatus(from, to);
                this.passenger.add(request);
                this.pickUp();//在主请求楼层的pick
            }
            //TimableOutput.println(this.status);
            Iterator<PersonRequest> iterator = this.passenger.iterator();
            while (iterator.hasNext()) {
                PersonRequest outperson = iterator.next();
                if (outperson.getToFloor() == this.curFloor) {
                    openDoor();
                    personOut(outperson.getPersonId());
                    iterator.remove();
                }
            }
            //出人
            closeDoor();
            //电梯内有人才看一下有没有必要更改方向
            boolean dirChange = true;
            if (!this.passenger.isEmpty()) {
                if (this.status == Up) {
                    iterator = this.passenger.iterator();
                    while (iterator.hasNext()) {
                        PersonRequest request1 = iterator.next();
                        if (request1.getToFloor() > this.curFloor) {
                            dirChange = false;
                        }
                    }
                } else if (this.status == Down) {
                    iterator = this.passenger.iterator();
                    while (iterator.hasNext()) {
                        PersonRequest request1 = iterator.next();
                        if (request1.getToFloor() < this.curFloor) {
                            dirChange = false;
                        }
                    }
                }
                if (dirChange) {
                    this.status = -this.status;
                }
            }
            if (this.passenger.isEmpty() && getMain) {
                // TimableOutput.println("break runele");
                return;
            }
            moveFloor();
        }
    }

    public void run() {
        while (true) {
            try {
                PersonRequest request = this.perQueue.take();
                //TimableOutput.println("get request");
                if (request == null) {
                    //TimableOutput.println("break ele");
                    break;
                }
                this.run(request);
            } catch (Exception e) {
                continue;
            }
            if (!this.perQueue.getHasrequest()
                    && this.perQueue.isEmpty()
                    && this.passenger.isEmpty()) {
                // TimableOutput.println(this.perQueue.getHasrequest()
                // + "Elevator stop");
                break;
            }
        }
    }

}
