package cn.edu.bupt.rsx.htmlparser.dao;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import cn.edu.bupt.rsx.htmlparser.model.IIdAware;
import cn.edu.bupt.rsx.htmlparser.model.Page;

public abstract class AbstractBaseDao <T extends IIdAware> extends SqlSessionDaoSupport implements IBaseDao<T>{
	protected static final int DEFAULT_BATCH_SIZE = 1000;
	
	protected <K> Page<K> selectPage(Page<K> page, String statementName, String countStatementName, Object parameter) {
		Number totalItems = (Number) getSqlSession().selectOne(countStatementName, parameter);
		if (totalItems != null && totalItems.longValue() > 0) {
			List<K> list = getSqlSession().selectList(statementName, toParameterMap(parameter, page));
			page.setResult(list);
			page.setTotalItems(totalItems.longValue());
		}
		return page;
	}
	public void insert(T t){
		getSqlSession().insert(getNameSpace() + ".insert", t);
	}
	
	public T selectById(long id){
		return getSqlSession().selectOne(getNameSpace() + ".selectById", id);
	}
	
	public int updateById(T t){
		return getSqlSession().update(getNameSpace() + ".updateById", t);
	}
	
	public int deleteById(long id){
		return getSqlSession().delete(getNameSpace() + ".deleteById", id); 
	}
	
	public void saveOrUpdate(T t){
		if(t.getId() > 0){
			updateById(t);
		}else{
			try {
				insert(t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
	}
	
	
	protected void batchInsert(List<T> list){
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		Collection<List<T>> subIdListCollections = toSubListCollections(list, DEFAULT_BATCH_SIZE);
		for(List<T> subList : subIdListCollections){
			getSqlSession().selectList(getNameSpace() + ".batchInsert", subList);
		}
		
	}
	
	protected List<T> batchSelectByIdList(List<Long> idList, boolean sortById){
		List<T> result = new ArrayList<T>();
		if(CollectionUtils.isNotEmpty(idList)){
			List<T> noSortList = new ArrayList<T>();
			Collection<List<Long>> subIdListCollections = toSubListCollections(idList, DEFAULT_BATCH_SIZE);
			for(List<Long> subIdList : subIdListCollections){
				List<T> subResult = getSqlSession().selectList(getNameSpace() + ".selectByIdList", subIdList);
				if(CollectionUtils.isNotEmpty(subResult)){
					noSortList.addAll(subResult);
				}
			}
			
			if(sortById){
				result = sortByIdList(idList, noSortList);
			}else{
				result = noSortList;
			}
		}
		return result;
	}
	
	protected abstract String getNameSpace();
	
	
	@SuppressWarnings("rawtypes")
	protected static Map<String, Object> toParameterMap(Object parameter, Page p) {
		Map<String, Object> map = toParameterMap(parameter);
		map.put("startRow", p.getStartRow());
		map.put("endRow", p.getEndRow());
		map.put("offset", p.getOffset());
		map.put("limit", p.getPageSize());
		return map;
	}

	@SuppressWarnings("unchecked")
	protected static Map<String, Object> toParameterMap(Object parameter) {
		if (parameter instanceof Map) {
			return (Map<String, Object>) parameter;
		} else {
			try {
				return PropertyUtils.describe(parameter);
			} catch (Exception e) {
				ReflectionUtils.handleReflectionException(e);
				return null;
			}
		}
	}
	
	protected static <T extends IIdAware> List<T> sortByIdList(List<Long> idList,
			List<T> resultList) {
		if(CollectionUtils.isEmpty(idList)){
			return new ArrayList<T>();
		}
		Map<Long, T> objById = resultList.stream().collect(Collectors.toMap(T::getId, obj -> obj));
		return idList.stream().map(objById::get).filter(obj->obj!= null).collect(Collectors.toList());
	}
	
	protected static <V> Collection<List<V>> toSubListCollections(List<V> list , int subSize){
		Collection<List<V>> subListCollections = new LinkedList<List<V>>();
		if(CollectionUtils.isEmpty(list)){
			return subListCollections;
		}
		int size = list.size();
		if(size < subSize){
			subListCollections.add(list);
		}else{
			int batchTimes = (size - 1)/DEFAULT_BATCH_SIZE + 1;
			for(int i = 0 ; i < batchTimes ; i ++){
				int start = i * DEFAULT_BATCH_SIZE;
				int end = (i+1) * DEFAULT_BATCH_SIZE;
				end =  end > size ? size : end;
				subListCollections.add(list.subList(start, end));
			}
		}
		return subListCollections;
	}
	
	@Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}