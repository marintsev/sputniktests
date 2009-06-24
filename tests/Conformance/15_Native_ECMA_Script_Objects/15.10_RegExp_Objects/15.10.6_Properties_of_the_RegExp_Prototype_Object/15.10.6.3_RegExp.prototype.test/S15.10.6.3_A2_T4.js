// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
* @name: S15.10.6.3_A2_T4;
* @section: 15.10.6.3;
* @assertion: A TypeError exception is thrown if the this value is not an object for which the value of the internal [[Class]] property is "RegExp";
* @description: The tested object is new String("[a-b]");
*/

__instance = new String("[a-b]");

__instance.test = RegExp.prototype.test;

//CHECK#1
with(__instance){
    try {
      $ERROR('#1.1: __instance = new String("[a-b]"); __instance.test = RegExp.prototype.test; test("message to investigate"). Actual: ' + (test("message to investigate")));
    } catch (e) {
      if ((e instanceof TypeError) !== true) {
        $ERROR('#1.2: __instance = new String("[a-b]"); __instance.test = RegExp.prototype.test; test("message to investigate"). Actual: ' + (e));
      }
   }
}

