package ie.samm.crawler.jsf.datamodel;
 
import java.util.Comparator;

import org.primefaces.model.SortOrder;

import ie.samm.crawler.model.Business;
 
public class LazySorter implements Comparator<Business> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Business business1, Business business2) {
        try {
            Object value1 = Business.class.getField(this.sortField).get(business1);
            Object value2 = Business.class.getField(this.sortField).get(business2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}