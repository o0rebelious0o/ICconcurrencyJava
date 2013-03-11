
/**
* Complete the implementation of this class in line with the FSP model
*/

import display.*;

public class Controller {

	protected NumberCanvas passengers;
	
	//Max number of passengers
	public static int Max = 9;
	
	//Current number of passengers on the platform
	public int passengerCount;

	//Objects to lock and unlock
	private final Object lock = new Object();
	private final Object entryLock = new Object();

	public Controller(NumberCanvas nc) {
		passengers = nc;
		passengerCount = 0;
	}

	//Add passengers to the platform
	public void newPassenger() throws InterruptedException {
		//If there is space on the platform
		if(passengerCount < Max){
			//Increment passenger count and update number canvas
			passengerCount++;
			passengers.setValue(passengerCount);
			synchronized (lock) {
				//Since there is a new passenger, awaken car as there may now be enough
				lock.notify();
			}
		}
		else{
			synchronized (entryLock) {
				//Force new passenger thread to wait until a car has left making space 
				entryLock.wait();
			}
		}
	}

	//Allow car to query number of passengers on the platform
	public int getPassengers(int mcar) throws InterruptedException {
		//While there aren't enough passengers to fill the car waiting on the platform loop here and rewait
		while (passengerCount < mcar){
			synchronized (lock) {
				//Wait to be notified that there may be enough passengers when new one arrives
				lock.wait();
			}
		}
		//Fill car with passengers and remove them from the platform
		passengerCount -= mcar;
		passengers.setValue(passengerCount);
		synchronized (entryLock){
			//A car of people has left, notify controller that there will be room on the platform for new passengers
			entryLock.notify();
		}
		//Return number of passengers in the car
		return mcar;
	}

	public synchronized void goNow() {
    // complete implementation for part II
	}

}
