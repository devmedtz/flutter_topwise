import 'dart:convert';
import 'dart:io';
import 'package:flutter/services.dart';
import 'package:flutter_topwise/printmodel.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'print.dart';
import 'transaction_monitor.dart';

class FlutterTopwise {
  static const MethodChannel _channel = MethodChannel("topwise");

  Future<TransactionMonitor> initialize(double amount) async {
    int finalamount = (amount * 100).toInt();
    Map<String, String> args = {
      "amount": finalamount.toString()
    };

    return _channel
        .invokeMethod("initializePayment", args)
        .then<TransactionMonitor>((dynamic result) =>
        TransactionMonitor.fromMap(Map<String, dynamic>.from(result)));
  }

  Future<TransactionMonitor> startprinting (Print print) async {
    return _channel
        .invokeMethod("startprint", print.toJson())
        .then<TransactionMonitor>((dynamic result) =>
        TransactionMonitor.fromMap(Map<String, dynamic>.from(result)));
  }

  Future<TransactionMonitor> startcustomprinting (List<Widget> template) async {
    Map<String, String> args = {
      "textprint": printmodelToJson( await printcomponent(template))
    };
    return _channel
        .invokeMethod("startcustomprint", args)
        .then<TransactionMonitor>((dynamic result) =>
        TransactionMonitor.fromMap(Map<String, dynamic>.from(result)));
  }

  Future<List<Printmodel>> printcomponent(List<Widget> children) async {
    List<Printmodel> generalprintvalue = [];
    generalprintvalue = await mutipleitem(children);
    return generalprintvalue;
  }

  Future<List<Printmodel>> mutipleitem (List<Widget> children)async{
    List<Printmodel> generalprintvalue = [];
    for(Widget i in children){
      if (i is Row) {
        generalprintvalue.add(Printmodel(data: await rowcol(i.children)));
      }else if (i is Container) {
        if (i.child != null) {
          if (generalprintvalue.isEmpty) {
            generalprintvalue = await container(i.child!);
          }  else{
            List<Printmodel> sola = await container(i.child!);
            for( int i = 0; i < sola.length; i++){
              generalprintvalue.add(sola[i]);
            }
          }
          // generalprintvalue.add(await container(i.child!));
        } else{
          generalprintvalue.add(Printmodel(data:[await item(i)]));
        }
      }else{
        generalprintvalue.add(Printmodel(data:[await expanded(i)]));
      }
    }
    return generalprintvalue;
  }

  Future<List<Printmodel>> container(Widget i) async{
    List<Printmodel> generalprintvalue = [];
    if (i is Column) {
      if (generalprintvalue.isEmpty) {
        generalprintvalue = await mutipleitem(i.children);
      }  else{
        List<Printmodel> sola = await mutipleitem(i.children);
        for( int i = 0; i < sola.length; i++){
          generalprintvalue.add(sola[i]);
        }
      }
    } else{
      generalprintvalue.add(Printmodel(data:[await item(i)]));
    }
    return generalprintvalue;
  }

  Future<List<Datum>> rowcol(List<Widget> children) async {
    List<Datum> printvalue= [];
    for(Widget i in children){
      printvalue.add( await expanded(i));
    }
    return printvalue;
  }

  Future<Datum> expanded (Widget i) async {
    if (i is Expanded) {
      return  await item(i.child,wrap: true, flex:3);
    }else{
      return  await item(i);
    }
  }

  Future<Datum> item(Widget i,{bool? wrap, int flex = 1}) async {
    Datum datum;
    if(i is Image){
      if (i.image is AssetImage) {
        String assetName = (i.image as AssetImage).assetName;
        final ByteData assetByteData = await rootBundle.load(assetName);
        final Uint8List imagebytes = assetByteData.buffer.asUint8List();
        datum = Datum(
            image:base64.encode(imagebytes),align:"center",imagewidth:550, imageheight:70
        );
      } else if (i.image is NetworkImage) {
        String result;
        String imageUrl = (i.image as NetworkImage).url;
        var response = await http.get(Uri.parse(imageUrl));
        if (response.statusCode == HttpStatus.ok) {
          var bytes = response.bodyBytes;
          result = base64.encode(bytes);
        } else {
          result = "";
          throw Exception('Failed to load image: ${response.statusCode}');
        }
        datum = Datum(image:result,align:"center",imagewidth:550, imageheight:70);
      } else if (i.image is MemoryImage) {
        Uint8List imageUrl = (i.image as MemoryImage).bytes;
        datum = Datum(image:base64.encode(imageUrl),align:"center",imagewidth:550, imageheight:70);
      } else{
        String imageUrl = (i.image as FileImage).file.path;
        File imagefile = File(imageUrl); //convert Path to File
        Uint8List imagebytes = await imagefile.readAsBytes(); //convert to bytes
        datum = Datum(image:base64.encode(imagebytes),align:"center",imagewidth:550, imageheight:70);
      }
    }else if (i is Text) {
      String? textalign;
      bool bold = false;

      if (i.textAlign != null) {
        textalign = i.textAlign.toString().split(".")[1];
      }
      if (i.style != null) {
        int weight = int.parse(i.style!.fontWeight.toString().split(".")[1].split("w")[1]);
        if (weight >= 700 ) {
          bold = true;
        }
      }
      bool? rap;
      if (wrap != null) {
        rap = wrap;
      } else if (i.softWrap != null) {
        rap = i.softWrap;
      }else{
        rap = false;
      }
      datum = Datum(text:i.data.toString(), textsize:"normal",
          align: textalign, textwrap: rap, bold: bold, flex: flex );
    }else if (i is Divider) {
      datum = Datum(text: "-------------------------------------------------------------------------",
          textsize:"normal",align: "center",textwrap: false, bold: true );
    }else{
      datum = Datum(text:"", textsize:"normal",align:"center");
    }
    return datum;
  }

}
