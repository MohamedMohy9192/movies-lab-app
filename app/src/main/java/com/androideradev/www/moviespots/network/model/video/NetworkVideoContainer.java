package com.androideradev.www.moviespots.network.model.video;

import java.util.List;

import com.androideradev.www.moviespots.network.model.video.NetworkVideo;
import com.google.gson.annotations.SerializedName;

public class NetworkVideoContainer{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<NetworkVideo> videos;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setVideos(List<NetworkVideo> videos){
		this.videos = videos;
	}

	public List<NetworkVideo> getVideos(){
		return videos;
	}
}