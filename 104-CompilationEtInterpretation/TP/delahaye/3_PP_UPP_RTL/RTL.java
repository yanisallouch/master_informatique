// RTL.java

/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	18 octobre 2020
*/

import java.util.*;

/**********/
/* Labels */
/**********/

class Label {

    private static int count = 0;
    int lab;

    Label () {
        lab = count;
        count++;
    }//Label

    int getLabel () {
        return lab;
    }//getLabel

    public String toString () {
        return "f" + lab;
    }//toString

    static void reset () {
        count = 0;
    }//reset

}//Label

/********************/
/* Pseudo-registers */
/********************/

class PRegister {

    private static int count = 0;
    int reg;

    PRegister () {
        reg = count;
        count++;
    }//Label

    int getPRegister () {
        return reg;
    }//getPRegister

    public String toString () {
        return "%" + reg;
    }//toString

    static void reset () {
        count = 0;
    }//reset

}//PRegister

/**********************/
/* Control flow graph */
/**********************/

abstract class RTLInst {

    Label lab;

}//RTLInst

// This is a leaf (which closes a branch of the graph).
class RTLEnd extends RTLInst {}//RTLEnd

class RTLCte extends RTLInst {

    PRegister reg;
    int imm;
    RTLInst succ;

    RTLCte (PRegister reg, int imm, RTLInst succ) {
        this.reg = reg;
        this.imm = imm;
        this.succ = succ;
    }//RTLCte

}//RTLCte

/* We deal only with global variables.
   Local variables are pseudo-registers. */
abstract class RTLVar extends RTLInst {

    String name;
    RTLInst succ;
    PRegister reg;

}//RTLVar

class RTLGetGVar extends RTLVar {

    RTLGetGVar (PRegister reg, String name, RTLInst succ) {
        this.reg = reg;
        this.name = name;
        this.succ = succ;
    }//RTLGetGVar

}//RTLGetGVar

class RTLSetGVar extends RTLVar {
    RTLSetGVar (String name, PRegister reg, RTLInst succ) {
        this.name = name;
        this.reg = reg;
        this.succ = succ;
    }//RTLSetGVar
}//RTLSetGVar

abstract class RTLUnOp extends RTLInst {

    PRegister regIn, regOut;
    RTLInst succ;

}//RTLUnOp

/* Used for the "not" operation
   not(e) = xori %1, %0, 1 */
class RTLXOri extends RTLUnOp {
    RTLXOri (PRegister regIn, PRegister regOut, RTLInst succ) {
        this.regIn = regIn;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLXOri
}//RTLXOri

// Used for the assignment instruction.
class RTLMove extends RTLUnOp {

    RTLMove (PRegister regIn, PRegister regOut, RTLInst succ) {
        this.regIn = regIn;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLMove

}//RTLMove

// Used for all binary operations except relations
abstract class RTLBinOp extends RTLInst{

    PRegister regIn1, regIn2, regOut;
    RTLInst succ;

}//RTLBinOp

class RTLAdd extends RTLBinOp {
    RTLAdd (PRegister regIn1, PRegister regIn2, PRegister regOut,
             RTLInst succ) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLAdd
}//RTLAdd

class RTLSub extends RTLBinOp {

    RTLSub (PRegister regIn1, PRegister regIn2, PRegister regOut,
             RTLInst succ) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLSub

}//RTLSub

class RTLMul extends RTLBinOp {

    RTLMul (PRegister regIn1, PRegister regIn2, PRegister regOut,
             RTLInst succ) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLMul

}//RTLMul

class RTLDiv extends RTLBinOp {

    RTLDiv (PRegister regIn1, PRegister regIn2, PRegister regOut,
             RTLInst succ) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLDiv

}//RTLDiv

class RTLAnd extends RTLBinOp {

    RTLAnd (PRegister regIn1, PRegister regIn2, PRegister regOut,
             RTLInst succ) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLAnd

}//RTLAnd

class RTLOr extends RTLBinOp {

    RTLOr (PRegister regIn1, PRegister regIn2, PRegister regOut,
             RTLInst succ) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLOr

}//RTLOr

// We marge function and procedure calls
abstract class RTLCall extends RTLInst {

    Callee callee;
    ArrayList<PRegister> args;
    RTLInst succ;

}//RTLCall

class RTLFunCall extends RTLCall {

    PRegister reg;

    RTLFunCall (Callee callee, ArrayList<PRegister> args, PRegister reg,
                RTLInst succ) {
        this.callee = callee;
        this.args = args;
        this.reg = reg;
        this.succ = succ;
    }//RTLFunCall

}//RTLFunCall

class RTLProcCall extends RTLCall {

    RTLProcCall (Callee callee, ArrayList<PRegister> args, RTLInst succ) {
        this.callee = callee;
        this.args = args;
        this.succ = succ;
    }//RTLProcCall

}//RTLProcCall

/* For arrays, the address is computed with the offset.
   Therefore, there is no offset variable. */
abstract class RTLArray extends RTLInst {

    PRegister addr;
    RTLInst succ;

}//RTLArray

class RTLLoad extends RTLArray {

    PRegister regOut;

    RTLLoad (PRegister addr, PRegister regOut, RTLInst succ) {
        this.addr = addr;
        this.regOut = regOut;
        this.succ = succ;
    }//RTLLoad

}//RTLLoad

class RTLStore extends RTLArray {

    PRegister regIn;

    RTLStore (PRegister addr, PRegister regIn, RTLInst succ) {
        this.addr = addr;
        this.regIn = regIn;
        this.succ = succ;
    }//RTLStore

}//RTLStore

abstract class RTLUnBranch extends RTLInst {

    PRegister regIn;
    RTLInst succ1, succ2;

}//RTLUnBranch

class RTLGtz extends RTLUnBranch {

    RTLGtz (PRegister regIn, RTLInst succ1, RTLInst succ2) {
        this.regIn = regIn;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLGtz

}//RTLGtz

abstract class RTLBinBranch extends RTLInst {

    PRegister regIn1, regIn2;
    RTLInst succ1, succ2;

}//RTLBinBranch

class RTLLt extends RTLBinBranch {

    RTLLt (PRegister regIn1, PRegister regIn2, RTLInst succ1, RTLInst succ2) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLLt

}//RTLLt

class RTLLe extends RTLBinBranch {

    RTLLe (PRegister regIn1, PRegister regIn2, RTLInst succ1, RTLInst succ2) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLLe

}//RTLLe

class RTLEq extends RTLBinBranch {

    RTLEq (PRegister regIn1, PRegister regIn2, RTLInst succ1, RTLInst succ2) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLEq

}//RTLEq

class RTLNe extends RTLBinBranch {

    RTLNe (PRegister regIn1, PRegister regIn2, RTLInst succ1, RTLInst succ2) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLNe

}//RTLNe

class RTLGe extends RTLBinBranch {

    RTLGe (PRegister regIn1, PRegister regIn2, RTLInst succ1, RTLInst succ2) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLGe

}//RTLGe

class RTLGt extends RTLBinBranch {

    RTLGt (PRegister regIn1, PRegister regIn2, RTLInst succ1, RTLInst succ2) {
        this.regIn1 = regIn1;
        this.regIn2 = regIn2;
        this.succ1 = succ1;
        this.succ2 = succ2;
    }//RTLGt

}//RTLGt

/***************************************/
/* Definitions of functions/procedures */
/***************************************/

/* The "locals" variable includes all the pseudo-registers, i.e. those of "args"
   and the pseudo-register used for returning a value (if the definition is a
   definition of function) as well. */
abstract class RTLDef {

    String name;
    ArrayList<PRegister> args, locals;
    RTLInst body;

}//RTLDef

class RTLFun extends RTLDef {

    PRegister regRet;

    RTLFun (String name, ArrayList<PRegister> args, ArrayList<PRegister> locals,
            PRegister regRet, RTLInst body) {
        this.name = name;
        this.args = args;
        this.locals = locals;
        this.regRet = regRet;
        this.body = body;
    }//RTLFun

}//RTLFun

class RTLProc extends RTLDef {

    RTLProc (String name, ArrayList<PRegister> args,
             ArrayList<PRegister> locals, RTLInst body) {
        this.name = name;
        this.args = args;
        this.locals = locals;
        this.body = body;
    }//RTLProc

}//RTLProc

/************/
/* Programs */
/************/

// The code of the program is considered as a procedure whose name is "_main".
class RTLProg {

    ArrayList<String> globals;
    ArrayList<RTLDef> defs;

    RTLProg (ArrayList<String> globals, ArrayList<RTLDef> defs) {
        this.globals = globals;
        this.defs = defs;
    }//RTLProg

}//RTLProg
