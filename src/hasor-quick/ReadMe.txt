Hasor-Quick

---Aop ���---
    ͨ�� @Aop��@GlobalAop ����ע���ṩ���� Aop ���档@Aop ���Ա�ǵ�
���������ϣ����ݱ�ǵ�λ�������� Aop �������õķ�Χ��@GlobalAop ע��
�Ǳ�ǵ��������ϵģ���������ȫ�������������ط�Χ����ͨ��ע��������á�
֧�ֱ��ʽ���ã�֧�� Aop ����

---Bean ���---
    �ò���Ὣ���б���� @Bean ����ͨ����ApiBinder.defineBean(...)��
���뽫��ע�ᵽ Hasor �����С�ע��֮�����ͨ����AppContext.getBean��
��ȡBean�����ù� Spring �Ļ�һ������İ����

---Cache ���---
    �ò���������ṩ���湦�ܣ�����Ϊʹ�û����ṩ��ͳһ�Ľӿڡ����湦��
���ṩ��Ҫʵ�� CacheCreator �ӿڲ�ͨ����� @Creator ע������Ч��
ʹ�û������ͨ������Ҫ����ķ�����ͨ����� @NeedCache ע�������ý�����档

---Event ���---
    ͨ����� @Listener ע������һ�� ��net.hasor.core.EventListener�����͵�
�¼���������ͨ����EventManager.doSync or .doAsync�����������¼���
Hasor ���¼��Ĵ����Ϊͬ��(Sync)���첽(Async)��

---Guice ���---
    ͨ�� @GuiceModule ע����Խ�����һ������ Guice �����ġ�com.google.inject.Module��
ģ����뵽 Hasor ����Ϊ Hasor ��һ��ģ�顣

---Setting ���---
    ͨ�� @Settings ע������һ�������ļ��ı��������Hasor ������֮������
���������ļ��Ƿ�ı䣬�緢���ı� Hasor ���Զ�������������� @Settings ע���
SettingsListener���������յ����֪ͨ��

---Controller ���---
    ͨ�� @Controller ע���ṩ�� WebMVC ���������֧�֡��ò�����ṩ��������Ӧ���������ܡ�
���п�����������Ҫ��̳��� AbstractController �����࣬���ĳ���������񱻷����� action��
����ͨ��@ControllerIgnore ע��������á�hasor-web.controller.globalIgnore���������ǡ�
    
---Restful ���---
    ͨ�� @RestfulService ע�ⷢ�� Restful �����֧�֣�Hasor �� restful �ο��� JSR-311��
@Any��@AttributeParam��@CookieParam��@Get��@Head��@HeaderParam��@HttpMethod��@Options
@Path��@PathParam��@Post��@Produces��@Put��@QueryParam ��Щע�������������ṩ�ġ�
    
---Result ���---
    �ò���� Controller��Result�����������չ�������Ϊ������������ṩ�˷���ֵ��������ơ�
@Forword��@Include��@Json��@Redirect �������ṩ�ģ������߻������Լ������Զ�����չ��

---Servlet3 ���---
    �ò��������֧��Servlet3.0 �淶����������� Servlet ������֧�� Servlet3.0 �淶ʱ����
ͨ���ò���ṩ�� @WebFilter��@WebServlet��@WebInitParam ��ʵ�� Servlet3.0��

---Resource ���---
    ͨ�����������Խ�λ��ClassPath��Zip��λ���е���Դ���� Web ������Ӧ���������
Servlet ��ʽ�ṩ����������Ҫ�Լ�ע������