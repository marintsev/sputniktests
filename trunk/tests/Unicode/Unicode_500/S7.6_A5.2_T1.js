// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S7.6_A5.2_T1;
 * @section: 7.6, 6;
 * @assertion: If a \UnicodeEscapeSequence sequence were replaced by its UnicodeEscapeSequence's CV, the result must still be a valid Identifier that has the exact same sequence of characters as the original Identifier;
 * @description: The CV of UnicodeEscapeSequence is Lu.
 * Complex test with eval;
*/

//CHECK
Lu = [[0x0041, 0x005A], [0x00C0, 0x00D6], [0x00D8, 0x00DE], [0x0100, 0x0100], [0x0102, 0x0102], [0x0104, 0x0104], [0x0106, 0x0106], [0x0108, 0x0108], [0x010A, 0x010A], [0x010C, 0x010C], [0x010E, 0x010E], [0x0110, 0x0110], [0x0112, 0x0112], [0x0114, 0x0114], [0x0116, 0x0116], [0x0118, 0x0118], [0x011A, 0x011A], [0x011C, 0x011C], [0x011E, 0x011E], [0x0120, 0x0120], [0x0122, 0x0122], [0x0124, 0x0124], [0x0126, 0x0126], [0x0128, 0x0128], [0x012A, 0x012A], [0x012C, 0x012C], [0x012E, 0x012E], [0x0130, 0x0130], [0x0132, 0x0132], [0x0134, 0x0134], [0x0136, 0x0136], [0x0139, 0x0139], [0x013B, 0x013B], [0x013D, 0x013D], [0x013F, 0x013F], [0x0141, 0x0141], [0x0143, 0x0143], [0x0145, 0x0145], [0x0147, 0x0147], [0x014A, 0x014A], [0x014C, 0x014C], [0x014E, 0x014E], [0x0150, 0x0150], [0x0152, 0x0152], [0x0154, 0x0154], [0x0156, 0x0156], [0x0158, 0x0158], [0x015A, 0x015A], [0x015C, 0x015C], [0x015E, 0x015E], [0x0160, 0x0160], [0x0162, 0x0162], [0x0164, 0x0164], [0x0166, 0x0166], [0x0168, 0x0168], [0x016A, 0x016A], [0x016C, 0x016C], [0x016E, 0x016E], [0x0170, 0x0170], [0x0172, 0x0172], [0x0174, 0x0174], [0x0176, 0x0176], [0x0178, 0x0179], [0x017B, 0x017B], [0x017D, 0x017D], [0x0181, 0x0182], [0x0184, 0x0184], [0x0186, 0x0187], [0x0189, 0x018B], [0x018E, 0x0191], [0x0193, 0x0194], [0x0196, 0x0198], [0x019C, 0x019D], [0x019F, 0x01A0], [0x01A2, 0x01A2], [0x01A4, 0x01A4], [0x01A6, 0x01A7], [0x01A9, 0x01A9], [0x01AC, 0x01AC], [0x01AE, 0x01AF], [0x01B1, 0x01B3], [0x01B5, 0x01B5], [0x01B7, 0x01B8], [0x01BC, 0x01BC], [0x01C4, 0x01C4], [0x01C7, 0x01C7], [0x01CA, 0x01CA], [0x01CD, 0x01CD], [0x01CF, 0x01CF], [0x01D1, 0x01D1], [0x01D3, 0x01D3], [0x01D5, 0x01D5], [0x01D7, 0x01D7], [0x01D9, 0x01D9], [0x01DB, 0x01DB], [0x01DE, 0x01DE], [0x01E0, 0x01E0], [0x01E2, 0x01E2], [0x01E4, 0x01E4], [0x01E6, 0x01E6], [0x01E8, 0x01E8], [0x01EA, 0x01EA], [0x01EC, 0x01EC], [0x01EE, 0x01EE], [0x01F1, 0x01F1], [0x01F4, 0x01F4], [0x01F6, 0x01F8], [0x01FA, 0x01FA], [0x01FC, 0x01FC], [0x01FE, 0x01FE], [0x0200, 0x0200], [0x0202, 0x0202], [0x0204, 0x0204], [0x0206, 0x0206], [0x0208, 0x0208], [0x020A, 0x020A], [0x020C, 0x020C], [0x020E, 0x020E], [0x0210, 0x0210], [0x0212, 0x0212], [0x0214, 0x0214], [0x0216, 0x0216], [0x0218, 0x0218], [0x021A, 0x021A], [0x021C, 0x021C], [0x021E, 0x021E], [0x0220, 0x0220], [0x0222, 0x0222], [0x0224, 0x0224], [0x0226, 0x0226], [0x0228, 0x0228], [0x022A, 0x022A], [0x022C, 0x022C], [0x022E, 0x022E], [0x0230, 0x0230], [0x0232, 0x0232], [0x023A, 0x023B], [0x023D, 0x023E], [0x0241, 0x0241], [0x0243, 0x0246], [0x0248, 0x0248], [0x024A, 0x024A], [0x024C, 0x024C], [0x024E, 0x024E], [0x0386, 0x0386], [0x0388, 0x038A], [0x038C, 0x038C], [0x038E, 0x038F], [0x0391, 0x03A1], [0x03A3, 0x03AB], [0x03D2, 0x03D4], [0x03D8, 0x03D8], [0x03DA, 0x03DA], [0x03DC, 0x03DC], [0x03DE, 0x03DE], [0x03E0, 0x03E0], [0x03E2, 0x03E2], [0x03E4, 0x03E4], [0x03E6, 0x03E6], [0x03E8, 0x03E8], [0x03EA, 0x03EA], [0x03EC, 0x03EC], [0x03EE, 0x03EE], [0x03F4, 0x03F4], [0x03F7, 0x03F7], [0x03F9, 0x03FA], [0x03FD, 0x042F], [0x0460, 0x0460], [0x0462, 0x0462], [0x0464, 0x0464], [0x0466, 0x0466], [0x0468, 0x0468], [0x046A, 0x046A], [0x046C, 0x046C], [0x046E, 0x046E], [0x0470, 0x0470], [0x0472, 0x0472], [0x0474, 0x0474], [0x0476, 0x0476], [0x0478, 0x0478], [0x047A, 0x047A], [0x047C, 0x047C], [0x047E, 0x047E], [0x0480, 0x0480], [0x048A, 0x048A], [0x048C, 0x048C], [0x048E, 0x048E], [0x0490, 0x0490], [0x0492, 0x0492], [0x0494, 0x0494], [0x0496, 0x0496], [0x0498, 0x0498], [0x049A, 0x049A], [0x049C, 0x049C], [0x049E, 0x049E], [0x04A0, 0x04A0], [0x04A2, 0x04A2], [0x04A4, 0x04A4], [0x04A6, 0x04A6], [0x04A8, 0x04A8], [0x04AA, 0x04AA], [0x04AC, 0x04AC], [0x04AE, 0x04AE], [0x04B0, 0x04B0], [0x04B2, 0x04B2], [0x04B4, 0x04B4], [0x04B6, 0x04B6], [0x04B8, 0x04B8], [0x04BA, 0x04BA], [0x04BC, 0x04BC], [0x04BE, 0x04BE], [0x04C0, 0x04C1], [0x04C3, 0x04C3], [0x04C5, 0x04C5], [0x04C7, 0x04C7], [0x04C9, 0x04C9], [0x04CB, 0x04CB], [0x04CD, 0x04CD], [0x04D0, 0x04D0], [0x04D2, 0x04D2], [0x04D4, 0x04D4], [0x04D6, 0x04D6], [0x04D8, 0x04D8], [0x04DA, 0x04DA], [0x04DC, 0x04DC], [0x04DE, 0x04DE], [0x04E0, 0x04E0], [0x04E2, 0x04E2], [0x04E4, 0x04E4], [0x04E6, 0x04E6], [0x04E8, 0x04E8], [0x04EA, 0x04EA], [0x04EC, 0x04EC], [0x04EE, 0x04EE], [0x04F0, 0x04F0], [0x04F2, 0x04F2], [0x04F4, 0x04F4], [0x04F6, 0x04F6], [0x04F8, 0x04F8], [0x04FA, 0x04FA], [0x04FC, 0x04FC], [0x04FE, 0x04FE], [0x0500, 0x0500], [0x0502, 0x0502], [0x0504, 0x0504], [0x0506, 0x0506], [0x0508, 0x0508], [0x050A, 0x050A], [0x050C, 0x050C], [0x050E, 0x050E], [0x0510, 0x0510], [0x0512, 0x0512], [0x0531, 0x0556], [0x10A0, 0x10C5], [0x1E00, 0x1E00], [0x1E02, 0x1E02], [0x1E04, 0x1E04], [0x1E06, 0x1E06], [0x1E08, 0x1E08], [0x1E0A, 0x1E0A], [0x1E0C, 0x1E0C], [0x1E0E, 0x1E0E], [0x1E10, 0x1E10], [0x1E12, 0x1E12], [0x1E14, 0x1E14], [0x1E16, 0x1E16], [0x1E18, 0x1E18], [0x1E1A, 0x1E1A], [0x1E1C, 0x1E1C], [0x1E1E, 0x1E1E], [0x1E20, 0x1E20], [0x1E22, 0x1E22], [0x1E24, 0x1E24], [0x1E26, 0x1E26], [0x1E28, 0x1E28], [0x1E2A, 0x1E2A], [0x1E2C, 0x1E2C], [0x1E2E, 0x1E2E], [0x1E30, 0x1E30], [0x1E32, 0x1E32], [0x1E34, 0x1E34], [0x1E36, 0x1E36], [0x1E38, 0x1E38], [0x1E3A, 0x1E3A], [0x1E3C, 0x1E3C], [0x1E3E, 0x1E3E], [0x1E40, 0x1E40], [0x1E42, 0x1E42], [0x1E44, 0x1E44], [0x1E46, 0x1E46], [0x1E48, 0x1E48], [0x1E4A, 0x1E4A], [0x1E4C, 0x1E4C], [0x1E4E, 0x1E4E], [0x1E50, 0x1E50], [0x1E52, 0x1E52], [0x1E54, 0x1E54], [0x1E56, 0x1E56], [0x1E58, 0x1E58], [0x1E5A, 0x1E5A], [0x1E5C, 0x1E5C], [0x1E5E, 0x1E5E], [0x1E60, 0x1E60], [0x1E62, 0x1E62], [0x1E64, 0x1E64], [0x1E66, 0x1E66], [0x1E68, 0x1E68], [0x1E6A, 0x1E6A], [0x1E6C, 0x1E6C], [0x1E6E, 0x1E6E], [0x1E70, 0x1E70], [0x1E72, 0x1E72], [0x1E74, 0x1E74], [0x1E76, 0x1E76], [0x1E78, 0x1E78], [0x1E7A, 0x1E7A], [0x1E7C, 0x1E7C], [0x1E7E, 0x1E7E], [0x1E80, 0x1E80], [0x1E82, 0x1E82], [0x1E84, 0x1E84], [0x1E86, 0x1E86], [0x1E88, 0x1E88], [0x1E8A, 0x1E8A], [0x1E8C, 0x1E8C], [0x1E8E, 0x1E8E], [0x1E90, 0x1E90], [0x1E92, 0x1E92], [0x1E94, 0x1E94], [0x1EA0, 0x1EA0], [0x1EA2, 0x1EA2], [0x1EA4, 0x1EA4], [0x1EA6, 0x1EA6], [0x1EA8, 0x1EA8], [0x1EAA, 0x1EAA], [0x1EAC, 0x1EAC], [0x1EAE, 0x1EAE], [0x1EB0, 0x1EB0], [0x1EB2, 0x1EB2], [0x1EB4, 0x1EB4], [0x1EB6, 0x1EB6], [0x1EB8, 0x1EB8], [0x1EBA, 0x1EBA], [0x1EBC, 0x1EBC], [0x1EBE, 0x1EBE], [0x1EC0, 0x1EC0], [0x1EC2, 0x1EC2], [0x1EC4, 0x1EC4], [0x1EC6, 0x1EC6], [0x1EC8, 0x1EC8], [0x1ECA, 0x1ECA], [0x1ECC, 0x1ECC], [0x1ECE, 0x1ECE], [0x1ED0, 0x1ED0], [0x1ED2, 0x1ED2], [0x1ED4, 0x1ED4], [0x1ED6, 0x1ED6], [0x1ED8, 0x1ED8], [0x1EDA, 0x1EDA], [0x1EDC, 0x1EDC], [0x1EDE, 0x1EDE], [0x1EE0, 0x1EE0], [0x1EE2, 0x1EE2], [0x1EE4, 0x1EE4], [0x1EE6, 0x1EE6], [0x1EE8, 0x1EE8], [0x1EEA, 0x1EEA], [0x1EEC, 0x1EEC], [0x1EEE, 0x1EEE], [0x1EF0, 0x1EF0], [0x1EF2, 0x1EF2], [0x1EF4, 0x1EF4], [0x1EF6, 0x1EF6], [0x1EF8, 0x1EF8], [0x1F08, 0x1F0F], [0x1F18, 0x1F1D], [0x1F28, 0x1F2F], [0x1F38, 0x1F3F], [0x1F48, 0x1F4D], [0x1F59, 0x1F59], [0x1F5B, 0x1F5B], [0x1F5D, 0x1F5D], [0x1F5F, 0x1F5F], [0x1F68, 0x1F6F], [0x1FB8, 0x1FBB], [0x1FC8, 0x1FCB], [0x1FD8, 0x1FDB], [0x1FE8, 0x1FEC], [0x1FF8, 0x1FFB], [0x2102, 0x2102], [0x2107, 0x2107], [0x210B, 0x210D], [0x2110, 0x2112], [0x2115, 0x2115], [0x2119, 0x211D], [0x2124, 0x2124], [0x2126, 0x2126], [0x2128, 0x2128], [0x212A, 0x212D], [0x2130, 0x2133], [0x213E, 0x213F], [0x2145, 0x2145], [0x2183, 0x2183], [0x2C00, 0x2C2E], [0x2C60, 0x2C60], [0x2C62, 0x2C64], [0x2C67, 0x2C67], [0x2C69, 0x2C69], [0x2C6B, 0x2C6B], [0x2C75, 0x2C75], [0x2C80, 0x2C80], [0x2C82, 0x2C82], [0x2C84, 0x2C84], [0x2C86, 0x2C86], [0x2C88, 0x2C88], [0x2C8A, 0x2C8A], [0x2C8C, 0x2C8C], [0x2C8E, 0x2C8E], [0x2C90, 0x2C90], [0x2C92, 0x2C92], [0x2C94, 0x2C94], [0x2C96, 0x2C96], [0x2C98, 0x2C98], [0x2C9A, 0x2C9A], [0x2C9C, 0x2C9C], [0x2C9E, 0x2C9E], [0x2CA0, 0x2CA0], [0x2CA2, 0x2CA2], [0x2CA4, 0x2CA4], [0x2CA6, 0x2CA6], [0x2CA8, 0x2CA8], [0x2CAA, 0x2CAA], [0x2CAC, 0x2CAC], [0x2CAE, 0x2CAE], [0x2CB0, 0x2CB0], [0x2CB2, 0x2CB2], [0x2CB4, 0x2CB4], [0x2CB6, 0x2CB6], [0x2CB8, 0x2CB8], [0x2CBA, 0x2CBA], [0x2CBC, 0x2CBC], [0x2CBE, 0x2CBE], [0x2CC0, 0x2CC0], [0x2CC2, 0x2CC2], [0x2CC4, 0x2CC4], [0x2CC6, 0x2CC6], [0x2CC8, 0x2CC8], [0x2CCA, 0x2CCA], [0x2CCC, 0x2CCC], [0x2CCE, 0x2CCE], [0x2CD0, 0x2CD0], [0x2CD2, 0x2CD2], [0x2CD4, 0x2CD4], [0x2CD6, 0x2CD6], [0x2CD8, 0x2CD8], [0x2CDA, 0x2CDA], [0x2CDC, 0x2CDC], [0x2CDE, 0x2CDE], [0x2CE0, 0x2CE0], [0x2CE2, 0x2CE2], [0xFF21, 0xFF3A]];

errorCount = 0;
count = 0;
for (indexI = 0; indexI < Lu.length; indexI++) {
  for (indexJ = Lu[indexI][0]; indexJ <= Lu[indexI][1]; indexJ++) {
    try {
      var hex = decimalToHexString(indexJ);
      var identifier1 = String.fromCharCode(indexJ);
      var identifier2 = "\\u" + hex;
      eval(identifier2 + " = 1"); 
      if (eval(identifier1 + " === " + identifier2) !== true) {
        $ERROR('#' + hex + ' ');
        errorCount++;
      }
    } catch (e) {
      $ERROR('#' + hex + ' ');
      errorCount++;
    }
    count++;
  }
}

if (errorCount > 0) {    
  $ERROR('Total error: ' + errorCount + ' bad Unicode character in ' + count);
}

function decimalToHexString(n) {
  n = Number(n);
  var h = "";
  for (var i = 3; i >= 0; i--) {
    if (n >= Math.pow(16, i)) {
      var t = Math.floor(n / Math.pow(16, i));
      n -= t * Math.pow(16, i);
      if ( t >= 10 ) {
        if ( t == 10 ) { h += "A"; }
        if ( t == 11 ) { h += "B"; }
        if ( t == 12 ) { h += "C"; }
        if ( t == 13 ) { h += "D"; }
        if ( t == 14 ) { h += "E"; }
        if ( t == 15 ) { h += "F"; }
      } else {
        h += String(t);
      }
    } else {
      h += "0";
    }
  }
  return h;
}
