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
package griffon.javafx.support.materialdesignicons;

import griffon.core.editors.PropertyEditorResolver;
import griffon.plugins.materialdesignicons.MaterialDesignIconFont;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.annotation.Nonnull;
import java.beans.PropertyEditor;

import static griffon.plugins.materialdesignicons.MaterialDesignIconFont.invalidDescription;
import static griffon.util.GriffonClassUtils.requireState;
import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class MaterialDesignIcon extends Text {
    private static final String MATERIALDESIGNICON_SET = "META-INF/resources/materialdesignicons/1.1.70/fonts/materialdesignicons-webfont.ttf";
    private static final String ERROR_MATERIALDESIGN_NULL = "Argument 'materialDesignIcon' must not be null";

    private static final String MATERIALDESIGNICON_FONT_FAMILY;

    static {
        Font font = Font.loadFont(Thread.currentThread().getContextClassLoader().getResource(MATERIALDESIGNICON_SET).toExternalForm(), 16);
        MATERIALDESIGNICON_FONT_FAMILY = font.getFamily();
    }

    private ObjectProperty<MaterialDesignIconFont> materialDesignIcon;
    private IntegerProperty iconSize;
    private ObjectProperty<Paint> iconColor;

    private ChangeListener<Number> iconSizeChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> v, Number o, Number n) {
            setStyle(getStyle() + " -fx-font-size: " + n + "px;");
        }
    };

    private ChangeListener<Paint> iconColorChangeListener = new ChangeListener<Paint>() {
        @Override
        public void changed(ObservableValue<? extends Paint> v, Paint o, Paint n) {
            setFill(n);
        }
    };

    public MaterialDesignIcon() {
        this(MaterialDesignIconFont.MDI_ACCOUNT);
    }

    public MaterialDesignIcon(@Nonnull MaterialDesignIconFont materialDesignIcon) {
        setMaterialDesignIconFont(requireNonNull(materialDesignIcon, ERROR_MATERIALDESIGN_NULL));
        getStyleClass().add("materialdesignicons-icon");
        setText(String.valueOf(materialDesignIcon.getCode()));
        setStyle("-fx-font-family: '" + MATERIALDESIGNICON_FONT_FAMILY + "';");
        setIconSize(16);
        setIconColor(Color.BLACK);
    }

    public MaterialDesignIcon(@Nonnull String description) {
        this(MaterialDesignIconFont.findByDescription(description));
        resolveSize(description);
        resolvePaint(description);
    }

    public ObjectProperty<MaterialDesignIconFont> materialDesignIconProperty() {
        if (materialDesignIcon == null) {
            materialDesignIcon = new SimpleObjectProperty<>(this, "materialDesignIcon", null);
        }
        return materialDesignIcon;
    }

    public ObjectProperty<MaterialDesignIconFont> getMaterialDesignIconProperty() {
        return materialDesignIconProperty();
    }

    public IntegerProperty iconSizeProperty() {
        if (iconSize == null) {
            iconSize = new SimpleIntegerProperty(this, "iconSize", 16);
            iconSize.addListener(iconSizeChangeListener);
        }
        return iconSize;
    }

    public IntegerProperty getIconSizeProperty() {
        return iconSizeProperty();
    }

    public ObjectProperty<Paint> iconColorProperty() {
        if (iconColor == null) {
            iconColor = new SimpleObjectProperty<>(this, "iconColor", null);
            iconColor.addListener(iconColorChangeListener);
        }
        return iconColor;
    }

    public ObjectProperty<Paint> getIconColorProperty() {
        return iconColorProperty();
    }

    @Nonnull
    public MaterialDesignIconFont getMaterialDesignIconFont() {
        return materialDesignIconProperty().get();
    }

    public void setMaterialDesignIconFont(@Nonnull MaterialDesignIconFont materialDesignIcon) {
        materialDesignIconProperty().set(requireNonNull(materialDesignIcon, ERROR_MATERIALDESIGN_NULL));
        setText(String.valueOf(materialDesignIcon.getCode()));
    }

    public void setMaterialDesignIconFont(@Nonnull String description) {
        requireNonBlank(description, "Argument 'description' must not be blank");
        materialDesignIconProperty().set(MaterialDesignIconFont.findByDescription(description));
        resolveSize(description);
        resolvePaint(description);
    }

    public void setIconSize(int size) {
        requireState(size > 0, "Argument 'size' must be greater than zero.");
        iconSizeProperty().set(size);
    }

    public int getIconSize() {
        return iconSizeProperty().get();
    }

    public void setIconColor(@Nonnull Paint color) {
        requireNonNull(color, "Argument 'color' must not be null");
        iconColorProperty().set(color);
    }

    @Nonnull
    public Paint getIconColor() {
        return iconColorProperty().get();
    }

    private void resolveSize(String description) {
        String[] parts = description.split(":");
        if (parts.length > 1) {
            try {
                setIconSize(Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                throw invalidDescription(description, e);
            }
        } else {
            setIconSize(16);
        }
    }

    private void resolvePaint(String description) {
        String[] parts = description.split(":");
        if (parts.length > 2) {
            PropertyEditor editor = PropertyEditorResolver.findEditor(Paint.class);
            editor.setValue(parts[2]);
            Paint paint = (Paint) editor.getValue();
            if (paint != null) {
                setIconColor(paint);
            }
        }
    }
}
