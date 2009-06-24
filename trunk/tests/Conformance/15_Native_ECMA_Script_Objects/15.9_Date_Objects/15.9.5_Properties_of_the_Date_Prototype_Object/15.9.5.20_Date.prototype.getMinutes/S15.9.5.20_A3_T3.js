// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S15.9.5.20_A3_T3;
 * @section: 15.9.5.20;
 * @assertion: The Date.prototype.getMinutes property "length" has { ReadOnly, DontDelete, DontEnum } attributes;
 * @description: Checking DontEnum attribute;
 */

if (Date.prototype.getMinutes.propertyIsEnumerable('length')) {
  $ERROR('#1: The Date.prototype.getMinutes.length property has the attribute DontEnum');
}

for(x in Date.prototype.getMinutes) {
  if(x === "length") {
    $ERROR('#2: The Date.prototype.getMinutes.length has the attribute DontEnum');
  }
}

