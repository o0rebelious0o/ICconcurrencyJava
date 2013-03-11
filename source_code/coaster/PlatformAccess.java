public class PlatformAccess {

	//Lock object
	private final Object platformLock = new Object();
	
	//Boolean to say car is already at the platform
	private boolean platformOccupied = false;

	//Car trying to arrive
	public void arrive() throws InterruptedException {
		//If there is already a car at the platform
		if(platformOccupied){			
			synchronized (platformLock) {
				//Make car wait until platform is free
				platformLock.wait();
			}
		}
		//Set platform occupied to true as car is now at platform
		platformOccupied = true;
	}

	//Car leaving
	public synchronized void depart() {
		//Set platform occupied to false as car has left
		platformOccupied = false;
		synchronized (platformLock) {
			//Wake up car waiting for the platform to be free
			platformLock.notify();
		}
	}
}
