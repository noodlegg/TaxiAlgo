import java.util.*;

public class Taxi {

	private int taxiID;
	private int maxSeats;
	private int currentOccSeats = 0; // Number of currently occupied seats
	private int currentPos;
	private int endPos;
	private boolean hasTarget = false;
	private ArrayList<Passenger> dest = new ArrayList<>();
	private ArrayList<Integer> path = new ArrayList<>();
	private ArrayList<Passenger> toPickUp = new ArrayList<>();

	public Taxi(int taxiID, int maxSeats) {
		this.taxiID = taxiID;
		this.maxSeats = maxSeats;
	}
	
	void setEndPos(int endPos) {
		this.endPos = endPos;
	}
	
	int getEndPos() {
		return endPos;
	}
	
	void addToPickUp(Passenger passenger) {
		toPickUp.add(passenger);
	}

	ArrayList<Passenger> getToPickUp() {
		return toPickUp;
	}
	
	void clearToPickUp() {
		toPickUp.clear();
	}
	
	void setCurrentPos(int newPos) {
		currentPos = newPos;
	}

	int getTaxiID() {
		return taxiID;
	}

	void copyPath(ArrayList<Integer> path) {
		this.path.clear();
		this.path.addAll(path);
	}

	String moveTaxi() {
		currentPos = path.get(0);
		path.remove(0);
		String output = "m " + taxiID + " " + currentPos + " ";

		return output;
	}

	void removeFromPath() {
		path.remove(0);
	}

	int getCurrentPos() {
		return currentPos;
	}
	
	void setHasTarget(boolean hasTarget) {
		this.hasTarget = hasTarget;
	}
	
	boolean getHasTarget() {
		return hasTarget;
	}

	boolean isDestination() {
		for (Passenger pass : dest) {
			if (pass.getDestination() == currentPos) {
				return true;
			}
		}
		return false;
	}

	int getRemainingSeats() {
		return (maxSeats - currentOccSeats);
	}

	ArrayList<Integer> hallo(ArrayList<Passenger> allPassengers) {
		ArrayList<Integer> ints = new ArrayList<>();
		for (int i = 0; i < dest.size(); i++) {
			for (int j = 0; j < allPassengers.size(); j++) {
				if (dest.get(i) == allPassengers.get(j)) {
					ints.add(j);
				}
			}
		}
		return ints;
	}

	String pickUpPassenger(Passenger pass) {
		String output = "";
		if (currentOccSeats < maxSeats) {
			currentOccSeats++;
			pass.setInTaxi(true);
			dest.add(pass);
			output += "p " + taxiID + " " + pass.getDestination() + " "; // CHANGE THIS FOR MULTIPLE TAXIS
		} else {
			throw new IllegalStateException("No available seats!");
		}
		return output;
	}

	String dropOffPassenger() {
		String output = "";
		if (currentOccSeats > 0) {
			currentOccSeats--;
			for (int i = 0; i < dest.size(); i++) {
				if ((dest.get(i)).getDestination() == currentPos) {
					output += "d " + taxiID + " " + dest.get(i).getDestination() + " ";
					dest.remove(i);
				}
			}
			hasTarget = false;
		} else {
			throw new IllegalStateException("Dropped off ghost passenger.");
		}

		return output;

	}

}