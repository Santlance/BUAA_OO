package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        PerQueue perQueue = new PerQueue();
        int[] arrA = {-3, -2, -1, 1, 15, 16, 17, 18, 19, 20};
        int[] arrB = {-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] arrC = {1, 3, 5, 7, 9, 11, 13, 15};
        Request request = new Request(perQueue, elevatorInput);
        request.start();
        Elevator elevatorA = new Elevator(perQueue, "A", arrA, 400, 6);
        Elevator elevatorB = new Elevator(perQueue, "B", arrB, 500, 8);
        Elevator elevatorC = new Elevator(perQueue, "C", arrC, 600, 7);
        elevatorA.start();
        elevatorB.start();
        elevatorC.start();
    }
}
