package com.rental.app.model.response;

import lombok.Data;

@Data
public class Link {
	private int nextOffset;
	
	public Link(int nextOffset) {
		this.nextOffset = nextOffset;
	}
}
