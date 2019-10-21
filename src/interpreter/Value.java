package interpreter;

import javax.sound.sampled.AudioFileFormat.Type;

public abstract class Value {

    public static class Number extends Value {
        double val;

        public Number(double v) { this.val = v; }
    } 

    public static class String extends Value {
        java.lang.String val;

        public String(java.lang.String v) { this.val = v; }
    }

    public static class Bool extends Value {
        boolean val;

        public Bool(boolean v) { this.val = v; }
    }

    public static Value fromObject(Object obj) throws TypeError {
        if (obj instanceof Integer) {
            return new Number(
                (double) ((Integer) obj).intValue());
        } else if (obj instanceof Double) {
            return new Number(
                ((Double) obj).doubleValue());
        } else if (obj instanceof java.lang.String) {
            return new String(
                (java.lang.String) obj);
        } else if (obj instanceof Boolean) {
            return new Bool(
                ((Boolean) obj).booleanValue());
        } else if (obj == null) {
            return null;
        } else {
            throw typeError(obj.getClass().toString());
        }
    }

    public static Value not(Value value) throws TypeError {
        if (value instanceof Bool) {
            Bool boolValue = (Bool) value;
            boolValue.val = !boolValue.val;
            return value;
        } else {
            throw typeError(value.getClass().toString());
        }
    }

    public static Value negate(Value value) throws TypeError {
        if (value instanceof Number) {
            Number numValue = (Number) value;
            numValue.val = -numValue.val;
            return value;
        } else {
            throw typeError(value.getClass().toString());
        }
    }

    public static Value isEqual(Value a, Value b) {
        return new Value.Bool(a.equals(b));
    }

    static TypeError typeError(java.lang.String type) {
        return new TypeError("Type error: " + type + " is not an appropriate type");
    }

    public boolean equals(Value other) {
        if (this instanceof Number) {
            return other instanceof Number &&
                ((Number) this).val == ((Number) other).val;
        } else if (this instanceof String) {
            return other instanceof String &&
                ((String) this).val.equals(((String) other).val);
        } else if (this instanceof Bool) {
            return other instanceof Bool &&
                ((Bool) this).val == ((Bool) other).val;
        } else {
            return this == null && other == null;
        }
    }

    public java.lang.String toString() {
        if (this instanceof Number) {
            return java.lang.String.valueOf(((Number) this).val);
        } else if (this instanceof String) {
            return ((String) this).val;
        } else if (this instanceof Bool) {
            return java.lang.String.valueOf(((Bool) this).val);
        } else {
            return "null";
        }
    }
}