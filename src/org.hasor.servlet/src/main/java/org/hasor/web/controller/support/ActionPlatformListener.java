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
package org.hasor.web.controller.support;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.hasor.Hasor;
import org.hasor.annotation.Module;
import org.hasor.context.AppContext;
import org.hasor.context.ModuleSettings;
import org.hasor.servlet.AbstractWebHasorModule;
import org.hasor.servlet.WebApiBinder;
import org.hasor.servlet.anno.support.WebAnnoSupportListener;
import org.hasor.web.controller.ActionBinder.ActionBindingBuilder;
import org.hasor.web.controller.ActionBinder.NameSpaceBindingBuilder;
import org.hasor.web.controller.Controller;
import org.hasor.web.controller.HttpMethod;
import org.hasor.web.controller.Produces;
import org.hasor.web.controller.RestfulMapping;
import org.hasor.web.controller.ResultDefine;
import org.hasor.web.controller.ResultProcess;
import org.more.util.ArrayUtils;
import org.more.util.BeanUtils;
import org.more.util.StringUtils;
import com.google.inject.Binder;
/**
 * Action���������࣬����װ��action��
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@Module(displayName = "ActionModuleListener", description = "org.hasor.web.controller���������֧�֡�")
public class ActionPlatformListener extends AbstractWebHasorModule {
    private ActionSettings settings      = null;
    private ActionManager  actionManager = null;
    @Override
    public void configuration(ModuleSettings info) {
        info.beforeMe(WebAnnoSupportListener.class);
    }
    @Override
    public void init(WebApiBinder apiBinder) {
        Binder binder = apiBinder.getGuiceBinder();
        apiBinder.filter("*").through(MergedController.class);
        this.settings = new ActionSettings();
        this.settings.onLoadConfig(apiBinder.getInitContext().getSettings());
        apiBinder.getGuiceBinder().bind(ActionSettings.class).toInstance(this.settings);
        /*����*/
        binder.bind(ActionManager.class).to(InternalActionManager.class).asEagerSingleton();
        /*��ʼ��*/
        this.loadController(apiBinder);
        /*ע����������*/
        this.loadResultDefine(apiBinder);
    }
    //
    /*װ��Controller*/
    protected void loadController(WebApiBinder event) {
        //1.��ȡ
        Set<Class<?>> controllerSet = event.getClassSet(Controller.class);
        if (controllerSet == null)
            return;
        //3.ע�����
        ActionBinderImplements actionBinder = new ActionBinderImplements();
        for (Class<?> controllerType : controllerSet) {
            Controller controllerAnno = controllerType.getAnnotation(Controller.class);
            for (String namespace : controllerAnno.value()) {
                NameSpaceBindingBuilder nsBinding = actionBinder.bindNameSpace(namespace);
                this.loadController(nsBinding, controllerType);
            }
        }
        event.getGuiceBinder().install(actionBinder);
    }
    /*װ��Controller*/
    private void loadController(NameSpaceBindingBuilder nsBinding, Class<?> controllerType) {
        List<Method> actionMethods = BeanUtils.getMethods(controllerType);
        Object[] ignoreMethods = settings.getIgnoreMethod().toArray();//����
        Controller controllerAnno = controllerType.getAnnotation(Controller.class);
        for (Method method : actionMethods) {
            //1.ִ�к���
            if (ArrayUtils.isInclude(ignoreMethods, method.getName()) == true)
                continue;
            //2.ע��Action
            ActionBindingBuilder actionBinding = nsBinding.bindActionMethod(method);
            for (String httpMethod : controllerAnno.httpMethod())
                actionBinding = actionBinding.onHttpMethod(httpMethod);
            //3.
            Produces mt = method.getAnnotation(Produces.class);
            mt = (mt == null) ? controllerType.getAnnotation(Produces.class) : mt;
            String minmeType = (mt != null) ? mt.value() : this.settings.getDefaultProduces();
            if (!StringUtils.isBlank(minmeType))
                actionBinding = actionBinding.returnMimeType(minmeType);
            //4.restful
            RestfulMapping mappingRestful = method.getAnnotation(RestfulMapping.class);
            if (mappingRestful != null) {
                for (HttpMethod httpMethod : mappingRestful.httpMethod())
                    actionBinding = actionBinding.onHttpMethod(httpMethod.name().toUpperCase());
                actionBinding.mappingRestful(mappingRestful.value());
            }
            //
        }
    }
    /*װ��ResultDefine*/
    protected void loadResultDefine(WebApiBinder event) {
        //1.��ȡ
        Set<Class<?>> resultDefineSet = event.getClassSet(ResultDefine.class);
        if (resultDefineSet == null)
            return;
        //3.ע�����
        ActionBinderImplements actionBinder = new ActionBinderImplements();
        for (Class<?> resultDefineType : resultDefineSet) {
            ResultDefine resultDefineAnno = resultDefineType.getAnnotation(ResultDefine.class);
            if (ResultProcess.class.isAssignableFrom(resultDefineType) == false) {
                Hasor.warning("loadResultDefine : not implemented ResultProcess. class=%s", resultDefineType);
            } else {
                actionBinder.bindResultProcess(resultDefineAnno.value()).toType((Class<? extends ResultProcess>) resultDefineType);
            }
        }
        event.getGuiceBinder().install(actionBinder);
    }
    //
    @Override
    public void start(AppContext appContext) {
        this.actionManager = appContext.getInstance(ActionManager.class);
        this.actionManager.initManager(appContext);
        Hasor.info("online ->> action is %s for style %s", (this.settings.isEnable() ? "enable." : "disable."), this.settings.getMode());
    }
    @Override
    public void destroy(AppContext appContext) {
        this.actionManager.destroyManager(appContext);
    }
}