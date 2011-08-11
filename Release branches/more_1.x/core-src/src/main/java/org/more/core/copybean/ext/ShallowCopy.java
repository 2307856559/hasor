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
import org.more.core.copybean.ConvertType;
import org.more.core.copybean.Copy;
import org.more.core.copybean.PropertyReaderWrite;
/**
 * ĳһ�����Ե�ǳ������
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class ShallowCopy extends Copy {
    /**  */
    private static final long serialVersionUID = 3577120677124915374L;
    /**
     * �������Ե�Ŀ��PropertyReaderWrite���Զ�д���С���������ɹ��򷵻�true���򷵻�fale��
     * @param toObject ׼��������Ŀ�����
     * @return ��������ɹ��򷵻�true���򷵻�fale��
     * @throws Exception  ��������з����쳣
     */
    public boolean copyTo(PropertyReaderWrite toObject) {
        if (this.canReader() == true && toObject.canWrite() == true) {
            //from -> to
            ConvertType convert = this.getConvertType(this.getPropertyClass(), toObject.getPropertyClass());
            if (convert == null) {
                //ֱ�ӿ�����ת��
                Object obj = this.get();
                if (obj != null)
                    toObject.set(obj);
            } else {
                //ת�����ҿ���
                Object obj = this.get();
                if (obj != null)
                    toObject.set(convert.convert(obj));
            }
            return true;
        } else
            return false;
    }
}