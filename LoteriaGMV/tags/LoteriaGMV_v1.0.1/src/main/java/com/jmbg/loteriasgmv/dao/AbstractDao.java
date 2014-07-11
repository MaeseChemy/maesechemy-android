package com.jmbg.loteriasgmv.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmbg.loteriasgmv.dao.entities.BaseEntity;
import com.jmbg.loteriasgmv.util.LogManager;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class AbstractDao<EntityClass extends BaseEntity> implements BaseDao<EntityClass> {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	protected BaseDBAdapter dbAdapter;

	protected Class<EntityClass> domainClass;

	protected EntityClass domainObject;

	private int lastId;

	public AbstractDao(BaseDBAdapter dbAdapter, Class<EntityClass> domainClass) {
		this.dbAdapter = dbAdapter;
		
		this.domainClass = domainClass;

		try {
			this.domainObject = this.domainClass.newInstance();
		} catch (IllegalAccessException e) {
			LOG.error("Domain class must define void constructor: " + e.getMessage());
			LOG.debug("Domain class must define void constructor", e);
		} catch (InstantiationException e) {
			LOG.error("Domain class must define void constructor: " + e.getMessage());
			LOG.debug("Domain class must define void constructor", e);		
		}			
	}
	
	/**
	 * Convierte el registro del Cursor en una Entidad
	 * @param cursor
	 * @return
	 */
	protected abstract EntityClass cursorToEntity(Cursor cursor);
	
	/**
	 * Convierte una entidad en ContentValues para poder realizar la insercion/edicion
	 * @param entity
	 * @return
	 */
	protected abstract ContentValues entityToContentValues(EntityClass entity);

	@Override
	public EntityClass findById(int id) {
		return findByUniqueField(EntityClass._ID, id);
	}

	/**
	 * Busqueda por campos UNIQUE
	 * @param username
	 * @return
	 */
	@Override
	public EntityClass findByUniqueField(String fieldName, Object fieldValue) {
		EntityClass entity = null;
		
		List<EntityClass> listUsers = this.findByField(fieldName, fieldValue);
		
		if (listUsers.size() != 0) {
			entity = listUsers.get(0);
		}
		
		return entity;
	}	
	
	@Override
	public List<EntityClass> findAll() {
		return findAll(null, null, null);
	}
	
	@Override
	public List<EntityClass> findAll(String groupBy, String having, String orderBy) {
		return findByField(null, null, groupBy, having, orderBy);
	}
	
	@Override
	public List<EntityClass> findByField(String fieldName, Object fieldValue) {
		return findByField(fieldName, fieldValue, null, null, null);
	}	

	@Override
	public List<EntityClass> findByField(String fieldName, Object fieldValue, String groupBy, String having, String orderBy) {
		List<EntityClass> list = new ArrayList<EntityClass>();
		EntityClass user = null;
		
		dbAdapter.open();
		
		Cursor cursor = queryByField(fieldName, fieldValue, groupBy, having, orderBy);
		
		if (cursor.moveToFirst()) {
			do {
				user = cursorToEntity(cursor);

				list.add(user);
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		dbAdapter.close();
		
		return list;
	}	
	
	@Override
	public boolean existsById(int id) {
		return existsByField(EntityClass._ID, id);
	}
	
	@Override
	public boolean existsByField(String fieldName, Object fieldValue) {
		Cursor cursor = queryByField(fieldName, fieldValue);
		
		return cursor.getCount() != 0;
	}
	
	public Cursor queryByField(String fieldName, Object fieldValue) {
		return queryByField(fieldName, fieldValue, null, null, null);
	}

	public Cursor queryByField(String fieldName, Object fieldValue, String groupBy, String having, String orderBy) {
		LOG.info("queryByField [table=" + domainObject.getTableName() + "][" + fieldName + "=" + fieldValue +"]");

		String selection = null;
		String[] selectionArgs = null;
		
		if (fieldName != null) {
			selection = fieldName + "=?";
			
			if (fieldValue != null) {
				selectionArgs = new String[]{fieldValue.toString()};
			}
		}
		
		Cursor cursor = dbAdapter.getSqlLiteDatabase().query(domainObject.getTableName(), null, selection, selectionArgs, groupBy, having, orderBy);

		return cursor;
	}

	@Override
	public EntityClass save(EntityClass entity) {
		LOG.info("Saving [table=" + domainObject.getTableName() + "][" + entity +"]");
		
		ContentValues contentValues = entityToContentValues(entity);
		
		dbAdapter.open();
		
		// Inserto el registro en la base de datos
		dbAdapter.getSqlLiteDatabase().insert(domainObject.getTableName(), null, contentValues);
		updateLastId();
		
		// Obtengo el ultimo autonumerico generado
		entity.setId(this.getLastId());
		
		dbAdapter.close();
		
		return entity;
	}	
	
	/**
	 * Save a list of entities
	 * @param entities List of entities
	 * @return Return the list of entities saved with the id
	 */
	@Override
	public List<EntityClass> save(List<EntityClass> entities) {
		
		if (entities == null || entities.size() == 0) {
			LOG.debug("No data to save");
			return entities;
		}
		
		LOG.info("Saving [table=" + domainObject.getTableName() + "][size=" + entities.size() +"]");
		
		dbAdapter.open();
		
		for (EntityClass entity : entities) {
			LOG.info("Saving [table=" + domainObject.getTableName() + "][" + entity +"]");
			
			ContentValues contentValues = entityToContentValues(entity);
			
			// Inserto el registro en la base de datos
			dbAdapter.getSqlLiteDatabase().insert(domainObject.getTableName(), null, contentValues);
			updateLastId();
			
			// Obtengo el ultimo autonumerico generado
			entity.setId(this.getLastId());
		}
		
		dbAdapter.close();
		
		return entities;		
	}
	
	@Override
	public EntityClass update(EntityClass entity) {
		LOG.info("Updating [table=" + domainObject.getTableName() + "][" + entity +"]");
		
		ContentValues contentValues = entityToContentValues(entity);

		if (entity.getId() == null) {
			return entity;
		}
		
		String whereClause = EntityClass._ID + "=" + entity.getId();
		
		dbAdapter.open();
		
		// Actualizo el registro en la base de datos
		dbAdapter.getSqlLiteDatabase().update(domainObject.getTableName(), contentValues, whereClause, null);
		updateLastId();
		
		dbAdapter.close();
		
		return entity;
	}	
	
	@Override
	public EntityClass saveOrUpdate(EntityClass entity) {
		if (entity.getId() == null || entity.getId() == 0) {
			return this.save(entity);
		} else {
			return this.update(entity);
		}
	}	
	
	@Override
	public void remove(EntityClass entity) {
		LOG.info("Deleting [table=" + domainObject.getTableName() + "][" + entity +"]");
		
		String whereClause = EntityClass._ID + "=" + entity.getId();
		
		dbAdapter.open();
		
		dbAdapter.getSqlLiteDatabase().delete(domainObject.getTableName(), whereClause, null);
		
		dbAdapter.close();
	}
	
	/**
	 * Remove a list of entities
	 */
	@Override
	public void remove(List<EntityClass> entities) {
		if (entities == null || entities.size() == 0) {
			LOG.debug("No data to delete");
			return;
		}
		
		LOG.info("Deleting [table=" + domainObject.getTableName() + "][size=" + entities.size() +"]");
		
		dbAdapter.open();
		
		String whereClause = null;
		List<String> idList = new ArrayList<String>();
		for (EntityClass entity : entities) {
			LOG.info("Deleting [table=" + domainObject.getTableName() + "][" + entity +"]");
			
			idList.add(entity.getId().toString());
		}

		whereClause = EntityClass._ID + " in (" + join(idList, ",") + ")";
		
		dbAdapter.getSqlLiteDatabase().delete(domainObject.getTableName(), whereClause, null);
		
		dbAdapter.close();
	}

	/**
	 * Delete all the entities
	 */
	@Override
	public void removeAll() {
		LOG.info("Deleting all entities [table=" + domainObject.getTableName() + "]");
		
		dbAdapter.open();
		
		dbAdapter.getSqlLiteDatabase().delete(domainObject.getTableName(), null, null);
		
		dbAdapter.close();
	}
	
	@Override
	public void removeByField(String fieldName, Object fieldValue) {
		LOG.info("Deleting [table=" + domainObject.getTableName() + "][" + fieldName + "=" + fieldValue +"]");
		
		String whereClause = null;
		String[] whereArgs = null;
		
		if (fieldName != null) {
			whereClause = fieldName + "=?";
			
			if (fieldValue != null) {
				whereArgs = new String[]{fieldValue.toString()};
			}
		}		
		
		dbAdapter.open();
		
		dbAdapter.getSqlLiteDatabase().delete(domainObject.getTableName(), whereClause, whereArgs);
		
		dbAdapter.close();
		
	}
	
	@Override
	public int getLastId() {
		return lastId;
	}
	
	protected int updateLastId() {
		Cursor cursor = dbAdapter.getSqlLiteDatabase().rawQuery("SELECT last_insert_rowid();", null);
		if(cursor.moveToFirst()) {
			lastId = cursor.getInt(0);
		}
		cursor.close();
		
		return lastId;
	}	
	
	/**
	 * Join a collection of objects into a String using the specified delimiter
	 * @param s
	 * @param delimiter
	 * @return
	 */
	protected static String join(List<String> s, String delimiter) {
		int capacity = 0;
		int delimLength = delimiter.length();
		Iterator<? extends CharSequence> iter = s.iterator();
		if (iter.hasNext()) {
			capacity += iter.next().length() + delimLength;
		}

		StringBuilder buffer = new StringBuilder(capacity);
		iter = s.iterator();
		if (iter.hasNext()) {
			buffer.append(iter.next());
			while (iter.hasNext()) {
				buffer.append(delimiter);
				buffer.append(iter.next());
			}
		}
		return buffer.toString();
	}
}
