package ie.samm.crawler.model.enumetaror;

public enum WebsiteEnum {

	GOLDEN_PAGES(1, "https://www.goldenpages.ie");
	
	private Integer id;
	private String address;
	
	private WebsiteEnum(Integer id, String address){
		this.id = id;
		this.address = address;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
}
