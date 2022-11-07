package com.androideradev.www.moviespots.network.model.image;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NetworkImageContainer{

	@SerializedName("backdrops")
	private List<NetworkBackdrop> backdrops;

	@SerializedName("posters")
	private List<NetworkPoster> posters;

	@SerializedName("id")
	private int id;

	@SerializedName("logos")
	private List<NetworkLogo> logos;

	public void setBackdrops(List<NetworkBackdrop> backdrops){
		this.backdrops = backdrops;
	}

	public List<NetworkBackdrop> getBackdrops(){
		return backdrops;
	}

	public void setPosters(List<NetworkPoster> posters){
		this.posters = posters;
	}

	public List<NetworkPoster> getPosters(){
		return posters;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLogos(List<NetworkLogo> logos){
		this.logos = logos;
	}

	public List<NetworkLogo> getLogos(){
		return logos;
	}
}