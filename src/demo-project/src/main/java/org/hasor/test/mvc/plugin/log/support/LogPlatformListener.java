package org.hasor.test.mvc.plugin.log.support;
import java.lang.reflect.Method;
import net.hasor.Hasor;
import net.hasor.core.ApiBinder;
import net.hasor.core.ModuleSettings;
import net.hasor.core.anno.DefineModule;
import net.hasor.core.module.AbstractHasorModule;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hasor.test.mvc.plugin.log.OutLog;
import com.google.inject.matcher.AbstractMatcher;
/**
 * ��־����
 * @version : 2013-4-8
 * @author ������ (zyc@hasor.net)
 */
@DefineModule(displayName = "LogPlatformListener", description = "org.test.plugin.log���������֧�֡�")
public class LogPlatformListener extends AbstractHasorModule {
    public void configuration(ModuleSettings info) {}
    public void init(ApiBinder apiBinder) {
        //1.����Aop
        apiBinder.getGuiceBinder().bindInterceptor(new ClassOutLogMatcher(), new MethodOutLogMatcher(), new OutLogInterceptor());
    }
    /*-------------------------------------------------------------------------------------*/
    /*���������Ƿ�ƥ�䡣����ֻҪ���ͻ򷽷��ϱ����@OutLog��*/
    private class ClassOutLogMatcher extends AbstractMatcher<Class<?>> {
        public boolean matches(Class<?> matcherType) {
            if (matcherType.isAnnotationPresent(OutLog.class) == true)
                return true;
            Method[] m1s = matcherType.getMethods();
            Method[] m2s = matcherType.getDeclaredMethods();
            for (Method m1 : m1s) {
                if (m1.isAnnotationPresent(OutLog.class) == true)
                    return true;
            }
            for (Method m2 : m2s) {
                if (m2.isAnnotationPresent(OutLog.class) == true)
                    return true;
            }
            return false;
        }
    }
    /*�����ⷽ���Ƿ�ƥ�䡣���򣺷����򷽷��������ϱ����@OutLog��*/
    private class MethodOutLogMatcher extends AbstractMatcher<Method> {
        public boolean matches(Method matcherType) {
            if (matcherType.isAnnotationPresent(OutLog.class) == true)
                return true;
            if (matcherType.getDeclaringClass().isAnnotationPresent(OutLog.class) == true)
                return true;
            return false;
        }
    }
    /*������*/
    private class OutLogInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            //1.��ȡ��������
            Method targetMethod = invocation.getMethod();
            OutLog cacheAnno = targetMethod.getAnnotation(OutLog.class);
            if (cacheAnno == null)
                cacheAnno = targetMethod.getDeclaringClass().getAnnotation(OutLog.class);
            if (cacheAnno == null)
                return invocation.proceed();
            //2.��ȡKey
            StringBuilder logKey = new StringBuilder(targetMethod.toString());
            //            Object[] args = invocation.getArguments();
            //            if (args != null)
            //                for (Object arg : args) {
            //                    if (arg == null) {
            //                        logKey.append("NULL");
            //                        continue;
            //                    }
            //                    /*��֤arg������Ϊ��*/
            //                    logKey.append(ObjectKeyBuilder.serializeKey(arg));
            //                }
            //4.��������
            String key = logKey.toString();
            long t = System.currentTimeMillis();
            Object returnData = invocation.proceed();
            System.out.println(String.format("log invoke at %s, use time: %s.", key, (System.currentTimeMillis() - t)));
            return returnData;
        }
    }
}