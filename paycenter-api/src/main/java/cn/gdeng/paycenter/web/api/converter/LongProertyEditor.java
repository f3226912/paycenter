package cn.gdeng.paycenter.web.api.converter;

import java.beans.PropertyEditorSupport;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;


public class LongProertyEditor extends PropertyEditorSupport implements
		WebBindingInitializer {


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if(null!=text&&!"".equals(text)&&!"null".equals(text)){
			
			setValue(new Long(text));
		}
		
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		super.setValue(value);
	}

	public void initBinder(WebDataBinder binder, WebRequest request) {
		// TODO Auto-generated method stub
		binder.registerCustomEditor(Long.class, this);
		//binder.registerCustomEditor(Double.class, this);
	}


}
