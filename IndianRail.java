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
	/**
     * When a passenger arrives in a station, it first invokes the function
     * station_wait_for_train(struct station *station)
     * This function must not return until a train is in the station (i.e., a call to station_load_train is in
     * progress) and there are enough free seats on the train for this passenger to sit down. Once this
     * function returns, the passenger robot will move the passenger on board the train and into a seat
     * (you do not need to worry about how this mechanism works).
     * */
    static void station_wait_for_train(Station station) throws InterruptedException {
        station.lock.lock();
        station.passengersAtTheStation++;
        synchronized (station.lock) {
            station.lock.wait(1);
        }
        station.lock.unlock();
    }


    /**
     *  When a train arrives in the station and has opened its doors, it invokes the function
     * station_load_train(struct station *station, int count)
     * where count indicates how many seats are available on the train. The function must not return
     * until the train is satisfactorily loaded (all passengers are in their seats, and either the train is full or
     * all waiting passengers have boarded).
     * */

    static void station_load_train(Station station,int count) throws InterruptedException {
        station.lock.lock();
        while((count >0) && (station.passengersAtTheStation > 0))
        {
            station.passengersInTrain++;
            count--;
        }
        synchronized (station.passengerSeatedCondition) {
            station.passengerSeatedCondition.wait(1);
        }
        station.lock.unlock();
    }

    /**
     * Once the passenger is seated, it will
     * call the function
     * station_on_board(struct station *station)
     * to let the train know that it's on board.
     * */

    static void station_on_board(Station station ) {
        station.lock.lock();
        while(station.passengersInTrain >0 && station.totalBoarderPassengers < totalNumberOfPassenger)
        {
            station.passengerSeatedCondition.signal();
            station.passengersInTrain--;
            System.out.println("|\t\uD83E\uDDCD\u200D ️➡\uD83D\uDE82");
            station.totalBoarderPassengers++;
        }
        station.passengerSeatedCondition.signalAll();
        station.lock.unlock();

    }

}
