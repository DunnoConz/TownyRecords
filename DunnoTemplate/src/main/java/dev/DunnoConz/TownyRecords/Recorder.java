package dev.DunnoConz.TownyRecords;

import java.util.ArrayList;

public class Recorder {
	private Boolean isComplete = false;
	private ArrayList<String> messages;
	
	// constructor
	public Recorder() {
		// add a timeout
		messages = new ArrayList<String>();
		messages.add("[START OF LOG]");
	}
	
	public void Log(String message) {
		if (!isComplete) {
			messages.add(message);
		}
	}
	
	public ArrayList<String> Complete(Boolean returnData) {
		isComplete = true; // in case data tries to get added whilst packing data
		if (returnData) {
			return messages; //return packed data
		}
		return new ArrayList<String>();
	}
}
