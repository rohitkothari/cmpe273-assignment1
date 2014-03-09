package edu.sjsu.cmpe.library.domain;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Book {
    private long isbn;
    private String title;
    @JsonProperty("publication-date")
    private String publication;
    private String language;
    @JsonProperty("num-pages")
    private int numberOfPages;
    private String status;
    //new fields added here
    private List<Review> reviews = new ArrayList<Review>();
    private ArrayList<Author> author = new ArrayList<Author>();
    
	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public  Review getbookReview(int reviewid) {
    	return this.reviews.get(reviewid);
    }
	
	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public ArrayList<Author> getAuthor() {
		return author;
	}

	public void setAuthor(ArrayList<Author> author) {
		this.author = author;
	}

	/**
     * @return the isbn
     */
    public long getIsbn() {
	return isbn;
    }

    /**
     * @param isbn
     *            the isbn to set
     */
    public void setIsbn(long isbn) {
	this.isbn = isbn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }
}
