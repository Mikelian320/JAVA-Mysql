package cn.greatwebtech.logger;

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
    		efHandler = new FileHandler("./Errlog%g.txt",100000,5,true);//�����ʼ������Σ�·������־��С���ޣ�Byte������־����������׷��д��
    		efHandler.setFormatter(exceptionFormatter);
    		errorLog.addHandler(efHandler);
    		errorLog.warning(ex.toString());
    	}catch(Exception e){
    		
    	}finally {
    		if(efHandler!=null) {
    			efHandler.close();
    		}
    	}
   
		//System.out.println("�쳣֪ͨ"+ex.getMessage());
	}
}
