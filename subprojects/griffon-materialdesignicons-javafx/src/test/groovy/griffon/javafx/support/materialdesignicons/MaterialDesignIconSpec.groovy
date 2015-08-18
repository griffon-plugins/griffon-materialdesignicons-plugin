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
package griffon.javafx.support.materialdesignicons

import griffon.plugins.materialdesignicons.MaterialDesignIconFont
import javafx.embed.swing.JFXPanel
import spock.lang.Specification

/**
 * @author Andres Almiray
 */
class MaterialDesignIconSpec extends Specification {
    static {
        new JFXPanel()
    }

    def 'Can create a MaterialDesignIcon instance'() {
        given:
        MaterialDesignIconFont expected = MaterialDesignIconFont.MDI_ACCOUNT

        expect:
        MaterialDesignIcon materialdesignIcon = new MaterialDesignIcon(expected)
        materialdesignIcon.materialDesignIconFont == expected
    }

    def 'Invalid MaterialDesignIcon arguments'() {
        when:
        new MaterialDesignIcon(arg)

        then:
        thrown(IllegalArgumentException)

        where:
        arg   | _
        ''    | _
        ' '   | _
        'foo' | _
    }
}
