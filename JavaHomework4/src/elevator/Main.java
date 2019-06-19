package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;

public class Main {
    public static void main(String[] args)  {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        PerQueue perQueue = new PerQueue();
        Request request = new Request(perQueue,elevatorInput);
        Elevator elevator = new Elevator(perQueue);
        elevator.start();
        request.start();
    }
}
