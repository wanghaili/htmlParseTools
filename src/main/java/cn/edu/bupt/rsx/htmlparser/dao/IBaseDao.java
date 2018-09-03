package cn.edu.bupt.rsx.htmlparser.dao;

import java.util.List;

public interface IBaseDao<T> {

	void insert(T record);
	
	int deleteById(long id);

	int updateById(T record);

	void saveOrUpdate(T record);
	
	T selectById(long id);
	
	List<T> selectByIdList(List<Long> idList);

}
