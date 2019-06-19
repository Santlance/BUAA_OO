package elevator;

import com.oocourse.TimableOutput;

import java.util.Iterator;
import java.util.Vector;

public class Elevator extends Thread {
    private int[] legalFloor;
    private String name;
    private PerQueue perQueue;
    private int curFloor;
    private int status;
    private Vector<Person> passenger;
    private boolean doorStatus;
    private long runTime;

    private static final long openTime = 200;
    private static final long closeTime = 200;
    private static final int Up = 1;
    private static final int Down = -1;
    private static final int Static = 0;

    public Elevator(PerQueue thisQueue, String name,
                    int[] legalFloor, long runTime, int maxNum) {
        this.perQueue = thisQueue;
        this.curFloor = 1;
        this.passenger = new Vector<>(maxNum);
        this.status = Static;
        this.doorStatus = false;
        this.legalFloor = legalFloor;
        this.runTime = runTime;
        this.name = name;
    }

    public synchronized void personIn(int personId) {
        TimableOutput.println(String.format("IN-%d-%d-%s",
                personId, this.curFloor, this.name));
    }

    public synchronized void personOut(int personId) {
        TimableOutput.println(String.format("OUT-%d-%d-%s",
                personId, this.curFloor, this.name));
    }

    public synchronized void closeDoor() throws InterruptedException {
        if (this.doorStatus) {
            this.sleep(closeTime);
            this.doorStatus = false;
            TimableOutput.println(String.format("CLOSE-%d-%s",
                    this.curFloor, this.name));
        }
    }

    public synchronized void openDoor() throws InterruptedException {
        if (!this.doorStatus) {
            TimableOutput.println(String.format("OPEN-%d-%s",
                    this.curFloor, this.name));
            this.sleep(openTime);
            this.doorStatus = true;
        }
    }

    public synchronized void moveFloor() throws InterruptedException {
        if (this.curFloor == this.legalFloor[legalFloor.length - 1]
                && this.status == Up) {
            this.status = Down;
        }
        if (this.curFloor == this.legalFloor[0] && this.status == Down) {
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
        TimableOutput.println(String.format("ARRIVE-%d-%s",
                this.curFloor, this.name));
    }

    public synchronized void setStatus(int from, int to) {
        if (from < to) {
            this.status = Up;
        } else if (from > to) {
            this.status = Down;
        } else {
            this.status = Static;
        }
    }

    public synchronized void pickUp() throws InterruptedException {
        Person inperson;
        //TimableOutput.println(this.name +
        // " passenger:" + this.passenger.size());
        if (this.passenger.size() >= this.passenger.capacity()) {
            //TimableOutput.println(this.name + " is full");
            return;
        }
        while (this.passenger.size() < this.passenger.capacity() && (inperson =
                this.perQueue.getPerson(this.curFloor,
                        this.status, this.legalFloor)) != null
        ) { //保证捎带未满，要先判断满不满再getPerson
            openDoor();
            personIn(inperson.getPersonId());
            this.passenger.add(inperson);
        }
    }

    public synchronized boolean isLegal() {
        for (int i = 0; i < this.legalFloor.length; i++) {
            if (this.legalFloor[i] == this.curFloor) {
                return true;
            }
        }
        return false;
    }

    public synchronized void judgeStatus() {
        boolean dirChange = true;
        Iterator<Person> iterator;
        if (!this.passenger.isEmpty()) {
            if (this.status == Up) {
                iterator = this.passenger.iterator();
                while (iterator.hasNext()) {
                    Person request1 = iterator.next();
                    if (request1.getFirstToFloor() > this.curFloor) {
                        dirChange = false;
                    }
                }
            } else if (this.status == Down) {
                iterator = this.passenger.iterator();
                while (iterator.hasNext()) {
                    Person request1 = iterator.next();
                    if (request1.getFirstToFloor() < this.curFloor) {
                        dirChange = false;
                    }
                }
            }
            if (dirChange) {
                this.status = -this.status;
            }
        }
    }

    public void run(Person request) throws InterruptedException {
        //TimableOutput.println("run ele");
        this.perQueue.countAdd();
        //TimableOutput.println(this.perQueue.getRunCount());
        int from = request.getFirstFromFloor();
        int to = request.getFirstToFloor();
        this.setStatus(this.curFloor, from);
        boolean getMain = false;
        //TimableOutput.println(from + " " + to);
        while (true) {
            this.pickUp();//接到主请求之前和之后的pick
            if (this.curFloor == from && !getMain
                    && passenger.size() < passenger.capacity()) { //保证主请求未满
                openDoor();
                getMain = true;
                personIn(request.getPersonId());
                this.setStatus(from, to);
                this.passenger.add(request);
                this.pickUp();//在主请求楼层的pick
            }
            //TimableOutput.println(this.status);
            Iterator<Person> iterator = this.passenger.iterator();
            while (iterator.hasNext()) {
                Person outperson = iterator.next();
                if (outperson.getFirstToFloor() == curFloor && this.isLegal()) {
                    openDoor();
                    personOut(outperson.getPersonId());
                    if (outperson.getSecondFromFloor() != 0) {
                        //TimableOutput.println("parse request" +
                        //       outperson.getPersonId() + " " +
                        //       outperson.getFirstFromFloor() + " "
                        //      + outperson.getFirstToFloor() + " "
                        //     + outperson.getSecondFromFloor() + " "
                        //    + outperson.getSecondToFloor());
                        this.perQueue.add(new Person(outperson.getPersonId(),
                                outperson.getSecondFromFloor(),
                                outperson.getSecondToFloor(), 0, 0));
                        this.perQueue.pickUpCountSub();
                    }
                    iterator.remove();
                }
            }
            //出人
            closeDoor();
            //电梯内有人才看一下有没有必要更改方向
            this.judgeStatus();
            if (this.passenger.isEmpty() && getMain) {
                //TimableOutput.println(this.name + " break runele");
                break;
            }
            moveFloor();
        }
        this.perQueue.countSub();
        // TimableOutput.println(this.perQueue.getRunCount());
    }

    public void run() {
        while (true) {
            try {
                // TimableOutput.println(this.name + "try to get people");
                Person request = this.perQueue.take(this.legalFloor);
                //TimableOutput.println(this.name + " get request");
                if (request == null) {
                    // TimableOutput.println(this.name + "break ele");
                    break;
                }
                this.run(request);
            } catch (Exception e) {
                continue;
            }
        }
    }

}
