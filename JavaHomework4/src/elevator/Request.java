package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Request extends Thread {
    private ElevatorInput elevatorInput;
    private PerQueue perQueue;

    public Request(PerQueue perQueue, ElevatorInput elevatorInput) {
        this.elevatorInput = elevatorInput;
        this.perQueue = perQueue;
    }

    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                this.perQueue.setHasrequest(false);
                //TimableOutput.println(this.perQueue.getHasrequest()
                // + "Request stop");
                break;
            } else {
                //TimableOutput.println(this.perQueue.getHasrequest()
                // + "Request start");
                // a new valid request
                try {
                    this.perQueue.add(request);
                    /*TimableOutput.println("R-" + this.perQueue.i sEmpty() +
                            "-" + request.toString());
                    this.sleep(500);*/
                } catch (Exception e) {
                    TimableOutput.println("Request run error");
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
