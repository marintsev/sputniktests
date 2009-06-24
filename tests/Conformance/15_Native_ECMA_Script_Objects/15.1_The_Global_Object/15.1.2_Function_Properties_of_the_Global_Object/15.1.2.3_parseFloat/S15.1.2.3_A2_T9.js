// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S15.1.2.3_A2_T9;
 * @section: 15.1.2.3;
 * @assertion: Operator remove leading StrWhiteSpaceChar;
 * @description: StrWhiteSpaceChar :: PS (U+2029);  
*/

//CHECK#1
if (parseFloat("\u20291.1") !== parseFloat("1.1")) {
  $ERROR('#1: parseFloat("\\u20291.1") === parseFloat("1.1"). Actual: ' + (parseFloat("\u20291.1")));
}

//CHECK#2
if (parseFloat("\u2029\u2029-1.1") !== parseFloat("-1.1")) {
  $ERROR('#2: parseFloat("\\u2029\\u2029-1.1") === parseFloat("-1.1"). Actual: ' + (parseFloat("\u2029\u2029-1.1")));
}

//CHECK#3
if (isNaN(parseFloat("\2029")) !== true) {
  $ERROR('#3: parseFloat("\\u2029") === Not-a-Number. Actual: ' + (parseFloat("\u2029")));
}
