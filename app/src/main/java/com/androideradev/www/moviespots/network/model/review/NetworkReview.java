package com.androideradev.www.moviespots.network.model.review;

import com.google.gson.annotations.SerializedName;

public class NetworkReview {

	@SerializedName("author_details")
	private NetworkAuthorDetails authorDetails;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("author")
	private String author;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("content")
	private String content;

	@SerializedName("url")
	private String url;

	public void setAuthorDetails(NetworkAuthorDetails networkAuthorDetails){
		this.authorDetails = networkAuthorDetails;
	}

	public NetworkAuthorDetails getAuthorDetails(){
		return authorDetails;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}