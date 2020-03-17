package com.ningmeng.manage_mdia;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ChunkTest  {

    @Test
    public void testChunk() throws IOException{
        File sourceFile = new File("D:/video/Minions.mp4");
        String chunkPath = "D:/video/ffmpeg/chunk/";
        File chunkFolder = new File(chunkPath);
        if(!chunkFolder.exists()){
            chunkFolder.mkdirs();
        }
        //分块大小
        long chunkSize = 1024*1024*1;
        //分快数量
        long chunkNum = (long)Math.ceil((sourceFile.length() * 1.0 / chunkSize));
        if(chunkNum<=0){
            chunkNum = 1;
        }
        //缓冲区大小
        byte[] b = new byte[1024];
        //使用RandomAccessFile 访问文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //分块
        for (int i=0;i<chunkNum;i++){
            //创建分快文件
            File file = new File(chunkPath + i);
            boolean newFile = file.createNewFile();
            if (newFile){
                //向分快文件中写数据
                RandomAccessFile raf_write = new RandomAccessFile(file,"rw");
                int len = -1;
                while ((len = raf_read.read(b)) != -1){
                    raf_write.write(b,0,len);
                    if(file.length() > chunkSize){
                        break;
                    }
                }
                raf_write.close();
            }
        }
        raf_read.close();
    }




    @Test
    public void testMerge() throws IOException{
        //块文件目录
        File chunkFolder = new File("D:/video/ffmpeg/chunk/");
        //合并文件
        File mergeFile = new File("D:/video/ffmpeg/Minion.mp4");
        if (mergeFile.exists()){
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //分块列表
        File[] fileArray = chunkFolder.listFiles();
        //转成集合，便于排序
        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(fileArray));
        //从小到大排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });
        //合并文件
        for (File chunFile:fileArray){
            RandomAccessFile raf_read = new RandomAccessFile(chunFile,"r");
            int len = -1;
            while ((len = raf_read.read(b)) != -1){
                raf_write.write(b,0, len);
            }
            raf_read.close();
        }
        raf_write.close();

    }



}
