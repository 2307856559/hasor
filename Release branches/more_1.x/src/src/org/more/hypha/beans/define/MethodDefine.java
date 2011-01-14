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
package org.more.hypha.beans.define;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.more.hypha.AbstractDefine;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.AbstractMethodDefine;
/**
 * �����Ǳ�ʾһ����������������ʵ����{@link AbstractMethodDefine}��
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
public class MethodDefine extends AbstractDefine<AbstractMethodDefine> implements AbstractMethodDefine {
    private String                 name          = null;
    private String                 codeName      = null;
    private ArrayList<ParamDefine> params        = new ArrayList<ParamDefine>(); //����
    private AbstractBeanDefine     forBeanDefine = null;
    /**����{@link MethodDefine}���Ͷ��󣬲��������÷���������bean���塣*/
    public MethodDefine(AbstractBeanDefine forBeanDefine) {
        this.forBeanDefine = forBeanDefine;
    }
    /**��ȡ�������������bean����*/
    public AbstractBeanDefine getForBeanDefine() {
        return this.forBeanDefine;
    }
    /**���ط����Ĵ������ƣ�������������������������Ŀ�ġ�*/
    public String getName() {
        return this.name;
    };
    /**���ط�������ʵ���ƣ��������Ǳ�ʾ��������ʵ��������*/
    public String getCodeName() {
        return this.codeName;
    };
    /**���ط����Ĳ����б����������صļ�����ֻ���ġ�*/
    public Collection<? extends ParamDefine> getParams() {
        return Collections.unmodifiableCollection(this.params);
    }
    /**��Ӳ���*/
    public void addParam(ParamDefine param) {
        this.params.add(param);
    }
    /**����name����*/
    public void setName(String name) {
        this.name = name;
    }
    /**����codeName����*/
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}