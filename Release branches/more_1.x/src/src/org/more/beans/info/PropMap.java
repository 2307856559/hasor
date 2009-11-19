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
/**
 * �����װbean�������й�Map�������Ե���Ϣ���½��ļ��϶������û��ָ��������Ĭ����:java.util.HashMp����
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class PropMap extends BeanProp {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -194590250590692070L;
    private BeanProp[][]      mapElements      = null;                //����Ԫ�ء�
    //==================================================================================Constructor
    /**����һ��java.util.HashMp���͵Ŀ�Map���ϡ�*/
    public PropMap() {
        this(null, "java.util.HashMp");
    }
    /**����һ��java.util.HashMp���͵ļ��ϣ�����Ԫ���ɲ���elements������*/
    public PropMap(BeanProp[][] elements) {
        this(elements, "java.util.HashMp");
    }
    /**����һ��PropMap���϶�����󣬼���Ԫ���ɲ���elements����������������mapType������*/
    public PropMap(BeanProp[][] elements, String mapType) {
        if (elements == null)
            this.mapElements = new BeanProp[0][0];
        else
            this.mapElements = elements;
        this.setPropType(mapType);
    }
    //==========================================================================================Job
    /**��ȡ����Ԫ�ء�*/
    public BeanProp[][] getMapElements() {
        return mapElements;
    }
    /**���ü���Ԫ�ء�*/
    public void setMapElements(BeanProp[][] mapElements) {
        this.mapElements = mapElements;
    }
}