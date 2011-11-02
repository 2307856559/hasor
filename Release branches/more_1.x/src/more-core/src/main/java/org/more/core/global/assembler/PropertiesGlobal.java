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
package org.more.core.global.assembler;
import java.io.File;
import java.io.Reader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URL;
import org.more.core.global.Global;
import org.more.core.global.GlobalFactory;
/**
* 
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public class PropertiesGlobal implements GlobalFactory {
    /*------------------------------------------------------------------------*/
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface PropertiesFile {
        /**��ʾ��ö�ٶ�Ӧ��ȫ�����������ļ���һ���ļ���Դ��*/
        public String file() default "";
        /**��ʾ��ö�ٶ�Ӧ��ȫ�����������ļ���һ��{@link URI}��Դ��*/
        public String uri() default "";
        /**��ʾ��ö�ٶ�Ӧ��ȫ�����������ļ�����classpathĿ¼�µ��ļ���Դ��*/
        public String value() default "";
    };
    /*------------------------------------------------------------------------*/
    public Global createGlobal(Object... objects) {
        Global global = Global.newInterInstance();
        for (Object obj : objects)
            if (obj instanceof File) {
                //
            } else if (obj instanceof URL) {
                //
            } else if (obj instanceof URI) {
                //
            } else if (obj instanceof String) {
                //
            } else if (obj instanceof Reader) {
                //
            }
        return global;
    }
    //    /*------------------------------------------------------------------------*/
    //    /**��һ��ö�ٵ�һ�������ļ��ϣ�������ö��������{@link PropertiesFile}ע����ʹ�ø�ע���ǵ������ļ�����װ�أ������װ����ö����ͬ���������ļ���*/
    //    public void addEnum(Class<? extends Enum<?>> enumType) throws Throwable {
    //        PropertiesFile pFile = enumType.getAnnotation(PropertiesFile.class);
    //        if (pFile != null)
    //            if (pFile.file().equals("") == false)
    //                this.addResource(enumType, new File(pFile.file()));
    //            else if (pFile.uri().equals("") == false)
    //                this.addResource(enumType, new URI(pFile.uri()));
    //            else if (pFile.value().equals("") == false)
    //                this.addResource(enumType, pFile.value());
    //            else
    //                this.addResource(enumType, enumType.getSimpleName());
    //        this.addResource(enumType, enumType.getSimpleName());
    //    };
    //    /**���һ�������ļ������Ұ󶨵�ָ����ö���ϡ�*/
    //    public void addResource(Class<? extends Enum<?>> enumType, String resource) throws IOException {
    //        InputStream stream = ResourcesUtil.getResourceAsStream(resource);
    //        IAttribute<String> att = this.loadConfig(stream);
    //        this.addAttribute(enumType, att);
    //    };
    //    /**���һ�������ļ������Ұ󶨵�ָ����ö���ϡ�*/
    //    public void addResource(Class<? extends Enum<?>> enumType, URI resource) throws MalformedURLException, IOException {
    //        IAttribute<String> att = this.loadConfig(new AutoCloseInputStream(resource.toURL().openStream()));
    //        this.addAttribute(enumType, att);
    //    };
    //    /**���һ�������ļ������Ұ󶨵�ָ����ö���ϡ�*/
    //    public void addResource(Class<? extends Enum<?>> enumType, File resource) throws IOException {
    //        IAttribute<String> att = this.loadConfig(new AutoCloseInputStream(new FileInputStream(resource)));
    //        this.addAttribute(enumType, att);
    //    };
    //    protected IAttribute<String> loadConfig(InputStream stream) throws IOException {
    //        Properties prop = new Properties();
    //        prop.load(stream);
    //        return new TransformToAttribute<String>(prop);
    //    }
};