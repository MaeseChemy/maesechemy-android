package com.jmbg.loteriasgmv.dao;

import java.util.List;

public interface BaseDao<EntityClass> {
	
	public int getLastId();
	
	public abstract EntityClass findById(int id);
	
	public abstract boolean existsById(int id);
	
	public abstract boolean existsByField(String fieldName, Object fieldValue);
	
	public List<EntityClass> findAll();
	
	public List<EntityClass> findAll(String groupBy, String having, String orderBy);
	
	public List<EntityClass> findByField(String fieldName, Object fieldValue);
	
	public List<EntityClass> findByField(String fieldName, Object fieldValue, String groupBy, String having, String orderBy);
	
	public EntityClass findByUniqueField(String fieldName, Object fieldValue);
	
	public EntityClass save(EntityClass entity);
	
	/**
	 * Save a list of entities
	 * @param entities List of entities
	 * @return Return the list of entities saved with the id
	 */
	public List<EntityClass> save(List<EntityClass> entities);
	
	public EntityClass update (EntityClass entity);
	
	public EntityClass saveOrUpdate (EntityClass entity);
	
	public void remove(EntityClass entity);	
	
	public void remove(List<EntityClass> entities);
	
	public void removeByField(String fieldName, Object fieldValue);
	
	/**
	 * Delete all the entities
	 */
	public void removeAll();	
}
