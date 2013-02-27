

/**
 * Complete the implementation of this class in line with the FSP model
 */

import display.*;

public class Controller {

  public static int Max = 9;
  protected NumberCanvas passengers;
	public int passengerCount;

	private final Object lock = new Object();
	private final Object entryLock = new Object();
	
	private boolean goNow = false;

  public Controller(NumberCanvas nc) {
    passengers = nc;
		passengerCount = 0;
  }

  public void newPassenger() throws InterruptedException {
		// complete implementation
		// use "passengers.setValue(integer value)" to update diplay
		if(passengerCount < Max){
			passengerCount++;
			passengers.setValue(passengerCount);
			synchronized (lock) {
				lock.notify();
			}
		}
		else{
			synchronized (entryLock) {
				entryLock.wait();
			}
		}
	}

	public int getPassengers(int mcar) throws InterruptedException {
    // complete implementation for part I
    // update for part II
    // use "passengers.setValue(integer value)" to update diplay
    // return 0; // dummy value to allow compilation
		while (passengerCount < mcar){
			if(goNow){
				int passCnt = passengerCount;
				passengerCount = 0;
				passengers.setValue(passengerCount);
				synchronized (entryLock){
					entryLock.notify();
				}
				goNow = !goNow;
				return passCnt;
			}			
			else{
				synchronized (lock) {
	  	    	lock.wait();
				}
			}
		}
		passengerCount -= mcar;
		passengers.setValue(passengerCount);
		synchronized (entryLock){
			entryLock.notify();
		}
		return mcar;
	}

  public synchronized void goNow() {
    // complete implementation for part II
		if(passengerCount > 0){
			goNow = true;
			synchronized (lock){
				lock.notifyAll();
			}
		}
  }
}
