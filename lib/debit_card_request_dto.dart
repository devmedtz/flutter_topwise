
class DebitCardRequestDto{
  String pan;
  String stan;
  String rrn;
  String pinBlock;
  String iccData;
  String track2Data;
  String postDataCode;
  String cardExpiryDate;
  String acceptorCode;
  String sequenceNumber;
  String serviceCode;
  String transactionCode;
  String terminalId;
  String merchantName;
  String customerName;
  String acquiringInstitutionalCode;
  String amount;
  String accountType;

  DebitCardRequestDto(
      this.pan,
      this.stan,
      this.rrn,
      this.pinBlock,
      this.iccData,
      this.track2Data,
      this.postDataCode,
      this.cardExpiryDate,
      this.acceptorCode,
      this.sequenceNumber,
      this.serviceCode,
      this.transactionCode,
      this.terminalId,
      this.merchantName,
      this.customerName,
      this.acquiringInstitutionalCode,
      this.amount,
      this.accountType
      );

  DebitCardRequestDto.fromMap(Map<String, dynamic> map)
      : pan = map['pan'],
        stan = map['stan'],
        rrn = map['rrn'],
        pinBlock = map['pinBlock'],
        iccData = map['iccData'],
        track2Data = map['track2Data'],
        postDataCode = map['postDataCode'],
        cardExpiryDate = map['cardExpiryDate'],
        acceptorCode = map['acceptorCode'],
        sequenceNumber = map['sequenceNumber'],
        serviceCode = map['serviceCode'],
        transactionCode = map['transactionCode'],
        terminalId = map['terminalId'],
        merchantName = map['merchantName'],
        customerName = map['customerName'],
        acquiringInstitutionalCode = map['acquiringInstitutionalCode'],
        amount = map['amount'],
        accountType = map['accountType']
  ;

  Map<String, dynamic> toMap() => {
    'pan': pan,
    'stan': stan,
    'rrn': rrn,
    'pinBlock': pinBlock,
    'iccData': iccData,
    'track2Data': track2Data,
    'postDataCode': postDataCode,
    'cardExpiryDate': cardExpiryDate,
    'acceptorCode': acceptorCode,
    'sequenceNumber': sequenceNumber,
    'serviceCode': serviceCode,
    'transactionCode': transactionCode,
    'terminalId': terminalId,
    'merchantName': merchantName,
    'customerName': customerName,
    'acquiringInstitutionalCode': acquiringInstitutionalCode,
    'amount': amount,
    'accountType': accountType,
  };

  @override
  String toString() {
    // TODO: implement toString
    return "pan: $pan, stan: $stan, rrn: $rrn, pinBlock: $pinBlock, iccData: $iccData,"
        " track2Data: $track2Data, postDataCode: $postDataCode, cardExpiryDate: $cardExpiryDate,"
        " acceptorCode: $acceptorCode, sequenceNumber: $sequenceNumber, serviceCode: $serviceCode,"
        " transactionCode: $transactionCode, terminalId: $terminalId, merchantName: $merchantName,"
        " customerName: $customerName, acquiringInstitutionalCode: $acquiringInstitutionalCode,"
        " amount: $amount, accountType: $accountType";
  }
}