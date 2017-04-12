package ie.samm.crawler.service;

import java.io.IOException;
import java.util.HashSet;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.BusinessFilter;
import ie.samm.crawler.model.Category;

public interface GoldenPagesCrawlerService {
	
	HashSet<Business> searchBusinessInfo(BusinessFilter filter, Category category) throws IOException;

	HashSet<Category> findCategories(String url, boolean subcategory) throws IOException;

}
