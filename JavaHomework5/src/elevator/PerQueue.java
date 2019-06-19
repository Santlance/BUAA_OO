package elevator;

//import com.oocourse.TimableOutput;

import com.oocourse.elevator2.PersonRequest;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class PerQueue {
    private ArrayBlockingQueue<PersonRequest> queue;
    private boolean hasrequest;

    public PerQueue() {
        this.queue = new ArrayBlockingQueue<>(100);
        this.hasrequest = true;
        //不能设置公平锁
    }

    public synchronized PersonRequest getPerson(int curFloor, int status) {
        if (this.queue.isEmpty()) {
            return null;
        }
        Iterator<PersonRequest> iterator = this.queue.iterator();
        PersonRequest request;
        while (iterator.hasNext()) {
            request = iterator.next();
            if (request.getFromFloor() == curFloor
                    && getStatus(request) == status) {
                this.queue.remove(request);
                return request;
            }
        }
        return null;
    }

    public int getStatus(PersonRequest request) {
        if (request.getFromFloor() < request.getToFloor()) {
            return 1;
        } else if (request.getFromFloor() > request.getToFloor()) {
            return -1;
        } else {
            return 0;
        }
    }

    public synchronized void add(PersonRequest addThis)
            throws InterruptedException {
        this.queue.put(addThis);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public synchronized PersonRequest take() throws InterruptedException {
        if (this.isEmpty()) {
            //TimableOutput.println("empty and wait");
            wait();
        }
        //TimableOutput.println("change empty");
        return this.queue.poll();
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
