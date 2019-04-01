package monopoly.model.spaces;

import org.javalite.activejdbc.Model;

public class ColorGroup extends Model {
    public enum Properties {
        HEX("HEX");

        private String value;

        private Properties(String value) {
            this.value = value;
        }

        public String getProperty() {
            return this.value;
        }
    }

    public static ColorGroup create(String hex) {
        // TODO: Add safety checks
        ColorGroup g = new ColorGroup();
        g.set(ColorGroup.Properties.HEX.getProperty());
        return g;
    }
}