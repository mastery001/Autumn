package org.web.service;

import java.util.List;

import org.web.access.factory.DaoAdviceFactory;
import org.web.dao.core.DaoAdvice;
import org.web.dao.core.support.Page;
import org.web.exception.BeanInitializationException;
import org.web.exception.DBException;
import org.web.exception.ErrorException;
import org.web.service.support.Caretaker;
import org.web.service.support.Originator;
import org.web.util.QueryValueUtils;

import tool.mastery.core.ClassUtil;
import tool.mastery.log.Logger;

/**
 * 对数据查询的统一处理类
 * 
 * @author mastery
 * @Time 2015-3-8 下午11:47:33
 * 
 */
public class QueryService extends AbstractService {

	private static final Logger LOG = Logger.getLogger(QueryService.class);
	
	private final Originator o = new Originator();

	private static final Caretaker caretaker = new Caretaker();

	private static List<Object> allList;

	// 是否缓存数据
	private boolean isCache;

	// 是否支持模糊查询,默认是不支持的
	protected boolean flag = false;

	public QueryService(String name) {
		super(name);
		if (caretaker.getDm(name) != null) {
			allList = caretaker.getDm(name).getList();
		}
	}

	public QueryService(String name, boolean flag) {
		this(name);
		this.flag = flag;
	}

	public QueryService(String name, boolean flag, boolean isCache) {
		this(name, flag);
		this.isCache = isCache;
	}

	/**
	 * @param vo
	 *            封装成对象的查询的条件，
	 * @param page
	 * @return
	 * @throws ErrorException
	 * @throws BeanInitializationException
	 */
	public List<Object> getResult(Object vo, Page page) throws ErrorException,
			BeanInitializationException {
		// 判断是否是由单表构成的视图
		Class<?> voClass = ClassUtil.getClassByClassName(name);
		// 对page对象进行处理
		page = processPage(page);
		List<Object> list = getList(vo, page, voClass);
		// 得到需要的几条数据
		return QueryValueUtils.getListByPage(list, page);
	}

	/**
	 * 对页面进行判别，若是查询第一页则查询数据库，若不是则直接使用以前查询出的所有数据
	 * 
	 * @param vo
	 * @param page
	 * @param voClass
	 * @return
	 * @throws ErrorException
	 * @throws BeanInitializationException
	 */
	protected List<Object> getList(Object vo, Page page, Class<?> voClass)
			throws ErrorException, BeanInitializationException {
		// 如果是第一页则进行查询
		if (page.getPage() == 1 || allList == null) {
			try {
				DaoAdvice sd = DaoAdviceFactory.getDao(name);
				allList = processList(sd.query(voClass, vo, page, flag));
			} catch (DBException e) {
				LOG.debug(e.getMessage() , e);
				throw new ErrorException(e.getMessage());
			}
			if (vo == null && isCache) {
				// 原发器初始化list
				o.set(allList);
				caretaker.setDm(name, o.createMemento());
			}
		}
		page.setCount(allList.size());
		return allList;
	}

	/**
	 * 对page对象进行处理，如果对象为空，则设置初始情况，使得查询所有
	 * 
	 * @param page
	 */
	private Page processPage(Page page) {
		if (page == null) {
			page = new Page();
			page.setPage(1);
		}
		return page;
	}

	public static List<Object> getCurrentMementoList(String name) {
		if (caretaker.getDm(name) != null) {
			return caretaker.getDm(name).getList();
		}
		return null;
	}
}
