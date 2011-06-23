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
package org.more.hypha.beans.assembler.support;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.more.hypha.beans.define.ScriptBeanDefine;
import org.more.util.ResourcesUtil;
/**
 * �ű�����Bean
 * @version : 2011-5-31
 * @author ������ (zyc@byshell.org)
 */
public class ScriptBean {
    private ScriptBeanDefine define = null;
    private ScriptEngine     engine = null;
    //
    public ScriptBean(ScriptBeanDefine define) throws ScriptException {
        this.define = define;
        String scriptEngine = define.getLanguage();
        //���������ű�ִ�л���
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName(scriptEngine);
        if (this.engine == null)
            throw new ScriptException("�޷������ű�����[" + scriptEngine + "]");
        //������ȡ�ű��ļ�������
        String str = define.getScriptText();
        if (str != null)
            this.engine.eval(new StringReader(str));
        else {
            InputStream in = ResourcesUtil.getResourceStream(define.getSourcePath());
            this.engine.eval(new InputStreamReader(in));
        }
    }
    /**��ȡBean����*/
    public ScriptBeanDefine getDefine() {
        return this.define;
    }
    public Object invokeMethod(String name, Object... objects) throws ScriptException, NoSuchMethodException {
        Invocable inv = (Invocable) engine;
        return inv.invokeFunction(name, objects);
    }
};