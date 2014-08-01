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
package net.hasor.mvc.controller.support;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import net.hasor.core.AppContext;
import net.hasor.core.context.AnnoModule;
import net.hasor.mvc.controller.ActionBinder.ActionBindingBuilder;
import net.hasor.mvc.controller.ActionBinder.NameSpaceBindingBuilder;
import net.hasor.mvc.controller.Controller;
import net.hasor.mvc.controller.HttpMethod;
import net.hasor.mvc.controller.Path;
import net.hasor.mvc.controller.Produces;
import net.hasor.servlet.AbstractWebModule;
import net.hasor.servlet.WebApiBinder;
import org.more.util.ArrayUtils;
import org.more.util.BeanUtils;
import org.more.util.StringUtils;
import com.google.inject.Binder;
/**
 * Action���������࣬����װ��action��
 * @version : 2013-4-8
 * @author ������ (zyc@hasor.net)
 */
@AnnoModule(description = "org.hasor.web.controller���������֧�֡�")
public class ServletControllerSupportModule extends AbstractWebModule {
    private ActionSettings settings      = null;
    private ActionManager  actionManager = null;
    //
    public void init(WebApiBinder apiBinder) {
        apiBinder.dependency().forced(ServletControllerSupportModule.class);
        //
        Binder binder = apiBinder.getGuiceBinder();
        apiBinder.filter("*").through(MergedController.class);
        this.settings = new ActionSettings();
        this.settings.onLoadConfig(apiBinder.getEnvironment().getSettings());
        apiBinder.getGuiceBinder().bind(ActionSettings.class).toInstance(this.settings);
        /*����*/
        binder.bind(ActionManager.class).asEagerSingleton();
        ActionManagerBuilder actionBinder = new ActionManagerBuilder();
        /*��ʼ��*/
        this.loadController(apiBinder, actionBinder);
        /*����*/
        actionBinder.buildManager(binder);
    }
    public void stop(AppContext appContext) {}
    //
    /**װ��Controller*/
    protected void loadController(WebApiBinder apiBinder, ActionManagerBuilder actionBinder) {
        //1.��ȡ
        Set<Class<?>> controllerSet = apiBinder.getClassSet(Controller.class);
        if (controllerSet == null)
            return;
        //3.ע�����
        for (Class<?> controllerType : controllerSet) {
            Controller controllerAnno = controllerType.getAnnotation(Controller.class);
            for (String namespace : controllerAnno.value()) {
                NameSpaceBindingBuilder nsBinding = actionBinder.bindNameSpace(namespace);
                this.loadController(nsBinding, controllerType);
            }
        }
    }
    private void loadController(NameSpaceBindingBuilder nsBinding, Class<?> controllerType) {
        List<Method> actionMethods = BeanUtils.getMethods(controllerType);
        Object[] ignoreMethods = settings.getIgnoreMethod().toArray();//����
        for (Method method : actionMethods) {
            //1.ִ�к���
            if (ArrayUtils.contains(ignoreMethods, method.getName()) == true)
                continue;
            //2.ע��Action
            ActionBindingBuilder actionBinding = nsBinding.bindActionMethod(method);
            //3.HttpMethod
            Annotation[] annos = method.getAnnotations();
            if (annos != null) {
                for (Annotation anno : annos) {
                    HttpMethod httpMethod = anno.annotationType().getAnnotation(HttpMethod.class);
                    if (httpMethod != null)
                        actionBinding = actionBinding.onHttpMethod(httpMethod.value());
                }
            }
            //4.��Ӧ����
            Produces mt = method.getAnnotation(Produces.class);
            mt = (mt == null) ? controllerType.getAnnotation(Produces.class) : mt;
            String minmeType = (mt != null) ? mt.value() : this.settings.getDefaultProduces();
            if (!StringUtils.isBlank(minmeType))
                actionBinding = actionBinding.returnMimeType(minmeType);
            //5.RESTful
            Path mappingRestful = method.getAnnotation(Path.class);
            if (mappingRestful != null)
                actionBinding.mappingRestful(mappingRestful.value());
        }
    }
    //
    /***/
    public void start(AppContext appContext) {
        this.actionManager = appContext.getInstance(ActionManager.class);
        this.actionManager.initManager(appContext);
    }
}