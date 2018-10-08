package com.sgcc.bg.common;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * mongodb工具类
 */
public class MongoDBUtil {

	private final static ThreadLocal<Mongo> mongos = new ThreadLocal<Mongo>();

	private static DB getdb() {
		return getMongos().getDB(ConfigUtils.getConfig(""));
	}

	private static Mongo getMongos() {
		Mongo mongo = mongos.get();
		if (mongo == null) {
			try {
				mongo = new Mongo(ConfigUtils.getConfig(""),Integer.parseInt(ConfigUtils.getConfig("")));
				mongos.set(mongo);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
		return mongo;
	}

	/**
	 * 获取集合（表）
	 * @param collection 集合名（表名）
	 */
	private static DBCollection getCollection(String collection) {
		return getdb().getCollection(collection);
	}

	/**
	 * 插入 一条数据
	 * @param collection 集合名
	 * @param obj 数据对象
	 */
	public static void insert(String collection, DBObject obj) {
		getCollection(collection).insert(obj);
	}

	/**
	 * 批量插入
	 * @param collection 集合名
	 * @param list  要插入的对象集合
	 */
	public void insertBatch(String collection, List<DBObject> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		getCollection(collection).insert(list);
	}

	/**
	 * 删除
	 * @param collection 集合名
	 * @param obj 删除条件对象
	 */
	public static void delete(String collection, DBObject obj) {
		getCollection(collection).remove(obj);
	}

	/**
	 * 批量删除
	 * 
	 * @param collection 集合名
	 * @param list  删除条件对象集合
	 */
	public  static void deleteBatch(String collection, List<DBObject> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (DBObject obj:list) {
			getCollection(collection).remove(obj);
		}
	}
	
	

	/**
	 * 更新数据
	 * @param collection 集合名
	 * @param queryObj 更新条件
	 * @param setFields  更新对象
	 */
	public  static void update(String collection, DBObject queryObj, DBObject setFields) {
		getCollection(collection).updateMulti(queryObj,new BasicDBObject("$set", setFields));
	}

	/**
	 * 查找集合所有对象 
	 * 数量大的情况下谨慎使用
	 * @param collection
	 */
	public static List<DBObject> findAll(String collection) {
		return getCollection(collection).find().toArray();
	}

	/**
	 * 查找（返回一个对象）
	 * 
	 * @param collection 集合名
	 * @param queryObj 查询条件
	 */
	public static DBObject findOne(String collection, DBObject queryObj) {
		return getCollection(collection).findOne(queryObj);
	}

	/**
	 * 查找（返回一个对象）
	 * 
	 * @param collection 集合名
	 * @param queryObj 查询条件
	 * @param fileds 返回字段
	 */
	public static DBObject findOne(String collection, DBObject queryObj, DBObject fileds) {
		return getCollection(collection).findOne(queryObj, fileds);
	}

	/**
	 * 分页查找集合对象，返回特定字段
	 * @param collection
	 * @param queryObj 查询条件
	 * @param fileds 返回字段
	 * @param pageNo 第n页
	 * @param perPageCount 每页记录数
	 */
	public static List<DBObject> findLess(String collection, DBObject queryObj, DBObject fileds, int pageNo, int perPageCount) {
		return getCollection(collection).find(queryObj, fileds).skip((pageNo - 1) * perPageCount).limit(perPageCount).toArray();
	}

}
