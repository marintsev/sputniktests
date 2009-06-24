// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
* @name: S15.5.4.10_A2_T1;
* @section: 15.5.4.10, 15.10.6.2;
* @assertion: match returns array as specified in 15.10.6.2;
* @description: Regular expression is 3;
*/

__string = "1234567890";

//////////////////////////////////////////////////////////////////////////////
//CHECK#1
if (__string.match(3)[0]!== "3") {
  $ERROR('#1: __string = "1234567890"; __string.match(3)[0]=== "3". Actual: '+__string.match(3)[0]);
}
//
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//CHECK#2
if (__string.match(3).length !==1) {
  $ERROR('#2: __string = "1234567890"; __string.match(3).length ===1. Actual: '+__string.match(3).length );
}
//
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//CHECK#3
if (__string.match(3).index !==2) {
  $ERROR('#3: __string = "1234567890"; __string.match(3).index ===2. Actual: '+__string.match(3).index );
}
//
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//CHECK#4
if (__string.match(3).input !==__string) {
  $ERROR('#4: __string = "1234567890"; __string.match(3).input ===__string. Actual: '+__string.match(3).input );
}
//
//////////////////////////////////////////////////////////////////////////////
