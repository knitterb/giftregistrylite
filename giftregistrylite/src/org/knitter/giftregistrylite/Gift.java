package org.knitter.giftregistrylite;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.datanucleus.api.jpa.annotations.Extension;

@Entity
public class Gift {

	public Gift() {
	}
	
	public Key getId() {
		return id;
	}
	
	public void setId(Key newId) {
		id=newId;
	}
	
    public Key getFamilymember() {
		return familymember_key;
	}
	public void setFamilymember(Key familymember_key) {
		this.familymember_key = familymember_key;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String descrition) {
		this.description = descrition;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getComment() {
		return comment.toString();
	}
	public void setComment(String comment) {
		this.comment = new Text(comment);
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Id
	private Key id;
	private Key familymember_key;
	private String description;
    private String priority; 
    private Link link;
    private Integer quantity;
    private Text comment;
    private double price;
    
    @Extension(vendorName = "datanucleus", key = "gae.parent-pk", value ="true")
	public Key parentID;
	
}
