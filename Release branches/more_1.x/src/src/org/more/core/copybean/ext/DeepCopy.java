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
package org.more.core.copybean.ext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.more.core.copybean.ConvertType;
import org.more.core.copybean.Copy;
import org.more.core.copybean.PropertyReaderWrite;
/**
 * ĳһ�����Ե������
 * Date : 2009-5-15
 * @author ������
 */
@SuppressWarnings("unchecked")
public class DeepCopy extends Copy {
    /**  */
    private static final long serialVersionUID = 3577120677124915374L;
    /**
     * �������Ե�Ŀ��PropertyReaderWrite���Զ�д���С���������ɹ��򷵻�true���򷵻�fale��
     * @param toObject ׼��������Ŀ�����
     * @return ��������ɹ��򷵻�true���򷵻�fale��
     * @throws Exception  ��������з����쳣
     */
    @Override
    public boolean copyTo(PropertyReaderWrite toObject) {
        Object obj = this.get();
        if (obj == null) {
            toObject.set(null);
            return true;
        }
        Class<?> obj_type = obj.getClass();
        //
        if (obj_type == String.class || //String
                obj_type == Character.class || obj_type == char.class || //char
                obj_type == Byte.class || obj_type == byte.class || //byte
                obj_type == Short.class || obj_type == short.class || //short
                obj_type == Integer.class || obj_type == int.class || //integer
                obj_type == Long.class || obj_type == long.class || //long
                obj_type == Float.class || obj_type == float.class || //float
                obj_type == Double.class || obj_type == double.class || //double
                obj_type == Boolean.class || obj_type == boolean.class || //boolean
                obj instanceof Date || //boolean
                obj_type.isInterface() == true || //�ӿڣ������಻ִ�����
                //
                obj instanceof List || //Map��ִ�����
                obj instanceof Set || //List��ִ�����
                obj instanceof Map //Set��ִ�����
        ) {} else {
            //ִ����� 
            this.copyBeanUtil.copy(obj, toObject.get());
            return true;
        }
        //ִ��ǳ����
        //===============================================================
        if (this.canReader() == true && toObject.canWrite() == true) {
            //from -> to
            ConvertType convert = this.getConvertType(this.getPropertyClass(), toObject.getPropertyClass());
            if (convert == null)
                //ֱ�ӿ�����ת�� 
                toObject.set(obj);
            else
                //ת�����ҿ���  
                toObject.set(convert.convert(obj));
            return true;
        } else
            return false;
    }
}
