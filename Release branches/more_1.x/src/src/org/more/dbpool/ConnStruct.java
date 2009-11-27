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
import java.util.Date;
import org.more.dbpool.exception.AllotAndRepeatException;
import org.more.dbpool.exception.AllotException;
import org.more.dbpool.exception.RepeatIsZeroException;
import org.more.dbpool.exception.ResLostnException;
/**
 * ���ӳ��е�һ����������
 * Date : 2009-5-17
 * @author ������
 */
public class ConnStruct {
    private boolean    _enable    = true; //�Ƿ�ʧЧ
    private boolean    _isUse     = false; //�Ƿ����ڱ�ʹ����
    private boolean    _allot     = true; //��ʾ�������Ƿ���Ա�����
    private Date       _createTime;       //����ʱ��
    private int        _useDegree = 0;    //��ʹ�ô���
    private int        _repeatNow = 0;    //��ǰ���ӱ��ظ����ö���
    private boolean    _isRepeat  = true; //�����Ƿ���Ա��ظ����ã����������ȥ�����ӿ���ʹ������ʱ�������Ա���ʶΪtrue
    private int        _connType;         //��������
    private Connection _connect   = null; //���Ӷ���
    /**
     * ���ӳ��е�����
     * @param dbc Connection ���ݿ�����
     */
    public ConnStruct(Connection dbc) {
        InitConnStruct(dbc, new Date());
    }
    /**
     * ���ӳ��е�����
     * @param dbc Connection  ���ݿ�����
     * @param dt Date ���Ӵ���ʱ��
     */
    public ConnStruct(Connection dbc, Date dt) {
        InitConnStruct(dbc, dt);
    }
    /**
     * ���ӳ��е�����
     * @param dbc Connection  ���ݿ�����
     * @param dt Date ���Ӵ���ʱ��
     */
    private void InitConnStruct(Connection dbc, Date dt) {
        _createTime = dt;
        _connect = dbc;
    }
    //--------------------------------------------------------------------
    /**
     * �õ�һ��ֵ��ʾ�������Ƿ���Ա�����
     * @return boolean �������Ƿ���Ա�����
     */
    public boolean GetAllot() {
        return _allot;
    }
    /**
     * ����һ��ֵ��ʾ�������Ƿ���Ա�����
     * @param value boolean true���Ա����䣬false�����Ա�����
     */
    public void SetAllot(boolean value) {
        _allot = value;
    }
    /**
     * �õ���ǰ�����Ƿ�ʧЧ
     * @return boolean �õ���ǰ�����Ƿ�ʧЧ��false��ʾʧЧ��ֻ��
     */
    public boolean GetEnable() {
        return _enable;
    }
    /**
     * �õ���ǰ�����Ƿ����ڱ�ʹ���У�ֻ��
     * @return boolean ��ǰ�����Ƿ����ڱ�ʹ����
     */
    public boolean GetIsUse() {
        return _isUse;
    }
    /**
     * �õ����Ӵ���ʱ�䣬ֻ��
     * @return Date ����ʱ��
     */
    public Date GetCreateTime() {
        return _createTime;
    }
    /**
     * �õ���ʹ�ô�����ֻ��
     * @return int �õ���ʹ�ô���
     */
    public int GetUseDegree() {
        return _useDegree;
    }
    /**
     * �õ���ǰ���ӱ��ظ����ö��٣�ֻ��
     * @return int ��ǰ���ӱ��ظ����ö���
     */
    public int GetRepeatNow() {
        return _repeatNow;
    }
    /**
     * �õ������ӣ�ֻ��
     * @return Connection ����
     */
    public Connection GetConnection() {
        return _connect;
    }
    /**
     * �õ������Ƿ���Ա��ظ�����
     * @return boolean �Ƿ���Ա��ظ�����
     */
    public boolean GetIsRepeat() {
        return _isRepeat;
    }
    /**
     * ���������Ƿ���Ա��ظ�����
     * @param value boolean true���Ա��ظ����ã�false�����Ա��ظ�����
     */
    public void SetIsRepeat(boolean value) {
        _isRepeat = value;
    }
    /**
     * �õ���������ConnectionPool.ConnType_*��ֻ��
     * @return int ��������
     */
    public int GetConnType() {
        return _connType;
    }
    /**
     * �ر����ݿ�����
     */
    public void Close() {
        try {
            _connect.close();
        } catch (Exception e) {}
    }
    /**
     * ����������������ΪʧЧ
     */
    public void SetConnectionLost() {
        _enable = false;
        _allot = false;
    }
    /**
     * �������ȥ���̰߳�ȫ��
     * @throws ResLostnExecption
     * @throws AllotExecption
     * @throws AllotAndRepeatExecption
     */
    public synchronized void Repeat() throws ResLostnException, AllotException, AllotAndRepeatException {
        if (_enable == false) //���ӿ���
            throw new ResLostnException(); //������Դ�Ѿ�ʧЧ
        if (_allot == false) //�Ƿ���Ա�����
            throw new AllotException(); //������Դ�����Ա�����
        if (_isUse == true && _isRepeat == false)
            throw new AllotAndRepeatException(); //������Դ�Ѿ������䲢�Ҳ������ظ�����
        _repeatNow++; //���ü���+1
        _useDegree++; //��ʹ�ô���+1
        _isUse = true; //��ʹ��
    }
    /**
     * ���ͷŻ������̰߳�ȫ��
     * @throws ResLostnExecption ������Դ�Ѿ�ʧЧ
     * @throws RepeatIsZeroExecption ���ü����Ѿ�Ϊ0
     */
    public synchronized void Remove() throws ResLostnException, RepeatIsZeroException {
        if (_enable == false) //���ӿ���
            throw new ResLostnException(); //������Դ�Ѿ�ʧЧ
        if (_repeatNow == 0)
            throw new RepeatIsZeroException(); //���ü����Ѿ�Ϊ0
        _repeatNow--; //���ü���-1
        if (_repeatNow == 0)
            _isUse = false; //δʹ��
        else
            _isUse = true; //ʹ����
    }
    /// <summary>
    /// �ͷ���Դ
    /// </summary>
    public void Dispose() {
        _enable = false;
        try {
            _connect.close();
        } catch (Exception e) {}
        _connect = null;
    }
}
