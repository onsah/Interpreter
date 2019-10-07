package parser;

import scanner.Token;

public abstract class Expr {
    
    public interface Visitor<T> {
        T visitLiteral(Literal lit);
        T visitUnary(Unary un);
        T visitBinary(Binary bin);
        T visitGrouping(Grouping gr);
    }

    abstract <T> T accept(Visitor<T> visitor);

    public static class Literal extends Expr {
        final Object value;

        public Literal(Object o) { value = o; }

        <T> T accept(Visitor<T> v) { return v.visitLiteral(this); }
    }

    public static class Unary extends Expr {
        final Token operator;
        final Expr expr;

        public Unary(Token op, Expr e) { 
            operator = op; 
            expr = e;
        }

        <T> T accept(Visitor<T> v) { return v.visitUnary(this); }
    }

    public static class Binary extends Expr {
        final Token operator;
        final Expr left, right;

        public Binary(Token op, Expr l, Expr r) { 
            operator = op;
            left = l;
            right = r;
        }

        <T> T accept(Visitor<T> v) { return v.visitBinary(this); }
    }

    public static class Grouping extends Expr {
        final Expr expr;

        public Grouping(Expr e) { expr = e; }

        <T> T accept(Visitor<T> v) { return v.visitGrouping(this); }
    }
} 