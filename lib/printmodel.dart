import 'dart:convert';

List<Printmodel> printmodelFromJson(String str) => List<Printmodel>.from(json.decode(str).map((x) => Printmodel.fromJson(x)));

String printmodelToJson(List<Printmodel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class Printmodel {
  Printmodel({
    required this.data,
  });

  List<Datum> data;

  factory Printmodel.fromJson(Map<String, dynamic> json) => Printmodel(
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
  @override
  toString() => "{data: $data}";
}

class Datum {
  Datum({
    this.image,
    this.align,
    this.imagewidth,
    this.imageheight,
    this.text,
    this.flex = 1,
    this.textsize,
    this.bold,
    this.textwrap,
  });

  String? image;
  String? align;
  int? imagewidth;
  int? imageheight;
  int flex;
  String? text;
  String? textsize;
  bool? bold;
  bool? textwrap;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    image: json["image"],
    align: json["align"],
    imagewidth: json["imagewidth"],
    imageheight: json["imageheight"],
    flex: json["flex"],
    text: json["text"],
    textsize: json["textsize"],
    bold: json["bold"],
    textwrap: json["textwrap"],
  );

  Map<String, dynamic> toJson() => {
    "image": image,
    "align": align,
    "imagewidth": imagewidth,
    "imageheight": imageheight,
    "flex": flex,
    "text": text,
    "textsize": textsize,
    "bold": bold,
    "textwrap": textwrap,
  };

  @override
  toString() =>
      '{"image": $image,'
          '"align": $align,'
          '"flex": $flex,'
          '"imagewidth": $imagewidth,'
          '"imageheight": $imageheight,'
          '"text": $text,'
          '"textsize": $textsize,'
          '"bold": $bold,'
          '"textwrap": $textwrap,}';
}
