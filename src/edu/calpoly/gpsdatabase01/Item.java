package edu.calpoly.gpsdatabase01;

public class Item {
	private long id;
	private String item;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return item;
	}
}
