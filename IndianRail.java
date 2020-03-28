/**
 * IndianRail has decided to improve its efficiency by automating not just its trains but also its
 * passengers. Each passenger and each train is controlled by a thread. You have been hired to write
 * synchronization functions that will guarantee orderly loading of trains. You must define a
 * structure struct station, plus several functions described below.
 *
 * @author Vishal Singh
 */

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IndianRail{

    static int totalNumberOfPassenger;
    static int noOfFreeSeatsInTheTrain;

    static class Station {

        int passengersAtTheStation;
        int passengersInTrain;
        int totalBoarderPassengers;
        final Lock lock;
        final Condition passengerSeatedCondition;

        Station(Lock lock,Condition passengerSeatedCondition) {
            passengersAtTheStation = 0;
            totalBoarderPassengers = 0;
            passengersInTrain = 0;
            this.lock = lock;
            this.passengerSeatedCondition = passengerSeatedCondition;
        }
    }
	static class Train extends Thread {
        Station s;
        Train(Station s) {
            this.s = s;
        }
        public void run() {
            try {
                station_load_train(s,noOfFreeSeatsInTheTrain);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    static class  Passenger extends Thread {
        Station s;
        Passenger(Station s) {
            this.s = s;
        }
        public void run() {
            try {
                station_wait_for_train(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
