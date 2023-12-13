import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_topwise/flutter_topwise.dart';
import 'package:flutter_topwise/print.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _topwisePlugin = FlutterTopwise();
  late String base64string;

  @override
  void initState() {
    super.initState();
    start();
  }

  Future<void> start() async {
    final ByteData assetByteData = await rootBundle.load("asset/logo1.png");
    final Uint8List imagebytes = assetByteData.buffer.asUint8List();
    base64string = base64.encode(imagebytes); //convert bytes to base64 string
  }

  Future<void> initPayment() async {
    _topwisePlugin.initialize(2000).then((value) {
      print(value);
    });
  }

  printReceipt() {
    var args = Print(
      base64image: base64string,
      marchantname: "TECHNOLOGY HOMESITE",
      datetime: "2023-12-11 18:55",
      terminalid: "2LUX4199",
      merchantid: "2LUXAA0000",
      transactiontype: "CARD WITHDRAWAL",
      copytype: "Merchant",
      rrn: "MEDSON NAFTALI KIULA",
      stan: "904165",
      pan: "539983******1954",
      expiry: null,
      transactionstatus: "DECLINED",
      responsecode: "55",
      message: "Incorrect PIN",
      appversion: "1.5.3",
      amount: "2,000,000",
      bottommessage: "END OF LEGAL RECEIPT",
    );
    _topwisePlugin.startprinting(args).then((value) {print(value);});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: ElevatedButton(
              onPressed: () => printReceipt(),
              child: const Text('Print Receipt')),
        ),
      ),
    );
  }
}
