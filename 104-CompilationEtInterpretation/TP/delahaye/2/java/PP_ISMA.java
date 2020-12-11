class Programme {
	AffecVar v;
	ArrayList < Procedure > p;
	Instruction i;

	public Programme(Instruction i) {
		this.v = null;
		this.p = null;
		this.i = i;
	}

	public Programme(AffecVar v, Instruction i) {
		this.v = v;
		this.p = null;
		this.i = i;
	}

	public Programme(ArrayList < Procedure > p, Instruction i) {
		this.v = null;
		this.p = p;
		this.i = i;
	}

	public Programme(AffecVar v, ArrayList < Procedure > p, Instruction i) {
		this.v = v;
		this.p = p;
		this.i = i;
	}

	public void print() {
		this.v.print();
		String args = "";
		for (int i = 0; i < this.p.size(); i++) {
			args += p.get(i).toString();
		}
		System.out.print(args);
		this.i.print();
	}
}

class Procedure {
	String n;
	ArrayList < AffecFun > funs;
	Type t;
	ArrayList < AffecVar > vars;
	Instruction i;

	//all
	public Procedure(String n, ArrayList < AffecFun > funs, Type t, ArrayList < AffecVar > vars, Instruction i) {
		this.n = n;
		this.funs = funs;
		this.t = t;
		this.vars = vars;
		this.i = i;
	}

	//no args
	public Procedure(String n, Type t, ArrayList < AffecVar > vars, Instruction i) {
		this.n = n;
		this.funs = null;
		this.t = t;
		this.vars = vars;
		this.i = i;
	}

	//no type
	public Procedure(String n, ArrayList < AffecFun > funs, ArrayList < AffecVar > vars, Instruction i) {
		this.n = n;
		this.funs = funs;
		this.t = null;
		this.vars = vars;
		this.i = i;
	}

	//no types no args
	public Procedure(String n, ArrayList < AffecVar > vars, Instruction i) {
		this.n = n;
		this.funs = null;
		this.t = null;
		this.vars = vars;
		this.i = i;
	}

	//no vars
	public Procedure(String n, ArrayList < AffecFun > funs, Type t, Instruction i) {
		this.n = n;
		this.funs = funs;
		this.t = t;
		this.vars = null;
		this.i = i;
	}

	//no vars no args
	public Procedure(String n, Type t, Instruction i) {
		this.n = n;
		this.funs = null;
		this.t = t;
		this.vars = null;
		this.i = i;
	}

	//no vars no type
	public Procedure(String n, Instruction i, ArrayList < AffecFun > funs) {
		this.n = n;
		this.funs = funs;
		this.t = null;
		this.vars = null;
		this.i = i;
	}

	//no vars no type no args
	public Procedure(String n, Instruction i) {
		this.n = n;
		this.funs = null;
		this.t = null;
		this.vars = null;
		this.i = i;
	}
}


abstract class Type {
	abstract void print();
}

class TypeInt extends Type {
	public void print() {
		System.out.print("integer ");
	}
}

class TypeBool extends Type {
	public void print() {
		System.out.print("boolean ");
	}
}

class TypeArray extends Type {
	protected Type t;
	public TypeArray(Type t) {
		this.t = t;
	}
	public void print() {
		System.out.print("array of ");
		t.print();
	}
}

abstract class Instruction {
	abstract void print();
	}
	class Affec extends Instruction {
	protected String v;
	protected Expr e;
	public Affec(String v, Expr e) {
		this.v = v;
		this.e = e;
	}
	public void print() {
		System.out.print(this.v);
		System.out.print(" := ");
		e.print();
	}
}

class AffecFun extends Instruction {
	String n;
	Type t;

	public AffecFun(String n, Type t) {
		this.n = n;
		this.t = t;
	}

	public void print() {
		System.out.print(this.n + ": ");
		this.t.print();
	}
}

class AffecVar extends Instruction {
	ArrayList < AffecFun > af;

	public AffecVar(ArrayList < AffecFun > af) {
		this.af = af;
	}

	public void print(){
	};

}

class AffecArray extends Instruction {
	protected Expr e1;
	protected Expr e2;
	protected Expr e3;
	public AffecArray(Expr e1, Expr e2, Expr e3) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}
	public void print() {
		e1.print();
		System.out.print("[");
		e2.print();
		System.out.print("]");
		System.out.print(" := ");
		e3.print();
	}
}

class IfThenElse extends Instruction {
	protected Expr e;
	protected Instruction i;
	protected Instruction i2;
	public IfThenElse(Expr e, Instruction i, Instruction i2) {
		this.e = e;
		this.i = i;
		this.i2 = i2;
	}
	public void print() {
		System.out.print("If ");
		e.print();
		System.out.print("Then");
		i.print();
		System.out.print("Else");
		i2.print();
	}
}

class DoWhile extends Instruction {
	protected Expr e;
	protected Instruction i;
	public DoWhile(Expr e, Instruction i) {
		this.e = e;
		this.i = i;
	}
	public void print() {
		System.out.print("While");
		e.print();
		System.out.print("do");
		i.print();
	}
}

class Skip extends Instruction {
	public void print() {
		System.out.print("skip");
	}
}

class SequenceInstruction extends Instruction {
	protected Instruction i;
	protected Instruction i2;
	public SequenceInstruction(Instruction i, Instruction i2) {
		this.i = i;
		this.i2 = i2;
	}
	public void print() {
		this.i.print();
		System.out.print(";");
		this.i2.print();
	}
}

abstract class Expr {
	public abstract void print();
}

abstract class Cst extends Expr {
	public abstract void print();
}

/* CST */
class CstInt extends Cst {
	protected String n;
	public CstInt(String n) {
		this.n = n;
	}
	public void print() {
		System.out.print(this.n + " ");
	}
}

class CstTrue extends Cst {
	public void print() {
		System.out.print("true ");
	}
}

class CstFalse extends Cst {
	public void print() {
		System.out.print("false ");
	}
}

/* UNAIRE */
abstract class Uop extends Expr {
	protected Expr e1;
	public abstract void print();
}

class UMinus extends Uop {
	public UMinus(Expr e1) {
		this.e1 = e1;
	}
	public void print() {
		System.out.print(" -");
		e1.print();
	}
}

class UNot extends Uop {
	public UNot(Expr e1) {
		this.e1 = e1;
	}
	public void print() {
		System.out.print(" not ");
		e1.print();
	}
}

/* BINAIRE */
abstract class Bop extends Expr {
	protected Expr e1;
	protected Expr e2;
}

class BAdd extends Bop {
	public BAdd(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" + ");
		e2.print();
	}
}

class BSub extends Bop {
	public BSub(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" - ");
		e2.print();
	}
}

class BMul extends Bop {
	public BMul(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		System.out.print(" * ");
	}
}

class BDiv extends Bop {
	public BDiv(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		System.out.print(" / ");
	}
}

class BAnd extends Bop {
	public BAnd(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" and ");
		e2.print();
	}
}

class BOr extends Bop {
	public BOr(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" or ");
		e2.print();
	}
}

class BInf extends Bop {
	public BInf(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" < ");
		e2.print();
	}
}

class BInfEg extends Bop {
	public BInfEg(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" <= ");
		e2.print();
	}
}

class BEg extends Bop {
	public BEg(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" = ");
		e2.print();
	}
}

class BNoEg extends Bop {
	public BNoEg(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" != ");
		e2.print();
	}
}

class BSupEg extends Bop {
	public BSupEg(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" >= ");
		e2.print();
	}
}

class BSup extends Bop {
	public BSup(Expr e1, Expr e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public void print() {
		e1.print();
		System.out.print(" > ");
		e2.print();
	}
}

/* APP FUNCTION */
class AppelFonction extends Expr {
	protected App a;
	protected ArrayList < Expr > args;

	public AppelFonction(App a) {
		this.a = a;
		this.args = null;
	}

	public AppelFonction(App a, ArrayList < Expr > args) {
		this.a = a;
		this.args = args;
	}
	public void print() {
		String s = "";
		for (Expr e: args)
			s += e.toString().toString();
		this.a.print();
		System.out.print("(" + s + ")");
	}
}

/* ARRAY OP */
class ArrayGet extends Expr {
	protected Expr array;
	protected Expr i;
	public ArrayGet(Expr array, Expr i) {
		this.array = array;
		this.i = i;
	}
	public void print() {
		array.print();
		System.out.print("[");
		i.print();
		System.out.print("]");
	}
}

class ArrayGetI extends Instruction {
	protected Expr array;
	protected Expr i;
	public ArrayGetI(Expr array, Expr i) {
		this.array = array;
		this.i = i;
	}
	public void print() {
		array.print();
		System.out.print("[");
		i.print();
		System.out.print("]");
	}
}

class NewArray extends Expr {
	protected Type t;
	protected Expr s;
	public NewArray(Type t, Expr s) {
		this.t = t;
		this.s = s;
	}
	public void print() {
		System.out.print("New array of ");
		t.print();
		System.out.print("[");
		s.print();
		System.out.print("]");
	}
}

abstract class App {
	abstract public void print();
}

class Read extends App {
	public void print() {
		System.out.print("Read");
	}
}

class Write extends App {
	public void print() {
		System.out.print("Write");
	}
}

class Fun extends App {
	public String n;
	public Fun(String n) {
		this.n = n;
	}
	public void print() {
		System.out.print(this.n);
	}
}
