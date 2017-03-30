package ie.samm.crawler.model;

import java.util.HashSet;

public class Category extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String name;
	
	private String url;
	
	private HashSet<Category> subcategories;

	public Category() {

	}
	
	public Category(String name, String url) {
		this.name = name;
		this.url = url;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the subcategories
	 */
	public HashSet<Category> getSubcategories() {
		return subcategories;
	}

	/**
	 * @param subcategories the subcategories to set
	 */
	public void setSubcategories(HashSet<Category> subcategories) {
		this.subcategories = subcategories;
	}
	
}
