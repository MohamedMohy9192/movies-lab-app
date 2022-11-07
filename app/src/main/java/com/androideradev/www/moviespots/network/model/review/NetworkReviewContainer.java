package com.androideradev.www.moviespots.network.model.review;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NetworkReviewContainer{

	@SerializedName("id")
	private int id;

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<NetworkReview> reviews;

	@SerializedName("total_results")
	private int totalResults;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setReviews(List<NetworkReview> reviews){
		this.reviews = reviews;
	}

	public List<NetworkReview> getReviews(){
		return reviews;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}
}