// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S10.1.3_A5.1_T1;
 * @section: 10.1.3;
 * @assertion: For each VariableDeclaration or VariableDeclarationNoIn in the 
 * code, create a property of the variable object whose name is the Identifier 
 * in the VariableDeclaration or VariableDeclarationNoIn, whose value is 
 * undefined and whose attributes are determined by the type of code;
 * @description: Checking variable existence only;
*/

//CHECK#1
function f1(){
  var x;
  
  return typeof x;
}

if(!(f1() === "undefined")){
  $PRINT('#1: f1() === "undefined"');
}

//CHECK#2
function f2(){
  var x;
  
  return x;
}

if(!(f2() === undefined)){
  $PRINT('#1: f2() === undefined');
}
