package cn.greatwebtech.logger;

import java.lang.reflect.Method;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import org.springframework.aop.MethodBeforeAdvice;

public class LogQueryStr implements MethodBeforeAdvice{
	private Formatter logFormatter;
	
	public void setLogFormatter(Formatter logFormatter) {
		this.logFormatter = logFormatter;
	}
	@Override
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		// TODO Auto-generated method stub
    	Logger searchRecord=Logger.getLogger("SearchRecord");
    	FileHandler sfHandler=null;
    	try {
    		sfHandler = new FileHandler(System.getProperty("user.dir")+"/SearchRecord%g.txt",100000,5,true);//�����ʼ������Σ�·������־��С���ޣ�Byte������־����������׷��д��
    		sfHandler.setFormatter(logFormatter);
    		searchRecord.addHandler(sfHandler);
    		searchRecord.info(arg1[0].toString());
    	}catch(Exception ex){
    		throw ex;//�ϲ��Ƿ��д����׳��쳣�Ļ��ƣ�
    	}finally {
    		if(sfHandler!=null) {
    			sfHandler.close();
    		}
    	}
	}

}
