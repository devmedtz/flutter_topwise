class Print {
  Print({
    required this.base64image,
    required this.marchantname,
    required this.datetime,
    required this.terminalid,
    required this.merchantid,
    required this.transactiontype,
    this.accountname,
    required this.copytype,
    this.rrn,
    required this.stan,
    this.pan,
    this.expiry,
    this.accountnumber,
    this.bank,
    required this.transactionstatus,
    required this.responsecode,
    required this.message,
    required this.appversion,
    required this.amount,
    required this.bottommessage,
  });

  String base64image;
  String marchantname;
  String datetime;
  String terminalid;
  String merchantid;
  String transactiontype;
  String? accountname;
  String copytype;
  String? rrn;
  String stan;
  String? pan;
  String? expiry;
  String? accountnumber;
  String? bank;
  String transactionstatus;
  String responsecode;
  String message;
  String appversion;
  String amount;
  String bottommessage;

  factory Print.fromJson(Map<String, dynamic> json) => Print(
    base64image: json["base64image"],
    marchantname: json["marchantname"],
    datetime: json["datetime"],
    terminalid: json["terminalid"],
    merchantid: json["merchantid"],
    transactiontype: json["transactiontype"],
    accountname: json["accountname"],
    copytype: json["copytype"],
    rrn: json["rrn"],
    stan: json["stan"],
    pan: json["pan"],
    expiry: json["expiry"],
    accountnumber: json["accountnumber"],
    bank: json["bank"],
    transactionstatus: json["transactionstatus"],
    responsecode: json["responsecode"],
    message: json["message"],
    appversion: json["appversion"],
    amount: json["amount"],
    bottommessage: json["bottommessage"],
  );

  Map<String, dynamic> toJson() => {
    "base64image": base64image,
    "marchantname": marchantname,
    "datetime": datetime,
    "terminalid": terminalid,
    "merchantid": merchantid,
    "transactiontype": transactiontype,
    "accountname": accountname,
    "copytype": copytype,
    "rrn": rrn,
    "stan": stan,
    "pan": pan,
    "expiry": expiry,
    "accountnumber": accountnumber,
    "bank": bank,
    "transactionstatus": transactionstatus,
    "responsecode": responsecode,
    "message": message,
    "appversion": appversion,
    "amount": amount,
    "bottommessage": bottommessage,
  };
}
