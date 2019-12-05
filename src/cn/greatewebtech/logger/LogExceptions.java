package cn.greatewebtech.logger;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import org.springframework.aop.ThrowsAdvice;

public class LogExceptions implements ThrowsAdvice {
	private Formatter exceptionFormatter;
	public void setExceptionFormatter(Formatter exceptionFormatter) {
		this.exceptionFormatter = exceptionFormatter;
	}
    public void afterThrowing(Exception ex) 
	{
    	Logger errorLog=Logger.getLogger("ErrorLog");
    	FileHandler efHandler=null;
    	try 
    	{
    		efHandler = new FileHandler("./Errlog%g.txt",100000,5,true);//句柄初始化，入参：路径、日志大小上限（Byte）、日志保存数量、追加写入
    		efHandler.setFormatter(exceptionFormatter);
    		errorLog.addHandler(efHandler);
    		errorLog.warning(ex.toString());
    	}catch(Exception e){
    		
    	}finally {
    		if(efHandler!=null) {
    			efHandler.close();
    		}
    	}
   
		//System.out.println("异常通知"+ex.getMessage());
	}
}
