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
package net.hasor.core.context.factorys.hasor.schema;
/**
 * ClassPathBeanDefine类用于定义一个常规的bean，这个bean有一个具体的class类对象。
 * @version 2010-9-15
 * @author 赵永春 (zyc@byshell.org)
 */
public class ClassPathBeanDefine extends BeanDefine {
    private String source = null; //class类
    /**返回“ClassBean”。*/
    public String getBeanType() {
        return "ClassPathBean";
    }
    /**获取类的class完整限定名。*/
    public String getSource() {
        return this.source;
    }
    /**设置类完整限定名。*/
    public void setSource(final String source) {
        this.source = source;
    }
}