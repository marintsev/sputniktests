// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S10.2.2_A1.1_T1;
 * @section: 10.2.2;
 * @assertion: The scope chain is initialised to contain the same objects, 
 * in the same order, as the calling context's scope chain;
 * @description: eval within global execution context;
*/

var i;
var j;
str1 = '';
str2 = '';
x = 1;
y = 2;

for(i in this){
  str1+=i;
}

eval('for(j in this){\nstr2+=j;\n}');

if(!(str1 === str2)){
  $ERROR("#1: scope chain must contain same objects in the same order as the calling context");
}
