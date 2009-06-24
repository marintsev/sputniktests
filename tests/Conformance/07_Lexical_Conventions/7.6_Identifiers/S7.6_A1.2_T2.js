// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S7.6_A1.2_T2;
 * @section: 7.6;
 * @assertion: IdentifierStart :: $; 
 * @description: The $ as unicode character \u0024;
*/

//CHECK#1
var \u0024 = 1;
if ($ !== 1) {
  $ERROR('#1: var \\u0024 = 1; $ === 1. Actual: ' + ($));
}
