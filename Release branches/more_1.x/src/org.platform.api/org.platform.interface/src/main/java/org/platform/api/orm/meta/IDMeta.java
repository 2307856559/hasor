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
package org.platform.api.orm.meta;
/**
 * ID����
 * @version : 2013-1-27
 * @author ������ (zyc@byshell.org)
 */
public class IDMeta extends AttMeta {
    /**�������ɲ���*/
    private KeyGeneratorEnum keyGenerator     = KeyGeneratorEnum.UUIDString;
    /**���������������࣬��keyGeneratorΪ�Զ���ʱ����Ч*/
    private String           keyGeneratorType = "";
    //
    //
    //
    public KeyGeneratorEnum getKeyGenerator() {
        return keyGenerator;
    }
    public void setKeyGenerator(KeyGeneratorEnum keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
    public String getKeyGeneratorType() {
        return keyGeneratorType;
    }
    public void setKeyGeneratorType(String keyGeneratorType) {
        this.keyGeneratorType = keyGeneratorType;
    }
}