package org.more.web.util;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * �ַ��������������������Դ�ģ��������Ӧ����������ʽ���ù�����ʹ��utf-8��ΪĬ�ϱ����ʽ��
 * Date : 2009-5-6
 * @author ������
 */
public class CharacterEncodingFilter implements Filter {
    /** ������� */
    private static final String requestEncoding  = "utf-8";
    /** ��Ӧ����*/
    private static final String responseEncoding = "utf-8";
    /**
     * ��������ʼ���������÷��������ʼ�������Լ���Ӧ���롣
     * @param config ���������ö���
     */
    public void init(FilterConfig config) {}
    /**
     * ��Դ�������������÷����д�����Դ���������Ӧ�������á�
     * @param request �������
     * @param response ��Ӧ����
     * @param chain ������ִ�ж���
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(CharacterEncodingFilter.requestEncoding);// ������������ʽ
        response.setCharacterEncoding(CharacterEncodingFilter.responseEncoding);// ������Ӧ�����ʽ
        chain.doFilter(request, response);//ִ�й�����
    }
    /** ���ٹ����� */
    public void destroy() {}
}