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
package org.more.webui.web;
import org.more.webui.context.FacesConfig;
import org.more.webui.context.FacesContext;
import org.more.webui.lifestyle.Lifecycle;
/**
 * 
 * @version : 2012-6-27
 * @author ������ (zyc@byshell.org)
 */
public interface WebUIFactory {
    /**����{@link Lifecycle}����*/
    public Lifecycle createLifestyle(FacesConfig config, FacesContext context);
    /**����{@link FacesContext}����*/
    public FacesContext createFacesContext(FacesConfig config);
}