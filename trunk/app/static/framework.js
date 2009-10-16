// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

// The type of exception thrown to abort a test.
function SputnikTestAborted() { }

// The test file has started executing.
function sputnikTestStarted() {
  $t.sputnikTestController.testStarted(testInfo);
}

// The test has completed normally.
function sputnikTestCompleted() {
  $t.sputnikTestController.testCompleted(testInfo);
}

// The test is over, either completing normally or aborting.
function sputnikTestDone() {
  $t.sputnikTestController.testDone(testInfo);
}

function sputnikPrint(message) {
  $t.sputnikTestController.print(message);
}

// The test has failed.  Report the problem and abort.
function sputnikTestFailed(message) {
  $t.sputnikTestController.testFailed(testInfo, message);
  throw new SputnikTestAborted();
}

var $hasFailed = false;
var $hasCompleted = false;

function getTestName() {
  if (testInfo.isNegative) {
    return testInfo.name + " (negative)";
  } else {
    return testInfo.name;
  }
}

function standAloneTestFailed(testId, message) {
  if ($hasFailed) {
    return;
  }
  $hasFailed = true;
  var output = message ? ": " + message : "";
  $d.innerHTML = "<b>" + getTestName() + " failed</b>" + output;
  $d.style.color = "#700000";
}

function standAloneTestDone() {
  if (!$hasCompleted && !$hasFailed) {
    standAloneTestFailed();
  }
}

function standAloneTestStarted() {
  $d = document.createElement('div');
  $d.style.fontFamily = "sans-serif";
  $d.innerHTML = "<b>Started " + getTestName() + "</b>";
  document.body.appendChild($d);
  var l = document.createElement('div');
  l.innerHTML = '<a href="' + testInfo.serial + '.js">View Source</a>';
  document.body.appendChild(l);
}

function standAlonePrint(str) {
  var l = document.createElement('div');
  l.style.fontFamily = "courier";
  l.innerText = str;
  document.body.appendChild(l);
}

function standAloneTestCompleted() {
  $hasCompleted = true;
  if (!$hasFailed) {
    $d.innerHTML = "<b>" + getTestName() + " completed</b>";
    $d.style.color = "#007000";
  }
}

if (typeof $t == 'undefined') {
  var $t = { };
}

if (typeof $t.sputnikTestController == 'undefined') {
  $t.sputnikTestController = {
    'testFailed': standAloneTestFailed,
    'testDone': standAloneTestDone,
    'testStarted': standAloneTestStarted,
    'testCompleted': standAloneTestCompleted,
    'print': standAlonePrint
  }
}
