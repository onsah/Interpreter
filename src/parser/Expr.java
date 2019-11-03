package parser;

import scanner.Token;

public abstract class Expr {
    
    public interface Visitor<T> {
        T visitLiteral(Literal lit);
        T visitUnary(Unary un);
        T visitBinary(Binary bin);
        T visitGrouping(Grouping gr);
        T visitIdent(Ident ident);
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public static class Literal extends Expr {
        public final Object value;

        public Literal(Object o) { value = o; }

        public <T> T accept(Visitor<T> v) { return v.visitLiteral(this); }
    }

    public static class Unary extends Expr {
        public final Token operator;
        public final Expr expr;

        public Unary(Token op, Expr e) { 
            operator = op; 
            expr = e;
        }

        public <T> T accept(Visitor<T> v) { return v.visitUnary(this); }
    }

    public static class Binary extends Expr {
        public final Token operator;
        public final Expr left, right;

        public Binary(Token op, Expr l, Expr r) { 
            operator = op;
            left = l;
            right = r;
        }

        public <T> T accept(Visitor<T> v) { return v.visitBinary(this); }
    }

    public static class Grouping extends Expr {
        public final Expr expr;

        public Grouping(Expr e) { expr = e; }

        public <T> T accept(Visitor<T> v) { return v.visitGrouping(this); }
    }

    public static class Ident extends Expr {
        public final java.lang.String name;

        public Ident(java.lang.String n) { name = n; }

        public <T> T accept(Visitor<T> v) { return v.visitIdent(this); }
    }
} 