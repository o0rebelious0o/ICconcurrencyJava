public class PlatformAccess {

	private final Object platformLock = new Object();
	private boolean platformOccupied = false;

	public void arrive() throws InterruptedException {
		if(platformOccupied){			
			synchronized (platformLock) {
				platformLock.wait();
			}
		}
		platformOccupied = true;
	}

	public synchronized void depart() {
		platformOccupied = false;
		synchronized (platformLock) {
			platformLock.notify();
		}
	}
}
