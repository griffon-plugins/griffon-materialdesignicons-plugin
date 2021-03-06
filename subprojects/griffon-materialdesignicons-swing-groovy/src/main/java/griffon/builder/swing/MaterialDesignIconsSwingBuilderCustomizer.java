/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.builder.swing;

import griffon.builder.swing.factory.MaterialDesignIconFactory;
import griffon.inject.DependsOn;
import groovy.util.Factory;
import org.codehaus.griffon.runtime.groovy.view.AbstractBuilderCustomizer;

import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Andres Almiray
 */
@DependsOn({"swing"})
@Named("materialdesignicons-swing")
public class MaterialDesignIconsSwingBuilderCustomizer extends AbstractBuilderCustomizer {
    public MaterialDesignIconsSwingBuilderCustomizer() {
        Map<String, Factory> factories = new LinkedHashMap<>();
        factories.put("materialdesignIcon", new MaterialDesignIconFactory());
        setFactories(factories);
    }
}
