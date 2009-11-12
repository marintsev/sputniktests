// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
* @name: S15.10.2.12_A1_T1;
* @section: 15.10.2.12, 7.2, 7.3;
* @assertion: The production CharacterClassEscape :: s evaluates by returning the set of characters
* containing the characters that are on the right-hand side of the WhiteSpace (7.2) or LineTerminator (7.3) productions;
* @description: WhiteSpace
*/

//CHECK#1
var arr = /\s/.exec("\u0009");
if ((arr === null) || (arr[0] !== "\u0009")) {
  $ERROR('#1: var arr = /\\s/.exec("\\u0009"); arr[0] === "\\u0009". Actual. ' + (arr && arr[0]));
}

//CHECK#2
var arr = /\s/.exec("\u000A");
if ((arr === null) || (arr[0] !== "\u000A")) {
  $ERROR('#2: var arr = /\\s/.exec("\\u000A"); arr[0] === "\\u000A". Actual. ' + (arr && arr[0]));
}

//CHECK#3
var arr = /\s/.exec("\u000B");
if ((arr === null) || (arr[0] !== "\u000B")) {
  $ERROR('#3: var arr = /\\s/.exec("\\u000B"); arr[0] === "\\u000B". Actual. ' + (arr && arr[0]));
}

//CHECK#4
var arr = /\s/.exec("\u000C");
if ((arr === null) || (arr[0] !== "\u000C")) {
  $ERROR('#4: var arr = /\\s/.exec("\\u000C"); arr[0] === "\\u000C". Actual. ' + (arr && arr[0]));
}

//CHECK#5
var arr = /\s/.exec("\u000D");
if ((arr === null) || (arr[0] !== "\u000D")) {
  $ERROR('#5: var arr = /\\s/.exec("\\u000D"); arr[0] === "\\u000D". Actual. ' + (arr && arr[0]));
}

//CHECK#6
var arr = /\s/.exec("\u0020");
if ((arr === null) || (arr[0] !== "\u0020")) {
  $ERROR('#6: var arr = /\\s/.exec("\\u0020"); arr[0] === "\\u0020". Actual. ' + (arr && arr[0]));
}

//CHECK#7
var arr = /\s/.exec("\u00A0");
if ((arr === null) || (arr[0] !== "\u00A0")) {
  $ERROR('#7: var arr = /\\s/.exec("\\u00A0"); arr[0] === "\\u00A0". Actual. ' + (arr && arr[0]));
}

//CHECK#8
var arr = /\s/.exec("\u1680");
if ((arr === null) || (arr[0] !== "\u1680")) {
  $ERROR('#8: var arr = /\\s/.exec("\\u1680"); arr[0] === "\\u1680". Actual. ' + (arr && arr[0]));
}

//CHECK#9
var arr = /\s/.exec("\u180E");
if ((arr === null) || (arr[0] !== "\u180E")) {
  $ERROR('#9: var arr = /\\s/.exec("\\u180E"); arr[0] === "\\u180E". Actual. ' + (arr && arr[0]));
}

//CHECK#10
var arr = /\s/.exec("\u2000");
if ((arr === null) || (arr[0] !== "\u2000")) {
  $ERROR('#10: var arr = /\\s/.exec("\\u2000"); arr[0] === "\\u2000". Actual. ' + (arr && arr[0]));
}

//CHECK#11
var arr = /\s/.exec("\u2001");
if ((arr === null) || (arr[0] !== "\u2001")) {
  $ERROR('#11: var arr = /\\s/.exec("\\u2001"); arr[0] === "\\u2001". Actual. ' + (arr && arr[0]));
}

//CHECK#12
var arr = /\s/.exec("\u2002");
if ((arr === null) || (arr[0] !== "\u2002")) {
  $ERROR('#12: var arr = /\\s/.exec("\\u2002"); arr[0] === "\\u2002". Actual. ' + (arr && arr[0]));
}

//CHECK#13
var arr = /\s/.exec("\u2003");
if ((arr === null) || (arr[0] !== "\u2003")) {
  $ERROR('#13: var arr = /\\s/.exec("\\u2003"); arr[0] === "\\u2003". Actual. ' + (arr && arr[0]));
}

//CHECK#14
var arr = /\s/.exec("\u2004");
if ((arr === null) || (arr[0] !== "\u2004")) {
  $ERROR('#14: var arr = /\\s/.exec("\\u2004"); arr[0] === "\\u2004". Actual. ' + (arr && arr[0]));
}

//CHECK#15
var arr = /\s/.exec("\u2005");
if ((arr === null) || (arr[0] !== "\u2005")) {
  $ERROR('#15: var arr = /\\s/.exec("\\u2005"); arr[0] === "\\u2005". Actual. ' + (arr && arr[0]));
}

//CHECK#16
var arr = /\s/.exec("\u2006");
if ((arr === null) || (arr[0] !== "\u2006")) {
  $ERROR('#16: var arr = /\\s/.exec("\\u2006"); arr[0] === "\\u2006". Actual. ' + (arr && arr[0]));
}

//CHECK#17
var arr = /\s/.exec("\u2007");
if ((arr === null) || (arr[0] !== "\u2007")) {
  $ERROR('#17: var arr = /\\s/.exec("\\u2007"); arr[0] === "\\u2007". Actual. ' + (arr && arr[0]));
}

//CHECK#18
var arr = /\s/.exec("\u2008");
if ((arr === null) || (arr[0] !== "\u2008")) {
  $ERROR('#18: var arr = /\\s/.exec("\\u2008"); arr[0] === "\\u2008". Actual. ' + (arr && arr[0]));
}

//CHECK#19
var arr = /\s/.exec("\u2009");
if ((arr === null) || (arr[0] !== "\u2009")) {
  $ERROR('#19: var arr = /\\s/.exec("\\u2009"); arr[0] === "\\u2009". Actual. ' + (arr && arr[0]));
}

//CHECK#20
var arr = /\s/.exec("\u200A");
if ((arr === null) || (arr[0] !== "\u200A")) {
  $ERROR('#20: var arr = /\\s/.exec("\\u200A"); arr[0] === "\\u200A". Actual. ' + (arr && arr[0]));
}

//CHECK#21
var arr = /\s/.exec("\u2028");
if ((arr === null) || (arr[0] !== "\u2028")) {
  $ERROR('#21: var arr = /\\s/.exec("\\u2028"); arr[0] === "\\u2028". Actual. ' + (arr && arr[0]));
}

//CHECK#22
var arr = /\s/.exec("\u2029");
if ((arr === null) || (arr[0] !== "\u2029")) {
  $ERROR('#22: var arr = /\\s/.exec("\\u2029"); arr[0] === "\\u2029". Actual. ' + (arr && arr[0]));
}

//CHECK#23
var arr = /\s/.exec("\u202F");
if ((arr === null) || (arr[0] !== "\u202F")) {
  $ERROR('#23: var arr = /\\s/.exec("\\u202F"); arr[0] === "\\u202F". Actual. ' + (arr && arr[0]));
}

//CHECK#24
var arr = /\s/.exec("\u205F");
if ((arr === null) || (arr[0] !== "\u205F")) {
  $ERROR('#24: var arr = /\\s/.exec("\\u205F"); arr[0] === "\\u205F". Actual. ' + (arr && arr[0]));
}

//CHECK#25
var arr = /\s/.exec("\u3000");
if ((arr === null) || (arr[0] !== "\u3000")) {
  $ERROR('#25: var arr = /\\s/.exec("\\u3000"); arr[0] === "\\u3000". Actual. ' + (arr && arr[0]));
}
