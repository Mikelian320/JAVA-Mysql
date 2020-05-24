package cn.greatwebtech.createfile;
import java.io.*;
//Log的文件格式：RG-IS2712G_总检_以管理板为主_G1N40PP00268C_80058845DBC6_PASS_2019-05-20 10_59_15.0.txt
public class WriteLogInLocal {
    private String directoryPath;
    public WriteLogInLocal(String directoryPath)
    {
        this.directoryPath=directoryPath;
    }
    public void writeDataInLocal(String fileName,String content) throws IOException
    {
        DataOutputStream out = new DataOutputStream(
            new BufferedOutputStream(
                new FileOutputStream(directoryPath+"/"+ fileName)));
        out.writeUTF(content);
        out.close();
    }
}