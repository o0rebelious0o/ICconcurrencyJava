

/**
 * Complete the implementation of this class in line with the FSP model
 */

public class PlatformAccess {

	private final Object platformLock = new Object();
	private boolean platformOccupied = false;

  /* declarations required */

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
