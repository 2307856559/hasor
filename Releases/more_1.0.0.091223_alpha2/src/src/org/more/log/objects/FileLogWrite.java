/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.more.log.objects;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.more.log.ILogWrite;
/**
 * ��־ϵͳ-���ļ������Ĭ�ϻ���512B
 * Date : 2009-5-13
 * @author ������
 */
public class FileLogWrite implements ILogWrite {
    private String               filename = "mylog"; // �ļ���1073741824L
    private String               filePath = "log";  // �ļ�������
    private String               encoding = "gbk";  // �ļ�����
    private int                  size     = 512;    // �����С����512B
    // ----------------------------------------------------------------------
    private File                 log_file;          // ��־
    private BufferedOutputStream bs;                // ������
    private FileOutputStream     stream;            // Ŀ����־�ļ���
    // ----------------------------------------------------------------------
    //
    public synchronized boolean writeLog(String msg) {
        try {
            String msgs = msg + "\n";
            byte[] bytemsg = msgs.getBytes(encoding);
            this.begin();
            bs = new BufferedOutputStream(stream, size);
            bs.write(bytemsg, 0, bytemsg.length);
            bs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    // ------------------------------------------------------------------------------˽�з���
    private File getFile() {
        File file = new File(filePath);
        if (file.exists() == false)
            try {
                file.mkdir();
            } catch (Exception e) {}
        String[] logfiles = file.list(new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if (arg1.indexOf(filename) != -1)
                    return true;
                return false;
            }
        });
        if (logfiles != null) {
            for (String str : logfiles) {
                String s = str.substring(this.filename.length() + 1);
                String t = s.substring(8, 10);
                String str1 = this.time();
                String t1 = str1.substring(8, 10);
                if (t1.equals(t) == false) {
                    String str2 = this.time();
                    log_file = new File(file.getAbsolutePath(), this.filename + "-" + str2 + ".log");
                    return log_file;
                }
            }
        }
        String str1 = this.time();
        log_file = new File(file, this.filename + "-" + str1 + ".log");
        return log_file;
    }
    private String time() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }
    private void begin() {
        try {
            this.stream = new FileOutputStream(this.getFile(), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------------------------��������
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    public String getFileanme() {
        return filename;
    }
    public void setFileanme(String fileanme) {
        this.filename = fileanme;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        if (filePath == null)
            this.filePath = "";
        else
            this.filePath = filePath;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
}
