package edu.sjsu.cmpe.library.api.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.dto.ReviewsDto;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.dto.ReviewDto;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    /** bookRepository instance */
    private final BookRepositoryInterface bookRepository;
    private int reviewId=0;

    /**
     * BookResource constructor
     * 
     * @param bookRepository
     *            a BookRepository instance
     */
    public BookResource(BookRepositoryInterface bookRepository) {
	this.bookRepository = bookRepository;
    }

    @GET
    @Path("/{isbn}")
    @Timed(name = "view-book")
    public BookDto getBookByIsbn(@PathParam("isbn") LongParam isbn) {
	Book book = bookRepository.getBookByISBN(isbn.get());
	BookDto bookResponse = new BookDto(book);
	bookResponse.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(),"GET"));
	bookResponse.addLink(new LinkDto("update-book","/books/" + book.getIsbn(), "POST"));
	bookResponse.addLink(new LinkDto("delete-book","/books/" + book.getIsbn(), "DELETE"));
	
	// add more links

	return bookResponse;
    }

    @POST
    @Timed(name = "create-book")
    public Response createBook(Book request) {
	// Store the new book in the BookRepository so that we can retrieve it.
	Book savedBook = bookRepository.saveBook(request);

	String location = "/books/" + savedBook.getIsbn();
	BookDto bookResponse = new BookDto(savedBook);
	bookResponse.addLink(new LinkDto("view-book", location, "GET"));
	bookResponse.addLink(new LinkDto("update-book", location, "POST"));
	bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
	bookResponse.addLink(new LinkDto("create-review", location, "POST"));
	// Add other links if needed

	return Response.status(201).entity(bookResponse).build();
    }
    
    @DELETE
    @Path("/{isbn}")
    @Timed(name = "delete-book")
    public Response deleteBook(@PathParam("isbn") LongParam isbn){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	bookRepository.deleteBook(book);
    	LinksDto links = new LinksDto();
   	 	links.addLink(new LinkDto("create-book", "/books", "POST"));;
      	return Response.ok(links).build();
    }
    
    @PUT
    @Path("/{isbn}")
    @Timed(name= "update-book")
    public Response updateBook(@PathParam("isbn") LongParam isbn,@QueryParam("status") String newStatus){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	bookRepository.updateBook(book,newStatus);
    	Map<String, Object> responseMap = new HashMap<String, Object>();
	    List<LinkDto> links = new ArrayList<LinkDto>();
	    links.add(new LinkDto("view-book", "/books/" + book.getIsbn(), "GET"));
	    links.add(new LinkDto("update-book","/books/" + book.getIsbn(),"PUT"));
	    links.add(new LinkDto("delete-book","/books/" + book.getIsbn(),"DELETE"));
	    links.add(new LinkDto("create-review","/books/" + book.getIsbn() + "/reviews","POST"));
	    responseMap.put("links", links);
    	return Response.status(200).entity(responseMap).build();
    }
    
    @POST
    @Path("/{isbn}/reviews")
    @Timed(name = "create-review")
    public Response createReview(@PathParam("isbn") LongParam isbn, Review reviews) {

    	Book book = bookRepository.getBookByISBN(isbn.get());

    	reviews.setId(reviewId);
    	reviewId++;
    	book.getReviews().add(reviews);

    	ReviewDto reviewResponse = new ReviewDto(reviews);


    	Map<String, Object> response_Map = new HashMap<String, Object>();
    	List<LinkDto> links = new ArrayList<LinkDto>();
    	links.add(new LinkDto("view-review", "/books/" + book.getIsbn() + "/reviews/" + reviews.getId(), "GET"));

    	response_Map.put("links", links);
    	return Response.status(201).entity(response_Map).build();

    } 
    
    @GET
    @Path("/{isbn}/reviews/{review_id}")
    @Timed(name = "view-book-review")

    public ReviewDto viewReview (@PathParam("isbn") LongParam isbn,@PathParam("review_id") int reviewid ) {

    	Book book = bookRepository.getBookByISBN(isbn.get());
    	Review review =book.getbookReview(reviewid);
    	
       	ReviewDto reviewResponse = new ReviewDto(review);
       	
       	reviewResponse.addLink(new LinkDto("view-book-review", "/books/" + book.getIsbn()+"/reviews/","GET"));
    	return reviewResponse;
    	  	
    	   }
    
    
}

