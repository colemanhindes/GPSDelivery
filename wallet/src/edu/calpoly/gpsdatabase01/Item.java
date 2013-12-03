package edu.calpoly.gpsdatabase01;

import com.google.android.gms.samples.wallet.ItemInfo;

public class Item {
	private long id;
	private String item;
	private String desc;
	private long price;
	private long tax;
	// CURRENCY_CODE_USD
	public final String CURRENCY_CODE = "USD";
	private String store_name;
	private int image;

	// Getters & Setters
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getPrice() {
		return price;
	}
	public void setTax(Long tax) {
		this.tax = tax;
	}
	public long getTax() {
		return tax;
	}
	public void setStore(String store) {
		this.store_name = store;
	}
	public String getStore() {
		return store_name;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public int getImage() {
		return image;
	}
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return item;
	}
}

//new ItemInfo("Large Pepperoni Fatte's Pizza", "Large Pepperoni Pizza from Fatte's", 20000000, 3000000, CURRENCY_CODE_USD,
//      "Fatte's Pizzaria", R.drawable.fattes)
