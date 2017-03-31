package ie.samm.crawler.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.util.Constants;

@Component
public class CrawlerService {

	/** Html Document parsed from the web page. */
	private Document htmlDocument;

	/** List of links on the page. */
	private List<String> links = new LinkedList<String>();

	private Elements elements = new Elements();

	private Connection connection;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashSet<Category> findCategories(String url, boolean subcategory) throws IOException{
		this.htmlDocument = getHtmlDocumentFrom(url); 
		if(getConnection().response().statusCode() == 200){
			// 200 is the HTTP OK status code indicating that everything is great.
			System.out.println("\n**Visiting** Received web page at " + url);
		}

		Elements linksOnPage = new Elements(); 
		HashSet objects = new HashSet();

		if(!subcategory){
			linksOnPage.addAll(this.htmlDocument.body().select("li:contains(Categories)").nextAll().select("a[href]"));
			linksOnPage.addAll(this.htmlDocument.body().select("ul.no-header").select("a[href]"));
			System.out.println(linksOnPage.size() + " categories found.");
		}else{
			linksOnPage = this.htmlDocument.body().select("h2:contains(All categories)").nextAll().select("a[href]");
			System.out.println(linksOnPage.size() + " Subcategories found.");
		}
		for (Element link : linksOnPage) {
			Category cat = new Category(link.text(), link.absUrl("href"));
			objects.add(cat);
		}

		return objects;

	}
	
	public void findBusinessMoreInfoElements(String url) throws IOException {
		this.htmlDocument = getHtmlDocumentFrom(url);
		elements = new Elements();
		elements.addAll(this.htmlDocument.select("div.col_left"));
	}

	public LinkedHashMap<String, String> findCompanies(String url) throws IOException {
		this.htmlDocument = getHtmlDocumentFrom(url);
		Elements linksOnPage = this.htmlDocument.body().select("ul.results_list").first().select("div.listing_more").select("a[href]").select("a.wt");
		LinkedHashMap<String, String> companies = new LinkedHashMap<>();
		for (Element link : linksOnPage) {
			companies.put(link.absUrl("href"), link.text());
		}
		System.out.println(linksOnPage.size() + " businesses found.");
		return companies;
	}

	private Document getHtmlDocumentFrom(String url) throws IOException {
		if (getConnection() == null) {
			setConnection(Jsoup.connect(url).userAgent(Constants.USER_AGENT));
		} else {
			setConnection(getConnection().url(url));
		}
		return getConnection().get();
	}

	
	/**
	 * 
	 * @throws IOException 
	 */
	public HashSet<Business> searchBusinessInfo(HashSet<Category> categories) throws IOException {
		HashSet<Business> businesses = new HashSet<Business>();
		for (Category category : categories) {
			String businessCategory = category.getName();
			HashSet<Category> subCategories = category.getSubcategories() != null ? category.getSubcategories() : findCategories(category.getUrl(), true);
			for (Category subCategory : subCategories) {
				LinkedHashMap<String, String> companies = findCompanies(subCategory.getUrl());
				for (Entry<String, String> company : companies.entrySet()) {
					findBusinessMoreInfoElements(company.getKey());
					Element element = getElements().get(0);
					String companyName = element.select("h1.name").text();
					String phone = searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("div.phone_contact").first());
					String address = element.select("li.contact-address").text();
					String mobile = element.select("#mobileinfo").first() != null? 
									searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("#mobileinfo").first()) : null;
					String email = element.select("#emailinfo").first() != null?
									searchForPatternInElement(Constants.REGEX_EMAIL, element.select("#emailinfo").first()) : null;
					Business business = new Business(businessCategory, companyName, phone, address, mobile, email);
					
					System.out.println(business.toString());
					businesses.add(business);
				}
				break;
			}
			break;
		}
		return businesses;
	}
	
	public Set<String> searchForPattern(String pattern) {
		// This method should only be used after a successful crawl.
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
		}
		System.out.println("Searching for the Pattern...");
		String bodyText = this.htmlDocument.body().text();
		Matcher m = Pattern.compile(pattern).matcher(bodyText.toLowerCase());
		Set<String> words = new HashSet<String>();
		while (m.find()) {
			words.add(m.group());
		}
		return words;
	}

	public String searchForPatternInElement(String pattern, Element element) {
		// This method should only be used after a successful crawl.
		if (element == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
		}
		// System.out.println("Searching for the Pattern...");
		String bodyText = element.text();
		Matcher m = Pattern.compile(pattern).matcher(bodyText.toLowerCase());
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	// GETTERS AND SETTERS
	public Document getHtmlDocument() {
		return htmlDocument;
	}

	public void setHtmlDocument(Document htmlDocument) {
		this.htmlDocument = htmlDocument;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public Elements getElements() {
		return elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}