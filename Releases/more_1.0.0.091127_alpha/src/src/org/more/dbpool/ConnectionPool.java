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
package org.more.dbpool;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import org.more.dbpool.exception.ConnLevelException;
import org.more.dbpool.exception.CreateConnectionException;
import org.more.dbpool.exception.KeyException;
import org.more.dbpool.exception.NotKeyException;
import org.more.dbpool.exception.OccasionException;
import org.more.dbpool.exception.ParameterBoundException;
import org.more.dbpool.exception.PoolFullException;
import org.more.dbpool.exception.PoolNotRunException;
import org.more.dbpool.exception.PoolNotStopException;
import org.more.dbpool.exception.ResCallBackException;
import org.more.dbpool.exception.StateException;
/**
 * 
 * Date : 2009-5-17
 * @author Administrator
 */
public class ConnectionPool {
    /**
     * <b>��ռ��ʽ</b><br>
     * ʹ�ÿ��е�ʵ�����ӷ���������Դ�������ڸ���Դ�ͷŻ�֮ǰ������Դ�����ӳ��н����ܽ������÷�������������ߡ�<br>
     * ������ӳ�������ʵ��������Դ���Ѿ������ȥ����ô��ʹ���ӳؿ����ڷ���������Դ�ڸ�ģʽ�����ӳٽ��������������Դ��
     * ���ӳػ����һ���쳣����־���ӳ���Դ�ľ���<br><br>
     * <font color='#006600'>��������һ��ʵ�����ӿ��Ա�����5�Σ���ôʹ�ø�ģʽ�������ӵĻ�������ʧ4���ɷ�������ӣ�ֻ���õ�һ��������Դ��
     * ֱ������Դ���ͷŻ����ӳأ����ӳزż���������ʣ���4�λ��ᡣ</font>
     * <br><br>
     * ������ʹ������ʱ����Ӧ�õ�����ʱ������ʹ�ø�ģʽ�����ӣ���ȷ������������ڼ������ԶԸ����Ӿ��ж���Ȩ�ޣ��Ա���������ݿ�������ʵĸ��š�
     */
    public static final int               ConnLevel_ReadOnly     = 10;
    /**
     * <b>���ȼ�-��</b><br>
     * ʹ�ÿ��е�ʵ�����ӷ���������Դ�������ڸ���Դ�ͷŻ�֮ǰ������Դ�����ӳ��н����ܽ������÷�������������ߡ�<br>
     * <font color='#FF0000'>*ע�⣺�˼��𲻱�֤�ڷ������Դ����Ȼ���ֶ���ռ��������Դ���������ռ����Դ��ʹ��ReadOnely��
     * ��Ϊ�����ӳشﵽĳһʱ��ʱ����Դ�����ظ����䣨���ü�����Ȼ�����ʱ���ǲ���Ԥ��ġ�</font><br>
     * �������������ӻ�����������������ʹ��<font color='#0033CC'>ConnLevel_ReadOnly</font>����
     */
    public static final int               ConnLevel_High         = 11;
    /**
     * <b>���ȼ�-��</b><br>
     * �ʵ�Ӧ�����ü�����������������Դ��<br>
     * �ڸ�ģʽ�£����ӳ��ڲ��ᰴ��ʵ�������Ѿ�ʹ�ô�������(��->��)��Ȼ���ڽ����ѡȡ 1/3 λ�õ�������Դ���ء�<br>
     * �����ȼ�-����ͬ��ģʽҲ���߱����ֶ���ռ��������Դ�����ԡ�<br>
     * �������������ӻ�����������������ʹ��<font color='#0033CC'>ConnLevel_ReadOnly</font>����
     */
    public static final int               ConnLevel_None         = 12;
    /**
     * <b>���ȼ�-��</b><br>
     * ������ʹ�����ü��������������ӡ�<br>
     * �ڸ�ģʽ�£����ӳ��ڲ��ᰴ��ʵ�������Ѿ�ʹ�ô�������(��->��)��Ȼ���ڽ����ѡȡ��ʹ�����ķ��ء�<br>
     * ��ģʽ�ʺϴ����Ϊ����Ҫ��������Դ����<br>
     * �����ȼ�-����ͬ��ģʽҲ���߱����ֶ���ռ��������Դ�����ԡ�<br>
     * �������������ӻ�����������������ʹ��<font color='#0033CC'>ConnLevel_ReadOnly</font>����
     */
    public static final int               ConnLevel_Bottom       = 13;
    /**
     * ��ʾδ�����ӳ�δ�����ù�StartSeivice������
     */
    public static final int               PoolState_UnInitialize = 20;
    /**
     * ���ӳس�ʼ���У���״̬�·������ڰ��ղ�����ʼ�����ӳء�StopServices֮��������ת����״̬
     */
    public static final int               PoolState_Initialize   = 21;
    /**
     * �����ӳؿ�ʼ����ʱ���ʾΪ��״̬
     */
    public static final int               PoolState_Run          = 22;
    /**
     * ���ӳر�����StopServicesֹͣ״̬
     */
    public static final int               PoolState_Stop         = 23;
    //����
    private int                           _RealFormPool;                                               //���ӳ��д��ڵ�ʵ��������(����ʧЧ������)
    private int                           _PotentRealFormPool;                                         //���ӳ��д��ڵ�ʵ��������(��Ч��ʵ������)
    private int                           _SpareRealFormPool;                                          //���е�ʵ������
    private int                           _UseRealFormPool;                                            //�ѷ����ʵ������
    private int                           _ReadOnlyFormPool;                                           //���ӳ��Ѿ��������ֻ������
    private int                           _UseFormPool;                                                //�Ѿ������ȥ��������
    private int                           _SpareFormPool;                                              //Ŀǰ�����ṩ��������
    private int                           _MaxConnection;                                              //����������������Դ�����������Ŀ
    private int                           _MinConnection;                                              //��С������
    private int                           _SeepConnection;                                             //ÿ�δ������ӵ�������
    private int                           _KeepRealConnection;                                         //������ʵ�ʿ������ӣ��Թ����ܳ��ֵ�ReadOnlyʹ�ã����������Ӳ������ֵʱ�����ӳؽ�����seepConnection������
    private int                           _Exist                 = 20;                                 //ÿ�������������� 20����
    private String                        _userID                = "";
    private String                        _password              = "";
    //���Ա��ظ�ʹ�ô��������ü������������ӱ��ظ������ֵ����ʾ�Ĵ���ʱ�������ӽ����ܱ������ȥ
    //�����ӳص����ӱ����価ʱ�����ӳػ����Ѿ������ȥ�������У��ظ��������ӣ����ü����������������ӳ�ѹ��
    private int                           _MaxRepeatDegree       = 5;
    private Date                          _StartTime;                                                  //��������ʱ��
    private String                        _ConnString            = null;                               //�����ַ���
    private String                        _DriveString           = null;                               //�����ַ���
    private int                           _PoolState;                                                  //���ӳ�״̬
    //�ڲ�����
    private ArrayList<ConnStruct>         al_All                 = new ArrayList<ConnStruct>(0);       //ʵ������
    private Hashtable<Object, ConnStruct> hs_UseConn             = new Hashtable<Object, ConnStruct>(); //����ʹ�õ�����
    private CreateThreadProcess           threadCreate;
    private CheckThreadProcess            threadCheck;
    //-------------------------------------------------------------------------------------------
    /**
     * ��ʼ�����ӳ�
     */
    public ConnectionPool() {
        InitConnectionPool("", "", 200, 30, 10, 5);
    }
    /**
     * ��ʼ�����ӳ�
     * @param connectionString String ���ݿ������ַ�����
     * @param driveString String ���ݿ������ַ�����
     */
    public ConnectionPool(String connectionString, String driveString) {
        InitConnectionPool(connectionString, driveString, 200, 30, 10, 5);
    }
    /**
     * ��ʼ�����ӳ�
     * @param connectionString String ���ݿ������ַ�����
     * @param driveString String ���ݿ������ַ�����
     * @param maxConnection int ���ʵ���������������Դ�����������Ŀ��
     * @param minConnection int ��Сʵ����������
     */
    public ConnectionPool(String connectionString, String driveString, int maxConnection, int minConnection) {
        InitConnectionPool(connectionString, driveString, maxConnection, minConnection, 10, 5);
    }
    /**
     * ��ʼ�����ӳ�
     * @param connectionString String ���ݿ������ַ�����
     * @param driveString String ���ݿ������ַ�����
     * @param maxConnection int ���ʵ���������������Դ�����������Ŀ��
     * @param minConnection int ��Сʵ����������
     * @param seepConnection int ÿ�δ������ӵ��������������е�ʵ�����Ӳ����ֵʱ�������ӣ�ֱ���ﵽ���������
     * @param keepRealConnection int ���������������������Ӳ������ֵʱ�����ӳؽ�����seepConnection�����ӡ�
     */
    public ConnectionPool(String connectionString, String driveString, int maxConnection, int minConnection, int seepConnection, int keepRealConnection) {
        InitConnectionPool(connectionString, driveString, maxConnection, minConnection, seepConnection, keepRealConnection);
    }
    /**
     * ��ʼ�����ӳ�
     * @param connectionString String ���ݿ������ַ�����
     * @param driveString String ���ݿ������ַ�����
     * @param maxConnection int ���ʵ���������������Դ�����������Ŀ��
     * @param minConnection int ��Сʵ����������
     * @param seepConnection int ÿ�δ������ӵ��������������е�ʵ�����Ӳ����ֵʱ�������ӣ�ֱ���ﵽ���������
     * @param keepRealConnection int ���������������������Ӳ������ֵʱ�����ӳؽ�����seepConnection�����ӡ�
     */
    private void InitConnectionPool(String connectionString, String driveString, int maxConnection, int minConnection, int seepConnection, int keepRealConnection) {
        this._PoolState = PoolState_UnInitialize;
        this._ConnString = connectionString;
        this._DriveString = driveString;
        this._MinConnection = minConnection;
        this._SeepConnection = seepConnection;
        this._KeepRealConnection = keepRealConnection;
        this._MaxConnection = maxConnection;
        this.threadCheck = new CheckThreadProcess();
        this.threadCheck.setDaemon(true);
        this.threadCreate = new CreateThreadProcess();
        this.threadCreate.setDaemon(true);
        this.threadCheck.start();
        while (threadCheck.getState() != Thread.State.WAITING) {}
    }
    //-------------------------------------------------------------------------------------------
    /**
     * <p>�����߳���</p>
     */
    private class CreateThreadProcess extends Thread {
        public int     createThreadMode        = 0;    //�����̹߳���ģʽ
        public int     createThreadProcessTemp = 0;    //��Ҫ������������
        public boolean createThreadProcessRun  = false; //�Ƿ���������߳̽���������������������������̻߳Ὣ�Լ�������ֹ״̬
        public void run() {
            boolean join = false;
            int createThreadProcessTemp_inside = createThreadProcessTemp;
            _PoolState = PoolState_Initialize;
            while (true) {
                join = false;
                _PoolState = PoolState_Run;
                if (createThreadProcessRun == false) {//������ֹ����
                    try {
                        this.join();//�ж��Լ�
                    } catch (Exception e) {/* */}
                } else {
                    if (createThreadMode == 0) {
                        //------------------------begin mode  ����ģʽ
                        synchronized (al_All) {
                            if (al_All.size() < createThreadProcessTemp_inside) {
                                ConnStruct cs = CreateConnectionTemp(_ConnString, _DriveString, _userID, _password);
                                if (cs != null)
                                    al_All.add(cs);
                            } else
                                join = true;
                        }
                        //------------------------end mode
                    } else if (createThreadMode == 1) {
                        //------------------------begin mode  ����ģʽ
                        synchronized (al_All) {
                            if (createThreadProcessTemp_inside != 0) {
                                createThreadProcessTemp_inside--;
                                ConnStruct cs = CreateConnectionTemp(_ConnString, _DriveString, _userID, _password);
                                if (cs != null)
                                    al_All.add(cs);
                            } else
                                join = true;
                        }
                        //------------------------end mode
                    } else
                        join = true;
                    //-------------------------------------------------------------------------
                    if (join == true) {
                        UpdateAttribute();//��������
                        try {
                            createThreadProcessTemp = 0;
                            this.join(); //�ж��Լ�
                        } catch (Exception e) {
                            createThreadProcessTemp_inside = createThreadProcessTemp;
                        } //�õ�����ı���
                    }
                }
            }
        }
    }
    /**
     * <p>����¼�</p>
     */
    private class CheckThreadProcess extends Thread {
        private long    _Interval = 100;  //ִ�м��
        private boolean timeSize  = false;
        /**
         * ������ʱ��
         */
        public void StartTimer() {
            timeSize = true;
            if (this.getState() == Thread.State.NEW)
                this.start();
            else if (this.getState() == Thread.State.WAITING)
                this.interrupt();
        }
        /**
         * ֹͣ��ʱ��
         */
        public void StopTimer() {
            timeSize = false;
            while (this.getState() != Thread.State.WAITING) {/**/}
        }
        public void aRun() {
            ConnStruct cs = null;
            //�������ִ�д����������˳�
            if (threadCreate.getState() != Thread.State.WAITING)
                return;
            //------------------------------------------------------
            synchronized (al_All) {
                for (int i = 0; i < al_All.size(); i++) {
                    cs = al_All.get(i);
                    TestConnStruct(cs);//����
                    if (cs.GetEnable() == false && cs.GetRepeatNow() == 0)//û�����õ�ʧЧ����
                    {
                        cs.Close();//�ر���
                        al_All.remove(cs);//ɾ��
                    }
                }
            }
            //------------------------------------------------------
            UpdateAttribute();//��������
            if (_SpareRealFormPool < _KeepRealConnection)//��������ʵ������������
                threadCreate.createThreadProcessTemp = GetNumOf(_RealFormPool, _SeepConnection, _MaxConnection);
            else
                threadCreate.createThreadProcessTemp = 0;
            //if (threadCreate.createThreadProcessTemp != 0)
            //{
            //    System.out.println("����" + threadCreate.createThreadProcessTemp);
            //    System.out.println(threadCreate.getState()+ " this " + this.getState());
            //}
            if (threadCreate.createThreadProcessTemp != 0) {
                //���������̣߳�����ģʽ1
                threadCreate.createThreadMode = 1;
                threadCreate.interrupt();
            }
        }
        public void run() {
            while (true) {
                try {
                    this.join(_Interval);
                    if (timeSize == true)
                        aRun();
                    else
                        this.join();
                } catch (InterruptedException ex1) {/**/}
            }
        }
        /**
         * ����ִ��ʱ����
         * @param value double ʱ����
         */
        public void setInterval(long value) {
            _Interval = value;
        }
        /**
         * ���ִ��ʱ����
         * @return double ʱ����
         */
        public long getInterval() {
            return _Interval;
        }
        /**
         * �õ���ǰҪ���ӵ���
         * @param nowNum int ��ǰֵ
         * @param seepNum int ����
         * @param maxNum int ���ֵ
         * @return int ��ǰҪ���ӵ���
         */
        private int GetNumOf(int nowNum, int seepNum, int maxNum) {
            if (maxNum >= nowNum + seepNum)
                return seepNum;
            else
                return maxNum - nowNum;
        }
    }
    //-------------------------------------------------------------------------------------------
    /**
     * �������ӳ��ַ���
     * @param _ConnString String �����ַ���
     * @param _DriveString String �����ַ���
     * @throws StateException ����״̬����
     */
    public void set_String(String _ConnString, String _DriveString) throws StateException {
        if (_ConnString != null && _DriveString != null && (_PoolState == PoolState_UnInitialize || _PoolState == PoolState_Stop)) {
            this._ConnString = _ConnString;
            this._DriveString = _DriveString;
        } else {
            throw new StateException(); //����״̬����
        }
    }
    /**
     * ����ÿ��������������(��λ����)��Ĭ��20����
     * @param _Exist int ������������
     * @throws StateException ����״̬����
     */
    public void set_Exist(int _Exist) throws StateException {
        if (_PoolState == PoolState_Stop) {
            this._Exist = _Exist;
        } else {
            throw new StateException(); //����״̬����
        }
    }
    /**
     * ������Сʵ��������
     * @param _MinConnection int ��Сʵ��������
     * @throws ParameterBoundExecption ������ΧӦ���� 0~MaxConnection֮�䣬����Ӧ�ô���KeepConnection
     */
    public void set_MinConnection(int _MinConnection) throws ParameterBoundException {
        if (_MinConnection < _MaxConnection && _MinConnection > 0 && _MinConnection >= _KeepRealConnection)
            this._MinConnection = _MinConnection;
        else
            throw new ParameterBoundException(); //������ΧӦ���� 0~MaxConnection֮�䣬����Ӧ�ô���KeepConnection
    }
    /**
     * ���Ա��ظ�ʹ�ô��������ü����������ӱ��ظ������ֵ����ʾ�Ĵ���ʱ�������ӽ����ܱ������ȥ��
     * �����ӳص����ӱ����価ʱ�����ӳػ����Ѿ������ȥ�������У��ظ��������ӣ����ü����������������ӳ�ѹ��
     * @param _MaxRepeatDegree int ���ü���
     * @throws ParameterBoundExecption �ظ����ô���Ӧ����0
     */
    public void set_MaxRepeatDegree(int _MaxRepeatDegree) throws ParameterBoundException {
        if (_MaxRepeatDegree > 0)
            this._MaxRepeatDegree = _MaxRepeatDegree;
        else
            throw new ParameterBoundException(); //�ظ����ô���Ӧ����0
    }
    /**
     * ���������Դ�����ʵ��������Ŀ
     * @param _MaxConnection int �����Դ�����ʵ��������Ŀ
     * @throws ParameterBoundExecption ������Χ���󣬲���Ӧ�ô���MinConnection
     */
    public void set_MaxConnection(int _MaxConnection) throws ParameterBoundException {
        if (_MaxConnection > _MinConnection && _MaxConnection > 0)
            this._MaxConnection = _MaxConnection;
        else
            throw new ParameterBoundException(); //������Χ���󣬲���Ӧ�ô���MinConnection
    }
    /**
     * ���ñ�����ʵ�ʿ������ӣ��Թ����ܳ��ֵ�ReadOnlyʹ��
     * @throws ParameterBoundExecption ����������Ӧ���ڵ���0��ͬʱС��MaxConnection
     */
    public void set_KeepRealConnection(int _KeepRealConnection) throws ParameterBoundException {
        if (_KeepRealConnection >= 0 && _KeepRealConnection < _MaxConnection)
            this._KeepRealConnection = _KeepRealConnection;
        else
            throw new ParameterBoundException(); //����������Ӧ���ڵ���0��ͬʱС��MaxConnection
    }
    /**
     * ����ÿ�δ������ӵ�������
     * @param _SeepConnection int ÿ�δ������ӵ�������
     * @throws ParameterBoundExecption ����������Ӧ���ڵ���0��ͬʱС��MaxConnection
     */
    public void set_SeepConnection(int _SeepConnection) throws ParameterBoundException {
        if (_SeepConnection > 0 && _SeepConnection < _MaxConnection)
            this._SeepConnection = _SeepConnection;
        else
            throw new ParameterBoundException(); //����������Ӧ���ڵ���0��ͬʱС��MaxConnection
    }
    /**
     * ���õ�½���ݿ������
     * @param userID String ���ݿ��½�ʻ�
     * @param password String ���ݿ��½����
     * @throws PoolNotStopException ��������������
     */
    public void set_DataBaseUser(String userID, String password) throws PoolNotStopException {
        if (_PoolState == PoolState_UnInitialize || _PoolState == PoolState_Stop) {
            this._userID = userID;
            this._password = password;
        } else
            throw new PoolNotStopException();
    }
    /**
     * �õ��Զ��������ӳص�ʱ����
     * @return double �Զ��������ӳص�ʱ����
     */
    public long get_Interval() {
        return threadCheck.getInterval();
    }
    /**
     * �����Զ��������ӳص�ʱ����
     * @param value int ������ʵ�ʿ���������
     */
    public void set_Interval(long value) {
        threadCheck.setInterval(value);
    }
    /**
     * �õ����ӳ�ʹ�õ������ַ���
     * @return String �����ַ���
     */
    public String get_ConnString() {
        return _ConnString;
    }
    /**
     * �õ����ӳ�ʹ�õ������ַ���
     * @return String ���ӳ�ʹ�õ������ַ���
     */
    public String get_DriveString() {
        return _DriveString;
    }
    /**
     * �õ�ÿ��������������(��λ����)��Ĭ��20����
     * @return int ÿ��������������(��λ����)
     */
    public int get_Exist() {
        return _Exist;
    }
    /**
     * ������ӳ��Ѿ��������ֻ������
     * @return int �Ѿ��������ֻ������
     */
    public int get_ReadOnlyFormPool() {
        return _ReadOnlyFormPool;
    }
    /**
     * ���ӳ��д��ڵ�ʵ��������(����ʧЧ������)
     * @return int ʵ��������
     */
    public int get_RealFormPool() {
        return _RealFormPool;
    }
    /**
     * �õ�ÿ�δ������ӵ�������
     * @return int ÿ�δ������ӵ���Ŀ
     */
    public int get_SeepConnection() {
        return _SeepConnection;
    }
    /**
     * �õ�Ŀǰ�����ṩ��������
     * @return int �õ�Ŀǰ�����ṩ��������
     */
    public int get_SpareFormPool() {
        return _SpareFormPool;
    }
    /**
     * ��ÿ��е�ʵ������
     * @return int ���е�ʵ������
     */
    public int get_SpareRealFormPool() {
        return _SpareRealFormPool;
    }
    /**
     * �õ�����������ʱ��
     * @return Date ���ط���������ʱ��
     */
    public Date get_StartTime() {
        return _StartTime;
    }
    /**
     * ����Ѿ������������
     * @return int �Ѿ������������
     */
    public int get_UseFormPool() {
        return _UseFormPool;
    }
    /**
     * ����ѷ����ʵ������
     * @return int �ѷ����ʵ������
     */
    public int get_UseRealFormPool() {
        return _UseRealFormPool;
    }
    /**
     * �õ���ǰ���ӳص�״̬
     * @return int ���ӳص�״̬
     */
    public int get_PoolState() {
        return _PoolState;
    }
    /**
     * �õ����ӳ��д��ڵ�ʵ��������(��Ч��ʵ������)
     * @return int ʵ��������
     */
    public int get_PotentRealFormPool() {
        return _PotentRealFormPool;
    }
    /**
     * �õ���Сʵ��������
     * @return int ��Сʵ��������
     */
    public int get_MinConnection() {
        return _MinConnection;
    }
    /**
     * �õ�ÿ��ʵ�����ӿ��Ա��ظ�ʹ�ô��������ü�����
     * @return int ÿ��ʵ�����ӿ��Ա��ظ�ʹ�ô��������ü�����
     */
    public int get_MaxRepeatDegree() {
        return _MaxRepeatDegree;
    }
    /**
     * �õ����ʵ��������Ŀ
     * @return int �����Դ�����ʵ��������Ŀ
     */
    public int get_MaxConnection() {
        return _MaxConnection;
    }
    /**
     * �õ�������ʵ�ʿ�������
     * @return int ������ʵ�ʿ�������
     */
    public int get_KeepRealConnection() {
        return _KeepRealConnection;
    }
    /**
     * �õ����ݿ��½����
     * @return String ���ݿ��½����
     */
    public String get_password() {
        return _password;
    }
    /**
     * �õ����ݿ��½�ʺ�
     * @return String ���ݿ��½�ʺ�
     */
    public String get_userID() {
        return _userID;
    }
    /**
     * �õ����ӳ��������ṩ���ٸ����ӣ���ֵ�����ʵ����������ÿ�����ӿ��Ա��ظ�ʹ�ô����ĳ˻�
     * @return int ���ӳ��������ṩ���ٸ�����
     */
    public int get_MaxConnectionFormPool() {
        return _MaxConnection * _MaxRepeatDegree;
    }
    //-------------------------------------------------------------------------------------------
    public Connection GetConnectionFormPool(Object key) throws StateException, PoolFullException, KeyException, OccasionException, ConnLevelException {
        return GetConnectionFormPool(key, ConnLevel_None);
    }
    /**
     * �����ӳ�������һ�����ӣ��̰߳�ȫ
     * @param key Object ������
     * @return Connection ���뵽������
     * @throws StateException ����״̬����
     * @throws PoolFullException ���ӳ��Ѿ����ͣ������ṩ����
     * @throws KeyExecption һ��key����ֻ������һ������
     * @throws OccasionExecption ������Դ�ľ��������ķ���ʱ����
     * @throws ConnLevelExecption ��Ч�Ĵ���ļ������
     */
    public Connection GetConnectionFormPool(Object key, int connLevel) throws StateException, PoolFullException, KeyException, OccasionException, ConnLevelException {
        synchronized (this) {
            if (_PoolState != PoolState_Run)
                throw new StateException(); //����״̬����
            if (hs_UseConn.size() == get_MaxConnectionFormPool())
                throw new PoolFullException(); //���ӳ��Ѿ����ͣ������ṩ����
            if (hs_UseConn.containsKey(key))
                throw new KeyException(); //һ��key����ֻ������һ������
            if (connLevel == ConnLevel_ReadOnly)
                return GetConnectionFormPool_ReadOnly(key); //ReadOnly����
            else if (connLevel == ConnLevel_High)
                return GetConnectionFormPool_High(key); //High����
            else if (connLevel == ConnLevel_None)
                return GetConnectionFormPool_None(key); //None����
            else if (connLevel == ConnLevel_Bottom)
                return GetConnectionFormPool_Bottom(key); //Bottom����
            else
                throw new ConnLevelException(); //��Ч�Ĵ���ļ������
        }
    }
    /**
     * ����һ��������ԴReadOnly����ֻ����ʽ���̰߳�ȫ
     * @param key Object ������
     * @return Connection ���뵽�����Ӷ���
     * @throws OccasionExecption ������Դ�ľ��������ķ���ʱ����
     */
    private Connection GetConnectionFormPool_ReadOnly(Object key) throws OccasionException {
        ConnStruct cs = null;
        for (int i = 0; i < al_All.size(); i++) {
            cs = al_All.get(i);
            if (cs.GetEnable() == false || cs.GetAllot() == false || cs.GetUseDegree() == _MaxRepeatDegree || cs.GetIsUse() == true)
                continue;
            return GetConnectionFormPool_Return(key, cs, ConnLevel_ReadOnly); //���صõ�������
        }
        return GetConnectionFormPool_Return(key, null, ConnLevel_ReadOnly);
    }
    /**
     * ����һ��������ԴHigh����ֻ����ʽ���̰߳�ȫ
     * @param key Object ������
     * @return Connection ���뵽�����Ӷ���
     * @throws OccasionExecption ������Դ�ľ��������ķ���ʱ����
     */
    private Connection GetConnectionFormPool_High(Object key) throws OccasionException {
        ConnStruct cs = null;
        ConnStruct csTemp = null;
        for (int i = 0; i < al_All.size(); i++) {
            csTemp = al_All.get(i);
            if (csTemp.GetEnable() == false || csTemp.GetAllot() == false || csTemp.GetUseDegree() == _MaxRepeatDegree) { //�����Է�����������ѭ����
                csTemp = null;
                continue;
            }
            if (csTemp.GetUseDegree() == 0) { //�õ�����ʵ�
                cs = csTemp;
                break;
            } else { //��������ʵķ��õ����ѡ����
                if (cs != null) {
                    if (csTemp.GetUseDegree() < cs.GetUseDegree())
                        //����һ�����ѡ��ѡ��һ����ѵķ��õ�cs��
                        cs = csTemp;
                } else
                    cs = csTemp;
            }
        }
        return GetConnectionFormPool_Return(key, cs, ConnLevel_High); //��������ʵ�����
    }
    /**
     * ����һ��������Դ�����ȼ�-None���̰߳�ȫ
     * @param key Object ������
     * @return Connection ���뵽�����Ӷ���
     * @throws OccasionExecption ������Դ�ľ��������ķ���ʱ����
     */
    private Connection GetConnectionFormPool_None(Object key) throws OccasionException {
        ArrayList<ConnStruct> al = new ArrayList<ConnStruct>();
        ConnStruct cs = null;
        for (int i = 0; i < al_All.size(); i++) {
            cs = al_All.get(i);
            if (cs.GetEnable() == false || cs.GetAllot() == false || cs.GetUseDegree() == _MaxRepeatDegree) //�����Է�����������ѭ����
                continue;
            if (cs.GetAllot() == true)
                al.add(cs);
        }
        if (al.size() == 0)
            return GetConnectionFormPool_Return(key, null, ConnLevel_None); //�����쳣
        else
            return GetConnectionFormPool_Return(key, ((ConnStruct) al.get(al.size() / 2)), ConnLevel_None); //��������
    }
    /**
     * ����һ��������Դ�����ȼ�-�ͣ��̰߳�ȫ
     * @param key Object ������
     * @return Connection ���뵽�����Ӷ���
     * @throws OccasionExecption
     */
    private Connection GetConnectionFormPool_Bottom(Object key) throws OccasionException {
        ConnStruct cs = null;
        ConnStruct csTemp = null;
        for (int i = 0; i < al_All.size(); i++) {
            csTemp = al_All.get(i);
            if (csTemp.GetEnable() == false || csTemp.GetAllot() == false || csTemp.GetUseDegree() == _MaxRepeatDegree)//�����Է�����������ѭ����
            {
                csTemp = null;
                continue;
            } else//��������ʵķ��õ����ѡ����
            {
                if (cs != null) {
                    if (csTemp.GetUseDegree() > cs.GetUseDegree())
                        //����һ�����ѡ��ѡ��һ����ѵķ��õ�cs��
                        cs = csTemp;
                } else
                    cs = csTemp;
            }
        }
        return GetConnectionFormPool_Return(key, cs, ConnLevel_Bottom);//��������ʵ�����
    }
    /**
     * ����Connection����ͬʱ���������ʱ�ı�Ҫ����
     * @param key Object ������
     * @param cs ConnStruct ConnStruct����
     * @param connLevel int ����
     * @return Connection �Ƿ�Ϊֻ������
     * @throws OccasionExecption
     */
    private Connection GetConnectionFormPool_Return(Object key, ConnStruct cs, int connLevel) throws OccasionException {
        try {
            if (cs == null)
                throw new Exception();
            cs.Repeat();
            hs_UseConn.put(key, cs);
            if (connLevel == ConnLevel_ReadOnly) {
                cs.SetAllot(false);
                cs.SetIsRepeat(false);
            }
        } catch (Exception e) {
            throw new OccasionException(); //������Դ�ľ��������ķ���ʱ����
        } finally {
            UpdateAttribute(); //��������
        }
        return cs.GetConnection();
    }
    /**
     * �ͷ���������ݿ����Ӷ����̰߳�ȫ
     * @param key Object ��ʾ���ݿ�����������
     * @throws NotKeyExecption �޷��ͷţ������ڵ�key
     * @throws PoolNotRunException ����δ����
     */
    public void DisposeConnection(Object key) throws NotKeyException, PoolNotRunException {
        synchronized (hs_UseConn) {
            ConnStruct cs = null;
            if (_PoolState == PoolState_Run) {
                if (!hs_UseConn.containsKey(key))
                    throw new NotKeyException(); //�޷��ͷţ������ڵ�key
                cs = hs_UseConn.get(key);
                cs.SetIsRepeat(true);
                if (cs.GetAllot() == false)
                    if (cs.GetEnable() == true)
                        cs.SetAllot(true);
                try {
                    cs.Remove();
                } catch (Exception e) { //��������������ڼ������������̽�����������ʧЧ
                    cs.Close();
                    cs.SetConnectionLost();
                }
                hs_UseConn.remove(key);
            } else
                throw new PoolNotRunException(); //����δ����
        }
        UpdateAttribute(); //��������
    }
    //--------------------------------------------------------------------
    /**
     * ���ذ�װ����µ����Ӷ���
     * @param connString String �����ַ���
     * @param driveString String �����ַ���
     * @return ConnStruct װ����µ����Ӷ���
     * @throws RuntimeException  
     */
    private ConnStruct CreateConnectionTemp(String connString, String driveString, String userID, String password) throws RuntimeException {
        try {
            Connection con = CreateConnection(connString, driveString, userID, password);
            return new ConnStruct(con, Calendar.getInstance().getTime());
        } catch (CreateConnectionException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * <p>��ָ�����ʹ�������</p>
     *
     * �ڲ�ʵ�ַ�����<br>
     *    protected Connection CreateConnection(String connString, String driveString,String userID,String password) throws CreateConnectionException {<br>
     *        try {<br>
     *            Class.forName(driveString);<br>
     *            Connection con = java.sql.DriverManager.getConnection(connString, userID, password);<br>
     *            return con;<br>
     *        } catch (Exception e) {<br>
     *            throw new CreateConnectionException();<br>
     *        }<br>
     *    }<br>
     *
     * <p>ʹ��ʾ����</p>
     *   ConnectionPool c = new ConnectionPool() {<br>
     *       protected Connection CreateConnection(String connString, String driveString, String userID, String password) throws CreateConnectionException {<br>
     *           try {<br>
     *               Class.forName(driveString);<br>
     *               Connection con = java.sql.DriverManager.getConnection(connString, userID, password);<br>
     *               System.out.println("new db connection");<br>
     *               return con;<br>
     *   } catch (Exception e) {throw new CreateConnectionException();}<br>
     *       }<br>
     *   };<br>
     * ��ʾ����ʹ����������ȷ��������ӵ����ݿ��Լ���ÿ�δ������ݿ�����ʱҪִ�еķ�������ʵ��ʹ�õ��������Լ̳и�������д�÷����ﵽ��Ŀ��
     *
     * @param connString String �����ַ�������ֵ������set_String��������
     * @param driveString String �����ַ�������ֵ������set_String��������
     * @param userID String ��½���ݿ�ʹ�õ��ʺţ���ֵ������set_DataBaseUser��������
     * @param password String ��½���ݿ�ʹ�õ��ʺ����룬��ֵ������set_DataBaseUser��������
     * @return Connection ���ش���������
     * @throws CreateConnectionExecption ����ڴ����г�������
     */
    protected Connection CreateConnection(String connString, String driveString, String userID, String password) throws CreateConnectionException {
        try {
            Class.forName(driveString);
            Connection con = java.sql.DriverManager.getConnection(connString, userID, password);
            return con;
        } catch (Exception e) {
            throw new CreateConnectionException();
        }
    }
    //-------------------------------------------------------------------------------------------
    /**
     * �������ӳط���ͬ������
     * @throws PoolNotStopException 
     */
    public void StartServices() throws PoolNotStopException {
        StartServices(false);
    }
    /** 
     * �������ӳط���
     * @param ansy ָ�����÷�ʽ�Ƿ�Ϊ�첽���ã�TrueΪʹ���첽���ã��첽����ָ���û����ø÷���������ȴ����������Ϳɼ�������������
     * @throws PoolNotStopException �����Ѿ����л���δ��ȫ����
     */
    public void StartServices(boolean ansy) throws PoolNotStopException {
        synchronized (this) {
            threadCreate.createThreadMode = 0; //����ģʽ0
            threadCreate.createThreadProcessRun = true;
            threadCreate.createThreadProcessTemp = _MinConnection;
            if (_PoolState == PoolState_UnInitialize)
                threadCreate.start();
            else if (_PoolState == PoolState_Stop)
                threadCreate.interrupt();
            else
                throw new PoolNotStopException(); //�����Ѿ����л���δ��ȫ����
            threadCheck.StartTimer();
            threadCheck.interrupt();//��ʼ����
        }
        if (!ansy)
            while (threadCreate.getState() != Thread.State.WAITING) {
                //�ȴ����ܴ��ڵĴ����߳̽���
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
    /**
     * ֹͣ����ͬ���ķ������̰߳�ȫ
     * @throws ResCallBackException ��Դδȫ������
     * @throws PoolNotRunException ����û������
     */
    public void StopServices() throws ResCallBackException, PoolNotRunException {
        StopServices(false);
    }
    /**
     * ֹͣ�����̰߳�ȫ
     * @param needs boolean �Ƿ�����˳������ָ��Ϊfalse��StartServices()������ͬ�����ָ��Ϊtrue����δ�ջص�������Դ�رգ��⽫��Σ�յġ���Ϊ������ĳ�������ʹ�ô���Դ��
     * @throws ResCallBackException ����δ����
     * @throws PoolNotRunException ����û������
     */
    public void StopServices(boolean needs) throws PoolNotRunException, ResCallBackException {
        synchronized (this) {
            if (_PoolState == PoolState_Run) {
                synchronized (hs_UseConn) {
                    if (needs == true) //�����˳�
                        hs_UseConn.clear();
                    else if (hs_UseConn.size() != 0)
                        throw new ResCallBackException(); //���ӳ���Դδȫ������
                }
                threadCheck.StopTimer();
                while (threadCreate.getState() != Thread.State.WAITING) {
                    //�ȴ�threadCreate�¼�����
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                threadCreate.createThreadProcessRun = false;
                while (threadCreate.getState() != Thread.State.WAITING) {
                    //�ȴ����ܴ��ڵĴ����߳̽���
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (al_All) {
                    for (int i = 0; i < al_All.size(); i++)
                        al_All.get(i).Dispose();
                    al_All.clear();
                }
                _PoolState = PoolState_Stop;
            } else
                throw new PoolNotRunException(); //����δ����
        }
        UpdateAttribute(); //��������
    }
    /**
     * ����������̳߳�����ص��ø÷������ý���ֹ�̳߳��ڲ����ڵȴ����̡߳�
     */
    @SuppressWarnings("deprecation")
    public synchronized void Dispose() {
        try {
            this.StopServices();
            threadCreate.stop();
            threadCheck.stop();
            al_All = null;
            hs_UseConn = null;
        } catch (Exception e) {}
    }
    //-------------------------------------------------------------------------------------------
    /**
     * ����ConnStruct�Ƿ����
     * @param cs ConnStruct �����Ե�ConnStruct
     */
    private void TestConnStruct(ConnStruct cs) {
        //�˴α������ȥ�������Ƿ��ڴ˴�֮��ʧЧ
        if (cs.GetUseDegree() == _MaxRepeatDegree)
            cs.SetConnectionLost(); //����ʹ�ô���
        Calendar c = Calendar.getInstance();
        c.setTime(cs.GetCreateTime());
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + _Exist);
        if (c.getTime().after(new Date()))
            cs.SetConnectionLost(); //���ӳ�ʱ
    }
    /**
     * ��������
     */
    private synchronized void UpdateAttribute() {
        int temp_readOnlyFormPool = 0;//���ӳ��Ѿ��������ֻ������
        int temp_potentRealFormPool = 0;//���ӳ��д��ڵ�ʵ��������(��Ч��ʵ������)
        int temp_spareRealFormPool = 0;//���е�ʵ������
        int temp_useRealFormPool = 0;//�ѷ����ʵ������
        int temp_spareFormPool = get_MaxConnectionFormPool();//Ŀǰ�����ṩ��������
        //---------------------------------
        synchronized (hs_UseConn) {
            _UseFormPool = hs_UseConn.size();
        }
        //---------------------------------
        ConnStruct cs = null;
        synchronized (al_All) {
            _RealFormPool = al_All.size();
            for (int i = 0; i < al_All.size(); i++) {
                cs = al_All.get(i);
                //ֻ��
                if (cs.GetAllot() == false && cs.GetIsUse() == true && cs.GetIsRepeat() == false)
                    temp_readOnlyFormPool++;
                //��Ч��ʵ������
                if (cs.GetEnable() == true)
                    temp_potentRealFormPool++;
                //���е�ʵ������
                if (cs.GetEnable() == true && cs.GetIsUse() == false)
                    temp_spareRealFormPool++;
                //�ѷ����ʵ������
                if (cs.GetIsUse() == true)
                    temp_useRealFormPool++;
                //Ŀǰ�����ṩ��������
                if (cs.GetAllot() == true)
                    temp_spareFormPool = temp_spareFormPool - cs.GetRepeatNow();
                else
                    temp_spareFormPool = temp_spareFormPool - _MaxRepeatDegree;
            }
        }
        _ReadOnlyFormPool = temp_readOnlyFormPool;
        _PotentRealFormPool = temp_potentRealFormPool;
        _SpareRealFormPool = temp_spareRealFormPool;
        _UseRealFormPool = temp_useRealFormPool;
        _SpareFormPool = temp_spareFormPool;
    }
    /**
     * ��ø����ӳصİ汾
     * @return ���ذ汾��Ϣ
     */
    public String getVerstion() {
        return "1.2";
    }
}
