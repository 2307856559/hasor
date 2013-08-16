package org.more.util;
import java.io.IOException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * ����Դ�иĶ�ʱ��������Run������
 * @version : 2012-8-2
 * @author ������ (zyc@hasor.net)
 */
public abstract class ResourceWatch extends Thread {
    private static Logger log           = LoggerFactory.getLogger(ResourceWatch.class);
    private URI           resourceURI   = null;                                        //��Դ��ַ
    //
    private long          lastHashCode  = 0;                                           //��һ�μ����Դ��У���룬���μ��У���벻һ�½�ִ��reload
    private long          checkSeepTime = 0;                                           //����ʱ����-15��
    //
    public ResourceWatch() {
        this(null, 15 * 1000);
    }
    public ResourceWatch(URI resourceURI) {
        this(resourceURI, 15 * 1000);
    }
    public ResourceWatch(URI resourceURI, long checkSeepTime) {
        this.resourceURI = resourceURI;
        this.checkSeepTime = checkSeepTime;
    }
    /**�״�����������*/
    public abstract void firstStart(URI resourceURI) throws IOException;
    /**��������Դ�ı�֮����á�*/
    public abstract void onChange(URI resourceURI) throws IOException;
    /**�����Դ�Ƿ��޸ģ����ҷ����޸ĵ�ʱ����������μ�鲻һ��ʱ�����{@link #reload(URI)}������*/
    public abstract long lastModify(URI resourceURI) throws IOException;
    /**�״���������ִ��loadȻ���������߳�*/
    @Override
    public synchronized void start() {
        try {
            this.firstStart(this.resourceURI);
            this.lastHashCode = this.lastModify(this.resourceURI);
        } catch (Exception e) {
            log.warn(this.resourceURI + " lastModify error.");
        }
        super.start();
    }
    @Override
    public final void run() {
        if (this.resourceURI == null)
            return;
        String schema = this.resourceURI.getScheme();
        schema = (schema == null) ? "" : schema;
        schema = schema.toLowerCase();
        while (true) {
            try {
                long lastHashCode = this.lastModify(this.resourceURI);
                if (this.lastHashCode != lastHashCode) {
                    try {
                        this.onChange(this.resourceURI);
                        this.lastHashCode = lastHashCode;
                    } catch (Exception e) {
                        log.error("reload config error :%s", e);
                    }
                }
                sleep(this.checkSeepTime);
            } catch (Exception e) {}
        }
    }
    public URI getResourceURI() {
        return resourceURI;
    }
    public void setResourceURI(URI resourceURI) {
        this.resourceURI = resourceURI;
    }
    public long getCheckSeepTime() {
        return checkSeepTime;
    }
    public void setCheckSeepTime(long checkSeepTime) {
        this.checkSeepTime = checkSeepTime;
    }
}