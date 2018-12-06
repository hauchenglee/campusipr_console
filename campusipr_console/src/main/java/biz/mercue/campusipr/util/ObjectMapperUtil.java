package biz.mercue.campusipr.util;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;




public class ObjectMapperUtil {
	
	private static Logger log = Logger.getLogger(ObjectMapperUtil.class.getName());
	
	public static String mapObjectWithView(Object obj, Class<?> view){
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
			jsonInString = mapper.writerWithView(view).writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.info("JsonProcessingException :"+e.getMessage());
		}
		
		return jsonInString;
	}
				
	public static Object readValue(String strJson, Class<?> mClass){
			ObjectMapper mapper = new ObjectMapper();
			Object object = null;
			try {
				object = mapper.readValue(strJson, mClass);
			} catch (IOException e) {
				log.info("IOException :"+e.getMessage());
			}
			
			return object;
	}
	

}
