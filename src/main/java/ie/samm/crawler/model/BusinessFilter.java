package ie.samm.crawler.model;

import java.io.Serializable;

import org.primefaces.model.SortOrder;

public class BusinessFilter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int firstResult;
	
	private int pageSize;
	
	private String sortField;
	
	private SortOrder sortOrder;

	private int rowCount;
	
	/**
	 * @return the firstResult
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/**
	 * @param firstResult the firstResult to set
	 */
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the sortField
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	/**
	 * @return the sortOrder
	 */
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

}
