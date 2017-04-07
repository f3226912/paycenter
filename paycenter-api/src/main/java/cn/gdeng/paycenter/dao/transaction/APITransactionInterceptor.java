package cn.gdeng.paycenter.dao.transaction;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.gudeng.framework.dba.client.DbaClient;
import com.gudeng.framework.dba.transaction.advisor.TransactionInterceptor;
import com.gudeng.framework.dba.transaction.annotation.RouteParam;
import com.gudeng.framework.dba.transaction.annotation.Transactional;
import com.gudeng.framework.dba.transaction.template.CallBackTemplate;
import com.gudeng.framework.dba.transaction.template.TransactionTemplate;

/**api事务拦截器。
 * @author wjguo
 * datetime 2016年8月10日 上午9:20:22  
 *
 */
public class APITransactionInterceptor implements MethodInterceptor {
	private static Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);
	private DbaClient dbaClient;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		if (invocation.getMethod().isAnnotationPresent(Transactional.class)) {
			Transactional transactional = (Transactional) invocation.getMethod().getAnnotation(Transactional.class);

			TransactionTemplate transactionTemplate = this.dbaClient.getTransactionTemplate(getParameter(invocation));

			try {
				 return transactionTemplate.execute(new CallBackTemplate() {
					public Object invoke() {
						try {
							return invocation.proceed();
						} catch (Throwable e) {
							if(e instanceof RuntimeException) {
								throw (RuntimeException)e;
							}
							//因接口不允许抛出对受检异常，需要对其进行包装。
							throw new ThrowableHolderException(e);
						}
					}
				}, transactional.propagation());
			} catch (ThrowableHolderException e) {
				//抛出原生异常。
				throw e.getThrowable();
			}
			
		} else {
			return invocation.proceed();
		}

	}

	private Object[] getParameter(MethodInvocation invocation) {
		Transactional transactional = (Transactional) invocation.getMethod().getAnnotation(Transactional.class);

		RouteParam routeParam = (RouteParam) invocation.getMethod().getAnnotation(RouteParam.class);

		Object[] indexParams = indexParam(transactional, invocation.getArguments());

		if (routeParam == null) {
			return indexParams;
		}

		Object obj = isInstance(indexParams, routeParam);

		if (obj == null) {
			return indexParams;
		}
		try {
			obj = getValue(obj, routeParam.field());
			logger.debug("interceptor route param is : " + obj);
		} catch (Exception e) {
			return indexParams;
		}
		return new Object[] { obj };
	}

	private Object isInstance(Object[] indexParams, RouteParam routeParam) {
		Object obj = null;
		for (Object param : indexParams) {
			if (routeParam.isArray()) {
				param = ((Object[]) param)[routeParam.index()];
			}

			if (routeParam.clazz().isInstance(param)) {
				obj = param;
				break;
			}
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getValue(Object obj, String field) throws Exception {
		Map routeMap = new HashMap();
		if (obj instanceof Map) {
			Map parentParamMap = (Map) obj;
			if (!(parentParamMap.containsKey(field))) {
				throw new Exception("param " + obj + " not contains key " + field);
			}

			routeMap.put(field, parentParamMap.get(field));
		} else if (obj instanceof String) {
			routeMap.put(field, obj);
		} else if (obj instanceof Number) {
			routeMap.put(field, obj);
		} else {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), field);
			Object value = descriptor.getReadMethod().invoke(obj, new Object[0]);
			routeMap.put(field, value);
		}
		return routeMap;
	}

	private Object[] indexParam(Transactional transactional, Object[] arguments) {
		Object[] parameter = null;
		int paramIndex = transactional.paramIndex();
		if (paramIndex < 0)
			parameter = arguments;
		else if (paramIndex < arguments.length) {
			parameter = new Object[] { arguments[paramIndex] };
		}
		return parameter;
	}

	public void setDbaClient(DbaClient dbaClient) {
		this.dbaClient = dbaClient;
	}
	
	
	/** 异常持有器
	 * @author wjguo
	 * datetime 2016年9月20日 上午10:41:21  
	 *
	 */
	private static class ThrowableHolderException  extends RuntimeException{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6496633763764865505L;
		private final Throwable throwable;

		public ThrowableHolderException(Throwable throwable) {
			this.throwable = throwable;
		}

		public final Throwable getThrowable() {
			return this.throwable;
		}

	}
	
}
