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
package org.hasor.context;
/**
 * ��������
 * @version : 2013-7-11
 * @author ������ (zyc@byshell.org)
 */
public interface LifeCycle {
    public static enum LifeCycleEnum {
        /**�׶��¼���Phase_OnInit*/
        PhaseEvent_Init("Phase_OnInit"),
        /**�׶��¼���Phase_OnStart*/
        PhaseEvent_Start("Phase_OnStart"),
        /**�׶��¼���Phase_OnStop*/
        PhaseEvent_Stop("Phase_OnStop"),
        /**�׶��¼���Phase_OnDestroy*/
        PhaseEvent_Destroy("Phase_OnDestroy"),
        /**�׶��¼���Phase_OnTimer*/
        PhaseEvent_Timer("Phase_OnTimer"), ;
        private String value = null;
        LifeCycleEnum(String var) {
            this.value = var;
        }
        public String getValue() {
            return value;
        }
        @Override
        public String toString() {
            return this.getValue();
        }
    }
    /**����*/
    public void start();
    /**ֹͣ*/
    public void stop();
    /**�Ƿ�������״̬*/
    public boolean isRunning();
}