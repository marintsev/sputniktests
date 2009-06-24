// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
* @name: S11.11.2_A4_T4;
* @section: 11.11.2;
* @assertion: If ToBoolean(x) is true, return x;
* @description: Type(x) or Type(y) vary between Null and Undefined;
*/

//CHECK#1
if ((true || undefined) !== true) {
  $ERROR('#1: (true || undefined) === true');
}

//CHECK#2
if ((true || null) !== true) {
  $ERROR('#2: (true || null) === true');
}
