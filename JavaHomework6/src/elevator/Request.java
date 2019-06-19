package elevator;

//import com.oocourse.TimableOutput;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

import java.io.IOException;

public class Request extends Thread {
    private ElevatorInput elevatorInput;
    private PerQueue perQueue;

    public Request(PerQueue perQueue, ElevatorInput elevatorInput) {
        this.elevatorInput = elevatorInput;
        this.perQueue = perQueue;
    }

    public synchronized Person parseRequest(PersonRequest request) {
        int fromFloor = request.getFromFloor();
        int toFloor = request.getToFloor();
        int personId = request.getPersonId();
        //TimableOutput.println("try to parse "+request.getPersonId());
        Person person = new Person(personId, fromFloor, toFloor, 0, 0);
        if (fromFloor == -3) { //从-3出发到中间楼层都要在1换乘
            if (toFloor >= 2 && toFloor <= 14) {
                person = new Person(personId, -3, 1, 1, toFloor);
            }
        } else if (fromFloor == -2 || fromFloor == -1) { //从-2或者-1出发到3要换乘
            if (toFloor == 3) {
                person = new Person(personId, fromFloor, 1, 1, toFloor);
            }
        } else if (fromFloor == 2) { //从2楼出发到高楼层就到15换乘，到-3或3就到1换乘
            if (toFloor == -3 || toFloor == 3) {   //到1换乘
                person = new Person(personId, 2, 1, 1, toFloor);
            } else if (toFloor >= 16 && toFloor <= 20) {
                person = new Person(personId, 2, 15, 15, toFloor);
            }
        } else if (fromFloor == 3) { //从3楼出发不是1的低楼层到1换乘，中间偶数楼层也到1换乘，高楼层到15换乘
            if (toFloor >= -3 && toFloor <= 2 && toFloor != 1) {
                person = new Person(personId, 3, 1, 1, toFloor);
            } else if (toFloor >= 4 && toFloor <= 14 && toFloor % 2 == 0) {
                //这里可稍稍改进
                person = new Person(personId, 3, 1, 1, toFloor);
            } else if (toFloor >= 16 && toFloor <= 20) {
                person = new Person(personId, 3, 15, 15, toFloor);
            }
        } else if (fromFloor >= 4 && fromFloor <= 14 && fromFloor % 2 == 0) {
            //中间楼层而且是偶数，去-3或者3到1换乘，去高楼层到15换乘
            if (toFloor == 3 || toFloor == -3) {
                person = new Person(personId, fromFloor, 1, 1, toFloor);
            } else if (toFloor >= 16 && toFloor <= 20) {
                person = new Person(personId, fromFloor, 15, 15, toFloor);
            }
        } else if (fromFloor >= 4 && fromFloor <= 14 && fromFloor % 2 != 0) {
            //中间楼层而且是奇数，去-3到1换乘，去高楼层到15换乘
            if (toFloor == -3) {
                person = new Person(personId, fromFloor, 1, 1, toFloor);
            } else if (toFloor >= 16 && toFloor <= 20) {
                person = new Person(personId, fromFloor, 15, 15, toFloor);
            }
        } else if (fromFloor <= 20 && fromFloor >= 16) { //高楼层去中间楼层到15换乘
            if (toFloor >= 2 && toFloor <= 14) { //这里也可以改进一下
                person = new Person(personId, fromFloor, 15, 15, toFloor);
            }
        } else { //不用换乘
            person = new Person(personId, fromFloor, toFloor, 0, 0);
        }
        //TimableOutput.println("parse request" +
        //person.getPersonId() + " " + person.getFirstFromFloor() + " "
        //     + person.getFirstToFloor() + " "
        //    + person.getSecondFromFloor() + " "
        //   + person.getSecondToFloor());
        if (person.getSecondFromFloor() != 0) {
            this.perQueue.pickUpCountAdd();
            //TimableOutput.println("pickup count:"
            // +this.perQueue.getPickUpCount());
        }
        return person;
    }

    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            //TimableOutput.println("get the request from input "
            // + request.getPersonId());
            if (request == null) {
                this.perQueue.setHasrequest(false);
                //TimableOutput.println("no request");
                break;
            } else {
                try {
                    Person person = parseRequest(request);
                    this.perQueue.add(person);
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
