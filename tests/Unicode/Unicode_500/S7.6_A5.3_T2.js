// Copyright 2009 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

/**
 * @name: S7.6_A5.3_T2;
 * @section: 7.6, 6;
 * @assertion: If a UnicodeEscapeSequence put a character into an identifier that would otherwise be illegal, throw SyntaxError;
 * @description: Complex test with eval;
*/

//CHECK
Lu = [[0x0041, 0x005A], [0x00C0, 0x00D6], [0x00D8, 0x00DE], [0x0100, 0x0100], [0x0102, 0x0102], [0x0104, 0x0104], [0x0106, 0x0106], [0x0108, 0x0108], [0x010A, 0x010A], [0x010C, 0x010C], [0x010E, 0x010E], [0x0110, 0x0110], [0x0112, 0x0112], [0x0114, 0x0114], [0x0116, 0x0116], [0x0118, 0x0118], [0x011A, 0x011A], [0x011C, 0x011C], [0x011E, 0x011E], [0x0120, 0x0120], [0x0122, 0x0122], [0x0124, 0x0124], [0x0126, 0x0126], [0x0128, 0x0128], [0x012A, 0x012A], [0x012C, 0x012C], [0x012E, 0x012E], [0x0130, 0x0130], [0x0132, 0x0132], [0x0134, 0x0134], [0x0136, 0x0136], [0x0139, 0x0139], [0x013B, 0x013B], [0x013D, 0x013D], [0x013F, 0x013F], [0x0141, 0x0141], [0x0143, 0x0143], [0x0145, 0x0145], [0x0147, 0x0147], [0x014A, 0x014A], [0x014C, 0x014C], [0x014E, 0x014E], [0x0150, 0x0150], [0x0152, 0x0152], [0x0154, 0x0154], [0x0156, 0x0156], [0x0158, 0x0158], [0x015A, 0x015A], [0x015C, 0x015C], [0x015E, 0x015E], [0x0160, 0x0160], [0x0162, 0x0162], [0x0164, 0x0164], [0x0166, 0x0166], [0x0168, 0x0168], [0x016A, 0x016A], [0x016C, 0x016C], [0x016E, 0x016E], [0x0170, 0x0170], [0x0172, 0x0172], [0x0174, 0x0174], [0x0176, 0x0176], [0x0178, 0x0179], [0x017B, 0x017B], [0x017D, 0x017D], [0x0181, 0x0182], [0x0184, 0x0184], [0x0186, 0x0187], [0x0189, 0x018B], [0x018E, 0x0191], [0x0193, 0x0194], [0x0196, 0x0198], [0x019C, 0x019D], [0x019F, 0x01A0], [0x01A2, 0x01A2], [0x01A4, 0x01A4], [0x01A6, 0x01A7], [0x01A9, 0x01A9], [0x01AC, 0x01AC], [0x01AE, 0x01AF], [0x01B1, 0x01B3], [0x01B5, 0x01B5], [0x01B7, 0x01B8], [0x01BC, 0x01BC], [0x01C4, 0x01C4], [0x01C7, 0x01C7], [0x01CA, 0x01CA], [0x01CD, 0x01CD], [0x01CF, 0x01CF], [0x01D1, 0x01D1], [0x01D3, 0x01D3], [0x01D5, 0x01D5], [0x01D7, 0x01D7], [0x01D9, 0x01D9], [0x01DB, 0x01DB], [0x01DE, 0x01DE], [0x01E0, 0x01E0], [0x01E2, 0x01E2], [0x01E4, 0x01E4], [0x01E6, 0x01E6], [0x01E8, 0x01E8], [0x01EA, 0x01EA], [0x01EC, 0x01EC], [0x01EE, 0x01EE], [0x01F1, 0x01F1], [0x01F4, 0x01F4], [0x01F6, 0x01F8], [0x01FA, 0x01FA], [0x01FC, 0x01FC], [0x01FE, 0x01FE], [0x0200, 0x0200], [0x0202, 0x0202], [0x0204, 0x0204], [0x0206, 0x0206], [0x0208, 0x0208], [0x020A, 0x020A], [0x020C, 0x020C], [0x020E, 0x020E], [0x0210, 0x0210], [0x0212, 0x0212], [0x0214, 0x0214], [0x0216, 0x0216], [0x0218, 0x0218], [0x021A, 0x021A], [0x021C, 0x021C], [0x021E, 0x021E], [0x0220, 0x0220], [0x0222, 0x0222], [0x0224, 0x0224], [0x0226, 0x0226], [0x0228, 0x0228], [0x022A, 0x022A], [0x022C, 0x022C], [0x022E, 0x022E], [0x0230, 0x0230], [0x0232, 0x0232], [0x023A, 0x023B], [0x023D, 0x023E], [0x0241, 0x0241], [0x0243, 0x0246], [0x0248, 0x0248], [0x024A, 0x024A], [0x024C, 0x024C], [0x024E, 0x024E], [0x0386, 0x0386], [0x0388, 0x038A], [0x038C, 0x038C], [0x038E, 0x038F], [0x0391, 0x03A1], [0x03A3, 0x03AB], [0x03D2, 0x03D4], [0x03D8, 0x03D8], [0x03DA, 0x03DA], [0x03DC, 0x03DC], [0x03DE, 0x03DE], [0x03E0, 0x03E0], [0x03E2, 0x03E2], [0x03E4, 0x03E4], [0x03E6, 0x03E6], [0x03E8, 0x03E8], [0x03EA, 0x03EA], [0x03EC, 0x03EC], [0x03EE, 0x03EE], [0x03F4, 0x03F4], [0x03F7, 0x03F7], [0x03F9, 0x03FA], [0x03FD, 0x042F], [0x0460, 0x0460], [0x0462, 0x0462], [0x0464, 0x0464], [0x0466, 0x0466], [0x0468, 0x0468], [0x046A, 0x046A], [0x046C, 0x046C], [0x046E, 0x046E], [0x0470, 0x0470], [0x0472, 0x0472], [0x0474, 0x0474], [0x0476, 0x0476], [0x0478, 0x0478], [0x047A, 0x047A], [0x047C, 0x047C], [0x047E, 0x047E], [0x0480, 0x0480], [0x048A, 0x048A], [0x048C, 0x048C], [0x048E, 0x048E], [0x0490, 0x0490], [0x0492, 0x0492], [0x0494, 0x0494], [0x0496, 0x0496], [0x0498, 0x0498], [0x049A, 0x049A], [0x049C, 0x049C], [0x049E, 0x049E], [0x04A0, 0x04A0], [0x04A2, 0x04A2], [0x04A4, 0x04A4], [0x04A6, 0x04A6], [0x04A8, 0x04A8], [0x04AA, 0x04AA], [0x04AC, 0x04AC], [0x04AE, 0x04AE], [0x04B0, 0x04B0], [0x04B2, 0x04B2], [0x04B4, 0x04B4], [0x04B6, 0x04B6], [0x04B8, 0x04B8], [0x04BA, 0x04BA], [0x04BC, 0x04BC], [0x04BE, 0x04BE], [0x04C0, 0x04C1], [0x04C3, 0x04C3], [0x04C5, 0x04C5], [0x04C7, 0x04C7], [0x04C9, 0x04C9], [0x04CB, 0x04CB], [0x04CD, 0x04CD], [0x04D0, 0x04D0], [0x04D2, 0x04D2], [0x04D4, 0x04D4], [0x04D6, 0x04D6], [0x04D8, 0x04D8], [0x04DA, 0x04DA], [0x04DC, 0x04DC], [0x04DE, 0x04DE], [0x04E0, 0x04E0], [0x04E2, 0x04E2], [0x04E4, 0x04E4], [0x04E6, 0x04E6], [0x04E8, 0x04E8], [0x04EA, 0x04EA], [0x04EC, 0x04EC], [0x04EE, 0x04EE], [0x04F0, 0x04F0], [0x04F2, 0x04F2], [0x04F4, 0x04F4], [0x04F6, 0x04F6], [0x04F8, 0x04F8], [0x04FA, 0x04FA], [0x04FC, 0x04FC], [0x04FE, 0x04FE], [0x0500, 0x0500], [0x0502, 0x0502], [0x0504, 0x0504], [0x0506, 0x0506], [0x0508, 0x0508], [0x050A, 0x050A], [0x050C, 0x050C], [0x050E, 0x050E], [0x0510, 0x0510], [0x0512, 0x0512], [0x0531, 0x0556], [0x10A0, 0x10C5], [0x1E00, 0x1E00], [0x1E02, 0x1E02], [0x1E04, 0x1E04], [0x1E06, 0x1E06], [0x1E08, 0x1E08], [0x1E0A, 0x1E0A], [0x1E0C, 0x1E0C], [0x1E0E, 0x1E0E], [0x1E10, 0x1E10], [0x1E12, 0x1E12], [0x1E14, 0x1E14], [0x1E16, 0x1E16], [0x1E18, 0x1E18], [0x1E1A, 0x1E1A], [0x1E1C, 0x1E1C], [0x1E1E, 0x1E1E], [0x1E20, 0x1E20], [0x1E22, 0x1E22], [0x1E24, 0x1E24], [0x1E26, 0x1E26], [0x1E28, 0x1E28], [0x1E2A, 0x1E2A], [0x1E2C, 0x1E2C], [0x1E2E, 0x1E2E], [0x1E30, 0x1E30], [0x1E32, 0x1E32], [0x1E34, 0x1E34], [0x1E36, 0x1E36], [0x1E38, 0x1E38], [0x1E3A, 0x1E3A], [0x1E3C, 0x1E3C], [0x1E3E, 0x1E3E], [0x1E40, 0x1E40], [0x1E42, 0x1E42], [0x1E44, 0x1E44], [0x1E46, 0x1E46], [0x1E48, 0x1E48], [0x1E4A, 0x1E4A], [0x1E4C, 0x1E4C], [0x1E4E, 0x1E4E], [0x1E50, 0x1E50], [0x1E52, 0x1E52], [0x1E54, 0x1E54], [0x1E56, 0x1E56], [0x1E58, 0x1E58], [0x1E5A, 0x1E5A], [0x1E5C, 0x1E5C], [0x1E5E, 0x1E5E], [0x1E60, 0x1E60], [0x1E62, 0x1E62], [0x1E64, 0x1E64], [0x1E66, 0x1E66], [0x1E68, 0x1E68], [0x1E6A, 0x1E6A], [0x1E6C, 0x1E6C], [0x1E6E, 0x1E6E], [0x1E70, 0x1E70], [0x1E72, 0x1E72], [0x1E74, 0x1E74], [0x1E76, 0x1E76], [0x1E78, 0x1E78], [0x1E7A, 0x1E7A], [0x1E7C, 0x1E7C], [0x1E7E, 0x1E7E], [0x1E80, 0x1E80], [0x1E82, 0x1E82], [0x1E84, 0x1E84], [0x1E86, 0x1E86], [0x1E88, 0x1E88], [0x1E8A, 0x1E8A], [0x1E8C, 0x1E8C], [0x1E8E, 0x1E8E], [0x1E90, 0x1E90], [0x1E92, 0x1E92], [0x1E94, 0x1E94], [0x1EA0, 0x1EA0], [0x1EA2, 0x1EA2], [0x1EA4, 0x1EA4], [0x1EA6, 0x1EA6], [0x1EA8, 0x1EA8], [0x1EAA, 0x1EAA], [0x1EAC, 0x1EAC], [0x1EAE, 0x1EAE], [0x1EB0, 0x1EB0], [0x1EB2, 0x1EB2], [0x1EB4, 0x1EB4], [0x1EB6, 0x1EB6], [0x1EB8, 0x1EB8], [0x1EBA, 0x1EBA], [0x1EBC, 0x1EBC], [0x1EBE, 0x1EBE], [0x1EC0, 0x1EC0], [0x1EC2, 0x1EC2], [0x1EC4, 0x1EC4], [0x1EC6, 0x1EC6], [0x1EC8, 0x1EC8], [0x1ECA, 0x1ECA], [0x1ECC, 0x1ECC], [0x1ECE, 0x1ECE], [0x1ED0, 0x1ED0], [0x1ED2, 0x1ED2], [0x1ED4, 0x1ED4], [0x1ED6, 0x1ED6], [0x1ED8, 0x1ED8], [0x1EDA, 0x1EDA], [0x1EDC, 0x1EDC], [0x1EDE, 0x1EDE], [0x1EE0, 0x1EE0], [0x1EE2, 0x1EE2], [0x1EE4, 0x1EE4], [0x1EE6, 0x1EE6], [0x1EE8, 0x1EE8], [0x1EEA, 0x1EEA], [0x1EEC, 0x1EEC], [0x1EEE, 0x1EEE], [0x1EF0, 0x1EF0], [0x1EF2, 0x1EF2], [0x1EF4, 0x1EF4], [0x1EF6, 0x1EF6], [0x1EF8, 0x1EF8], [0x1F08, 0x1F0F], [0x1F18, 0x1F1D], [0x1F28, 0x1F2F], [0x1F38, 0x1F3F], [0x1F48, 0x1F4D], [0x1F59, 0x1F59], [0x1F5B, 0x1F5B], [0x1F5D, 0x1F5D], [0x1F5F, 0x1F5F], [0x1F68, 0x1F6F], [0x1FB8, 0x1FBB], [0x1FC8, 0x1FCB], [0x1FD8, 0x1FDB], [0x1FE8, 0x1FEC], [0x1FF8, 0x1FFB], [0x2102, 0x2102], [0x2107, 0x2107], [0x210B, 0x210D], [0x2110, 0x2112], [0x2115, 0x2115], [0x2119, 0x211D], [0x2124, 0x2124], [0x2126, 0x2126], [0x2128, 0x2128], [0x212A, 0x212D], [0x2130, 0x2133], [0x213E, 0x213F], [0x2145, 0x2145], [0x2183, 0x2183], [0x2C00, 0x2C2E], [0x2C60, 0x2C60], [0x2C62, 0x2C64], [0x2C67, 0x2C67], [0x2C69, 0x2C69], [0x2C6B, 0x2C6B], [0x2C75, 0x2C75], [0x2C80, 0x2C80], [0x2C82, 0x2C82], [0x2C84, 0x2C84], [0x2C86, 0x2C86], [0x2C88, 0x2C88], [0x2C8A, 0x2C8A], [0x2C8C, 0x2C8C], [0x2C8E, 0x2C8E], [0x2C90, 0x2C90], [0x2C92, 0x2C92], [0x2C94, 0x2C94], [0x2C96, 0x2C96], [0x2C98, 0x2C98], [0x2C9A, 0x2C9A], [0x2C9C, 0x2C9C], [0x2C9E, 0x2C9E], [0x2CA0, 0x2CA0], [0x2CA2, 0x2CA2], [0x2CA4, 0x2CA4], [0x2CA6, 0x2CA6], [0x2CA8, 0x2CA8], [0x2CAA, 0x2CAA], [0x2CAC, 0x2CAC], [0x2CAE, 0x2CAE], [0x2CB0, 0x2CB0], [0x2CB2, 0x2CB2], [0x2CB4, 0x2CB4], [0x2CB6, 0x2CB6], [0x2CB8, 0x2CB8], [0x2CBA, 0x2CBA], [0x2CBC, 0x2CBC], [0x2CBE, 0x2CBE], [0x2CC0, 0x2CC0], [0x2CC2, 0x2CC2], [0x2CC4, 0x2CC4], [0x2CC6, 0x2CC6], [0x2CC8, 0x2CC8], [0x2CCA, 0x2CCA], [0x2CCC, 0x2CCC], [0x2CCE, 0x2CCE], [0x2CD0, 0x2CD0], [0x2CD2, 0x2CD2], [0x2CD4, 0x2CD4], [0x2CD6, 0x2CD6], [0x2CD8, 0x2CD8], [0x2CDA, 0x2CDA], [0x2CDC, 0x2CDC], [0x2CDE, 0x2CDE], [0x2CE0, 0x2CE0], [0x2CE2, 0x2CE2], [0xFF21, 0xFF3A]];
Ll = [[0x0061, 0x007A], [0x00AA, 0x00AA], [0x00B5, 0x00B5], [0x00BA, 0x00BA], [0x00DF, 0x00F6], [0x00F8, 0x00FF], [0x0101, 0x0101], [0x0103, 0x0103], [0x0105, 0x0105], [0x0107, 0x0107], [0x0109, 0x0109], [0x010B, 0x010B], [0x010D, 0x010D], [0x010F, 0x010F], [0x0111, 0x0111], [0x0113, 0x0113], [0x0115, 0x0115], [0x0117, 0x0117], [0x0119, 0x0119], [0x011B, 0x011B], [0x011D, 0x011D], [0x011F, 0x011F], [0x0121, 0x0121], [0x0123, 0x0123], [0x0125, 0x0125], [0x0127, 0x0127], [0x0129, 0x0129], [0x012B, 0x012B], [0x012D, 0x012D], [0x012F, 0x012F], [0x0131, 0x0131], [0x0133, 0x0133], [0x0135, 0x0135], [0x0137, 0x0138], [0x013A, 0x013A], [0x013C, 0x013C], [0x013E, 0x013E], [0x0140, 0x0140], [0x0142, 0x0142], [0x0144, 0x0144], [0x0146, 0x0146], [0x0148, 0x0149], [0x014B, 0x014B], [0x014D, 0x014D], [0x014F, 0x014F], [0x0151, 0x0151], [0x0153, 0x0153], [0x0155, 0x0155], [0x0157, 0x0157], [0x0159, 0x0159], [0x015B, 0x015B], [0x015D, 0x015D], [0x015F, 0x015F], [0x0161, 0x0161], [0x0163, 0x0163], [0x0165, 0x0165], [0x0167, 0x0167], [0x0169, 0x0169], [0x016B, 0x016B], [0x016D, 0x016D], [0x016F, 0x016F], [0x0171, 0x0171], [0x0173, 0x0173], [0x0175, 0x0175], [0x0177, 0x0177], [0x017A, 0x017A], [0x017C, 0x017C], [0x017E, 0x0180], [0x0183, 0x0183], [0x0185, 0x0185], [0x0188, 0x0188], [0x018C, 0x018D], [0x0192, 0x0192], [0x0195, 0x0195], [0x0199, 0x019B], [0x019E, 0x019E], [0x01A1, 0x01A1], [0x01A3, 0x01A3], [0x01A5, 0x01A5], [0x01A8, 0x01A8], [0x01AA, 0x01AB], [0x01AD, 0x01AD], [0x01B0, 0x01B0], [0x01B4, 0x01B4], [0x01B6, 0x01B6], [0x01B9, 0x01BA], [0x01BD, 0x01BF], [0x01C6, 0x01C6], [0x01C9, 0x01C9], [0x01CC, 0x01CC], [0x01CE, 0x01CE], [0x01D0, 0x01D0], [0x01D2, 0x01D2], [0x01D4, 0x01D4], [0x01D6, 0x01D6], [0x01D8, 0x01D8], [0x01DA, 0x01DA], [0x01DC, 0x01DD], [0x01DF, 0x01DF], [0x01E1, 0x01E1], [0x01E3, 0x01E3], [0x01E5, 0x01E5], [0x01E7, 0x01E7], [0x01E9, 0x01E9], [0x01EB, 0x01EB], [0x01ED, 0x01ED], [0x01EF, 0x01F0], [0x01F3, 0x01F3], [0x01F5, 0x01F5], [0x01F9, 0x01F9], [0x01FB, 0x01FB], [0x01FD, 0x01FD], [0x01FF, 0x01FF], [0x0201, 0x0201], [0x0203, 0x0203], [0x0205, 0x0205], [0x0207, 0x0207], [0x0209, 0x0209], [0x020B, 0x020B], [0x020D, 0x020D], [0x020F, 0x020F], [0x0211, 0x0211], [0x0213, 0x0213], [0x0215, 0x0215], [0x0217, 0x0217], [0x0219, 0x0219], [0x021B, 0x021B], [0x021D, 0x021D], [0x021F, 0x021F], [0x0221, 0x0221], [0x0223, 0x0223], [0x0225, 0x0225], [0x0227, 0x0227], [0x0229, 0x0229], [0x022B, 0x022B], [0x022D, 0x022D], [0x022F, 0x022F], [0x0231, 0x0231], [0x0233, 0x0239], [0x023C, 0x023C], [0x023F, 0x0240], [0x0242, 0x0242], [0x0247, 0x0247], [0x0249, 0x0249], [0x024B, 0x024B], [0x024D, 0x024D], [0x024F, 0x0293], [0x0295, 0x02AF], [0x037B, 0x037D], [0x0390, 0x0390], [0x03AC, 0x03CE], [0x03D0, 0x03D1], [0x03D5, 0x03D7], [0x03D9, 0x03D9], [0x03DB, 0x03DB], [0x03DD, 0x03DD], [0x03DF, 0x03DF], [0x03E1, 0x03E1], [0x03E3, 0x03E3], [0x03E5, 0x03E5], [0x03E7, 0x03E7], [0x03E9, 0x03E9], [0x03EB, 0x03EB], [0x03ED, 0x03ED], [0x03EF, 0x03F3], [0x03F5, 0x03F5], [0x03F8, 0x03F8], [0x03FB, 0x03FC], [0x0430, 0x045F], [0x0461, 0x0461], [0x0463, 0x0463], [0x0465, 0x0465], [0x0467, 0x0467], [0x0469, 0x0469], [0x046B, 0x046B], [0x046D, 0x046D], [0x046F, 0x046F], [0x0471, 0x0471], [0x0473, 0x0473], [0x0475, 0x0475], [0x0477, 0x0477], [0x0479, 0x0479], [0x047B, 0x047B], [0x047D, 0x047D], [0x047F, 0x047F], [0x0481, 0x0481], [0x048B, 0x048B], [0x048D, 0x048D], [0x048F, 0x048F], [0x0491, 0x0491], [0x0493, 0x0493], [0x0495, 0x0495], [0x0497, 0x0497], [0x0499, 0x0499], [0x049B, 0x049B], [0x049D, 0x049D], [0x049F, 0x049F], [0x04A1, 0x04A1], [0x04A3, 0x04A3], [0x04A5, 0x04A5], [0x04A7, 0x04A7], [0x04A9, 0x04A9], [0x04AB, 0x04AB], [0x04AD, 0x04AD], [0x04AF, 0x04AF], [0x04B1, 0x04B1], [0x04B3, 0x04B3], [0x04B5, 0x04B5], [0x04B7, 0x04B7], [0x04B9, 0x04B9], [0x04BB, 0x04BB], [0x04BD, 0x04BD], [0x04BF, 0x04BF], [0x04C2, 0x04C2], [0x04C4, 0x04C4], [0x04C6, 0x04C6], [0x04C8, 0x04C8], [0x04CA, 0x04CA], [0x04CC, 0x04CC], [0x04CE, 0x04CF], [0x04D1, 0x04D1], [0x04D3, 0x04D3], [0x04D5, 0x04D5], [0x04D7, 0x04D7], [0x04D9, 0x04D9], [0x04DB, 0x04DB], [0x04DD, 0x04DD], [0x04DF, 0x04DF], [0x04E1, 0x04E1], [0x04E3, 0x04E3], [0x04E5, 0x04E5], [0x04E7, 0x04E7], [0x04E9, 0x04E9], [0x04EB, 0x04EB], [0x04ED, 0x04ED], [0x04EF, 0x04EF], [0x04F1, 0x04F1], [0x04F3, 0x04F3], [0x04F5, 0x04F5], [0x04F7, 0x04F7], [0x04F9, 0x04F9], [0x04FB, 0x04FB], [0x04FD, 0x04FD], [0x04FF, 0x04FF], [0x0501, 0x0501], [0x0503, 0x0503], [0x0505, 0x0505], [0x0507, 0x0507], [0x0509, 0x0509], [0x050B, 0x050B], [0x050D, 0x050D], [0x050F, 0x050F], [0x0511, 0x0511], [0x0513, 0x0513], [0x0561, 0x0587], [0x1D00, 0x1D2B], [0x1D62, 0x1D77], [0x1D79, 0x1D9A], [0x1E01, 0x1E01], [0x1E03, 0x1E03], [0x1E05, 0x1E05], [0x1E07, 0x1E07], [0x1E09, 0x1E09], [0x1E0B, 0x1E0B], [0x1E0D, 0x1E0D], [0x1E0F, 0x1E0F], [0x1E11, 0x1E11], [0x1E13, 0x1E13], [0x1E15, 0x1E15], [0x1E17, 0x1E17], [0x1E19, 0x1E19], [0x1E1B, 0x1E1B], [0x1E1D, 0x1E1D], [0x1E1F, 0x1E1F], [0x1E21, 0x1E21], [0x1E23, 0x1E23], [0x1E25, 0x1E25], [0x1E27, 0x1E27], [0x1E29, 0x1E29], [0x1E2B, 0x1E2B], [0x1E2D, 0x1E2D], [0x1E2F, 0x1E2F], [0x1E31, 0x1E31], [0x1E33, 0x1E33], [0x1E35, 0x1E35], [0x1E37, 0x1E37], [0x1E39, 0x1E39], [0x1E3B, 0x1E3B], [0x1E3D, 0x1E3D], [0x1E3F, 0x1E3F], [0x1E41, 0x1E41], [0x1E43, 0x1E43], [0x1E45, 0x1E45], [0x1E47, 0x1E47], [0x1E49, 0x1E49], [0x1E4B, 0x1E4B], [0x1E4D, 0x1E4D], [0x1E4F, 0x1E4F], [0x1E51, 0x1E51], [0x1E53, 0x1E53], [0x1E55, 0x1E55], [0x1E57, 0x1E57], [0x1E59, 0x1E59], [0x1E5B, 0x1E5B], [0x1E5D, 0x1E5D], [0x1E5F, 0x1E5F], [0x1E61, 0x1E61], [0x1E63, 0x1E63], [0x1E65, 0x1E65], [0x1E67, 0x1E67], [0x1E69, 0x1E69], [0x1E6B, 0x1E6B], [0x1E6D, 0x1E6D], [0x1E6F, 0x1E6F], [0x1E71, 0x1E71], [0x1E73, 0x1E73], [0x1E75, 0x1E75], [0x1E77, 0x1E77], [0x1E79, 0x1E79], [0x1E7B, 0x1E7B], [0x1E7D, 0x1E7D], [0x1E7F, 0x1E7F], [0x1E81, 0x1E81], [0x1E83, 0x1E83], [0x1E85, 0x1E85], [0x1E87, 0x1E87], [0x1E89, 0x1E89], [0x1E8B, 0x1E8B], [0x1E8D, 0x1E8D], [0x1E8F, 0x1E8F], [0x1E91, 0x1E91], [0x1E93, 0x1E93], [0x1E95, 0x1E9B], [0x1EA1, 0x1EA1], [0x1EA3, 0x1EA3], [0x1EA5, 0x1EA5], [0x1EA7, 0x1EA7], [0x1EA9, 0x1EA9], [0x1EAB, 0x1EAB], [0x1EAD, 0x1EAD], [0x1EAF, 0x1EAF], [0x1EB1, 0x1EB1], [0x1EB3, 0x1EB3], [0x1EB5, 0x1EB5], [0x1EB7, 0x1EB7], [0x1EB9, 0x1EB9], [0x1EBB, 0x1EBB], [0x1EBD, 0x1EBD], [0x1EBF, 0x1EBF], [0x1EC1, 0x1EC1], [0x1EC3, 0x1EC3], [0x1EC5, 0x1EC5], [0x1EC7, 0x1EC7], [0x1EC9, 0x1EC9], [0x1ECB, 0x1ECB], [0x1ECD, 0x1ECD], [0x1ECF, 0x1ECF], [0x1ED1, 0x1ED1], [0x1ED3, 0x1ED3], [0x1ED5, 0x1ED5], [0x1ED7, 0x1ED7], [0x1ED9, 0x1ED9], [0x1EDB, 0x1EDB], [0x1EDD, 0x1EDD], [0x1EDF, 0x1EDF], [0x1EE1, 0x1EE1], [0x1EE3, 0x1EE3], [0x1EE5, 0x1EE5], [0x1EE7, 0x1EE7], [0x1EE9, 0x1EE9], [0x1EEB, 0x1EEB], [0x1EED, 0x1EED], [0x1EEF, 0x1EEF], [0x1EF1, 0x1EF1], [0x1EF3, 0x1EF3], [0x1EF5, 0x1EF5], [0x1EF7, 0x1EF7], [0x1EF9, 0x1EF9], [0x1F00, 0x1F07], [0x1F10, 0x1F15], [0x1F20, 0x1F27], [0x1F30, 0x1F37], [0x1F40, 0x1F45], [0x1F50, 0x1F57], [0x1F60, 0x1F67], [0x1F70, 0x1F7D], [0x1F80, 0x1F87], [0x1F90, 0x1F97], [0x1FA0, 0x1FA7], [0x1FB0, 0x1FB4], [0x1FB6, 0x1FB7], [0x1FBE, 0x1FBE], [0x1FC2, 0x1FC4], [0x1FC6, 0x1FC7], [0x1FD0, 0x1FD3], [0x1FD6, 0x1FD7], [0x1FE0, 0x1FE7], [0x1FF2, 0x1FF4], [0x1FF6, 0x1FF7], [0x2071, 0x2071], [0x207F, 0x207F], [0x210A, 0x210A], [0x210E, 0x210F], [0x2113, 0x2113], [0x212F, 0x212F], [0x2134, 0x2134], [0x2139, 0x2139], [0x213C, 0x213D], [0x2146, 0x2149], [0x214E, 0x214E], [0x2184, 0x2184], [0x2C30, 0x2C5E], [0x2C61, 0x2C61], [0x2C65, 0x2C66], [0x2C68, 0x2C68], [0x2C6A, 0x2C6A], [0x2C6C, 0x2C6C], [0x2C74, 0x2C74], [0x2C76, 0x2C77], [0x2C81, 0x2C81], [0x2C83, 0x2C83], [0x2C85, 0x2C85], [0x2C87, 0x2C87], [0x2C89, 0x2C89], [0x2C8B, 0x2C8B], [0x2C8D, 0x2C8D], [0x2C8F, 0x2C8F], [0x2C91, 0x2C91], [0x2C93, 0x2C93], [0x2C95, 0x2C95], [0x2C97, 0x2C97], [0x2C99, 0x2C99], [0x2C9B, 0x2C9B], [0x2C9D, 0x2C9D], [0x2C9F, 0x2C9F], [0x2CA1, 0x2CA1], [0x2CA3, 0x2CA3], [0x2CA5, 0x2CA5], [0x2CA7, 0x2CA7], [0x2CA9, 0x2CA9], [0x2CAB, 0x2CAB], [0x2CAD, 0x2CAD], [0x2CAF, 0x2CAF], [0x2CB1, 0x2CB1], [0x2CB3, 0x2CB3], [0x2CB5, 0x2CB5], [0x2CB7, 0x2CB7], [0x2CB9, 0x2CB9], [0x2CBB, 0x2CBB], [0x2CBD, 0x2CBD], [0x2CBF, 0x2CBF], [0x2CC1, 0x2CC1], [0x2CC3, 0x2CC3], [0x2CC5, 0x2CC5], [0x2CC7, 0x2CC7], [0x2CC9, 0x2CC9], [0x2CCB, 0x2CCB], [0x2CCD, 0x2CCD], [0x2CCF, 0x2CCF], [0x2CD1, 0x2CD1], [0x2CD3, 0x2CD3], [0x2CD5, 0x2CD5], [0x2CD7, 0x2CD7], [0x2CD9, 0x2CD9], [0x2CDB, 0x2CDB], [0x2CDD, 0x2CDD], [0x2CDF, 0x2CDF], [0x2CE1, 0x2CE1], [0x2CE3, 0x2CE4], [0x2D00, 0x2D25], [0xFB00, 0xFB06], [0xFB13, 0xFB17], [0xFF41, 0xFF5A]];
Lt = [[0x01C5, 0x01C5], [0x01C8, 0x01C8], [0x01CB, 0x01CB], [0x01F2, 0x01F2], [0x1F88, 0x1F8F], [0x1F98, 0x1F9F], [0x1FA8, 0x1FAF], [0x1FBC, 0x1FBC], [0x1FCC, 0x1FCC], [0x1FFC, 0x1FFC]];
Lm = [[0x02B0, 0x02C1], [0x02C6, 0x02D1], [0x02E0, 0x02E4], [0x02EE, 0x02EE], [0x037A, 0x037A], [0x0559, 0x0559], [0x0640, 0x0640], [0x06E5, 0x06E6], [0x07F4, 0x07F5], [0x07FA, 0x07FA], [0x0E46, 0x0E46], [0x0EC6, 0x0EC6], [0x10FC, 0x10FC], [0x17D7, 0x17D7], [0x1843, 0x1843], [0x1D2C, 0x1D61], [0x1D78, 0x1D78], [0x1D9B, 0x1DBF], [0x2090, 0x2094], [0x2D6F, 0x2D6F], [0x3005, 0x3005], [0x3031, 0x3035], [0x303B, 0x303B], [0x309D, 0x309E], [0x30FC, 0x30FE], [0xA015, 0xA015], [0xA717, 0xA71A], [0xFF70, 0xFF70], [0xFF9E, 0xFF9F]];
Lo = [[0x01BB, 0x01BB], [0x01C0, 0x01C3], [0x0294, 0x0294], [0x05D0, 0x05EA], [0x05F0, 0x05F2], [0x0621, 0x063A], [0x0641, 0x064A], [0x066E, 0x066F], [0x0671, 0x06D3], [0x06D5, 0x06D5], [0x06EE, 0x06EF], [0x06FA, 0x06FC], [0x06FF, 0x06FF], [0x0710, 0x0710], [0x0712, 0x072F], [0x074D, 0x076D], [0x0780, 0x07A5], [0x07B1, 0x07B1], [0x07CA, 0x07EA], [0x0904, 0x0939], [0x093D, 0x093D], [0x0950, 0x0950], [0x0958, 0x0961], [0x097B, 0x097F], [0x0985, 0x098C], [0x098F, 0x0990], [0x0993, 0x09A8], [0x09AA, 0x09B0], [0x09B2, 0x09B2], [0x09B6, 0x09B9], [0x09BD, 0x09BD], [0x09CE, 0x09CE], [0x09DC, 0x09DD], [0x09DF, 0x09E1], [0x09F0, 0x09F1], [0x0A05, 0x0A0A], [0x0A0F, 0x0A10], [0x0A13, 0x0A28], [0x0A2A, 0x0A30], [0x0A32, 0x0A33], [0x0A35, 0x0A36], [0x0A38, 0x0A39], [0x0A59, 0x0A5C], [0x0A5E, 0x0A5E], [0x0A72, 0x0A74], [0x0A85, 0x0A8D], [0x0A8F, 0x0A91], [0x0A93, 0x0AA8], [0x0AAA, 0x0AB0], [0x0AB2, 0x0AB3], [0x0AB5, 0x0AB9], [0x0ABD, 0x0ABD], [0x0AD0, 0x0AD0], [0x0AE0, 0x0AE1], [0x0B05, 0x0B0C], [0x0B0F, 0x0B10], [0x0B13, 0x0B28], [0x0B2A, 0x0B30], [0x0B32, 0x0B33], [0x0B35, 0x0B39], [0x0B3D, 0x0B3D], [0x0B5C, 0x0B5D], [0x0B5F, 0x0B61], [0x0B71, 0x0B71], [0x0B83, 0x0B83], [0x0B85, 0x0B8A], [0x0B8E, 0x0B90], [0x0B92, 0x0B95], [0x0B99, 0x0B9A], [0x0B9C, 0x0B9C], [0x0B9E, 0x0B9F], [0x0BA3, 0x0BA4], [0x0BA8, 0x0BAA], [0x0BAE, 0x0BB9], [0x0C05, 0x0C0C], [0x0C0E, 0x0C10], [0x0C12, 0x0C28], [0x0C2A, 0x0C33], [0x0C35, 0x0C39], [0x0C60, 0x0C61], [0x0C85, 0x0C8C], [0x0C8E, 0x0C90], [0x0C92, 0x0CA8], [0x0CAA, 0x0CB3], [0x0CB5, 0x0CB9], [0x0CBD, 0x0CBD], [0x0CDE, 0x0CDE], [0x0CE0, 0x0CE1], [0x0D05, 0x0D0C], [0x0D0E, 0x0D10], [0x0D12, 0x0D28], [0x0D2A, 0x0D39], [0x0D60, 0x0D61], [0x0D85, 0x0D96], [0x0D9A, 0x0DB1], [0x0DB3, 0x0DBB], [0x0DBD, 0x0DBD], [0x0DC0, 0x0DC6], [0x0E01, 0x0E30], [0x0E32, 0x0E33], [0x0E40, 0x0E45], [0x0E81, 0x0E82], [0x0E84, 0x0E84], [0x0E87, 0x0E88], [0x0E8A, 0x0E8A], [0x0E8D, 0x0E8D], [0x0E94, 0x0E97], [0x0E99, 0x0E9F], [0x0EA1, 0x0EA3], [0x0EA5, 0x0EA5], [0x0EA7, 0x0EA7], [0x0EAA, 0x0EAB], [0x0EAD, 0x0EB0], [0x0EB2, 0x0EB3], [0x0EBD, 0x0EBD], [0x0EC0, 0x0EC4], [0x0EDC, 0x0EDD], [0x0F00, 0x0F00], [0x0F40, 0x0F47], [0x0F49, 0x0F6A], [0x0F88, 0x0F8B], [0x1000, 0x1021], [0x1023, 0x1027], [0x1029, 0x102A], [0x1050, 0x1055], [0x10D0, 0x10FA], [0x1100, 0x1159], [0x115F, 0x11A2], [0x11A8, 0x11F9], [0x1200, 0x1248], [0x124A, 0x124D], [0x1250, 0x1256], [0x1258, 0x1258], [0x125A, 0x125D], [0x1260, 0x1288], [0x128A, 0x128D], [0x1290, 0x12B0], [0x12B2, 0x12B5], [0x12B8, 0x12BE], [0x12C0, 0x12C0], [0x12C2, 0x12C5], [0x12C8, 0x12D6], [0x12D8, 0x1310], [0x1312, 0x1315], [0x1318, 0x135A], [0x1380, 0x138F], [0x13A0, 0x13F4], [0x1401, 0x166C], [0x166F, 0x1676], [0x1681, 0x169A], [0x16A0, 0x16EA], [0x1700, 0x170C], [0x170E, 0x1711], [0x1720, 0x1731], [0x1740, 0x1751], [0x1760, 0x176C], [0x176E, 0x1770], [0x1780, 0x17B3], [0x17DC, 0x17DC], [0x1820, 0x1842], [0x1844, 0x1877], [0x1880, 0x18A8], [0x1900, 0x191C], [0x1950, 0x196D], [0x1970, 0x1974], [0x1980, 0x19A9], [0x19C1, 0x19C7], [0x1A00, 0x1A16], [0x1B05, 0x1B33], [0x1B45, 0x1B4B], [0x2135, 0x2138], [0x2D30, 0x2D65], [0x2D80, 0x2D96], [0x2DA0, 0x2DA6], [0x2DA8, 0x2DAE], [0x2DB0, 0x2DB6], [0x2DB8, 0x2DBE], [0x2DC0, 0x2DC6], [0x2DC8, 0x2DCE], [0x2DD0, 0x2DD6], [0x2DD8, 0x2DDE], [0x3006, 0x3006], [0x303C, 0x303C], [0x3041, 0x3096], [0x309F, 0x309F], [0x30A1, 0x30FA], [0x30FF, 0x30FF], [0x3105, 0x312C], [0x3131, 0x318E], [0x31A0, 0x31B7], [0x31F0, 0x31FF], [0x3400, 0x4DB5], [0x4E00, 0x9FBB], [0xA000, 0xA014], [0xA016, 0xA48C], [0xA800, 0xA801], [0xA803, 0xA805], [0xA807, 0xA80A], [0xA80C, 0xA822], [0xA840, 0xA873], [0xAC00, 0xD7A3], [0xF900, 0xFA2D], [0xFA30, 0xFA6A], [0xFA70, 0xFAD9], [0xFB1D, 0xFB1D], [0xFB1F, 0xFB28], [0xFB2A, 0xFB36], [0xFB38, 0xFB3C], [0xFB3E, 0xFB3E], [0xFB40, 0xFB41], [0xFB43, 0xFB44], [0xFB46, 0xFBB1], [0xFBD3, 0xFD3D], [0xFD50, 0xFD8F], [0xFD92, 0xFDC7], [0xFDF0, 0xFDFB], [0xFE70, 0xFE74], [0xFE76, 0xFEFC], [0xFF66, 0xFF6F], [0xFF71, 0xFF9D], [0xFFA0, 0xFFBE], [0xFFC2, 0xFFC7], [0xFFCA, 0xFFCF], [0xFFD2, 0xFFD7], [0xFFDA, 0xFFDC]];
Nl = [[0x16EE, 0x16F0], [0x2160, 0x2182], [0x3007, 0x3007], [0x3021, 0x3029], [0x3038, 0x303A]];
Mn = [[0x0300, 0x036F], [0x0483, 0x0486], [0x0591, 0x05BD], [0x05BF, 0x05BF], [0x05C1, 0x05C2], [0x05C4, 0x05C5], [0x05C7, 0x05C7], [0x0610, 0x0615], [0x064B, 0x065E], [0x0670, 0x0670], [0x06D6, 0x06DC], [0x06DF, 0x06E4], [0x06E7, 0x06E8], [0x06EA, 0x06ED], [0x0711, 0x0711], [0x0730, 0x074A], [0x07A6, 0x07B0], [0x07EB, 0x07F3], [0x0901, 0x0902], [0x093C, 0x093C], [0x0941, 0x0948], [0x094D, 0x094D], [0x0951, 0x0954], [0x0962, 0x0963], [0x0981, 0x0981], [0x09BC, 0x09BC], [0x09C1, 0x09C4], [0x09CD, 0x09CD], [0x09E2, 0x09E3], [0x0A01, 0x0A02], [0x0A3C, 0x0A3C], [0x0A41, 0x0A42], [0x0A47, 0x0A48], [0x0A4B, 0x0A4D], [0x0A70, 0x0A71], [0x0A81, 0x0A82], [0x0ABC, 0x0ABC], [0x0AC1, 0x0AC5], [0x0AC7, 0x0AC8], [0x0ACD, 0x0ACD], [0x0AE2, 0x0AE3], [0x0B01, 0x0B01], [0x0B3C, 0x0B3C], [0x0B3F, 0x0B3F], [0x0B41, 0x0B43], [0x0B4D, 0x0B4D], [0x0B56, 0x0B56], [0x0B82, 0x0B82], [0x0BC0, 0x0BC0], [0x0BCD, 0x0BCD], [0x0C3E, 0x0C40], [0x0C46, 0x0C48], [0x0C4A, 0x0C4D], [0x0C55, 0x0C56], [0x0CBC, 0x0CBC], [0x0CBF, 0x0CBF], [0x0CC6, 0x0CC6], [0x0CCC, 0x0CCD], [0x0CE2, 0x0CE3], [0x0D41, 0x0D43], [0x0D4D, 0x0D4D], [0x0DCA, 0x0DCA], [0x0DD2, 0x0DD4], [0x0DD6, 0x0DD6], [0x0E31, 0x0E31], [0x0E34, 0x0E3A], [0x0E47, 0x0E4E], [0x0EB1, 0x0EB1], [0x0EB4, 0x0EB9], [0x0EBB, 0x0EBC], [0x0EC8, 0x0ECD], [0x0F18, 0x0F19], [0x0F35, 0x0F35], [0x0F37, 0x0F37], [0x0F39, 0x0F39], [0x0F71, 0x0F7E], [0x0F80, 0x0F84], [0x0F86, 0x0F87], [0x0F90, 0x0F97], [0x0F99, 0x0FBC], [0x0FC6, 0x0FC6], [0x102D, 0x1030], [0x1032, 0x1032], [0x1036, 0x1037], [0x1039, 0x1039], [0x1058, 0x1059], [0x135F, 0x135F], [0x1712, 0x1714], [0x1732, 0x1734], [0x1752, 0x1753], [0x1772, 0x1773], [0x17B7, 0x17BD], [0x17C6, 0x17C6], [0x17C9, 0x17D3], [0x17DD, 0x17DD], [0x180B, 0x180D], [0x18A9, 0x18A9], [0x1920, 0x1922], [0x1927, 0x1928], [0x1932, 0x1932], [0x1939, 0x193B], [0x1A17, 0x1A18], [0x1B00, 0x1B03], [0x1B34, 0x1B34], [0x1B36, 0x1B3A], [0x1B3C, 0x1B3C], [0x1B42, 0x1B42], [0x1B6B, 0x1B73], [0x1DC0, 0x1DCA], [0x1DFE, 0x1DFF], [0x20D0, 0x20DC], [0x20E1, 0x20E1], [0x20E5, 0x20EF], [0x302A, 0x302F], [0x3099, 0x309A], [0xA806, 0xA806], [0xA80B, 0xA80B], [0xA825, 0xA826], [0xFB1E, 0xFB1E], [0xFE00, 0xFE0F], [0xFE20, 0xFE23]];
Mc = [[0x0903, 0x0903], [0x093E, 0x0940], [0x0949, 0x094C], [0x0982, 0x0983], [0x09BE, 0x09C0], [0x09C7, 0x09C8], [0x09CB, 0x09CC], [0x09D7, 0x09D7], [0x0A03, 0x0A03], [0x0A3E, 0x0A40], [0x0A83, 0x0A83], [0x0ABE, 0x0AC0], [0x0AC9, 0x0AC9], [0x0ACB, 0x0ACC], [0x0B02, 0x0B03], [0x0B3E, 0x0B3E], [0x0B40, 0x0B40], [0x0B47, 0x0B48], [0x0B4B, 0x0B4C], [0x0B57, 0x0B57], [0x0BBE, 0x0BBF], [0x0BC1, 0x0BC2], [0x0BC6, 0x0BC8], [0x0BCA, 0x0BCC], [0x0BD7, 0x0BD7], [0x0C01, 0x0C03], [0x0C41, 0x0C44], [0x0C82, 0x0C83], [0x0CBE, 0x0CBE], [0x0CC0, 0x0CC4], [0x0CC7, 0x0CC8], [0x0CCA, 0x0CCB], [0x0CD5, 0x0CD6], [0x0D02, 0x0D03], [0x0D3E, 0x0D40], [0x0D46, 0x0D48], [0x0D4A, 0x0D4C], [0x0D57, 0x0D57], [0x0D82, 0x0D83], [0x0DCF, 0x0DD1], [0x0DD8, 0x0DDF], [0x0DF2, 0x0DF3], [0x0F3E, 0x0F3F], [0x0F7F, 0x0F7F], [0x102C, 0x102C], [0x1031, 0x1031], [0x1038, 0x1038], [0x1056, 0x1057], [0x17B6, 0x17B6], [0x17BE, 0x17C5], [0x17C7, 0x17C8], [0x1923, 0x1926], [0x1929, 0x192B], [0x1930, 0x1931], [0x1933, 0x1938], [0x19B0, 0x19C0], [0x19C8, 0x19C9], [0x1A19, 0x1A1B], [0x1B04, 0x1B04], [0x1B35, 0x1B35], [0x1B3B, 0x1B3B], [0x1B3D, 0x1B41], [0x1B43, 0x1B44], [0xA802, 0xA802], [0xA823, 0xA824], [0xA827, 0xA827]];
Nd = [[0x0030, 0x0039], [0x0660, 0x0669], [0x06F0, 0x06F9], [0x07C0, 0x07C9], [0x0966, 0x096F], [0x09E6, 0x09EF], [0x0A66, 0x0A6F], [0x0AE6, 0x0AEF], [0x0B66, 0x0B6F], [0x0BE6, 0x0BEF], [0x0C66, 0x0C6F], [0x0CE6, 0x0CEF], [0x0D66, 0x0D6F], [0x0E50, 0x0E59], [0x0ED0, 0x0ED9], [0x0F20, 0x0F29], [0x1040, 0x1049], [0x17E0, 0x17E9], [0x1810, 0x1819], [0x1946, 0x194F], [0x19D0, 0x19D9], [0x1B50, 0x1B59], [0xFF10, 0xFF19]];
Pc = [[0x005F, 0x005F], [0x203F, 0x2040], [0x2054, 0x2054], [0xFE33, 0xFE34], [0xFE4D, 0xFE4F], [0xFF3F, 0xFF3F]];

UnicodeLetterLu = [];
UnicodeLetterLl = [];
UnicodeLetterLt = [];
UnicodeLetterLm = [];
UnicodeLetterLo = [];
UnicodeLetterNl = [];
UnicodeLetterMn = [];
UnicodeLetterMc = [];
UnicodeLetterNd = [];
UnicodeLetterPc = [];

for (indexI = 0; indexI < Lu.length; indexI++) {
  for (indexJ = Lu[indexI][0]; indexJ <= Lu[indexI][1]; indexJ++) {
    UnicodeLetterLu[indexJ] = true;
  }
}
for (indexI = 0; indexI < Ll.length; indexI++) {
  for (indexJ = Ll[indexI][0]; indexJ <= Ll[indexI][1]; indexJ++) {
    UnicodeLetterLl[indexJ] = true;
  }
}  
for (indexI = 0; indexI < Lt.length; indexI++) {
  for (indexJ = Lt[indexI][0]; indexJ <= Lt[indexI][1]; indexJ++) {
    UnicodeLetterLt[indexJ] = true;
  }
}
for (indexI = 0; indexI < Lm.length; indexI++) {
  for (indexJ = Lm[indexI][0]; indexJ <= Lm[indexI][1]; indexJ++) {
    UnicodeLetterLm[indexJ] = true;
  }
}
for (indexI = 0; indexI < Lo.length; indexI++) {
  for (indexJ = Lo[indexI][0]; indexJ <= Lo[indexI][1]; indexJ++) {
    UnicodeLetterLo[indexJ] = true;
  }
}
for (indexI = 0; indexI < Nl.length; indexI++) {
  for (indexJ = Nl[indexI][0]; indexJ <= Nl[indexI][1]; indexJ++) {
    UnicodeLetterNl[indexJ] = true;
  }
}
for (indexI = 0; indexI < Mn.length; indexI++) {
  for (indexJ = Mn[indexI][0]; indexJ <= Mn[indexI][1]; indexJ++) {
    UnicodeLetterMn[indexJ] = true;
  }
}
for (indexI = 0; indexI < Mc.length; indexI++) {
  for (indexJ = Mc[indexI][0]; indexJ <= Mc[indexI][1]; indexJ++) {
    UnicodeLetterMc[indexJ] = true;
  }
}
for (indexI = 0; indexI < Nd.length; indexI++) {
  for (indexJ = Nd[indexI][0]; indexJ <= Nd[indexI][1]; indexJ++) {
    UnicodeLetterNd[indexJ] = true;
  }
}
for (indexI = 0; indexI < Pc.length; indexI++) {
  for (indexJ = Pc[indexI][0]; indexJ <= Pc[indexI][1]; indexJ++) {
    UnicodeLetterPc[indexJ] = true;
  }
}

errorCount = 0;
count = 0;
var indexP;
var indexO = 0;
for (index = 0; index <= 65535; index++) {  
  if ((UnicodeLetterLu[index] === undefined) && (UnicodeLetterLl[index] === undefined) && (UnicodeLetterLt[index] === undefined) && (UnicodeLetterLm[index] === undefined) && (UnicodeLetterLo[index] === undefined) && (UnicodeLetterNl[index] === undefined) && (UnicodeLetterMn[index] === undefined) && (UnicodeLetterMc[index] === undefined) && (UnicodeLetterNd[index] === undefined) && (UnicodeLetterPc[index] === undefined) && (index !== 0x0024) && (index !== 0x005F)) {   
    try {
      var hex = decimalToHexString(index);
      var identifier1 = "$" + String.fromCharCode(index) + "1";
      var identifier2 = "$\\u" + hex + "1";
      eval(identifier1 + " = 1"); 
      eval(identifier1 + " === " + identifier2);      
      if (indexO === 0) { 
        indexO = index;
      } else {
        if ((index - indexP) !== 1) {             
          if ((indexP - indexO) !== 0) {
            var hexP = decimalToHexString(indexP);
            var hexO = decimalToHexString(indexO);
            $ERROR('#' + hexO + '-' + hexP + ' ');
          } 
          else {
            var hexP = decimalToHexString(indexP);
            $ERROR('#' + hexP + ' ');
          }  
          indexO = index;
        }         
      }
      indexP = index;
      errorCount++;    
    } catch (e) {      
    }
    count++;
  }  
}

if (errorCount > 0) {
  if ((indexP - indexO) !== 0) {
    var hexP = decimalToHexString(indexP);
    var hexO = decimalToHexString(indexO);
    $ERROR('#' + hexO + '-' + hexP + ' ');
  } else {
    var hexP = decimalToHexString(indexP);
    $ERROR('#' + hexP + ' ');
  }     
  $ERROR('Total error: ' + errorCount + ' bad Unicode character in ' + count + ' ');
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
