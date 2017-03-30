/**
 * 
 */
package ie.samm.crawler.model;

/**
 * @author Felipe
 *
 */
public class Business extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String category;

	private String company;
	
	private String phone;
	
	private String address;
	
	private String mobile;
	
	private String email;
	 
	public Business(String category, String company, String phone, String address, String mobile, String email) {
		this.category = category;
		this.company = company;
		this.phone = phone;
		this.address = address;
		this.mobile = mobile;
		this.email = email;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Business information:\n" + "Name: " + getCompany() + "\nCategory: " + getCategory() + "\nAddress: " + getAddress() + "\nPhone: " + getPhone() + "\nMobile: " + getMobile() +"\nEmail: " + getEmail();
	}
	
}
