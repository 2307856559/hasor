package org.more.webui.render;
import java.util.HashMap;
import java.util.Map;
import org.more.webui.tag.TagObject;
/**
 * 
 * @version : 2012-5-22
 * @author ������ (zyc@byshell.org)
 */
public class RenderKit {
    private Map<String, Class<? extends Render>> renderMapping = new HashMap<String, Class<? extends Render>>();
    private Map<String, Object>                  tagMap        = new HashMap<String, Object>();
    //
    /**��ȡ�Ѿ�ע��ı�ǩ���󼯺�*/
    public Map<String, Object> getTags() {
        return this.tagMap;
    }
    public Render getRender(String tagName) {
        try {
            Class<? extends Render> renderType = this.renderMapping.get(tagName);
            return renderType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("��ǩ���� ��" + tagName + "�����ܱ�����.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("��ǩ�����ڴ��� ��" + tagName + "���ڼ�����һ������ķ���Ȩ��.", e);
        }
    }
    /**ע���ǩ�ֻ࣬�ܶ��Ѿ�ע��render���齨����ע��*/
    public void addTag(String tagName, TagObject tagObject) {
        if (this.renderMapping.containsKey(tagName) == true)
            if (tagObject != null)
                tagMap.put(tagName, tagObject);
            else
                throw new NullPointerException("TagObject���Ͳ�������Ϊ�ա�");
    }
    public void addRender(String tagName, Class<? extends Render> renderClass) {
        this.renderMapping.put(tagName, renderClass);
        this.tagMap.put(tagName, new TagObject());//���Ĭ�ϱ�ǩ
    }
}