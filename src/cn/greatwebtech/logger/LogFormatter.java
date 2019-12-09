package cn.greatwebtech.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	
    private static String getTime(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentTime=dateFormat.format(new Date());
        return currentTime;
    }
	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		return getTime() + ":" + record.getMessage()+"\r\n"; 
	}

}
