package ie.samm.crawler.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	
	/**Html Document parsed from the web page.*/
	private Document htmlDocument;
	
	/**List of links on the page.*/
	private List<String> links = new LinkedList<String>();
	
	private Elements elements = new Elements();
	
	private Connection connection;
	
	public void crawl(String url) throws IOException{
		Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
		this.htmlDocument = connection.get();
		if(connection.response().statusCode() == 200){
			// 200 is the HTTP OK status code indicating that everything is great.
			System.out.println("\n**Visiting** Received web page at " + url);
		}
		if (!connection.response().contentType().contains("text/html")){
			System.out.println("**Failure** Retrieved something other than HTML");
		}
		elements.addAll(htmlDocument.select("div.listing_content"));
//		System.out.println("Found (" + linksOnPage.size() + ") links");
//		for (Element link : linksOnPage) {
//			this.links.add(link.absUrl("href"));
//		}
	}
	
	public Map<String, String> crawl(String url, int layer) throws IOException{
		connectTo(url);
		this.htmlDocument = getConnection().get();
		if(getConnection().response().statusCode() == 200){
			// 200 is the HTTP OK status code indicating that everything is great.
			System.out.println("\n**Visiting** Received web page at " + url);
		}

		Elements linksOnPage = new Elements(); 
		switch (layer) {
		case 0:
			linksOnPage.addAll(this.htmlDocument.body().select("li:contains(Categories)").nextAll().select("a[href]"));
			linksOnPage.addAll(this.htmlDocument.body().select("ul.no-header").select("a[href]"));
			System.out.println(linksOnPage.size() + " categories found.");
			break;
		
		case 1:
			linksOnPage = this.htmlDocument.body().select("h2:contains(All categories)").nextAll().select("a[href]");
			System.out.println(linksOnPage.size() + " Subcategories found.");
			break;
			
		case 2:
			linksOnPage = this.htmlDocument.body().select("ul.results_list").first().select("div.listing_more").select("a[href]").select("a.wt");
			System.out.println(linksOnPage.size() + " businesses found.");
			break;
			
		case 3:
			elements = new Elements();
			elements.addAll(this.htmlDocument.select("div.col_left"));
			break;

		default:
			break;
		}

		Map<String, String> links = new LinkedHashMap<String, String>();
		for (Element link : linksOnPage) {
			links.put(link.absUrl("href"), link.text());
		}
		return links;
	}
	
	private void connectTo(String url) throws IOException{
		if(getConnection() == null){
			setConnection(Jsoup.connect(url).userAgent(USER_AGENT));
		}else{
			setConnection(getConnection().url(url));
		}
	}
	
	public boolean searchForWord(String searchWord){
        // This method should only be used after a successful crawl.
        if(this.htmlDocument == null){
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }
	
	public Set<String> searchForPattern(String pattern){
        // This method should only be used after a successful crawl.
        if(this.htmlDocument == null){
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
        }
        System.out.println("Searching for the Pattern...");
        String bodyText = this.htmlDocument.body().text();
        Matcher m = Pattern.compile(pattern).matcher(bodyText.toLowerCase());
        Set<String> words = new HashSet<String>();
        while(m.find()){
        	words.add(m.group());
        }
        return words;
    }
	
	public String searchForPatternInElement(String pattern, Element element){
        // This method should only be used after a successful crawl.
        if(element == null){
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
        }
//        System.out.println("Searching for the Pattern...");
        String bodyText = element.text();
        Matcher m = Pattern.compile(pattern).matcher(bodyText.toLowerCase());
        if(m.find()){
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
