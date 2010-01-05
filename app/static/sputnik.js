// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

var kRotation = 2.14;
var kIterations = 50;
var kTestCaseChunkSize = 128;
var kTestListAppendSize = 64;
var kChunkAheadCount = 1;

var plotter = null;

function gebi(id) {
  return document.getElementById(id);
}

function Persistent(key) {
  this.keyEq_ = key + "=";
}

Persistent.prototype.get = function () {
  var parts = document.cookie.split(/\s*;\s*/);
  for (var i = 0; i < parts.length; i++) {
    if (parts[i].substring(0, this.keyEq_.length) == this.keyEq_)
      return parts[i].substring(this.keyEq_.length);
  }
};

Persistent.prototype.set = function (value, expiryHoursOpt) {
  var expiryHours = expiryHoursOpt || 6;
  var date = new Date();
  date.setTime(date.getTime() + (expiryHours * 60 * 60 * 1000));
  var expiry = "expires=" + date.toGMTString();
  document.cookie = this.keyEq_ + value + ";" + expiry + "; path=/";
};

Persistent.prototype.clear = function () {
  document.cookie = this.keyEq_ + "; path=/";
};

var storedTestStatus = new Persistent("sputnik_test_status");

function SputnikTestFailed(message) {
  this.message_ = message;
};

function BrowserData(name, type, data) {
  this.name = name;
  this.type = type;
  this.data = data;
}

function delay(timeoutOpt) {
  var pResult = new Promise();
  var timeout = (timeoutOpt === undefined) ? 0 : timeoutOpt;
  window.setTimeout(function () { pResult.fulfill(null); }, timeout);
  return pResult;
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

//var results = [
    //  new BrowserData('Chrome4Linux', 'cm', '5246:oAQ*/*/*/*uD*FE*Lw*DgC*Cc*cQ*vE/uAQoGDAF*V//8+/B*JQKQQCEBR*LBAEIQggAEEIIggABEQIQABBCIIEIIQgABEIIggACEEIAhAC*IkUQQYgNg*Xg*EgB*GB*EgEFISB*IB*PD*dY*DIE*CQC*HS*OB*CCY*CDAK*CE*CQ*DgBAD*DC*CI*EFAK*DI*DC*CJ*EQ*DFAgBg*OCACAg*CC*CU*JQ*DR*EE*GR*CG*DQg*Cw*DE*DgAE*DC*CI*DBAIz/2FIAQQAwD*CC*DF*CQQRIBhR*Qi*HE*CQC*CDB*EC*DC*CU*DF*CG*DoAME*FE*CI*CgC*UgQ'),
//  new BrowserData('Firefox35Win', 'fx', '5246:B*EI*dg*Kg*GC*nk*/*FEAE*PgAI*IQ*uD*FE*Lw*DgC*CM*Ew///Dg*IE*JQ*vE/uAQoGDIF*IQ*DkxIie*E//8//B*CH*GQKQQCEBR*/*DkUQQYgN*Yg*EgB*GB*EYB*Dg*Iy*PD*MU*QY*CQAE*CQC*GgS*DIQ*CB*GB*GDAK*CE*CQ*DgBAD*CICAEJ*EFAK*DMACACoAJ*Ew*DFAgBgE*LQACAGgg*CCCAUI*CE*DEAQ*CBRB*DE*GR*CG*DQg*Cw*DE*DkAE*DCAII*DBAIz/2FIAQQAwD*CC*DF*CQQRIBhR*Qi*FI*EQC*CDBQAgICAIQC*CU*DFABGAgAoANE*FE*CJEAgC*KQ*CgC*FgQ'),
//  new BrowserData('Safari4Mac', 'sf', '
//  new BrowserData('IE8Win', 'ie', '
//  new BrowserData('Opera', 'op', '
//];

function TestRunSignature(signature) {
  var splitter = signature.indexOf(':');
  this.count_ = parseInt(signature.substring(0, splitter));
  this.data_ = signature.substring(splitter + 1);
  this.vector_ = null;
}

TestRunSignature.prototype.count = function () {
  return this.count_;
};

TestRunSignature.prototype.getVector = function () {
  return parseTestSignature(this.count_, this.data_);
};

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
  var scores = this.calcDistanceMatrix();
  this.calcInitialPositions(scores);
  this.runLassesSpringyAlgorithm(scores, true);
  for (var i = 0; i < this.positions.length; i++)
    this.positions[i].isFixed = true;
};

Plotter.prototype.displayOn = function (root) {
  var elm = document.createElement('object', true);
  elm.setAttribute('width', 500);
  elm.setAttribute('height', 500);
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

function reportError(str) {
  alert(str);
}

function assert(value) {
  if (!value) {
    alert("Assertion failed");
    (undefined).foo; // force debugger
  }
}

// --- R u n n e r ---

var runnerTraits = { };

function inheritTraits(fun, traits) {
  for (var name in traits)
    fun.prototype[name] = traits[name];
}

runnerTraits.openTestPage = function () {
  window.open(this.testCase_.getHtmlUrl(), '_blank');
}

function MockRunner(serial, result) {
  this.serial_ = serial;
  this.result_ = result;
}

inheritTraits(MockRunner, runnerTraits);

MockRunner.prototype.hasUnexpectedResult = function () {
  return !this.result_;
};

MockRunner.prototype.getMessage = function () {
  return "Test " + this.serial_ + " failed.";
};

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

inheritTraits(Runner, runnerTraits);

Runner.prototype.testPrint = function (str) {
  this.printed_.push(str);
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
  if (!this.hasCompleted_)
    this.hasFailed_ = true;
  if (this.testRun_)
    this.testRun_.testDone(this);
  this.pResult_.fulfill(null);
  if (this.root_)
    this.root_.removeChild(this.iframe_);
};

Runner.prototype.testCompleted = function () {
  this.hasCompleted_ = true;
};

Runner.prototype.testFailed = function (message) {
  this.recordFailure(message);
  throw new SputnikTestFailed(message);
};

Runner.prototype.recordFailure = function (message) {
  if (!this.hasFailed_) {
    this.hasFailed_ = true;
    this.failedMessage_ = message;
  }
};

Runner.prototype.hasUnexpectedResult = function () {
  var hasSucceeded = this.hasCompleted_ && !this.hasFailed_;
  var isPositive = !this.testCase_.isNegative();
  return isPositive !== hasSucceeded;
};

Runner.prototype.inject = function (code) {
  var doc = this.iframe_.contentWindow.document;
  try {
    doc.write('<script>' + code + '</script>');
  } catch (e) {
    this.recordFailure(String(e));
  }
};

Runner.prototype.schedule = function () {
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
  var pResult = new Promise();
  goog.net.XhrIo.send(path, function () {
    var obj = this.getResponseJson();
    pResult.fulfill(obj);
  });
  return pResult;
}

function TestCase(run, serial, data) {
  this.run_ = run;
  this.serial_ = serial;
  this.data_ = data;
  this.description_ = null;
}

TestCase.prototype.getHtmlUrl = function () {
  var suite = this.run_.getSuiteName();
  return "cases/" + suite + "/" + this.serial_ + ".html";
};

TestCase.prototype.getSource = function () {
  var rawSource = this.data_.source;
  var source = rawSource.replace(/\$ERROR/g, 'testFailed');
  var source = source.replace(/\$FAIL/g, 'testFailed');
  var source = source.replace(/\$PRINT/g, 'testPrint');
  source += "\ntestCompleted();";
  return source;
};

TestCase.prototype.getName = function () {
  return this.data_.name;
};

TestCase.prototype.getDescription = function () {
  if (!this.description_) {
    var result;
    var match = /@description:(.*)$/m.exec(this.data_.source);
    if (!match) {
      result = "";
    } else {
      var str = match[1];
      var stripped = /^\s*(.*)\s*;$/.exec(str);
      result = stripped ? stripped[1] : str;
    }
    this.description_ = result;
  }
  return this.description_;
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

function TestQuery(suite) {
  this.suite_ = suite;
  this.chunks_ = [];
  this.cases_ = {};
  this.initializeChunks();
}

TestQuery.prototype.getSize = function () {
  return this.suite_.count;
};

TestQuery.prototype.initializeChunks = function () {
  var count = this.suite_.count;
  for (var i = 0, c = 0; i < count; i += kTestCaseChunkSize, c++) {
    var to = Math.min(i + kTestCaseChunkSize, count);
    var chunk = new TestChunk(this, i, to);
    this.chunks_[c] = chunk;
  }
};

TestQuery.prototype.registerCase = function (index, test) {
  this.cases_[index] = test;
};

TestQuery.prototype.ensureChunkLoaded = function (index) {
  var result = this.chunks_[index].ensureLoaded();
  var limit = Math.min(index + kChunkAheadCount + 1, this.chunks_.length);
  for (var i = index + 1; i < limit; i++)
    this.chunks_[i].ensureLoaded();
  return result;
};

TestQuery.prototype.getTestCase = function (index) {
  var pResult = new Promise();
  var chunkIndex = Math.floor(index / kTestCaseChunkSize);
  var pLoadedTestCases = this.ensureChunkLoaded(chunkIndex);
  pLoadedTestCases.onValue(this, function () {
    pResult.fulfill(this.cases_[index]);
  });
  return pResult;
};

TestQuery.prototype.getEntry = function (serial) {
  var pResult = new Promise();
  this.getTestCase(serial).onValue(this, function (test) {
    pResult.fulfill(toDataEntry(test, TestPanelEntry.NONE));
  });
  return pResult;
};

TestQuery.prototype.getSuiteName = function () {
  return this.suite_.name;
};

TestQuery.prototype.size = function () {
  return this.suite_.count;
}

function TestRun(data, progress) {
  this.current_ = 0;
  this.progress_ = progress;
  this.data_ = data;
  this.doneCount_ = 0;
  this.failedTests_ = [];
  this.succeededCount_ = 0;
  this.runs_ = {};
  this.abort_ = false;
  this.passFailVector_ = [];
}

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
  if (this.current_ >= this.size()) return;
  var serial = this.current_++;
  var pDelay = delay();
  pDelay.onValue(this, function () {
    var pCase = this.getTestCase(serial);
    pCase.onValue(this, function (value) {
      var pDoneRunning = this.runTest(serial, value);
      pDoneRunning.onValue(this, function () {
        this.scheduleNextTest();
      });
    });
  });
};

TestRun.prototype.getTestCase = function (serial) {
  return this.data_.getTestCase(serial);
};

TestRun.prototype.size = function () {
  return this.data_.size();
};

function parseTestResults(progress) {
  var splitter = progress.indexOf(':');
  var count = parseInt(progress.substring(0, splitter));
  var data = progress.substring(splitter + 1);
  var bits = parseTestSignature(count, data);
  assert(count == bits.length);
  return bits;
}

TestRun.prototype.fastForward = function (target) {
  var bits = target.getVector();
  // We force the last test to have been failed.
  bits.push(false);
  for (var i = 0; i < bits.length; i++) {
    var runner = new MockRunner(i, bits[i]);
    this.testDone(runner, true);
  }
  this.current_ = bits.length;
  this.updateUi();
};

TestRun.prototype.setTestList = function (testList) {
  this.testList_ = testList;
};

TestRun.prototype.updateUi = function () {
  this.progress_.setValue((100 * this.doneCount_ / this.size()) | 0);
  this.testList_.addPendingEntries();
  document.getElementById('failed').innerHTML = this.failedTests_.length;
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

TestRun.prototype.getTestStatus = function (index) {
  return this.passFailVector_[index];
};

TestRun.prototype.getFailedBits = function (count) {
  var result = [];
  for (var i = 0; i < count; i++)
    result[i] = this.getTestStatus(i);
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
      var failed = !this.getTestStatus(i + j);
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
  /*
  var signature = this.getSignature(this.suite_.count);
  var sigDiv = document.createElement('div');
  sigDiv.innerHTML = "[" + signature + "]";
  document.body.appendChild(sigDiv);
  */
  this.progress_.setText("Done");
  storedTestStatus.clear();
};

TestRun.prototype.updateCounts = function (runner) {
  this.doneCount_++;
  var hadUnexpectedResult = runner.hasUnexpectedResult();
  this.passFailVector_[runner.serial_] = !hadUnexpectedResult;
  if (hadUnexpectedResult) {
    this.failedTests_.push(runner.serial_);
    var message = runner.getMessage();
    function onErrorClicked() { runner.openTestPage(); }
    this.reportFailure(this, onErrorClicked, message);
  } else {
    this.succeededCount_++;
  }
  var resultSignature = this.getSignature(runner.serial_);
  storedTestStatus.set(resultSignature);
};

TestRun.prototype.testDone = function (runner, silent) {
  this.updateCounts(runner);
  if (!silent)
    this.updateUi();
  if (this.doneCount_ >= this.size()) {
    // Complete the test run on a timeout to allow the ui to update with
    // the last results first.
    var pDelay = delay();
    pDelay.onValue(this, function () {
      this.allDone();
    });
  }
};

TestRun.prototype.getResultData = function () {
  return new TestRunData(this);
};

TestRun.prototype.reportFailure = function (self, fun, message) {

};

function TestRunData(run) {
  this.run_ = run;
}

TestRunData.prototype.size = function () {
  return this.run_.failedTests_.length;
};

function toDataEntry(test, status) {
  return {
    getName: function () { return test.getName(); },
    getDescription: function () { return test.getDescription(); },
    getSource: function () { return test.getSource(); },
    getStatus: function () { return status; }
  };
}

TestRunData.prototype.getEntry = function (serial) {
  var pResult = new Promise();
  this.run_.getTestCase(this.run_.failedTests_[serial]).onValue(this, function (test) {
    pResult.fulfill(toDataEntry(test, TestPanelEntry.FAILED));
  });
  return pResult;
};

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
  // Resume if required
  var storedStatus = storedTestStatus.get();
  var doResume = storedStatus && gebi('resume').checked;
  var lastResult;
  if (doResume)
    lastResult = new TestRunSignature(storedStatus);

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
  var testRun = new TestRun(defaultTestSuite,
                            progress,
                            crosshair);
  if (doResume)
    testRun.fastForward(lastResult);

  testRun.start();
}

function TestPanel(data, element) {
  this.data_ = data;
  this.entries_ = [];
  this.element_ = element;
  this.targetCount_ = kTestListAppendSize;
  this.decorate();
}

TestPanel.prototype.decorate = function () {
  var elm = this.element_;
  elm.className += ' test-panel';
  var self = this;
  elm.onscroll = function (event) { self.onScroll(event); };
  this.element_ = elm;
};

TestPanel.prototype.onScroll = function (event) {
  var elm = this.element_;
  var remaining = elm.scrollHeight - elm.scrollTop - elm.clientHeight;
  if (remaining < 100) {
    this.targetCount_ = this.entries_.length + kTestListAppendSize;
    this.addPendingEntries();
  }
};

TestPanel.prototype.element = function () {
  return this.element_;
};

TestPanel.prototype.addPendingEntries = function () {
  var end = Math.min(this.targetCount_, this.data_.size());
  for (var i = this.entries_.length; i < end; i++) {
    var entry = new TestPanelEntry(this, i);
    entry.appendTo(this.element_);
    this.entries_.push(entry);
  }
};

function TestPanelEntry(panel, serial) {
  this.panel_ = panel;
  this.serial_ = serial;
  this.isOpen_ = false;
  this.details_ = null;
  this.create();
}

TestPanelEntry.NONE = 'none';
TestPanelEntry.FAILED = 'failed';

TestPanelEntry.prototype.getData = function () {
  return this.panel_.data_.getEntry(this.serial_);
}

TestPanelEntry.prototype.create = function () {
  var elm = document.createElement('div');
  elm.className = 'test-panel-entry';

  var header = document.createElement('div');
  header.className = 'test-panel-entry-header';
  var self = this;
  header.onclick = function () { self.onClick() };
  elm.appendChild(header);

  var status = document.createElement('div');
  status.className = 'test-panel-entry-status test-panel-entry-status-none';
  header.appendChild(status);
  var title = document.createElement('div');
  title.className = 'test-panel-entry-text loading';
  header.appendChild(title);
  title.innerHTML = "Loading...";
  this.element_ = elm;
  this.getData().onValue(this, function (test) {
    title.className = 'test-panel-entry-text';
    title.innerHTML = test.getName() + " <span class='description'> - " + test.getDescription() + "</span>";
    status.className = 'test-panel-entry-status test-panel-entry-status-' + test.getStatus();
  });
};

TestPanelEntry.prototype.ensureDetails = function () {
  if (this.details_) return;
  this.details_ = document.createElement('table');
  this.details_.className = 'test-panel-details';
  var row = this.details_.insertRow();
  var status = row.insertCell(0);
  status.className = 'test-panel-entry-details-status';
  var source = row.insertCell(1);
  source.className = 'test-panel-source';
  var sourceDiv = document.createElement('div');
  sourceDiv.style.marginLeft = '3px';
  source.appendChild(sourceDiv);
  this.getData().onValue(this, function (test) {
    var text = test.getSource();
    sourceDiv.innerHTML = text.replace(/[\n\r\f]/g, '<br/>');
  });
};

TestPanelEntry.prototype.showDetails = function () {
  this.ensureDetails();
  this.element_.appendChild(this.details_);
};

TestPanelEntry.prototype.hideDetails = function () {
  this.element_.removeChild(this.details_);
};

TestPanelEntry.prototype.onClick = function () {
  this.getData().onValue(this, function (test) {
    if (this.isOpen_) {
      this.hideDetails();
    } else {
      this.showDetails();
    }
    this.isOpen_ = !this.isOpen_;
  });
};

TestPanelEntry.prototype.appendTo = function (elm) {
  elm.appendChild(this.element_);
};

function ProgressBar(outer, label) {
  this.outer_ = outer;
  this.label_ = label;
  this.control_ = new goog.ui.ProgressBar();
  this.control_.decorate(this.outer_);
}

ProgressBar.prototype.setValue = function (value) {
  this.control_.setValue(value);
  this.setText(value + '%');
};

ProgressBar.prototype.setText = function (value) {
  this.label_.innerHTML = value;
};

function start() {
  testRun.start();
}

var testRun;
function loaded() {
  var selectors = ['about', 'browse', 'run', 'compare'];
  var bevel = 10;
  for (var i = 0; i < selectors.length; i++) {
    (function () { // I really need an inner scope here!
      var id = selectors[i];
      var elm = goog.dom.getElement(id);
      var panel = goog.ui.RoundedPanel.create(bevel, 1, '#cccccc', '#ffffff', 15);
      elm.onclick = function () { window.location = '/' + id };
      panel.decorate(elm);
    })();
  }
  var mainPanel = goog.ui.RoundedPanel.create(bevel, 1, '#cccccc', '#ffffff', 15);
  mainPanel.decorate(goog.dom.getElement('contents'));
  var suite = new TestQuery(defaultTestSuite);
  var runcontrols = gebi('runcontrols');
  if (runcontrols) {
    var bar = new ProgressBar(runcontrols, gebi('progress'));
    bar.setValue(0);
    testRun = new TestRun(suite, bar);
  }
  var testlist = gebi('testlist');
  if (testlist) {
    var data;
    if (testRun) {
      data = testRun.getResultData();
    } else {
      data = suite;
    }
    var output = new TestPanel(data, testlist);
    output.addPendingEntries();
    if (testRun)
      testRun.setTestList(output);
  }
  var button = gebi('button');
  if (button) {
    var b = goog.ui.decorate(button);
    goog.events.listen(b, goog.ui.Component.EventType.ACTION, function (e) {
      b.setEnabled(false);
      start();
    });
  }
  var plotBox = gebi('plotBox');
  if (plotBox) {
    var plotter = new Plotter(results);
    plotter.placeFixpoints();
    plotter.displayOn(plotBox);
  }
}
