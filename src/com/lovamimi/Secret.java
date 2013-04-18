package com.lovamimi;

public class Secret {
	String body;
	String datetime;
	int numComments;
	int numLikes;

	public Secret(String body, String datetime, int numComments, int numLikes) {
		super();
		this.body = body;
		this.datetime = datetime;
		this.numComments = numComments;
		this.numLikes = numLikes;
	}
}
