package elevator;

//import com.oocourse.TimableOutput;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class PerQueue {
    private ArrayBlockingQueue<Person> queue;
    private boolean hasrequest;
    private int runCount;
    private int pickUpCount;

    public PerQueue() {
        this.queue = new ArrayBlockingQueue<>(100);
        this.hasrequest = true;
        this.runCount = 0;
        this.pickUpCount = 0;
        //不能设置公平锁
    }

    public synchronized int getPickUpCount() {
        return this.pickUpCount;
    }

    public synchronized void pickUpCountAdd() {
        this.pickUpCount++;
        //TimableOutput.println("pickup count:" + this.getPickUpCount());
    }

    public synchronized void pickUpCountSub() {
        this.pickUpCount--;
        notifyAll();
        //TimableOutput.println("pickup count:" + this.getPickUpCount());
    }

    public synchronized int getRunCount() {
        return this.runCount;
    }

    public synchronized void countAdd() {
        this.runCount = this.runCount + 1;
    }

    public synchronized void countSub() {
        this.runCount = this.runCount - 1;
        notifyAll();
    }

    public synchronized Person getPerson(int curFloor,
                                         int status, int[] legalFloor) {
        if (this.queue.isEmpty()) {
            return null;
        }
        //TimableOutput.println("try to offer a people to pick up");
        Iterator<Person> iterator = this.queue.iterator();
        Person request;
        while (iterator.hasNext()) {
            request = iterator.next();
            //TimableOutput.println("check try to offer a people to pick up");
            if (request.getFirstFromFloor() == curFloor
                    && getStatus(request) == status
                    && isLegal(legalFloor, request.getFirstToFloor())
                    && isLegal(legalFloor, request.getFirstFromFloor())) {
                //保证捎带是合法楼层
                this.queue.remove(request);
                return request;
            }
        }
        return null;
    }

    public synchronized boolean isLegal(int[] legalFloor, int floor) {
        for (int i = 0; i < legalFloor.length; i++) {
            if (legalFloor[i] == floor) {
                return true;
            }
        }
        return false;
    }

    public synchronized int getStatus(Person request) {
        if (request.getFirstFromFloor() < request.getFirstToFloor()) {
            return 1;
        } else if (request.getFirstFromFloor() > request.getFirstToFloor()) {
            return -1;
        } else {
            return 0;
        }
    }

    public synchronized void add(Person addThis)
            throws InterruptedException {
        //TimableOutput.println("add" + addThis.getPersonId());
        this.queue.put(addThis);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public synchronized Person take(int[] legalFloor)
            throws InterruptedException {
        if (this.isEmpty() && this.hasrequest) {    //队列为空而且还有请求的时候的等待才是有意义的
            //TimableOutput.println(legalFloor[0] + " " +
            // legalFloor[legalFloor.length - 1] + " empty and wait");
            wait();
        }
        Person takePerson;
        while ((takePerson = takePerson(legalFloor)) == null) {
            if (!this.hasrequest && this.runCount == 0
                    && this.pickUpCount == 0) {
                break;
            }
            //TimableOutput.println(legalFloor[0] + " " +
            // legalFloor[legalFloor.length - 1] + " try to getPerson wait");
            wait();
            //TimableOutput.println(legalFloor[0] + " " +
            // legalFloor[legalFloor.length - 1] +" getperson awake");
        }
        //TimableOutput.println("change empty");
        return takePerson;
    }

    public synchronized Person takePerson(int[] legalFloor) { //保证主请求是合法楼层
        Iterator<Person> iterator = this.queue.iterator();
        Person request;
        while (iterator.hasNext()) {
            request = iterator.next();
            if (isLegal(legalFloor, request.getFirstFromFloor())
                    && isLegal(legalFloor, request.getFirstToFloor())) {
                this.queue.remove(request);
                return request;
            }
        }
        return null;
    }

    /**
     * wait and notify need synchroniazed
     */

    public synchronized void setHasrequest(boolean hasrequest) {
        this.hasrequest = hasrequest;
        //TimableOutput.println("set norequest");
        notifyAll();
    }

    public synchronized boolean getHasrequest() {
        return this.hasrequest;
    }
}
