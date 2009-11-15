// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

var kTestStatusCookieName = "sputnik_test_status";
var kTestBlacklistCookieName = "sputnik_test_blacklist";
var kLastTestStarted = "sputnik_last_test_started";
var kRotation = 2.14;
var kIterations = 50;
var kTestCaseChunkSize = 256;
var kChunkAheadCount = 2;
var kCrosshairUpdateInterval = 64;

var plotter = null;

function SputnikTestFailed(message) {
  this.message_ = message;
};

function BrowserData(name, type, data) {
  this.name = name;
  this.type = type;
  this.data = data;
}

function delay(self, fun) {
  window.setTimeout(function () { fun.call(self); }, 0);
}

var results = [
  // new BrowserData('Chrome3Win', 'cm', '5246:Y*DE*/*/*/*yIAIC*tQ*CE*/*bQrwQKGhR*LBAEIQggAEEIIggABEQIQABBCIIgABCCEIQABCCIIgABBCQIg*Go*CJFEEGYT*SI*Dg*FD*GB*EgEFABiU*3G*DCB*Ck*HgE*PCY*CDAC*CE*CQ*Dg*CD*DC*HBAC*DI*DC*CB*Cg*GQ*CI*Cg*Og*DC*CU*ID*CE*CQE*EB*FQEAgB*FBC*CE*DgAE*DC*EC*CQ*Cy8vdBCAEEA8AC*EQBQQRIBhR*Qi*HE*CQC*CDB*EC*DC*CU*DF*CG*DoAME*FE*CI*CgC*UgQ'),
  new BrowserData('Chrome4Linux', 'cm', '5246:Y*DE*/*/*/*yIAIC*QQ*cQ*CE*/*bQrwQKGhR*LBAEIQggAEEIIggABEQIQABBCIIgABCCEIQABCCIIgABBCQIg*Go*CJFEEGYT*SI*Dg*FD*GB*EgEFABiU*3G*DCB*Ck*HgE*PCY*CDAC*CE*CQ*Dg*CD*DC*HBAC*DI*DC*CB*Cg*GQ*CI*Cg*Og*DC*CU*ID*CE*CQE*EB*FQEAgB*FBC*CE*DgAE*DC*EC*CQ*Cy8vdBCAEEA8AC*EQBQQRIBhR*Qi*HE*CQC*CDB*EC*DC*CU*DF*CG*DoAME*FE*CI*CgC*UgQ'),
  // new BrowserData('Chrome4Mac', 'cm', '5246:Y*DE*/*/*/*yIAIC*tQ*CE*/*bQrwQKGhR*LBAEIQggAEEIIggABEQIQABBCIIgABCCEIQABCCIIgABBCQIg*Go*CJFEEGYT*SI*Dg*FD*GB*EgEFABiU*3G*DCB*Ck*HgE*PCY*CDAC*CE*CQ*Dg*CD*DC*HBAC*DI*DC*CB*Cg*GQ*CI*Cg*Og*DC*CU*ID*CE*CQE*EB*FQEAgB*FBC*CE*DgAE*DC*EC*CQ*Cy8vdBCAEEA8AC*EQBQQRIBhR*Qi*HE*CQC*CDB*EC*DC*CU*DF*CG*DoAME*FE*CI*CgC*UgQ'),
  // new BrowserData('Firefox2Linux', 'fx', '5246:B*EI*YQ*RQ*JI*lB*EB*EQ*CQ*DE*DI*sE*MQ*Jg*GC*CB*wIAIC*Vw///Dg*SQ*CE*/*EgHAioH*RQKQQCEBR*/*Co*CJFEEGYT*SI*Dg*EQD*GB*E4B*Cz*CI*XG*CY*Cg*Yn*CEABAIk*HoE*CCCEAQ*MDAC*CE*CQ*Dg*CD*CICAEB*EBAC*DMACACoABAIg*DI*CQ*CIEAg*LE*CgBoACCAUI*HDBAE*CQU*EB*FQEAgB*FBC*CE*DkAE*DC*DCC*CQ*Cy8vdBCAEEA8AC*EQBQQRIBhR*CI*Ni*FII*DQKAkDDRAgICAIQC*CU*DFABGAgAoAMUQ*EE*CJEAgC*KQ*CgC*FgQ'),
  // new BrowserData('Firefox3Mac', 'fx', '5246:B*EI*eB*LQ*JI*/*oEAE*Ug*JB*HCE*CC*FIB*CQ*aIAIC*Vw///Dg*SQ*CE*HCB*GD*bI*DI*CgI*PgHAioH*RQKQQCEBR*/*Co*CJFEEGYT*SI*Dg*EgD*GB*E4B*Cz*CI*2G*CEAB*Ck*HoE*DCEAQ*MDAC*CE*CQ*Dg*CD*CICAEB*EBAC*DMACACoAB*Cg*DI*CQ*CIEAg*LE*CgBIACCAUI*CE*EDBAE*CQU*EB*FQEAgB*FJC*CE*D0AE*DD*DSC*CQ*Cy8vdBCAEEA8AC*EQBQQRIBhR*Qi*FI*EQKAkDBQAgICAIQC*CU*DFABGIiAoANE*DCAE*CJEAgC*KQ*CgC*FgQ'),
  new BrowserData('Firefox35Win', 'fx', '5246:B*EI*eB*LQ*JI*/*oEAE*TCg*JB*wIAIC*Vw///Dg*SQ*CE*HCB*GD*bI*DI*CgI*PgHAioH*RQKQQCEBR*/*Co*CJFEEGYT*SI*Dg*EgD*GB*EYB*Cy*CI*2G*CEAB*Ck*HoE*DCEAQ*MDAC*CE*CQ*Dg*CD*CICAEB*EBAC*DMACACoAB*Cg*DI*CQ*CIEAg*LE*CgBIACCAUI*CE*EDBAEAQQU*EB*FQEAgB*FJC*CE*D0AE*DD*DSC*CQ*Cy8vdBCAEEA8AC*EQBQQRIBhR*Qi*FI*EQC*CDBQAgICAIYC*CU*DFABGIiAoANE*FE*CJEAgC*KQ*CgC*FgQ'),
  new BrowserData('Safari4Mac', 'sf', '5246:*eQ*YC*CI*/*/*CI*4IAIC*QQ*PggE*KQ*CE*/*FVBRAB*RQrwQKGhR*LBAEIQggAEEIIggABEQIQABBCIIgABCCEIQABCCIIgABBCQIg*Go*CIEEAEQS*SI*IgC*LgEFABiU*qQ*LQn*CE*DIE*HoAIAC*CI*JCY*CC*HQ*GC*CI*CE*PC*HFAIF*aC*CQM*HgBAgCC*GC*oy8vdBCAEEA8AC*EQBQQRIBhR*Qi*HE*DIAk*FIAgAC*CE*Tg*SQ*CgC*FgQ'),
  // new BrowserData('Safari3Win', 'sf', '5246:*eQ*Fl*CQ*IQ*/AE*2Q*FB*JC*7IAIC*gggE*KQ*CE*HCB*GD*bI*DI*CgI*QFARAB*RQr0SLOpT*LhCEIQggAEEIIggABEQIQABBCIIgABCCEIQABCCIIgABBCQIg*Go*CIEEAEQS*ICAB*HI*IgC*LgEFABiU*fwJ*JQ*LQn*CE*DIE*HoAIAC*CI*JCY*CC*GIQAB*ECgC*Og*DBC*DC*DFAIF*FE*NE*CI*CBC*CQI*HgBAgCC*WI*HQ*FB*DQ*Gy8vdBCAEEA8AC*EQBQQRIBhR*Qi*HE*DIAk*FIAgAC*LIC*Jg*SQ*CgC*FgQ'),
  // new BrowserData('IE7Win', 'ie', '5246:AQCAIUAIgAEAQg*DQ4K*CIE*CCg*EBQMI*DIQAEQAC*CwAE*EB*CMCAhJYBAI*CQ*CFE*CCAgQEEAQCAgIg*CIG*Ck*CCAIE*CBgg*CJAEQCgAQCABYAIAggI*DCQ*CQCAQAEC*CEgACQgIAIASQ*CIxAhAgAEB*CcACB*CgEAIQBQUABAEEABE*CBEAEIAB*FBABABg*DCgg*DBAE*GCIJ4AiAB*EI*DIAYK*IC*DC*DQ*IMI*SQ*CE*EEg*DE*DB*GQ*DI*Wg*Dg*PsXBi6H*JCQC*FQKQQCEBR*/gQo*CIEEAEQSI*DQC*IYABhI*IgC*GQE//Pi0FEByW*KQ*HE*Eo+//////0JgIEIgn*CQX*DIUGAIUAgQ*DE*MICI*CCACQ*EC*EY*CC*HSAQB*DCAgO*CG*PC*HoAIKC*EEAFACAQAk*CQIFEAI*CgAC*CQIAQE*DQABI*EQ*DC*GE*CE*FEg*CSC*FC*CB*EE*CgABAy8vdBCAEEA8AC*EQBQQRIBhR*CI*Ic*Ei*EE*EBAIEk*CQAgI*CIQ*DBK*Cgq7BAg*DC*JBE*NQ*CgC*FgQ'),
  new BrowserData('IE8Win', 'ie', '5246:AQCAIUAIgAEAQg*DQ4K*CIE*CCg*EBQMI*DIQAEQAC*CwAE*EB*CMCAhJYBAI*CQ*CFE*CCAgQEEAQCAgIg*CIG*Ck*CCAIE*CBgg*CJAEQCgAQCABYAIAggI*DCQ*CQCAQAEC*CEgACQgIAIASQ*CIxAhAgAEB*CcACB*CgEAIQBQUABAEEABE*CBEAEIAB*FBABABg*DCgg*DBAE*GCIJ*Ci*GI*DIAYK*IC*DC*DQ*IMI*SQ*CE*EEg*DE*DB*GQ*DI*Wg*Dg*PsXBi6H*JCQC*FQKQQCEBR*/gQo*CIEEAEQSI*DQC*IYABhI*IgC*GQE//Pi0FEByW*KQ*HE*Eo+//////0JgIEIgn*CQX*DIUGAIUAgQ*DE*MICI*CCACQ*EC*EY*CC*HSAQB*DCAgO*CG*PC*HoAIKC*EEAFACAQAk*CQIFEAI*CgAC*CQIAQE*DQABI*EQ*DC*GE*CE*FEg*CSC*FC*CB*EE*CgABAy8vdBCAEEA8AC*EQBQQRIBhR*CI*Ic*Ei*EE*CBABKJEk*CQggI*CIQ*DBK*Cgq7BAg*DC*JBE*NQ*CgC*FgQ'),
  new BrowserData('Opera', 'op', '5246:*FB*MQo*JEI*GC*KI*JC*DQC*NC*HC*/*JCAY*RE*CB*FI*JI*DB*WE*DCI*DC*KIAIC*QQ*Cg*HE*UE*HCBAQ*ED*CI*C9*CgO*pgXBzoH*RQKQQCEBRE*LB*0o*CIEEAEQS*GE*DJI*GI*JC*HI/AP*Fg*Kg*tG*CE*EE*CE*Eo*EDEIQ*MC*HQ*GC*CI*CE*OoD*HFAIF*IgB*DC*CIE*DE*EC*CQI*IB*DQAQ*ty8vdBCAEEA8AC*EQBQQRIBhR*CEC*CCE*Ii*QB*JE*CIXCQ*DQM*EQ*QQ*FQ*DC*FgQ')
];

function Plotter(values) {
  this.values = values;
  this.results = [];
  this.matrix = null;
  this.positions = null;
  this.maxScore = null;
}

Plotter.prototype.calcDistanceMatrix = function () {
  for (var i = 0; i < this.values.length; i++)
    this.results.push(parseTestResults(this.values[i].data));
  this.matrix = [];
  for (var i = 0; i < this.results.length; i++)
    this.matrix[i] = [];
  for (var i = 0; i < this.results.length; i++) {
    this.matrix[i][i] = 0.0;
    for (var j = 0; j < i; j++) {
      var dist = calcVectorDistance(this.results[i], this.results[j]);
      this.matrix[i][j] = dist;
      this.matrix[j][i] = dist;
    }
  }
  var scores = [];
  for (var i = 0; i < this.results.length; i++)
    scores.push(calcScore(this.results[i]));
  return scores;
};

Plotter.prototype.calcInitialPositions = function (scores) {
  var clusters = [];
  for (var i = 0; i < this.values.length; i++)
    clusters.push(new Cluster(this, scores, [i], null, null));
  while (clusters.length > 1) {
    var minDist = null;
    var minI = null;
    var minJ = null;
    for (var i = 0; i < clusters.length; i++) {
      for (var j = 0; j < i; j++) {
        var a = clusters[i];
        var b = clusters[j];
        var dist = a.distanceTo(b);
        if ((minDist === null) || (dist < minDist)) {
          minDist = dist;
          minI = i;
          minJ = j;
        }
      }
    }
    var a = clusters[minI];
    var b = clusters[minJ];
    var newValues = a.values.concat(b.values);
    var next = new Cluster(this, scores, newValues, a, b);
    var newClusters = [];
    for (var i = 0; i < clusters.length; i++) {
      if (i != minI && i != minJ)
        newClusters.push(clusters[i]);
    }
    newClusters.push(next);
    clusters = newClusters;
  }
  this.positions = [];
  this.maxScore = 0;
  for (var i = 0; i < scores.length; i++)
    this.maxScore = Math.max(scores[i], this.maxScore);
  this.positionCluster(0, 2 * Math.PI, clusters[0]);
};

Plotter.prototype.positionCluster = function (from, to, cluster) {
  if (cluster.values.length == 1) {
    var scale = cluster.score() / this.maxScore * 100;
    var index = cluster.values[0];
    var pos = (from + to) / 2;
    var x = Math.sin(pos + kRotation) * scale;
    var y = Math.cos(pos + kRotation) * scale;
    this.positions.push(new Point(this, index, x, y, this.values[index]));
  } else {
    var ratio = cluster.leftChild.size() / cluster.size();
    var mid = (to - from) * ratio;
    this.positionCluster(from, from + mid, cluster.leftChild);
    this.positionCluster(from + mid, to, cluster.rightChild);
  }
};

Plotter.prototype.distance = function (i, j, scores) {
  if (i == -1) {
    return scores[j];
  } else if (j == -1) {
    return scores[i];
  } else {
    return this.matrix[i][j];
  }
};

Plotter.prototype.dampen = function (pull, temp) {
  var dx = pull[0];
  var dy = pull[1];
  var length = Math.sqrt(dx * dx + dy * dy);
  if (length > temp) {
    var ratio = temp / length;
    pull[0] *= ratio;
    pull[1] *= ratio;
  }
};

Plotter.prototype.runLassesSpringyAlgorithm = function (scores, adjustCenter) {
  // First apply springy algorithm to adjust positions to match distances.
  var center = new Point(this, -1, 0, 0, null);
  this.positions.push(center);
  var count = this.positions.length;
  var max = 20 / count;
  for (var l = 0; l < kIterations; l++) {
    var temp = max * (1 - (l / kIterations));
    var pulls = [];
    for (var i = 0; i < count; i++)
      pulls[i] = [];
    for (var i = 0; i < count; i++) {
      for (var j = 0; j < count; j++) {
        var pull = this.positions[i].calcPull(this.positions[j], scores);
        this.dampen(pull, temp);
        pulls[i][j] = pull;
      }
    }
    for (var i = 0; i < count; i++) {
      for (var j = 0; j < count; j++) {
        if (i == j || this.positions[i].isFixed) continue;
        var pull = pulls[i][j];
        this.positions[i].x += pull[0];
        this.positions[i].y += pull[1];
      }
    }
  }
  this.positions.pop();
  if (adjustCenter) {
    // Then move all the points to get the midpoint to (0, 0)
    for (var i = 0; i < this.positions.length; i++) {
      var point = this.positions[i];
      point.x -= center.x;
      point.y -= center.y;
    }
  }
  var maxDist = 0;
  for (var i = 0; i < this.positions.length; i++) {
    var point = this.positions[i];
    var dist = Math.sqrt(point.x * point.x + point.y * point.y);
    maxDist = Math.max(maxDist, dist);
  }
  // Then scale to get the farthes point to a distance of 100.
  var ratio = 100 / maxDist;
  for (var i = 0; i < this.positions.length; i++) {
    var point = this.positions[i];
    point.x *= ratio;
    point.y *= ratio;
  }
  return center;
};

Plotter.prototype.getUrl = function () {
  var result = [];
  for (var i = 0; i < this.positions.length; i++) {
    var pos = this.positions[i];
    result.push(pos.data.type + "@" + (pos.x << 0) + "," + (pos.y << 0));
  }
  return result.join(":");
};

Plotter.prototype.placeFixpoints = function () {
  var scores = plotter.calcDistanceMatrix();
  plotter.calcInitialPositions(scores);
  plotter.runLassesSpringyAlgorithm(scores, true);
  for (var i = 0; i < this.positions.length; i++)
    this.positions[i].isFixed = true;
};

Plotter.prototype.displayOn = function (root) {
  var elm = document.createElement('object', true);
  elm.setAttribute('width', 450);
  elm.setAttribute('height', 450);
  elm.setAttribute('data', "compare/plot.svg?m=" + this.getUrl());
  elm.setAttribute('type', "image/svg+xml");
  elm.setAttribute('id', 'plot');
  svgweb.appendChild(elm, root);
};

Plotter.prototype.placeByDistance = function (distances) {
  return this.runLassesSpringyAlgorithm(distances, false);
};

function Point(plotter, id, x, y, data) {
  this.plotter = plotter;
  this.id = id;
  this.x = x;
  this.y = y;
  this.data = data;
  this.isFixed = false;
}

Point.prototype.toString = function () {
  return "(" + this.x + ", " + this.y + ")";
};

Point.prototype.calcPull = function (other, scores) {
  var ratio = (100 / this.plotter.maxScore);
  var ideal = this.plotter.distance(this.id, other.id, scores) * ratio;
  var dx = this.x - other.x;
  var dy = this.y - other.y;
  if (ideal == 0) {
    return [-dx, -dy];
  } else {
    var force = (ideal - Math.sqrt(dx * dx + dy * dy)) / ideal / 2;
    return [dx * force, dy * force];
  }
};

function Cluster(plotter, scores, values, leftChild, rightChild) {
  this.scores = scores;
  this.plotter = plotter;
  this.values = values;
  this.leftChild = leftChild;
  this.rightChild = rightChild;
}

Cluster.prototype.score = function () {
  return this.scores[this.values[0]];
};

Cluster.prototype.toString = function () {
  if (this.leftChild === null) {
    return this.plotter.values[this.values[0]].name + "(" + this.values[0] + ")";
  } else {
    return "(" + this.leftChild + " " + this.rightChild + ")";
  }
};

Cluster.prototype.size = function () {
  return this.values.length;
};

Cluster.prototype.distanceTo = function (other) {
  var sum = 0;
  var count = 0;
  for (var i = 0; i < this.values.length; i++) {
    for (var j = 0; j < other.values.length; j++) {
      sum += this.plotter.distance(this.values[i], other.values[j]);
      count += 1;
    }
  }
  return sum / count;
};

function matrixToString(keys, scores, matrix) {
  var result = '';
  for (var i = 0; i < keys.length; i++) {
    if (i > 0) result += ",";
    result += keys[i];
  }
  result += ":";
  for (var i = 0; i < keys.length; i++) {
    if (i > 0) result += ",";
    result += scores[i];
  }
  for (var i = 1; i < matrix.length; i++) {
    result += ":";
    for (var j = 0; j < i; j++) {
      if (j > 0) result += ",";
      result += matrix[i][j];
    }
  }
  return result;
}

function calcScore(a) {
  var result = 0;
  for (var i = 0; i < a.length; i++) {
    if (!a[i])
      result++;
  }
  return result;
}

function calcVectorDistance(a, b) {
  var minLength = Math.min(a.length, b.length);
  var result = 0;
  for (var i = 0; i < minLength; i++) {
    if (a[i] != b[i])
      result++;
  }
  var longest = (a.length > b.length) ? a : b;
  for (; i < longest.length; i++) {
    if (!longest[i])
      result++;
  }
  return result;
}

function jsonDebugToString() {
  var parts = [];
  for (var name in this) {
    var value = this[name];
    if (typeof value == 'function')
      continue;
    parts.push(name + " = " + value);
  }
  return "json {" + parts.join(", ") + "}"
}

function parseJson(str) {
  var result;
  if (typeof JSON != 'undefined') result = JSON.parse(str);
  else result = eval("(" + str + ")");
  try {
    result.__proto__ = {'toString': jsonDebugToString};
  } catch (e) {
    // ignore
  }
  return result;
}

function request(path, onDone) {
  var xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4 && xhr.status == 200) {
      onDone(parseJson(xhr.responseText));
    }
  };
  xhr.open("GET", path, true);
  xhr.send();
}

function getCookie(name) {
  var parts = document.cookie.split(/\s*;\s*/);
  var nameEq = name + "=";
  for (var i = 0; i < parts.length; i++) {
    if (parts[i].substring(0, nameEq.length) == nameEq)
      return parts[i].substring(nameEq.length);
  }
}

function setCookie(name, value, expiryHoursOpt) {
  var expiryHours = expiryHoursOpt || 6;
  var date = new Date();
  date.setTime(date.getTime() + (expiryHours * 60 * 60 * 1000));
  var expiry = "expires=" + date.toGMTString();
  document.cookie = name + "=" + value + ";" + expiry + "; path=/";
}

function deleteCookie(name) {
  document.cookie = name + "=; path=/";
}

function reportError(str) {
  alert(str);
}

function assert(value) {
  if (!value)
    alert("Assertion failed");
}

function ProgressBar() {
  this.root = document.createElement('div');
  this.root.className = 'progress';
  this.bar = document.createElement('div');
  this.bar.className = 'bar';
  this.label = document.createElement('span');
  this.label.className = 'label';
  this.root.appendChild(this.bar);
  this.root.appendChild(this.label);
  this.value = 0;
  this.max = 1;
}

ProgressBar.prototype.replaceInto = function (parent, placeholder) {
  parent.replaceChild(this.root, placeholder);
};

ProgressBar.prototype.setMax = function (value) {
  this.max = value;
};

ProgressBar.prototype.setValue = function (value) {
  this.value = value;
  var percent = Math.floor(100 * this.value / this.max);
  this.bar.style.width = percent + "%";
  this.label.innerHTML = percent + "%";
};

// --- R u n n e r ---

function Runner(testRun, serial, testCase) {
  this.testRun_ = testRun;
  this.serial_ = serial;
  this.testCase_ = testCase;
  this.root_ = document.getElementById('workerRoot');
  this.iframe_ = document.createElement('iframe');
  this.pResult_ = new Promise();
  this.start_ = null;
  this.hasFailed_ = false;
  this.hasCompleted_ = false;
  this.failedMessage_ = null;
  this.printed_ = [];
}

Runner.prototype.testPrint = function (str) {
  this.printed_.push(str);
};

Runner.prototype.openTestPage = function () {
  window.open(this.testCase_.getHtmlUrl(), '_blank');
};

Runner.prototype.getName = function () {
  return this.testCase_.getName();
};

Runner.prototype.getMessage = function () {
  assert(this.hasUnexpectedResult());
  if (this.hasFailed()) {
    if (this.failedMessage_) {
      return this.failedMessage_;
    } else {
      return this.getName() + " failed";
    }
  } else {
    return this.getName() + " was expected to fail";
  }
};

Runner.prototype.hasFailed = function () {
  return this.hasFailed_;
};

Runner.prototype.testStart = function () {
  this.start_ = new Date();
};

Runner.prototype.testDone = function () {
  if (this.root_)
    this.root_.removeChild(this.iframe_);
  if (!this.hasCompleted_)
    this.hasFailed_ = true;
  this.pResult_.fulfill(null);
  if (this.testRun_)
    this.testRun_.testDone(this);
};

Runner.prototype.testCompleted = function () {
  this.hasCompleted_ = true;
};

Runner.prototype.testFailed = function (message) {
  if (!this.hasFailed_) {
    this.hasFailed_ = true;
    this.failedMessage_ = message;
  }
  throw new SputnikTestFailed(message);
};

Runner.prototype.hasUnexpectedResult = function () {
  var hasSucceeded = this.hasCompleted_ && !this.hasFailed_;
  var isPositive = !this.testCase_.isNegative();
  return isPositive !== hasSucceeded;
};

Runner.prototype.inject = function (code) {
  var doc = this.iframe_.contentWindow.document;
  doc.write('<script>' + code + '</script>')
};

Runner.prototype.schedule = function () {
  this.registerCookie();
  var source = this.testCase_.getSource();
  this.root_.appendChild(this.iframe_);
  var self = this;
  var global = this.iframe_.contentWindow;
  this.inject('');
  global.testFailed = function (s) { self.testFailed(s); };
  global.testDone = function () { self.testDone(); };
  global.testStart = function () { self.testStart(); };
  global.testCompleted = function () { self.testCompleted(); };
  global.testPrint = function () { self.testPrint(); };
  this.inject('testStart();');
  this.inject(source);
  this.inject('testDone();');
  return this.pResult_;
};

Runner.prototype.registerCookie = function () {
  setCookie(kLastTestStarted, this.serial_);
};

Runner.prototype.toString = function () {
  return "a Runner(" + this.serial_ + ")";
};

// --- T e s t   R e s u l t ---

function cloneJson(obj) {
  if (typeof obj == 'object') {
    var clone = { };
    for (var p in obj)
      clone[p] = cloneJson(obj[p]);
    return clone;
  } else {
    return obj;
  }
}

function TestResult(info) {
  // Note that since a JSON literal may (and in v8 will) keep the
  // surrounding page's context alive we do a clone here and throw the
  // original object away.
  this.info = cloneJson(info);
  this.status = TestResult.RUNNING;
  this.message = null;
}

TestResult.prototype.asExpected = function () {
  return (this.status == TestResult.COMPLETED) || this.info.isNegative;
};

TestResult.RUNNING = "running";
TestResult.COMPLETED = "completed";
TestResult.FAILED = "failed";
TestResult.ABORTED = "aborted";

// --- T e s t   R u n ---

function Promise(valueOpt) {
  this.hasValue = false;
  this.value = undefined;
  this.onValues = [];
};

Promise.prototype.fulfill = function (value) {
  this.hasValue = true;
  this.value = value;
  this.fire();
};

Promise.prototype.fire = function () {
  for (var i = 0; i < this.onValues.length; i++) {
    var pair = this.onValues[i];
    pair[1].call(pair[0], this.value);
  }
  this.onValues.length = 0;
};

Promise.prototype.onValue = function (self, fun) {
  if (this.hasValue) {
    fun.call(self, this.value);
  } else {
    this.onValues.push([self, fun]);
  }
};

function makeRequest(path) {
  var result = new Promise();
  var xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4 && xhr.status == 200) {
      result.fulfill(parseJson(xhr.responseText));
    }
  };
  xhr.open("GET", path, true);
  xhr.send("");
  return result;
}

function TestCase(run, serial, data) {
  this.run_ = run;
  this.serial_ = serial;
  this.data_ = data;
}

TestCase.prototype.getHtmlUrl = function () {
  var suite = this.run_.getSuiteName();
  return "cases/" + suite + "/" + this.serial_ + ".html";
};

TestCase.prototype.getSource = function () {
  var rawSource = this.data_.source;
  var source = rawSource.replace(/\$ERROR/g, 'testFailed');
  var source = rawSource.replace(/\$FAIL/g, 'testFailed');
  var source = rawSource.replace(/\$PRINT/g, 'testPrint');
  source += "\ntestCompleted();";
  return source;
};

TestCase.prototype.getName = function () {
  return this.data_.name;
};

TestCase.prototype.isNegative = function () {
  return this.data_.isNegative;
};

TestCase.prototype.toString = function() {
  return "a TestCase { id = " + this.serial_ + " }";
};

function TestChunk(run, from, to) {
  this.run_ = run;
  this.from_ = from;
  this.to_ = to;
  this.state_ = TestChunk.EMPTY;
  this.pLoaded_ = new Promise();
  this.data_ = null;
};

TestChunk.EMPTY = "empty";
TestChunk.LOADING = "loading";
TestChunk.LOADED = "loaded";

TestChunk.prototype.ensureLoaded = function () {
  if (this.state_ === TestChunk.EMPTY) {
    this.state_ = TestChunk.LOADING;
    var path = 'cases/' + this.run_.getSuiteName() + '/' + this.from_ + '-' + this.to_ + '.json';
    var pGotRequest = makeRequest(path);
    pGotRequest.onValue(this, function (value) {
      this.state_ = TestChunk.LOADED;
      for (var i = this.from_; i < this.to_; i++)
        this.run_.registerCase(i, new TestCase(this.run_, i, value[i - this.from_]));
      this.pLoaded_.fulfill(null);
    });
  }
  return this.pLoaded_;
};

function TestRun(suite, progress, crosshair, isResumable) {
  this.suite_ = suite;
  this.current_ = 0;
  this.progress_ = progress;
  this.crosshair_ = crosshair;
  this.progress_.setMax(suite.count);
  this.doneCount_ = 0;
  this.failedCount_ = 0;
  this.succeededCount_ = 0;
  this.runs_ = {};
  this.abort_ = false;
  this.cases_ = {};
  this.chunks_ = [];
  this.passFailVector_ = [];
  this.initializeChunks();
}

TestRun.prototype.getSuiteName = function () {
  return this.suite_.name;
};

TestRun.prototype.registerCase = function (index, test) {
  this.cases_[index] = test;
};

TestRun.prototype.initializeChunks = function () {
  var count = this.suite_.count;
  for (var i = 0, c = 0; i < count; i += kTestCaseChunkSize, c++) {
    var to = Math.min(i + kTestCaseChunkSize, count);
    var chunk = new TestChunk(this, i, to);
    this.chunks_[c] = chunk;
  }
};

TestRun.prototype.ensureChunkLoaded = function (index) {
  var result = this.chunks_[index].ensureLoaded();
  var limit = Math.min(index + kChunkAheadCount + 1, this.chunks_.length);
  for (var i = index + 1; i < limit; i++)
    this.chunks_[i].ensureLoaded();
  return result;
};

TestRun.prototype.getTestCase = function (index) {
  var pResult = new Promise();
  var chunkIndex = Math.floor(index / kTestCaseChunkSize);
  var pLoadedTestCases = this.ensureChunkLoaded(chunkIndex);
  pLoadedTestCases.onValue(this, function () {
    pResult.fulfill(this.cases_[index]);
  });
  return pResult;
};

TestRun.prototype.start = function () {
  this.scheduleNextTest();
};

TestRun.prototype.runTest = function (serial, testCase) {
  return new Runner(this, serial, testCase).schedule();
};

TestRun.prototype.calculateCurrentDistances = function (plotter) {
  var dists = [];
  var results = this.passFailVector_;
  for (var i = 0; i < plotter.results.length; i++) {
    var dist = calcVectorDistance(results, plotter.results[i]);
    dists.push(dist);
  }
  return dists;
};

TestRun.prototype.scheduleNextTest = function () {
  if (this.current_ > this.suite_.count) return;
  if (this.current_ % kCrosshairUpdateInterval == 0) {
    var distances = this.calculateCurrentDistances(plotter);
    var pnt = plotter.placeByDistance(distances);
    this.crosshair_.setPosition(pnt.x / 2 + 50, pnt.y / 2 + 50);
  }
  var serial = this.current_++;
  delay(this, function () {
    var pCase = this.getTestCase(serial);
    pCase.onValue(this, function (value) {
      var pDoneRunning = this.runTest(serial, value);
      pDoneRunning.onValue(this, function () {
        this.scheduleNextTest();
      });
    });
  });
};

function parseTestResults(progress) {
  var splitter = progress.indexOf(':');
  var count = parseInt(progress.substring(0, splitter));
  var data = progress.substring(splitter + 1);
  var bits = parseTestSignature(count, data);
  assert(count == bits.length);
  return bits;
}

TestRun.prototype.fastForward = function (progress) {
  var splitter = progress.indexOf(':');
  var count = parseInt(progress.substring(0, splitter));
  var data = progress.substring(splitter + 1);
  var bits = parseTestSignature(count, data);
  assert(count == bits.length);
  // We force the last test to have been failed.
  bits.push(false);
  for (var i = 0; i < bits.length; i++) {
    var info = {serial: 0, isNegative: false};
    var result = new TestResult(info);
    if (bits[i]) {
      result.status = TestResult.COMPLETED;
      this.succeededCount_++;
    } else {
      result.status = TestResult.FAILED;
      this.failedCount_++;
    }
    this.results[i] = result;
  }
  this.doneCount = bits.length;
  this.current = bits.length;
  this.updateUi();
};

TestRun.prototype.updateUi = function () {
  this.progress_.setValue(this.doneCount_);
  document.getElementById('failed').innerHTML = this.failedCount_;
  document.getElementById('succeeded').innerHTML = this.succeededCount_;
  document.getElementById('total').innerHTML = this.doneCount_;
};

var kBase64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

function base64Char(index) {
  assert(0 <= index);
  assert(index < kBase64Chars.length);
  return kBase64Chars.charAt(index);
}

function indexOfBase64Char(c) {
  var result = kBase64Chars.indexOf(c);
  assert(result != -1);
  return result;
}

TestRun.prototype.getFailedBits = function (count) {
  var result = [];
  for (var i = 0; i < count; i++)
    result[i] = this.results[i].asExpected();
  return result;
};

TestRun.prototype.getSignature = function (count) {
  var result = [];
  var zeroChunkCount = 0;
  function flushZeroChunks() {
    if (zeroChunkCount == 0) {
      return;
    } else if (zeroChunkCount == 1) {
      result.push(kBase64Chars.charAt(0));
      zeroChunkCount = 0;
    } else {
      result.push('*' + base64Char(zeroChunkCount));
      zeroChunkCount = 0;
    }
  }
  for (var i = 0; i < count; i += 6) {
    // Collect the next chunk of 6 test results
    var chunk = 0;
    for (var j = 0; (j < 6) && (i + j < count); j++) {
      var failed = !this.results[i + j].asExpected();
      chunk = chunk | ((failed ? 1 : 0) << j);
    }
    if (chunk == 0) {
      if (zeroChunkCount == 63)
        flushZeroChunks();
      zeroChunkCount++;
    } else {
      flushZeroChunks();
      result.push(base64Char(chunk));
    }
  }
  var data = result.join("");
  assert(String(parseTestSignature(count, data)) == String(this.getFailedBits(count)));
  return count + ":" + data;
};

function parseTestSignature(count, data) {
  var i = 0;
  var result = [ ];
  while (i < data.length) {
    var c = data.charAt(i++);
    if (c == '*') {
      var next = data.charAt(i++);
      var zeroChunkCount = indexOfBase64Char(next);
      for (var j = 0; j < 6 * zeroChunkCount; j++) {
        result.push(true);
      }
    } else {
      var bits = indexOfBase64Char(c);
      for (var j = 0; j < 6 && result.length < count; j++) {
        result.push((bits & (1 << j)) == 0);
      }
    }
  }
  while (result.length < count)
    result.push(true);
  return result;
}

TestRun.prototype.allDone = function () {
  if (this.isResumable)
    deleteCookie(kTestStatusCookieName);
  var signature = this.getSignature(this.suite_.count);
  var sigDiv = document.createElement('div');
  sigDiv.innerHTML = "[" + signature + "]";
  document.body.appendChild(sigDiv);
};

TestRun.prototype.updateCounts = function (runner) {
  this.doneCount_++;
  var hadUnexpectedResult = runner.hasUnexpectedResult();
  this.passFailVector_[runner.serial_] = !hadUnexpectedResult;
  if (hadUnexpectedResult) {
    this.failedCount_++;
    var message = runner.getMessage();
    function onErrorClicked() { runner.openTestPage(); }
    this.reportFailure(this, onErrorClicked, message);
  } else {
    this.succeededCount_++;
  }
};

TestRun.prototype.testDone = function (runner) {
  this.updateCounts(runner);
  this.updateUi();
  if (this.doneCount == this.suite_.count) {
    // Complete the test run on a timeout to allow the ui to update with
    // the last results first.
    delay(this, function () { this.allDone(); });
  }
};

TestRun.prototype.reportFailure = function (self, fun, message) {
  var errors = gebi('errors');
  var row = errors.insertRow(0);
  row.className = 'logLine';
  var col = row.insertCell(0);
  col.innerHTML = message;
  col.style.overflow = "hidden";
  col.onclick = function () { fun.call(self); };
};

function gebi(id) {
  return document.getElementById(id);
}

function getProgressCookie() {
  return getCookie(kTestStatusCookieName);
}

function getResumePoint() {
  var isResumable = false;
  if (isResumable) {
    var cookie = getProgressCookie();
    if (cookie) {
      var count = parseInt(cookie.substring(0, cookie.indexOf(":")));
      return (count + 1);
    }
  }
}

function resumableClicked() {
  var resumePoint = getResumePoint();
  var controls = gebi("resumeControls");
  if (resumePoint) {
    gebi("interruptCount").innerHTML = resumePoint;
    controls.className = "";
  } else {
    controls.className = "hidden";
  }
}

function Crosshair() {
  try {
    this.element_ = this.makeElement();
  } catch (e) {
    this.element_ = null;
  }
}

Crosshair.prototype.makeElement = function () {
  function color(c) {
    c.setAttribute('stroke-width', 0.25);
    c.setAttribute('stroke', '#ff0000');
    c.setAttribute('opacity', 0.5);
    c.setAttribute('fill', 'none');
  }
  var g = document.createElementNS(svgns, 'g');
  var c1 = document.createElementNS(svgns, 'circle');
  c1.setAttribute('cx', 0);
  c1.setAttribute('cy', 0);
  c1.setAttribute('r', 1);
  color(c1);
  g.appendChild(c1);
  var c2 = document.createElementNS(svgns, 'circle');
  c2.setAttribute('cx', 0);
  c2.setAttribute('cy', 0);
  c2.setAttribute('r', 2);
  color(c2);
  g.appendChild(c2);
  var l1 = document.createElementNS(svgns, 'line');
  l1.setAttribute('x1', -3);
  l1.setAttribute('y1', 0);
  l1.setAttribute('x2', 3);
  l1.setAttribute('y2', 0);
  color(l1);
  g.appendChild(l1);
  var l2 = document.createElementNS(svgns, 'line');
  l2.setAttribute('x1', 0);
  l2.setAttribute('y1', -3);
  l2.setAttribute('x2', 0);
  l2.setAttribute('y2', 3);
  color(l2);
  g.appendChild(l2);
  return g;
};

Crosshair.prototype.setPosition = function (x, y) {
  try {
    var transform = 'translate(' + x + ',' + y + ')'
    this.element_.setAttribute('transform', transform);
  } catch (e) {
    // ignore
  }
};

Crosshair.prototype.appendTo = function (root) {
  try {
    root.appendChild(this.element_);
  } catch (e) {
    // ignore
  }
};

function moreInfo() {
  gebi('runDetails').className = undefined;
}

function run() {
  // Create crosshair
  var p = document.getElementById('plot');
  var crosshair = new Crosshair();
  crosshair.setPosition(50, 50);
  var l = p.contentDocument.getElementById('outer');
  crosshair.appendTo(l);

  var runControls = gebi('runControls');
  runControls.className = undefined;
  gebi('message').className = "hidden";
  var progress = new ProgressBar();
  progress.replaceInto(runControls, gebi('progressPlaceholder'));
  var isResumable = false;
  sputnikTestController = new TestRun(defaultTestSuite,
                                      progress,
                                      crosshair,
                                      isResumable);
  var resumePoint = getResumePoint();
  if (resumePoint) {
    sputnikTestController.fastForward(getProgressCookie());
  } else {
    deleteCookie(kTestStatusCookieName);
  }
  sputnikTestController.start();
}

function loaded() {
  plotter = new Plotter(results);
  plotter.placeFixpoints();
  var root = gebi('plotBox');
  plotter.displayOn(root);
}
