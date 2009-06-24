// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S11.13.2_A4.6_T2.5;
 * @section: 11.13.2, 11.7.1;
 * @assertion: The production x <<= y is the same as x = x << y; 
 * @description: Type(x) is different from Type(y) and both types vary between String (primitive or object) and Boolean (primitive and object);
*/

//CHECK#1
x = true;
x <<= "1";
if (x !== 2) {
  $ERROR('#1: x = true; x <<= "1"; x === 2. Actual: ' + (x));
}

//CHECK#2
x = "1";
x <<= true;
if (x !== 2) {
  $ERROR('#2: x = "1"; x <<= true; x === 2. Actual: ' + (x));
}

//CHECK#3
x = new Boolean(true);
x <<= "1";
if (x !== 2) {
  $ERROR('#3: x = new Boolean(true); x <<= "1"; x === 2. Actual: ' + (x));
}

//CHECK#4
x = "1";
x <<= new Boolean(true);
if (x !== 2) {
  $ERROR('#4: x = "1"; x <<= new Boolean(true); x === 2. Actual: ' + (x));
}

//CHECK#5
x = true;
x <<= new String("1");
if (x !== 2) {
  $ERROR('#5: x = true; x <<= new String("1"); x === 2. Actual: ' + (x));
}

//CHECK#6
x = new String("1");
x <<= true;
if (x !== 2) {
  $ERROR('#6: x = new String("1"); x <<= true; x === 2. Actual: ' + (x));
}

//CHECK#7
x = new Boolean(true);
x <<= new String("1");
if (x !== 2) {
  $ERROR('#7: x = new Boolean(true); x <<= new String("1"); x === 2. Actual: ' + (x));
}

//CHECK#8
x = new String("1");
x <<= new Boolean(true);
if (x !== 2) {
  $ERROR('#8: x = new String("1"); x <<= new Boolean(true); x === 2. Actual: ' + (x));
}
