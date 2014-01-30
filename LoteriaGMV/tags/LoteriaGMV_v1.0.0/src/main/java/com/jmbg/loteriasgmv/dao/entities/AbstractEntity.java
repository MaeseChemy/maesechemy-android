package com.jmbg.loteriasgmv.dao.entities;
public abstract class AbstractEntity implements BaseEntity {

	protected Integer id;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}	
}
