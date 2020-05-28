package cn.greatwebtech.createfile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//Log的文件格式：RG-IS2712G_总检_以管理板为主_G1N40PP00268C_80058845DBC6_PASS_2019-05-20 10_59_15.0.txt
public class WriteLogInLocal {
    private static final int  BUFFER_SIZE = 2 * 1024;

    public void writeDataInLocal(String directoryPath,String fileName,String content,boolean utf) throws IOException
    {
        DataOutputStream out = new DataOutputStream(
            new BufferedOutputStream(
                new FileOutputStream(directoryPath+"/"+ fileName)));
                //content=content.replaceAll("(?!\\r)\\n", "\r\n");
                content=content.replaceAll("(?!\\r)\\n|\\r(?!\\n)", "\r\n");
                if (utf) {
                    out.writeUTF(content);
                }else{
                    out.write(content.getBytes("GBK"));
                }
                out.close();
    }
    public void compressToZip(String srcDir,OutputStream out,boolean KeepDirStructure)throws RuntimeException
    {
        ZipOutputStream zos=null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile =new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error",e);
            //TODO: handle exception
        }
    }
    //系统提供的方法只能删除文件和空的文件夹，因此需要采用递归调用的方式删除文件夹及其内部的内容
    public boolean deleteDirAndFile(String path) throws Exception{
        File file =new File(path);
        if (!file.exists()) {
            throw new Exception(path+"does not exists!");
        }
        if (file.isFile()) {
            return file.delete();
        }else{
            File[] files=file.listFiles();
            for (File tempfile: files) {
                if (tempfile.isFile()) {
                    if (!tempfile.delete()) {
                        return false;
                    }
                }else{
                    if (!this.deleteDirAndFile(tempfile.getAbsolutePath())) {
                        return false;
                    }
                }
            }
            return file.delete();
        }
    }

    private void compress(File sourceFile,ZipOutputStream zos,String name,boolean KeepDirStructure)throws Exception
    {
        byte[] buf =new byte[BUFFER_SIZE];
        if(sourceFile.isFile())
        {   
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in =new FileInputStream(sourceFile);
            while ((len=in.read(buf))!=-1){
                zos.write(buf,0,len);
            }
            zos.closeEntry();
            in.close();
        }else{
            File[] listFiles=sourceFile.listFiles();
            if (listFiles==null||listFiles.length==0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name+"/"));
                    zos.closeEntry();
                }
            }else{
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    }else{
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }
}