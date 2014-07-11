package com.jmbg.apuestasgmv.model.dao.entities;
public abstract class AbstractEntity implements BaseEntity {

	protected Integer id;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}	
}
