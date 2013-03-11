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
	
	//Boolean allowing car to leave early
	private boolean goNow = false;

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

	//Allow car to query the number of passengers on the platform
	public int getPassengers(int mcar) throws InterruptedException {
		//While there are insufficient passengers on the platform to fill car
		while (passengerCount < mcar){
			//If the go now button has been pressed
			if(goNow){
				//Fetch number of available passengers
				int passCnt = passengerCount;
				//Remove passengers from platform and update number canvas
				passengerCount = passengerCount - passCnt;
				passengers.setValue(passengerCount);
				synchronized (entryLock){
					//Wake up entry lock in case it is waiting
					entryLock.notify();
				}
				//Reset go now boolean
				goNow = !goNow;
				//Return the number of passengers that were put into the car
				return passCnt;
			}			
			else{
				synchronized (lock) {
					//Force car to wait until there is a new passenger on the platform to retry
					lock.wait();
				}
			}
		}
		//Remove full car amount from passengers on platform and update number canvas
		passengerCount -= mcar;
		passengers.setValue(passengerCount);
		synchronized (entryLock){
			//Awaken entry lock since there is now space on the platform
			entryLock.notify();
		}
		return mcar;
	}

	//Allow car to leave immediately if operator hits the go now button
	public synchronized void goNow() {
		//If there is at least one passenger on the platform
		if(passengerCount > 0){
			//Set go now true
			goNow = true;
			synchronized (lock){
				//Awaken car so it can leave with however many passengers on the platform
				lock.notifyAll();
			}
		}
  }
}
