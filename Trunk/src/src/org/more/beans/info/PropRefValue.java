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
package org.more.beans.info;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����һ�����ö����������ԵĶ��塣��������һ����Ϊ���������ǡ����������ԡ���������bean���塢���ô���������Ԫ��Ϣ���ԡ���
 * �������͵ĵ�propType����ֵ���������ö���仯�ģ����PropRefValue����Ҫ����propType���ԡ�
 * <br/><br/>һ�����������ԣ�PRV_ContextAtt����<br/>
 *   bean��������ֻ��ʵ����{@link IAttribute}�ӿ�ʱ��ž������������ԣ�
 *   ʹ������������ע��ʱ��ζ����ע������ʱ����ֵ��Ѱ���ǵ������Ķ����{@link IAttribute}�ӿ���Ѱ�ҡ�
 * <br/><br/>������������bean���壨PRV_Bean����<br/>
 *   �������Ͷ��壬����ע����Ҫ����ע��һ��������{@link BeanDefinition}����Ķ��󡣶��������ͨ�������ٴα�����{@link BeanDefinition}�����á�
 * <br/><br/>�������ô���������PRV_Param����<br/>
 *   ��������ע����ָ��������getBean(name,params)����ʱ��ѡ���params���������е�ĳһ��������Ϊ����ֵ�������������ǲ��߱���ȷ�������͵ġ�
 * <br/><br/>�ġ�Ԫ��Ϣ���ԣ�PRV_Mime����<br/>
 *   Ԫ��Ϣ�������õ�������Դ����info�����п��Ի�õ�������������ֵ����Щ����ֵ�������{@link AttBase}�����С�info������е������඼�Ѿ��̳���{@link AttBase}���͡�
 *   ����������{@link AttBase}��û���ҵ����Ԫ��Ϣ��ϵͳ���Զ�����һ���ṹ�в������ԡ������û�ҵ����ٴ�����Ѱ��һֱѰ�ҵ�<b>����������</b>Ϊֹ��
 *   <br/>��ʾ��BeanDefinition�Ĳ�νṹ�ο�info�����������
 * @version 2009-11-18
 * @author ������ (zyc@byshell.org)
 */
public class PropRefValue extends BeanProp {
    //========================================================================================Field
    /**��ʾ���õ�������һ��bean�����bean��һ���Ѿ������bean��*/
    public static final String PRV_Bean         = "bean";
    /**��ʾ���Դ�Factory�����л�ȡ��*/
    public static final String PRV_ContextAtt   = "context";
    /**��ʾ������������getBeanʱ���ݵĻ��������С�*/
    public static final String PRV_Param        = "param";
    /**��ʾ���Դ������mime�����л�ȡ��*/
    public static final String PRV_Mime         = "mime";
    /**  */
    private static final long  serialVersionUID = -194590250590692070L;
    private String             refValue         = null;                //����ֵ
    private String             refType          = null;                //�������ͣ���PRV_�������塣
    //==================================================================================Constructor
    /**�������ö������͡�*/
    public PropRefValue() {
        refType = PropRefValue.PRV_ContextAtt;
    }
    /**�������ö������͡�*/
    public PropRefValue(String refValue, String refType) {
        this.refValue = refValue;
        this.setRefType(refType);
    }
    //==========================================================================================Job
    private static String find(String pStr, String string) {
        Matcher ma_tem = Pattern.compile(pStr).matcher(string);
        ma_tem.find();
        return ma_tem.group(1);
    }
    public static PropRefValue getPropRefValue(String refValueString) {
        //refBean|{#attName}|{@number}|{$mime}
        String pStr_1 = "\\x20*\\{#(\\w+)\\}\\x20*";// 1.{#PRV_ContextAtt}
        String pStr_2 = "\\x20*\\{@(\\d+)\\}\\x20*";// 2.{@PRV_Param}
        String pStr_3 = "\\x20*\\{\\$(\\w+)\\}\\x20*";// 3.{$PRV_Mime}
        PropRefValue propRef = new PropRefValue();
        String var = refValueString;
        if (isPRV_ContextAtt(refValueString) == true) {
            propRef.setRefType(PropRefValue.PRV_ContextAtt);
            var = find(pStr_1, var);
        } else if (isPRV_Param(refValueString) == true) {
            propRef.setRefType(PropRefValue.PRV_Param);
            var = find(pStr_2, var);
        } else if (isPRV_Mime(refValueString) == true) {
            propRef.setRefType(PropRefValue.PRV_Mime);
            var = find(pStr_3, var);
        } else {
            propRef.setRefType(PropRefValue.PRV_Bean);
        }
        propRef.setRefValue(var);
        return propRef;
    }
    //{#PRV_ContextAtt}
    public static boolean isPRV_ContextAtt(String refValueString) {
        return refValueString.matches("\\x20*\\{#(\\w+)\\}\\x20*");
    }
    //{@PRV_Param}
    public static boolean isPRV_Param(String refValueString) {
        return refValueString.matches("\\x20*\\{@(\\d+)\\}\\x20*");
    }
    //{$PRV_Mime}
    public static boolean isPRV_Mime(String refValueString) {
        return refValueString.matches("\\x20*\\{\\$(\\w+)\\}\\x20*");
    }
    public static boolean isPRV_Bean(String refValueString) {
        return !(isPRV_ContextAtt(refValueString) | isPRV_Param(refValueString) | isPRV_Mime(refValueString));
    }
    /**��ȡ����ֵ��*/
    public String getRefValue() {
        return refValue;
    }
    /**��������ֵ��*/
    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }
    /**��ȡ�������ͣ���PRV_�������塣*/
    public String getRefType() {
        return refType;
    }
    /**�����������ͣ���PRV_�������塣*/
    public void setRefType(String refType) {
        this.refType = refType;
    }
}