package com.androideradev.www.moviespots.network.model.video;

import com.google.gson.annotations.SerializedName;

public class NetworkVideo {

	@SerializedName("site")
	private String site;

	@SerializedName("size")
	private int size;

	@SerializedName("iso_3166_1")
	private String iso31661;

	@SerializedName("name")
	private String name;

	@SerializedName("official")
	private boolean official;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("published_at")
	private String publishedAt;

	@SerializedName("iso_639_1")
	private String iso6391;

	@SerializedName("key")
	private String key;

	public void setSite(String site){
		this.site = site;
	}

	public String getSite(){
		return site;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setIso31661(String iso31661){
		this.iso31661 = iso31661;
	}

	public String getIso31661(){
		return iso31661;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setOfficial(boolean official){
		this.official = official;
	}

	public boolean isOfficial(){
		return official;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setPublishedAt(String publishedAt){
		this.publishedAt = publishedAt;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public void setIso6391(String iso6391){
		this.iso6391 = iso6391;
	}

	public String getIso6391(){
		return iso6391;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}
}