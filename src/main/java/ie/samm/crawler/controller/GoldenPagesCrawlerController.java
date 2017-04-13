package ie.samm.crawler.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ie.samm.crawler.jsf.datamodel.LazySorter;
import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.BusinessFilter;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.enumetaror.WebsiteEnum;
import ie.samm.crawler.service.GoldenPagesCrawlerService;

@ManagedBean(name="goldenPagesCrawlerController")
@ViewScoped
public class GoldenPagesCrawlerController extends AbstractController{

	@ManagedProperty("#{crawlerServiceImpl}")
	private GoldenPagesCrawlerService crawlerService;
	
	private List<Business> businesses;
	
	private HashSet<Category> categories;
	
	private HashSet<Category> subcategories;
	
	private Category category;
	
	private Category subcategory;
	
	private LazyDataModel<Business> dataModel;
	
	private BusinessFilter businessFilter;
	
	private LinkedHashMap<String, String> linksCompanies;
	
	@PostConstruct
	public void init(){
		try {
			initVariables();
			getCategories().addAll(getCrawlerService().findCategories(WebsiteEnum.GOLDEN_PAGES.getAddress(), false));
			addSuccessMessage(getCategories().size() + " categories found.");
			
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Error while scraping " + WebsiteEnum.GOLDEN_PAGES.getAddress());
		}
	}

	private void initVariables() {
		this.category = null;
		this.subcategory = null;
		this.businesses = new ArrayList<Business>();
		this.categories = new HashSet<Category>();
		this.subcategories = new HashSet<Category>();
		this.businessFilter = new BusinessFilter();
	}
	
	public void initSubcategories() throws Exception{
		try {
			this.subcategories = new HashSet<Category>();
			this.subcategories = getCategory() != null? getCrawlerService().findCategories(getCategory().getUrl(), true) : null;
			addSuccessMessage(this.subcategories.size() + " subcategories found.");
		} catch (IOException e) {
			addErrorMessage("Error.");
		}
	}
	
	public void search(){
		try {
			linksCompanies = crawlerService.findLinkCompanies(getSubcategory());
			businessFilter.setRowCount(linksCompanies.size());
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("goldenPagesForm:businesses");
			dataTable.reset();
			loadDataModel();	
		} catch (Exception e) {
			addErrorMessage("Error finding companies." + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void loadDataModel(){
		this.dataModel = new LazyDataModel<Business>() {
			private static final long serialVersionUID = 1L;
			
			@Override
		    public List<Business> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
				
				try {
					businessFilter.setFirstResult(first);
					businessFilter.setPageSize(pageSize);
					businessFilter.setSortField(sortField);
					businessFilter.setSortOrder(sortOrder);
					businesses = new ArrayList<>();
					businesses.addAll(crawlerService.searchBusinessInfo(businessFilter, subcategory, linksCompanies));	
					setRowCount(businessFilter.getRowCount());
					if(sortField != null) {
						Collections.sort(getBusinesses(), new LazySorter(sortField, sortOrder));
					}
				} catch (Exception e) {
					addErrorMessage("Error retrieving businesses from Golden Pages.");
				}
		    	//sort
		        return businesses;
		    }
			
		};
	}
	

	/**
	 * @return the crawlerService
	 */
	public GoldenPagesCrawlerService getCrawlerService() {
		return crawlerService;
	}

	/**
	 * @param crawlerService the crawlerService to set
	 */
	public void setCrawlerService(GoldenPagesCrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}

	// GETTERS AND SETTERS
	/**
	 * @return the businesses
	 */
	public List<Business> getBusinesses() {
		return businesses;
	}

	/**
	 * @param businesses the businesses to set
	 */
	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}


	/**
	 * @return the categories
	 */
	public HashSet<Category> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(HashSet<Category> categories) {
		this.categories = categories;
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

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the subcategory
	 */
	public Category getSubcategory() {
		return subcategory;
	}

	/**
	 * @param subcategory the subcategory to set
	 */
	public void setSubcategory(Category subcategory) {
		this.subcategory = subcategory;
	}

	/**
	 * @return the dataModel
	 */
	public LazyDataModel<Business> getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel the dataModel to set
	 */
	public void setDataModel(LazyDataModel<Business> dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * @return the businessFilter
	 */
	public BusinessFilter getBusinessFilter() {
		return businessFilter;
	}

	/**
	 * @param businessFilter the businessFilter to set
	 */
	public void setBusinessFilter(BusinessFilter businessFilter) {
		this.businessFilter = businessFilter;
	}

	/**
	 * @return the linksCompanies
	 */
	public LinkedHashMap<String, String> getLinksCompanies() {
		return linksCompanies;
	}

	/**
	 * @param linksCompanies the linksCompanies to set
	 */
	public void setLinksCompanies(LinkedHashMap<String, String> linksCompanies) {
		this.linksCompanies = linksCompanies;
	}
	
}