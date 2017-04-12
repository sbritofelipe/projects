package ie.samm.crawler.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.BusinessFilter;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.enumetaror.WebsiteEnum;
import ie.samm.crawler.model.util.Constants;
import ie.samm.crawler.service.GoldenPagesCrawlerService;

@Service("crawlerServiceImpl")
public class GoldenPagesCrawlerServiceImpl implements GoldenPagesCrawlerService{

	private static final int NUMBER_OF_RESULTS_PER_PAGE = 19;
	
	private static final Logger LOGGER = Logger.getLogger(GoldenPagesCrawlerServiceImpl.class.getName());

	/** Html Document parsed from the web page. */
	private Document htmlDocument;

	/** List of links on the page. */
	private List<String> links = new LinkedList<String>();

	private Elements elements = new Elements();

	private Connection connection;
	
	public GoldenPagesCrawlerServiceImpl() {
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashSet<Category> findCategories(String url, boolean subcategory) throws IOException{
		this.htmlDocument =  getHtmlDocumentFrom(url);//Jsoup.parse(html); 
//		if(getConnection().response().statusCode() == 200){
			// 200 is the HTTP OK status code indicating that everything is great.
		System.out.println("\n**Visiting** Received web page at " + url);
//		}

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
	
	public boolean findBusinessMoreInfoElements(String url) throws IOException {
		this.htmlDocument = getHtmlDocumentFrom(url);
		if(this.htmlDocument != null){
			elements = new Elements();
			elements.addAll(this.htmlDocument.select("div.col_left"));
			return true;
		}else{
			return false;
		}
	}

//	@SuppressWarnings("resource")
	public LinkedHashMap<String, String> findCompanies(BusinessFilter filter, String url) throws IOException {
		if(filter.getFirstResult() > NUMBER_OF_RESULTS_PER_PAGE){
			System.setProperty("webdriver.chrome.driver", "/users/public/chromedriver.exe");
			WebDriver webDriver = new ChromeDriver();
			webDriver.get(url);
			try {
				Thread.sleep(8000);
				for (int i = 1; i < filter.getFirstResult()/filter.getPageSize(); i++) {
					webDriver.findElement(By.id("result-next")).click();
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			WebClient webClient = new WebClient(BrowserVersion.CHROME);
//			webClient.getCookieManager().setCookiesEnabled(true);
//	        webClient.getOptions().setJavaScriptEnabled(true);
//	        webClient.getOptions().setTimeout(2000);
//	        webClient.getOptions().setUseInsecureSSL(true);
//	        // overcome problems in JavaScript
//	        webClient.getOptions().setThrowExceptionOnScriptError(false);
//	        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
//	        webClient.setCssErrorHandler(new SilentCssErrorHandler());
//            HtmlPage page = webClient.getPage(url);
//			page = page.getElementById("result-next").click();
//			while(page.getByXPath("//li[@class='result-item22']") == null) {
			    // Wait for javascript to catch up.
//			    webClient. waitForBackgroundJavaScript(1000);
//			}
//			WebResponse response = page.getWebResponse();
			String html = webDriver.getPageSource(); //response.getContentAsString();
			this.htmlDocument = Jsoup.parse(html);
			webDriver.close();
		}else{
			this.htmlDocument = getHtmlDocumentFrom(url);
		}
		if(this.htmlDocument != null){
			String resultspan = this.htmlDocument.body().select("div#results_header > span").text();
			filter.setRowCount(Integer.parseInt(resultspan.replaceAll("[^0-9]", "")));
			Elements linksOnPage = this.htmlDocument.body().select("li[class^=result-item]").select("div.listing_more").select("a[href]").select("a.wt");
			LinkedHashMap<String, String> companies = new LinkedHashMap<>();
			int size = filter.getFirstResult() + filter.getPageSize() > linksOnPage.size()? linksOnPage.size() : filter.getFirstResult() + filter.getPageSize();
			for (int i = filter.getFirstResult(); i < size; i++) {
				Element link = linksOnPage.get(i);
				companies.put(WebsiteEnum.GOLDEN_PAGES.getAddress() + link.attr("href"), link.text());
			}
			return companies;
		}
		return new LinkedHashMap<String, String>();
	}

	private Document getHtmlDocumentFrom(String url) throws IOException, HttpStatusException {
		setConnection(Jsoup.connect(url).userAgent(Constants.USER_AGENT));
		if(getConnection().response().statusCode() == 200){ 
			// 200 is the HTTP OK status code
			System.out.println("\n**Visiting** Received web page at " + url);
		}
		if(getConnection().response().statusCode() == 404){
			return null;
		}
		Document document = null;
		try{
			document = getConnection().get();
		}catch (HttpStatusException e) {
			LOGGER.log(Level.WARNING, "Url " + url + "does not exist.");
		}
		return document;
	}

	
	/**
	 * 
	 * @throws IOException 
	 */
	public HashSet<Business> searchBusinessInfo(BusinessFilter filter, Category category) throws IOException {
		HashSet<Business> businesses = new HashSet<Business>();
		if(category != null && category.getUrl() != null){
			HashSet<Category> subCategories = category.getSubcategories() != null ? category.getSubcategories() : findCategories(category.getUrl(), true);
			for (Category subCategory : subCategories) {
				LinkedHashMap<String, String> companies = findCompanies(filter, subCategory.getUrl());
				for (Entry<String, String> company : companies.entrySet()) {
					if(findBusinessMoreInfoElements(company.getKey())){
						Business business = populateBusiness(subCategory);
						businesses.add(business);
					}
				}
			}
		}
		return businesses;
	}

	private Business populateBusiness(Category subCategory) {
		Element element = getElements().get(0);
		String companyName = element.select("h1.name").text();
		String phone = element.select("div.phone_contact").first() != null? searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("div.phone_contact").first()) : null;
		String address = element.select("li.contact-address").text();
		String mobile = element.select("#mobileinfo").first() != null? searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("#mobileinfo").first()) : null;
		String email = element.select("#emailinfo").first() != null? searchForPatternInElement(Constants.REGEX_EMAIL, element.select("#emailinfo").first()) : null;
		Business business = new Business(subCategory, companyName, phone, address, mobile, email);
		
		System.out.println(business.toString());
		return business;
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