package ie.samm.crawler.model;

import java.io.Serializable;

public abstract class Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the id
	 */
	public abstract Serializable getId();

}
