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
package org.more.hypha.aop.define;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * ������鶨�壬�������û���κ�����㶨����ƥ��ʱ������true����������ڼ���ƥ��ֵʱ���Ǹ���ע��˳����������ƥ�䡣
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public class AopPointcutGroupDefine extends AbstractPointcutDefine {
    /**�Ƚ�{@link AopPointcutGroupDefine}�����������ʱʹ�õĹ�ϵ������*/
    public enum RelEnum {
        /**��*/
        And,
        /**��*/
        Or,
        /**��*/
        Not
    }
    private ArrayList<AbstractPointcutDefine> defineList = new ArrayList<AbstractPointcutDefine>();
    private RelEnum                           relation   = RelEnum.And;
    /**��ȡ��ƥ��ʱ��ƥ����ԡ�*/
    public RelEnum getRelation() {
        return relation;
    }
    /**���õ�ƥ��ʱ��ƥ����ԡ�*/
    public void setRelation(RelEnum relation) {
        this.relation = relation;
    }
    /**���������һ���е㣬����Ѿ����ڸ��е�����Ӳ���ʧЧ��*/
    public void addPointcutDefine(AbstractPointcutDefine define) {
        if (this.defineList.contains(define) == false)
            this.defineList.add(define);
    }
    /**����{@link AopPointcutGroupDefine}����*/
    public List<AbstractPointcutDefine> getPointcutList() {
        return this.defineList;
    };
    /**���չ�ϵҪ������������б��ʽ�������ؼ���֮���ƥ������*/
    public boolean isMatch(Method method) {
        if (this.defineList.size() == 0)
            return true;
        //
        boolean res_A = defineList.get(0).isMatch(method);
        for (int i = 1; i < this.defineList.size(); i++) {
            boolean res_B = this.defineList.get(i).isMatch(method);
            if (this.processRel(res_A, res_B) == false)
                return false;
            res_A = true;
        }
        return this.processRel(res_A);
    }
    /**��ϵ���㣬�������and,or,not���ֹ�ϵ��*/
    private boolean processRel(boolean res) {
        if (this.relation == RelEnum.Not && res == false)
            return true;
        return res;
    }
    /**��ϵ���㣬�������and,or,not���ֹ�ϵ��*/
    private boolean processRel(boolean res_1, boolean res_2) {
        if (this.relation == RelEnum.And)
            return res_1 == true && res_2 == true;
        else if (this.relation == RelEnum.Or)
            return res_1 == true || res_2 == true;
        else if (this.relation == RelEnum.Not)
            return res_1 == false && res_2 == false;
        return true;
    }
}