package interpreter;


import parser.Expr;
import parser.Expr.Literal;
import parser.Expr.Unary;
import parser.Expr.Binary;
import parser.Expr.Grouping;
import scanner.Token;
import scanner.Token.TokenType;

public class Interpreter implements Expr.Visitor<Value> {

    public Value evaluate(Expr expr) {
        return expr.accept(this);
    }

    public Value visitLiteral(Literal lit) {
        try {
            return Value.fromObject(lit.value);        
        } catch (TypeError e) {
            return null;
        }
    }

    public Value visitUnary(Unary unary) {
        Value value = unary.expr.accept(this);
        try {
            switch (unary.operator.getType()) {
                case NOT:
                    return Value.not(value);
                case MINUS:
                    return Value.negate(value);
                default:
                    return null;
            }
        } catch (TypeError e) {
            return null;
        }
    } 

    public Value visitBinary(Binary binary) {
        // process left and right first
        Value left = binary.left.accept(this);
        Value right = binary.right.accept(this);
        
        switch (binary.operator.getType()) {
            case PLUS: 
                if ((left instanceof Value.Number) && (right instanceof Value.Number)) {
                    return new Value.Number(((Value.Number) left).val + ((Value.Number) right).val);
                } else if ((left instanceof Value.String) && (right instanceof Value.String)) {
                    return new Value.String(((Value.String) left).val + ((Value.String) right).val);
                } else {
                    return null;
                }
            case MINUS: 
                if (left instanceof Value.Number && right instanceof Value.Number) {
                    return new Value.Number(((Value.Number) left).val - ((Value.Number) right).val);
                } else {
                    return null;
                }
            case STAR: 
                if (left instanceof Value.Number && right instanceof Value.Number) {
                    return new Value.Number(((Value.Number) left).val * ((Value.Number) right).val);
                } else {
                    return null;
                }
            case SLASH: 
                if (left instanceof Value.Number && right instanceof Value.Number) {
                    if (((Value.Number) right).val == 0) {
                        return null;
                    }
                    return new Value.Number(((Value.Number) left).val / ((Value.Number) right).val);
                } else {
                    return null;
                }
            case EQUAL_EQUAL:
                return Value.isEqual(left, right);
            default:    
                return null;            
        }
    }

    public Value visitGrouping(Grouping gr) {
        return gr.expr.accept(this);
    }
}