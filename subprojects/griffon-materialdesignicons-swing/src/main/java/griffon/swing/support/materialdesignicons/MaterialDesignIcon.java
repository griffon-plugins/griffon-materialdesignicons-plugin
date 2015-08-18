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
package griffon.swing.support.materialdesignicons;

import griffon.core.editors.PropertyEditorResolver;
import griffon.plugins.materialdesignicons.MaterialDesignIconFont;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.InputStream;

import static griffon.plugins.materialdesignicons.MaterialDesignIconFont.invalidDescription;
import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class MaterialDesignIcon implements Icon {
    private static final String MATERIALDESIGNICON_SET = "META-INF/resources/materialdesignicons/1.1.70/fonts/materialdesignicons-webfont.ttf";
    private static final String ERROR_MATERIALDESIGNICON_NULL = "Argument 'materialdesignIcon' must not be null";

    private static final Font MATERIALDESIGNICONFONT;
    private static final Object LOCK = new Object[0];

    private int size;
    private int width;
    private int height;
    private BufferedImage buffer;

    private MaterialDesignIconFont materialDesignIconFont;
    private Color color = Color.BLACK;
    private Font font;

    static {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(MATERIALDESIGNICON_SET);
            MATERIALDESIGNICONFONT = Font.createFont(Font.TRUETYPE_FONT, stream);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(MATERIALDESIGNICONFONT);
            stream.close();
        } catch (FontFormatException | IOException ffe) {
            throw new RuntimeException(ffe);
        }
    }

    public MaterialDesignIcon() {
        this(MaterialDesignIconFont.MDI_ACCOUNT);
    }

    public MaterialDesignIcon(@Nonnull MaterialDesignIconFont materialDesignIconFont) {
        this.materialDesignIconFont = requireNonNull(materialDesignIconFont, ERROR_MATERIALDESIGNICON_NULL);
        setSize(16);
    }

    public MaterialDesignIcon(@Nonnull String description) {
        this(MaterialDesignIconFont.findByDescription(description));
        resolveSize(description);
        resolveColor(description);
    }

    @Nonnull
    public MaterialDesignIconFont getMaterialDesignIcon() {
        return materialDesignIconFont;
    }

    public void setMaterialDesignIcon(@Nonnull MaterialDesignIconFont materialDesignIconFont) {
        this.materialDesignIconFont = requireNonNull(materialDesignIconFont, ERROR_MATERIALDESIGNICON_NULL);
    }

    public void setMaterialDesignIcon(@Nonnull String description) {
        requireNonBlank(description, "Argument 'description' must not be blank");
        setMaterialDesignIcon(MaterialDesignIconFont.findByDescription(description));
        resolveSize(description);
        resolveColor(description);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        synchronized (LOCK) {
            if (buffer == null) {
                buffer = new BufferedImage(getIconWidth(), getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2 = (Graphics2D) buffer.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setFont(font);
                g2.setColor(color);

                int sy = height - (height / 4) + (height / 16);
                g2.drawString(String.valueOf(materialDesignIconFont.getCode()), 0, sy);

                g2.dispose();
            }

            g.drawImage(buffer, x, y, null);
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size > 0) {
            this.size = size;
            font = MATERIALDESIGNICONFONT.deriveFont(Font.PLAIN, size);

            BufferedImage tmp = new BufferedImage(size, size,
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(tmp);
            g2.setFont(font);
            this.width = g2.getFontMetrics().charWidth(materialDesignIconFont.getCode());
            this.height = g2.getFontMetrics().getHeight();

            g2.dispose();

            synchronized (LOCK) {
                buffer = null;
            }
        }
    }

    @Nonnull
    public Color getColor() {
        return color;
    }

    public void setColor(@Nonnull Color color) {
        requireNonNull(color, "Argument 'color' must not be null.");
        this.color = color;
        synchronized (LOCK) {
            buffer = null;
        }
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    private void resolveSize(String description) {
        String[] parts = description.split(":");
        if (parts.length > 1) {
            try {
                setSize(Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                throw invalidDescription(description, e);
            }
        } else {
            setSize(16);
        }
    }

    private void resolveColor(String description) {
        String[] parts = description.split(":");
        if (parts.length > 2) {
            PropertyEditor editor = PropertyEditorResolver.findEditor(Color.class);
            editor.setValue(parts[2]);
            Color color = (Color) editor.getValue();
            if (color != null) {
                setColor(color);
            }
        }
    }
}
