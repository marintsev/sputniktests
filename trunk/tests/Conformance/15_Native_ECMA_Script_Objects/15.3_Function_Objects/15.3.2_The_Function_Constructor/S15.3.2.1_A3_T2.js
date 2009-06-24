// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
* @name: S15.3.2.1_A3_T2;
* @section: 15.3.2.1, 13.2;
* @assertion: When the Function constructor is called with arguments p, body the following steps are taken:
* i) Let Result(i) be the first argument
* ii) Let P be ToString(Result(i))
* iii) Call ToString(body)
* iv) If P is not parsable as a FormalParameterList_opt then throw a SyntaxError exception
* v) If body is not parsable as FunctionBody then throw a SyntaxError exception
* vi) Create a new Function object as specified in 13.2 with parameters specified by parsing P as a FormalParameterListopt and body specified by parsing body as a FunctionBody
* Pass in a scope chain consisting of the global object as the Scope parameter
* vii) Return Result(vi);
* @description: Values of the function constructor arguments are "{toString:function(){return 'a';}}" and "return a;";
*/

p = {toString:function(){return "a";}};
body = "return a;";

//CHECK#1
try {
  f = new Function(p,body);
} catch (e) {
  $FAIL('#1: test failed with error '+e);
}

//CHECK#2
if (f.constructor !== Function) {
  $ERROR('#2: When the Function constructor is called with arguments p, body creates a new Function object as specified in 13.2');
}

//CHECK#3
if (f(42)!==42) {
  $ERROR('#3: When the Function constructor is called with arguments p, body creates a new Function object as specified in 13.2');
}


