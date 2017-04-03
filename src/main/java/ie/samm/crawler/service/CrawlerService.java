package ie.samm.crawler.service;

import java.io.IOException;
import java.util.HashSet;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.Category;

public interface CrawlerService {
	
	HashSet<Business> searchBusinessInfo(HashSet<Category> category) throws IOException;

	HashSet<Category> findCategories(String url, boolean b) throws IOException;

}
