package com.lovamimi;

import java.io.Serializable;

public class Secret implements Serializable {
	private static final long serialVersionUID = 0x12345678L;
	
	String body;
	String datetime;
	String iconName;
	int numComments;
	int numLikes;
	
	@Override
	public String toString() {
		return "Secret [body=" + body + ", datetime=" + datetime
				+ ", iconName=" + iconName + ", numComments=" + numComments
				+ ", numLikes=" + numLikes + "]";
	}

	public Secret(String body, String datetime, String iconName,
			int numComments, int numLikes) {
		super();
		this.body = body;
		this.datetime = datetime;
		this.iconName = iconName;
		this.numComments = numComments;
		this.numLikes = numLikes;
	}
	 
	public int getIconResource() {
		if (iconName == "0.jpg") {
			return R.drawable.lovamimi_0;
		} else if (iconName.equals("1.jpg")) {
			return R.drawable.lovamimi_1;
		} else if (iconName.equals("2.jpg")) {
			return R.drawable.lovamimi_2;
		} else if (iconName.equals("2.jpg")) {
			return R.drawable.lovamimi_2;
		} else if (iconName.equals("3.jpg")) {
			return R.drawable.lovamimi_3;
		} else if (iconName.equals("4.jpg")) {
			return R.drawable.lovamimi_4;
		} else if (iconName.equals("5.jpg")) {
			return R.drawable.lovamimi_5;
		} else if (iconName.equals("6.jpg")) {
			return R.drawable.lovamimi_6;
		} else if (iconName.equals("7.jpg")) {
			return R.drawable.lovamimi_7;
		} else if (iconName.equals("8.jpg")) {
			return R.drawable.lovamimi_8;
		} else if (iconName.equals("9.jpg")) {
			return R.drawable.lovamimi_9;
		} else {
			return R.drawable.lovamimi_0;
		}
	}
}
