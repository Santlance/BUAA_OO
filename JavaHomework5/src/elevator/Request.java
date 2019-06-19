package elevator;

//import com.oocourse.TimableOutput;

import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;

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
            if (request == null) {
                this.perQueue.setHasrequest(false);
                //TimableOutput.println("no request");
                break;
            } else {
                try {
                    this.perQueue.add(request);
                } catch (Exception e) {
                    e.printStackTrace();
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
