package cn.greatewebtech.logger;

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
    		sfHandler = new FileHandler("./SearchRecord%g.txt",100000,5,true);//句柄初始化，入参：路径、日志大小上限（Byte）、日志保存数量、追加写入
    		sfHandler.setFormatter(logFormatter);
    		searchRecord.addHandler(sfHandler);
    		searchRecord.info(arg1[0].toString());
    	}catch(Exception ex){
    		throw ex;//上层是否有处理抛出异常的机制？
    	}finally {
    		if(sfHandler!=null) {
    			sfHandler.close();
    		}
    	}
	}

}
