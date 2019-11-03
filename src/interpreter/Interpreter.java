package interpreter;


import java.util.HashMap;

import parser.Expr;
import parser.Expr.*;
import parser.Stmt.*;
import parser.Stmt;
import scanner.Token;
import scanner.Token.TokenType;

public class Interpreter implements Expr.Visitor<Value>, Stmt.Visitor<Void> {

    private HashMap<String, Value> env = new HashMap<>();

    public Void evaluate(Stmt stmt) {
        return stmt.accept(this);
    }

    public Value evaluate(Expr expr) {
        return expr.accept(this);
    }

    public Void visitDeclaration(Declaration declStmt) {
        Value val = evaluate(declStmt.value);
        env.put(declStmt.name, val);
        return null;
    }

    public Void visitPrint(Print print) {
        Value val = evaluate(print.value);
        System.out.println(val);
        return null;
    }

    public Void visitBlock(Block block) {
        for (Stmt s: block.stmts) {
            evaluate(s);
        }
        return null;
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

    public Value visitIdent(Ident ident) {
        if (env.containsKey(ident.name)) {
            return env.get(ident.name);
        } else {
            return null;
        }
    }
}