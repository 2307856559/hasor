Hasor-Web

    ����Ŀ�� Hasor Ϊ֧�� Web ���Զ���������ǣ�����Ŀ������ Hasor-Core��
Hasor-Web ��Ϊ���ĺͲ���������֡�ʹ�� Hasor-Web ����ͨ��������ʽ��̬ע��
Servlet/Filter ��Hasor-Web Ϊ���ǽ�����ͳһ�� Dispatcher��


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