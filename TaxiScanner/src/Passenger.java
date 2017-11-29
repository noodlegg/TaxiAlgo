public class Passenger {

	private int currentPos;
	private int destination;
	private int taxiID;
	private boolean inTaxi = false;
	private boolean isTarget = false;

	public Passenger(int currentPos, int destination) {
		this.currentPos = currentPos;
		this.destination = destination;
	}
	
	boolean isTarget() {
		return isTarget;
	}
	
	void changeTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
	
	void setTaxiID(int taxiID) {
		this.taxiID = taxiID;
	}
	
	int getTaxiID() {
		return taxiID;
	}

	boolean reachedDestination() {
		if (currentPos == destination) {
			return true;
		}
		return false;
	}
	
	int getDestination() {
		return destination;
	}

	int getCurrentPos() {
		return currentPos;
	}

	void setInTaxi(boolean inTaxi) {
		this.inTaxi = inTaxi;
	}

	boolean isInTaxi() {
		return inTaxi;
	}

}