package com.androideradev.www.moviespots.network.model.credit;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NetworkCreditContainer{

	@SerializedName("cast")
	private List<NetworkCast> cast;

	@SerializedName("id")
	private int id;

	@SerializedName("crew")
	private List<NetworkCrew> crew;

	public void setCast(List<NetworkCast> cast){
		this.cast = cast;
	}

	public List<NetworkCast> getCast(){
		return cast;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCrew(List<NetworkCrew> crew){
		this.crew = crew;
	}

	public List<NetworkCrew> getCrew(){
		return crew;
	}
}