package org.oss.console.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


/**
 * Converts an array of strings into a typed bean instance of a given interface.<br>
 * To assign the values to the getter methods of the interface the interface has to declare a <br>
 * comma separated string with the field names. This string is named FIELD_ORDER.<br>
 * For each field declared in FIELD_ORDER the interface must declare a getter method.<br>
 *  
 * @author donatmuller, 2015, last change 16:18:42
 * 
 */
public class TypedResultBean implements InvocationHandler  {
	private static final String GETTER_PREFIX = "get";
	private static final String FIELD_ORDER = "FIELD_ORDER";
	private final Map<String,String> methodToValue;
	
	@SuppressWarnings({"unchecked" })
	public static <T> T newInstance(Class<T> clazz, String[] values, boolean appendOverlapingValuesTolastField) {
		T result = (T) Proxy.newProxyInstance(TypedResultBean.class.getClassLoader(), new Class<?> [] {clazz}, new TypedResultBean(clazz,values,appendOverlapingValuesTolastField));
		return result;
	}
	
	public TypedResultBean(Class<?> clazz, String[] values, boolean appendOverlapingValuesTolastField) {
		try {
			this.methodToValue = new HashMap<String,String>();
			int index = 0;
			Field f = clazz.getField(FIELD_ORDER);
			String methodNames = (String) f.get(null);
			String lastName = null;
			for (String name : methodNames.split(",")) {
				if (values.length<=index) {
					break;
				}
				lastName = GETTER_PREFIX + name;
				methodToValue.put(GETTER_PREFIX + name, values[index++]);
			}
			if (appendOverlapingValuesTolastField && values.length>index && lastName!=null) {
				String lastValue = methodToValue.get(lastName);
				while (values.length>index) {
					lastValue += " " + values[index++];
				}
				methodToValue.put(lastName, lastValue);
			}
		} catch (Exception e) {
			throw new RuntimeException("Interface " + clazz.getName() + " is missing Field: FIELD_ORDER",e);
		} 
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return methodToValue.get(method.getName());
	}

}
