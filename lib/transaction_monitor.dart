

import 'debit_card_request_dto.dart';

class TransactionMonitor{
  String state;
  String message;
  bool status;
  DebitCardRequestDto? transactionData;

  TransactionMonitor(
      this.state,
      this.message,
      this.status,
      this.transactionData);

  TransactionMonitor.fromMap(Map<String, dynamic> map)
      : state = map['state'],
        message = map['message'],
        status = map['status'],
        transactionData = map['transactionData'] == null?null:DebitCardRequestDto.fromMap(Map<String, dynamic>.from(map['transactionData']));

  Map<String, dynamic> toMap() => {
    'state': state,
    'message': message,
    'status': status,
    'transactionData': transactionData,
  };

  @override
  String toString() {
    // TODO: implement toString
    return "message: $message, status: $status, transactionData: $transactionData";
  }
}

// TransactionMonitor(state=INFO, message=Card read successfully, status=true, transactionData=DebitCardRequestDto(pan=506109032963986301, stan=935717, rrn=817539267476, pinBlock=, iccData=9F37041047E52295054200A000009A032301269C01009F02060000000010005F2A0205665F340101820258009F1A0205669F03060000000000009F3303E0F8C88407A00000037100019F34034103019F3501229F410400000001, track2Data=506109032963986301D230962115610255, postDataCode=510101511344101, cardExpiryDate=2309, acceptorCode=N/A, sequenceNumber=001, serviceCode=621, transactionCode=566, terminalId=N/A, merchantName=N/A, customerName=CUSTOMER, acquiringInstitutionalCode=506109, amount=1000, accountType=SAVINGS))