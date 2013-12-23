/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.project.test.mgr.menus.services;
import java.util.ArrayList;
import java.util.List;
import net.hasor.core.AppContext;
import net.hasor.core.Settings;
import net.hasor.core.XmlNode;
import net.project.test.mgr.menus.entity.MenuBean;
import org.more.util.StringUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
/**
 * ������
 * @version : 2013-12-23
 * @author ������(zyc@hasor.net)
 */
@Singleton
public class MenuServices {
    /*�ӹ��췽����ע�� AppContext �ӿڶ���*/
    @Inject
    public MenuServices(AppContext appContext) {
        this.appContext = appContext;
    };
    //
    //
    private AppContext     appContext;
    private List<MenuBean> menuList;
    private void init() {
        if (menuList != null)
            return;
        this.menuList = new ArrayList<MenuBean>();
        /*��ȡ���������ļ��Ľӿ�*/
        Settings setting = appContext.getSettings();
        /*ȡ�á�/demoProject/menus�� Xml�ڵ�*/
        XmlNode xmlNode = setting.getXmlProperty("demoProject.menus");
        /*ʹ�� DOM ��ʽ���� Xml�ڵ�*/
        List<XmlNode> menus = xmlNode.getChildren("menu");
        for (XmlNode node : menus) {
            MenuBean menuBean = new MenuBean();
            menuBean.setCode(node.getAttribute("code"));
            menuBean.setName(node.getAttribute("name"));
            menuBean.setUrl(node.getAttribute("url"));
            menuList.add(menuBean);
        }
    }
    public MenuBean findMenuByCode(String code) {
        init();
        for (MenuBean menu : menuList) {
            /*���Դ�Сд�ж��Ƿ����*/
            if (!StringUtils.endsWithIgnoreCase(menu.getCode(), code))
                continue;
            return menu;
        }
        return null;
    }
    public List<MenuBean> getMenuList() {
        init();
        return this.menuList;
    }
}