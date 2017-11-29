import java.util.*;

public class Run {

	TaxiScanner taxiScanner = TaxiScanner.getInstance();
	double alpha;
	int maxTime;
	int numOfTaxis;
	int numOfSeats;
	int numOfNodes;
	int[][] graph;
	int trainingPeriod;
	int totalPeriod;
	Taxi[] taxi;
	ArrayList<Passenger> allPassengers = new ArrayList<>();

	int[] scanInput(boolean countFirst) {
		String intSplitter = taxiScanner.nextLine();
		String[] ar = intSplitter.split(" ");
		int[] inputInts;

		if (!countFirst) {
			inputInts = new int[ar.length - 1];

			for (int i = 0; i < ar.length - 1; i++) {
				inputInts[i] = Integer.parseInt(ar[i + 1]);
			}
		} else {
			inputInts = new int[ar.length];

			for (int i = 0; i < ar.length; i++) {
				inputInts[i] = Integer.parseInt(ar[i]);
			}
		}

		return inputInts;
	}

	void init() {
		int numOfPreamble;
		numOfPreamble = Integer.parseInt(taxiScanner.nextLine());
		alpha = Double.parseDouble(taxiScanner.nextLine());
		maxTime = Integer.parseInt(taxiScanner.nextLine());
		int[] ar = scanInput(true);
		numOfTaxis = ar[0];
		numOfSeats = 1/*ar[1]*/;
		numOfNodes = Integer.parseInt(taxiScanner.nextLine());

		graph = new int[numOfNodes][];

		for (int i = 0; i < numOfNodes; i++) {
			graph[i] = scanInput(false);
		}

		ar = scanInput(true);
		trainingPeriod = ar[0];
		totalPeriod = ar[1];

		taxi = new Taxi[numOfTaxis];
		for (int i = 1; i <= numOfTaxis; i++) {
			taxi[i - 1] = new Taxi(i, numOfSeats);
		}

	}

	void doTestPhase() {

		String output = "";
		boolean canPickUp = false;

		for (int i = 0; i < numOfTaxis; i++) {
			taxi[i].setCurrentPos(0);
			output += "m " + taxi[i].getTaxiID() + " " + taxi[i].getCurrentPos() + " ";
		}
		output += "c";
		taxiScanner.println(output);

		int[] input;
		int start, end = 0;
		for (int i = 0; i < totalPeriod; i++) {

			output = "";
			input = scanInput(true);
			//System.err.print(input.length / 2 + " ");
			for (int j = 1; j < input.length; j += 2) {
				//System.err.print(input[j] + " " + input[j + 1] + " ");
				allPassengers.add(new Passenger(input[j], input[j + 1]));
			}
			//System.err.println();

			for (int k = 0; k < numOfTaxis; k++) {
				start = taxi[k].getCurrentPos();
				if (taxi[k].getRemainingSeats() > 0 ) {
					for (Passenger passenger : allPassengers) {
						if (!passenger.isInTaxi() && (passenger.getTaxiID() == k + 1 || !passenger.isTarget()) && !taxi[k].getHasTarget()) {
							                                    // Taxi has available seat and there is a waiting passenger
							passenger.setTaxiID(k + 1);
							taxi[k].setEndPos(passenger.getCurrentPos()); // HAS TO BE CALCULATED
							taxi[k].addToPickUp(passenger);
							// canPickUp = true;
							passenger.changeTarget(true);
							taxi[k].setHasTarget(true);
						/*} else { // Taxi has available seat but there is no waiting passenger
							end = start;*/
						}
					}
				} else if (!(taxi[k].getRemainingSeats() > 0)) {
					for (Passenger passenger : allPassengers) {
						if (passenger.isInTaxi() && (passenger.getTaxiID() == k + 1)){ // Taxi has no available seat. PLEASE OPTIMIZE THIS
							taxi[k].setEndPos(passenger.getDestination());
							break;
						}
					}
				}
				end = taxi[k].getEndPos();
				if (start != end) {
					dijkstra(start, end, k + 1);
					output += taxi[k].moveTaxi();
				} else if (taxi[k].isDestination()) {
					ArrayList<Integer> ints = taxi[k].hallo(allPassengers);
					for (int index : ints) {
						allPassengers.remove(index);
					}
					output += taxi[k].dropOffPassenger();
				} else if (end == start) {
					for (Passenger passenger : taxi[k].getToPickUp()) {
						output += taxi[k].pickUpPassenger(passenger);
					}
					taxi[k].clearToPickUp();
				}
			}
			output += "c";
			//System.err.println(output);
			taxiScanner.println(output);
		}
	}

	void dijkstra(int start, int end, int taxiID) {
		int[] dist = new int[graph.length];
		int[] prev = new int[graph.length];
		ArrayList<Integer> path = new ArrayList<>();
		LinkedList<Integer> queue = new LinkedList<>();

		for (int i = 0; i < dist.length; i++) {
			dist[i] = graph.length + 1;
			queue.add(i);
		}

		dist[start] = 0;

		int u = start;

		while (!queue.isEmpty()) {
			int z = graph.length + 1;
			for (int i = 0; i < queue.size(); i++) {
				if (dist[queue.get(i)] < z) {
					z = dist[queue.get(i)];
					u = queue.get(i);
					dist[u] = dist[queue.get(i)];
				}
			}

			for (int i = 0; i < queue.size(); i++) {
				if (queue.get(i) == u) {
					queue.remove(i);
					break;
				}
			}

			for (int i = 0; i < graph[u].length; i++) {
				int alt = dist[u] + 1;
				if (alt < dist[graph[u][i]]) {
					dist[graph[u][i]] = alt;
					prev[graph[u][i]] = u;
				}
			}
		}
		int v = end;
		while (v != start) {
			path.add(v);
			v = prev[v];
		}
		Collections.reverse(path);
		taxi[taxiID - 1].copyPath(path);
	}

	void run() {
		init();
		doTestPhase();
	}

	public static void main(String[] args) {
		new Run().run();
	}

}
