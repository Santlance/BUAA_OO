package elevator;

import com.oocourse.elevator1.PersonRequest;

import java.util.concurrent.ArrayBlockingQueue;

public class PerQueue {
    private ArrayBlockingQueue<PersonRequest> queue;
    private boolean hasrequest;

    public PerQueue() {
        this.queue = new ArrayBlockingQueue<>(100);
        this.hasrequest = true;
        //不能设置公平锁
    }

    public void add(PersonRequest addThis) throws InterruptedException {
        this.queue.put(addThis);
    }

    public synchronized boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public PersonRequest take() throws InterruptedException {
        return this.queue.take();
    }

    public synchronized void setHasrequest(boolean hasrequest) {
        this.hasrequest = hasrequest;
    }

    public synchronized boolean getHasrequest() {
        return this.hasrequest;
    }
}
