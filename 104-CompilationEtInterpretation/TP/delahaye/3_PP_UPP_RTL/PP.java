// PP.java

import java.util.*;

/*********/
/* Types */
/*********/

abstract class Type {}//Type

class Int extends Type {}//Int

class Bool extends Type {}//Bool

class Array extends Type {

    Type elements;

    Array (Type elements) {
        this.elements = elements;
    }//Array

}//Array

/**************************************/
/* Arithmetic and boolean expressions */
/**************************************/

abstract class PPExpr {

    abstract UPPExpr toUPP (ArrayList<String> locals);

}//PPExpr

class PPCte extends PPExpr {

    int val;

    PPCte (int val) {
        this.val = val;
    }//PPCte

    UPPExpr toUPP(ArrayList<String> locals) {
        return new UPPCte(val);
    }//toUPP

}//PPCte

class PPTrue extends PPExpr {

    UPPExpr toUPP(ArrayList<String> locals) {
        return new UPPTrue();
    }//toUPP

}//PPTrue

class PPFalse extends PPExpr {

    UPPExpr toUPP(ArrayList<String> locals) {
        return new UPPFalse();
    }//toUPP

}//PPFalse

class PPVar extends PPExpr {

    String name;

    PPVar (String name) {
        this.name = name;
    }//PPVar

    UPPExpr toUPP(ArrayList<String> locals) {
        if(locals.contains(name)) {return new UPPVar(name);}
        else {return new UPPGVar(name);}
    }//toUPP

}//PPVar

abstract class PPUnOp extends PPExpr {

    PPExpr e;

}//PPUnOp

class PPInv extends PPUnOp {

    PPInv (PPExpr e) {
        this.e = e;
    }//PPInv

    UPPExpr toUPP(ArrayList<String> locals) {
      UPPExpr zero = new UPPCte(0);
        return new UPPSub(zero,e.toUPP(locals));
    }//toUPP
}//PPInv

class PPNot extends PPUnOp {

    PPNot (PPExpr e) {
        this.e = e;
    }//PPNot

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne = e.toUPP(locals);
        return new UPPNot(ne);
    }//toUPP

}//PPNot

abstract class PPBinOp extends PPExpr {

    PPExpr e1, e2;

}//PPBinOp

class PPAdd extends PPBinOp {

    PPAdd (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPAdd

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPAdd(ne1,ne2);
    }//toUPP

}//PPAd

class PPSub extends PPBinOp {

    PPSub (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPSub

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPSub(ne1,ne2);
    }//toUPP

}//PPSub

class PPMul extends PPBinOp {

    PPMul (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPMul

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPMul(ne1,ne2);
    }//toUPP

}//PPMul

class PPDiv extends PPBinOp {

    PPDiv (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPDiv

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPDiv(ne1,ne2);
    }//toUPP

}//PPDiv

class PPAnd extends PPBinOp {

    PPAnd (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPAnd

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPAnd(ne1,ne2);
    }//toUPP

}//PPAnd

class PPOr extends PPBinOp {

    PPOr (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPOr

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPOr(ne1,ne2);
    }//toUPP

}//PPOr

class PPLt extends PPBinOp {

    PPLt (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPLt

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPLt(ne1,ne2);
    }//toUPP

}//PPLt

class PPLe extends PPBinOp {

    PPLe (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPLe

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr ne1 = e1.toUPP(locals);
        UPPExpr ne2 = e2.toUPP(locals);
        return new UPPLe(ne1,ne2);
    }//toUPP

}//PPLe

class PPEq extends PPBinOp {

    PPEq (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPEq

    UPPExpr toUPP(ArrayList<String> locals) {
      UPPExpr ne1 = e1.toUPP(locals);
      UPPExpr ne2 = e2.toUPP(locals);
      return new UPPEq(ne1,ne2);
    }//toUPP

}//PPEq

class PPNe extends PPBinOp {

    PPNe (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPNe

    UPPExpr toUPP(ArrayList<String> locals) {
      UPPExpr ne1 = e1.toUPP(locals);
      UPPExpr ne2 = e2.toUPP(locals);
      return new UPPNe(ne1,ne2);
    }//toUPP

}//PPNe

class PPGe extends PPBinOp {

    PPGe (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPGe

    UPPExpr toUPP(ArrayList<String> locals) {
      UPPExpr ne1 = e1.toUPP(locals);
      UPPExpr ne2 = e2.toUPP(locals);
      return new UPPGe(ne1,ne2);
    }//toUPP

}//PPGe

class PPGt extends PPBinOp {

    PPGt (PPExpr e1, PPExpr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }//PPGt

    UPPExpr toUPP(ArrayList<String> locals) {
      UPPExpr ne1 = e1.toUPP(locals);
      UPPExpr ne2 = e2.toUPP(locals);
      return new UPPGt(ne1,ne2);
    }//toUPP

}//PPGt

abstract class Callee {}//Callee

class Read extends Callee {}//Read

class Write extends Callee {}//Write

class User extends Callee {

    String name;

    User (String name) {
        this.name = name;
    }//User

}//User

class PPFunCall extends PPExpr {

    Callee callee;
    ArrayList<PPExpr> args;

    PPFunCall (Callee callee, ArrayList<PPExpr> args) {
        this.callee = callee;
        this.args = args;
    }//FunCall

    UPPExpr toUPP(ArrayList<String> locals) {
        ArrayList<UPPExpr> nargs = new ArrayList<UPPExpr>();
        for (PPExpr e : args)
            nargs.add(e.toUPP(locals));
        return new UPPFunCall(callee,nargs);
    }//toUPP

}//FunCall

class PPArrayGet extends PPExpr {

    PPExpr arr, index;

    PPArrayGet (PPExpr arr, PPExpr index) {
        this.arr = arr;
        this.index = index;
    }//PPArrayGet

    UPPExpr toUPP(ArrayList<String> locals) {
      UPPExpr tab = arr.toUPP(locals);
      UPPExpr taille = new UPPCte(4);
      UPPExpr decalage = new UPPMul(taille,index.toUPP(locals));
      return new UPPAdd(tab,decalage);
    }//toUPP

}//PPArrayGet

class PPArrayAlloc extends PPExpr {

    Type type;
    PPExpr size;

    PPArrayAlloc (Type type, PPExpr size) {
        this.type = type;
        this.size = size;
    }//PPArrayAlloc

    UPPExpr toUPP(ArrayList<String> locals) {
        UPPExpr nsize = size.toUPP(locals);
        UPPExpr sizeBytes = new UPPMul(new UPPCte(4),nsize);
        ArrayList<UPPExpr> args = new ArrayList<UPPExpr>();
        args.add(sizeBytes);
        return new UPPFunCall(new Alloc(),args);
    }//toUPP

}//PPArrayAlloc

/****************/
/* Instructions */
/****************/

abstract class PPInst {

    abstract UPPInst toUPP (ArrayList<String> locals);

}//PPInst

class PPAssign extends PPInst {

    String name;
    PPExpr val;

    PPAssign (String name, PPExpr val) {
        this.name = name;
        this.val = val;
    }//PPAssign

    UPPInst toUPP (ArrayList<String> locals) {
        UPPExpr nval = val.toUPP(locals);
        return new UPPAssign(name,nval);
    }//toUPP

}//PPAssign

class PPArraySet extends PPInst {

    PPExpr arr, index, val;

    PPArraySet (PPExpr arr, PPExpr index, PPExpr val) {
        this.arr = arr;
        this.index = index;
        this.val = val;
    }//PPArraySet

    UPPInst toUPP(ArrayList<String> locals) {
      UPPExpr tab = arr.toUPP(locals);
      UPPExpr taille = new UPPCte(4);
      UPPExpr decalage = new UPPMul(taille,index.toUPP(locals));
      UPPExpr addr = new UPPAdd(tab,decalage);
      return new UPPStore(addr,val.toUPP(locals));
    }//toUPP

}//PPArraySet

class PPCond extends PPInst {

    PPExpr cond;
    PPInst i1, i2;

    PPCond (PPExpr cond, PPInst i1, PPInst i2) {
        this.cond = cond;
        this.i1 = i1;
        this.i2 = i2;
    }//PPCond

    UPPInst toUPP (ArrayList<String> locals) {
        UPPExpr ncond = cond.toUPP(locals);
        UPPInst ni1 = i1.toUPP(locals), ni2 = i2.toUPP(locals);
        return new UPPCond(ncond,ni1,ni2);
    }//toUPP

}//PPCond

class PPWhile extends PPInst {

    PPExpr cond;
    PPInst i;

    PPWhile (PPExpr cond, PPInst i) {
        this.cond = cond;
        this.i = i;
    }//PPWhile

    UPPInst toUPP (ArrayList<String> locals) {
        UPPExpr c = cond.toUPP(locals);
        UPPInst ins = i.toUPP(locals);
        return new UPPWhile(c, ins);
    }//toUPP

}//PPWhile

class PPProcCall extends PPInst {

    Callee callee;
    ArrayList<PPExpr> args;

    PPProcCall (Callee callee, ArrayList<PPExpr> args) {
        this.callee = callee;
        this.args = args;
    }//PPProcCall

    UPPInst toUPP (ArrayList<String> locals) {
        ArrayList<UPPExpr> argsU = new ArrayList<UPPExpr>();
        for(int i=0; i<args.size();i++){
          argsU.add(args.get(i).toUPP(locals));
        }
        return new UPPProcCall(callee,argsU);
    }//toUPP

}//PPProcCall

class PPSkip extends PPInst {

    UPPInst toUPP (ArrayList<String> locals) {
        return new UPPSkip();
    }//toUPP

}//PPSkip

class PPSeq extends PPInst {

    PPInst i1, i2;

    PPSeq (PPInst i1, PPInst i2) {
        this.i1 = i1;
        this.i2 = i2;
    }//PPSeq

    UPPInst toUPP (ArrayList<String> locals) {
        UPPInst ni1 = i1.toUPP(locals), ni2 = i2.toUPP(locals);
        return new UPPSeq(ni1,ni2);
    }//toUPP

}//PPSeq

/***************************************/
/* Definitions of functions/procedures */
/***************************************/

class Pair<L,R> {

    final L left;
    final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }//Pair

    static <L,R> Pair<L,R> of(L left, R right){
        return new Pair<L,R>(left, right);
    }//of

}//Pair

abstract class PPDef {

    String name;
    ArrayList<Pair<String,Type>> args, locals;
    PPInst code;

    abstract UPPDef toUPP ();

}//PPDef

class PPFun extends PPDef {

    Type ret;

    PPFun (String name, ArrayList<Pair<String,Type>> args,
         ArrayList<Pair<String,Type>> locals, PPInst code, Type ret) {
        this.name = name;
        this.args = args;
        this.locals = locals;
        this.code = code;
        this.ret = ret;
    }//PPFun

    UPPDef toUPP () {
        ArrayList<String> nargs = new ArrayList<String>();
        ArrayList<String> nlocals = new ArrayList<String>();
        ArrayList<String> nall = new ArrayList<String>();
        UPPInst ncode;
        for (Pair<String,Type> e : args) {
            nargs.add(e.left);
            nall.add(e.left);
        }//for
        nlocals.add(name);
        nall.add(name);
        for (Pair<String,Type> e : locals) {
            nlocals.add(e.left);
            nall.add(e.left);
        }//for
        ncode = code.toUPP(nall);
        return new UPPFun(name,nargs,nlocals,ncode);
    }//toUPP

}//PPFun

class PPProc extends PPDef {

    PPProc (String name, ArrayList<Pair<String,Type>> args,
            ArrayList<Pair<String,Type>> locals, PPInst code) {
        this.name = name;
        this.args = args;
        this.locals = locals;
        this.code = code;
    }//PPProc

    UPPDef toUPP () {
        //name ok
        ArrayList<String> argsU = new ArrayList<String>();
        ArrayList<String> localsU = new ArrayList<String>();
        ArrayList<String> localsLeft = new ArrayList<String>();

        for(Pair<String,Type> e : locals){
          localsLeft.add(e.left);
        }

        UPPInst codeU = code.toUPP(localsLeft);
        for (Pair<String,Type> e : args){
            argsU.add(e.left);
        }
        for (Pair<String,Type> e : locals){
            localsU.add(e.left);
        }


        return new UPPProc(name,argsU,localsU,codeU);

    }//toUPP

}//PPProc

/************/
/* Programs */
/************/

class PPProg {

    ArrayList<Pair<String,Type>> globals;
    ArrayList<PPDef> defs;
    PPInst code;

    PPProg (ArrayList<Pair<String,Type>> globals, ArrayList<PPDef> defs,
          PPInst code) {
        this.globals = globals;
        this.defs = defs;
        this.code = code;
    }//PPProg

    UPPProg toUPP () {
        ArrayList<String> nglobals = new ArrayList<String>();
        ArrayList<UPPDef> ndefs = new ArrayList<UPPDef>();
        UPPInst ncode;
        for (Pair<String,Type> e : globals)
            nglobals.add(e.left);
        for (PPDef e : defs)
            ndefs.add(e.toUPP());
        ncode = code.toUPP(new ArrayList<String>());
        return new UPPProg(nglobals,ndefs,ncode);
    }//toUPP

}//PPProg
