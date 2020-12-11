/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	04 octobre 2020
*/
import java.util.*;

abstract class PP {
	public abstract void print();
}

class Constante extends PP {
	//If a boolean the value will be different than 0
	public Integer value;

	public Constante (int value){
		this.value = value;
	}

	public void print(){
		System.out.println(value);
	}
}

class ID extends PP {
	public String value;

	public ID (String value){
		this.value = value;
	}

	public void print(){
		System.out.print(this.value);
	}
}

class Expressions extends PP {
	public Expressions e1, e2;

	public Expressions(Expressions e1, Expressions e2){
		this.e1 = e1;
		this.e2 = e2;
	}
	public Expressions(Expressions element){
		this.e1 = element;
		this.e2 = null;
	}
	public Expressions(){
		this.e1 = null;
		this.e2 = null;
	}
	public void print(){
		e1.print();
		if(e2 != null){
			e2.print();
		}
	}
}

enum Types {
	INTEGER ("integer"), BOOLEAN ("boolean"), ARRAY ("array of");
 	public final String label;
	private Types(String label) {
		this.label = label;
	}
}

class Type extends PP {
	public Types type;
	public Type arrayOF;

	public Type (Types type){
		this.type = type;
	}
	public Type (Type arrayOF){
		this.arrayOF = arrayOF;
	}
	public Type (Types type, Type arrayOF){
		this.type = type;
		this.arrayOF = arrayOF;
	}
	public void print(){
		if(this.arrayOF != null){
			System.out.println(type); // supposé "ARRAY OF ..."
			arrayOF.print(); // on complete "..."
		}
		System.out.println(type.label);
	}
}

class Variable extends PP{
	public ID name;
	public Type type;
	Variable(ID name, Type type) {
		this.name = name;
		this.type = type;
	}

	public void print(){
		System.out.print("var ");
		this.name.print();
		System.out.print(" : ");
		this.type.print();
		System.out.print("\n");
	}
}

class Argument extends PP{
	public ID name;
	public Type type;
	Argument (ID name, Type type) {
		this.name = name;
		this.type = type;
	}
	public void print(){
		this.name.print();
		System.out.print(" : ");
		this.type.print();
	}
}

class DefFonctionProcedure extends PP{
	public ID nomDeFonctionProcedure;
	public ArrayList<Argument> listeArguments;
	public Type type;
	public Instruction instructions;
	DefFonctionProcedure(ID id, ArrayList<Argument> args, Type type, Instruction inst){
		this.nomDeFonctionProcedure = id;
		this.listeArguments = args;
		this.type = type;
		this.instructions = inst;
	}
	public void print(){
		this.nomDeFonctionProcedure.print();
		System.out.print(" ( ");
		String tmpArg = "";
		for (int i = 0; i < listeArguments.size(); i++){
			Argument e = listeArguments.get(i);
			e.print();
			if(i < listeArguments.size() - 1 ) {
				System.out.print(",");
			}
		}
		System.out.print(" ) ");
		String tmpType = "";
		if (type != null){
			this.type.print();
		}
		instructions.print();
	}
}

class Instruction extends PP{
	public void print(){
	}
}

class BlocIfThenElse extends Instruction{
	public Expressions cond;
	public Instruction blocTrue;
	public Instruction blocFalse;

	public BlocIfThenElse(Expressions cond, Instruction blocTrue, Instruction blocFalse){
		this.cond = cond;
		this.blocTrue = blocTrue;
		this.blocFalse = blocFalse;
	}
	public BlocIfThenElse(Expressions cond, Instruction blocTrue){
		this.cond = cond;
		this.blocTrue = blocTrue;
		this.blocFalse = new NoOp();
	}
	public void print(){
		System.out.print("if (");
		this.cond.print();
		System.out.println(" ) then {");
		System.out.print("	");
		this.blocTrue.print();
		System.out.println("} else {");
		System.out.print("	");
		this.blocFalse.print();
		System.out.println("}");
	}
}

abstract class Boucle extends Instruction{
	public Expressions cond;
	public Instruction bloc;
	Boucle (Expressions cond, Instruction bloc){
		this.cond = cond;
		this.bloc = bloc;
	}
}

class While extends Boucle {
	While(Expressions cond, Instruction bloc){
		super(cond, bloc);
	}
	public void print(){
		System.out.println("While (");
		this.cond.print();
		System.out.println(") {");
		System.out.print("	");
		this.bloc.print();
		System.out.println("}");
	}
}

class DeuxInstructions extends Instruction{
	public Instruction ins1;
	public Instruction ins2;
	DeuxInstructions (Instruction ins1, Instruction ins2){
		this.ins1 = ins1;
		this.ins2 = ins2;
	}
	public void print(){
		ins1.print();
		System.out.println(" ;\n");
		ins2.print();
	}
}

class Affectation extends Instruction{
	public Expressions val;
	public ID var;
	Affectation (Expressions val, ID var){
		this.val = val;
		this.var = var;
	}
	public void print(){
		var.print();
		System.out.print(" := ");
		val.print();
	}
}

class NoOp extends Instruction{
	//do nothing
	public void print(){
		System.out.print("skip");
	}
}

class AffectationElementTab extends Instruction{
	public Expressions val;
	public Expressions var;
	public Expressions index;
	AffectationElementTab(Expressions val, Expressions var, Expressions index){
		this.val = val;
		this.var = var;
		this.index = index;
	}
	public void print(){
		var.print();
		System.out.print("[");
		index.print();
		System.out.print("] := ");
		val.print();
	}
}

class CibleAppel extends Instruction{
	public ID nameFonction;
	CibleAppel(ID nameFonction){
		this.nameFonction	= nameFonction;
	}
	public void print(){
		nameFonction.print();
	}
}

class AppelFonction extends Instruction {
	public CibleAppel cible;
	public ArrayList<Argument> listeArguments;
	AppelFonction(CibleAppel cible, ArrayList<Argument> listeArguments){
		this.cible = cible;
		this.listeArguments = listeArguments;
	}
	public void print(){
		//functioName
		cible.print();
		//argumentList
		System.out.print("(");
		String tmpArg = "";
		for (int i = 0; i < listeArguments.size(); i++){
			Argument e = listeArguments.get(i);
			e.print();
			if(i < listeArguments.size() - 1 ) {
				System.out.print(",");
			}
		}
		System.out.print(")");
	}
}

/*
	public void println(){
		System.out.println( );
	}
*/

class sumE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" + ");
		e2.print();
	}
}

class minusE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" - ");
		e2.print();
	}
}

class prodE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" * ");
		e2.print();
	}
}

class divE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" / ");
		e2.print();
	}
}

class andE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" and ");
		e2.print();
	}
}

class orE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" or ");
		e2.print();
	}
}

class infE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" < ");
		e2.print();
	}
}

class infEqE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" <= ");
		e2.print();
	}
}

class eqE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" == ");
		e2.print();
	}
}

class diffE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" != ");
		e2.print();
	}
}

class suppE extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" >= ");
		e2.print();
	}
}

class supp extends Expressions {
	public void print(){
		e1.print();
		System.out.print(" > ");
		e2.print();
	}
}

class NewArray extends Expressions {
	public Type type;
	public NewArray (Type type, Expressions taille){
		super(taille); // e1 <==> taille
		this.type = type;
	}
	public void print(){
		System.out.print("new array of ");
		type.print();
		System.out.print("[");
		e1.print();
		System.out.print("]");
	}
}

class AccesArray extends Expressions {
	public void print(){
		e1.print();
		System.out.print("[");
		e2.print();
		System.out.print("]");
	}
}

class Programmes extends PP {
	public ArrayList<Variable> variables;
	public ArrayList<DefFonctionProcedure> defintionsFonctionsProcedures;
	public Instruction instructions;



	public void print(){
		//Variables des Programmes
		for (int i = 0; i < variables.size(); i++){
			Variable e = variables.get(i);
			e.print();
			if(i < variables.size() - 1 ) {
				System.out.print(",");
			}
		}
		System.out.println(" \n ");
		//Fonctions des Programmes
		for (int i = 0; i < defintionsFonctionsProcedures.size(); i++){
			DefFonctionProcedure e = defintionsFonctionsProcedures.get(i);
			e.print();
			System.out.print(" \n ");
		}
		//instructions du programme
		instructions.print();
	}
}
