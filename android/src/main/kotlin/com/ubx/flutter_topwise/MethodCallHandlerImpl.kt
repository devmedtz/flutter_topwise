package com.ubx.flutter_topwise

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.topwise.cloudpos.aidl.printer.Align
import com.topwise.cloudpos.aidl.printer.ImageUnit
import com.topwise.cloudpos.aidl.printer.PrintTemplate
import com.topwise.cloudpos.aidl.printer.TextUnit
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import android.util.Base64
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.topwise.emv.TopWiseDevice
import com.topwise.emv.utlis.DeviceState


const val ERROR_CODE_PAYMENT_INITIALIZATION = "INIT_PAYMENT_ERROR"

class MethodCallHandlerImpl(
        messenger: BinaryMessenger?,
        private val binding: ActivityPluginBinding
) :
        MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener, PluginRegistry.RequestPermissionsResultListener {

    private var channel: MethodChannel? = null
    private var result: MethodChannel.Result? = null


    init {
        channel = MethodChannel(messenger!!, "topwise")

        channel?.setMethodCallHandler(this)

        binding.addActivityResultListener(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        this.result = result

        topWiseDevice.configureTerminal()

        when (call.method) {

            "initializePayment" -> {
                makePayment(call)
            }
            "startprint" -> {
                startPrint(call)
            }
            "startcustomprint" -> {
                startcustomPrint(call)
            }

            else -> result.notImplemented()
        }

    }

    /**
     * It is invoked when making transaction
     * @param arg is the data that was passed in from the flutter side to make payment
     */
    private fun makePayment(call: MethodCall) {

        if (call.arguments == null || !(call.arguments is Map<*,*>)) {
            result?.error(ERROR_CODE_PAYMENT_INITIALIZATION, "Invalid input(s)", null)
            return
        }

        val amount = call.argument<String>("amount")!!
        topWiseDevice.startEmv(amount)
    }

    private val topWiseDevice by lazy {
        TopWiseDevice(binding.activity) {
            when (it.state) {
                DeviceState.INSERT_CARD -> {
                    val map: MutableMap<String, Any> = mutableMapOf()
                    val map1: MutableMap<String, Any> = mutableMapOf()
                    val transactionData = it.transactionData
                    map1.put("pan",transactionData?.pan!!)
                    map1.put("stan",transactionData.stan)
                    map1.put("rrn",transactionData.rrn)
                    map1.put("pinBlock",transactionData?.pinBlock!!)
                    map1.put("iccData",transactionData.iccData)
                    map1.put("track2Data",transactionData.track2Data)
                    map1.put("postDataCode",transactionData.postDataCode)
                    map1.put("cardExpiryDate",transactionData.cardExpiryDate)
                    map1.put("acceptorCode",transactionData.acceptorCode)
                    map1.put("sequenceNumber",transactionData.sequenceNumber)
                    map1.put("serviceCode",transactionData.serviceCode)
                    map1.put("transactionCode",transactionData.transactionCode)
                    map1.put("terminalId",transactionData.terminalId)
                    map1.put("merchantName",transactionData.merchantName)
                    map1.put("customerName",transactionData?.customerName!!)
                    map1.put("acquiringInstitutionalCode",transactionData.acquiringInstitutionalCode)
                    map1.put("amount",transactionData.amount.toString())
                    map1.put("accountType",transactionData.accountType.toString())

                    map.put("state", it.state.toString())
                    map.put("message", it.message)
                    map.put("status", it.status)
                    map.put("transactionData", map1)

                    result?.success(map)
                }
                DeviceState.PROCESSING -> {
                }
                DeviceState.INPUT_PIN -> {
                }
                DeviceState.PIN_DATA -> {
                }
                DeviceState.INFO -> {
                    val map: MutableMap<String, Any> = mutableMapOf()
                    val map1: MutableMap<String, Any> = mutableMapOf()
                    val transactionData = it.transactionData
                    map1.put("pan",transactionData?.pan!!)
                    map1.put("stan",transactionData.stan)
                    map1.put("rrn",transactionData.rrn)
                    map1.put("pinBlock",transactionData.pinBlock!!)
                    map1.put("iccData",transactionData.iccData)
                    map1.put("track2Data",transactionData.track2Data)
                    map1.put("postDataCode",transactionData.postDataCode)
                    map1.put("cardExpiryDate",transactionData.cardExpiryDate)
                    map1.put("acceptorCode",transactionData.acceptorCode)
                    map1.put("sequenceNumber",transactionData.sequenceNumber)
                    map1.put("serviceCode",transactionData.serviceCode)
                    map1.put("transactionCode",transactionData.transactionCode)
                    map1.put("terminalId",transactionData.terminalId)
                    map1.put("merchantName",transactionData.merchantName)
                    map1.put("customerName",transactionData.customerName!!)
                    map1.put("acquiringInstitutionalCode",transactionData.acquiringInstitutionalCode)
                    map1.put("amount",transactionData.amount.toString())
                    map1.put("accountType",transactionData.accountType.toString())

                    map.put("state", it.state.toString())
                    map.put("message", it.message)
                    map.put("status", it.status)
                    map.put("transactionData", map1)

                    result?.success(map)
                    //Get Transaction INFO
                }
                else -> {
                    val map: MutableMap<String, Any> = mutableMapOf()

                    map["state"] = it.state.toString()
                    map["message"] = it.message
                    map["status"] = it.status

                    result?.success(map)
                }
            }
        }
    }

    fun getQrCodeBitmap(txnId: String, amount: String, from: String): Bitmap {
        val size = 512 //pixels
        val qrCodeContent = "FROM:$from;TXNID;:$txnId;AMOUNT:$amount;;"
        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }

    private fun startPrint(call: MethodCall) {

        val base64String = call.argument<String>("base64image")!!
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        val template: PrintTemplate = PrintTemplate.getInstance()
        template.init(binding.activity, null)
        template.clear()

        val copytype = call.argument<String>("copytype")!!
        template.add(
                TextUnit(
                        "******* " + copytype +" Copy" + " *******",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(false)
        )

        template.add(ImageUnit(Align.CENTER,bitmap, 550, 80, ))

        template.add(
                TextUnit(
                        "",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setWordWrap(false)
                        .setBold(true)
        )

        val marchantname = call.argument<String>("marchantname")!!

        template.add(
                TextUnit(
                        marchantname,
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(false)
        )

        template.add(
                TextUnit("")
                        .setWordWrap(false)
                        .setBold(false)
        )

        val terminalid = call.argument<String>("terminalid")!!
        template.add(
                1,
                TextUnit("TERMINAL ID:", TextUnit.TextSize.NORMAL, Align.LEFT)
                        .setBold(false),
                1,
                TextUnit(terminalid, TextUnit.TextSize.NORMAL, Align.RIGHT)
                        .setBold(false)
        )

        val merchantid = call.argument<String>("merchantid")!!
        template.add(
                1,
                TextUnit("MERCHANT ID:", TextUnit.TextSize.NORMAL, Align.LEFT)
                        .setBold(false),
                1,
                TextUnit(merchantid, TextUnit.TextSize.NORMAL, Align.RIGHT)
                        .setBold(false)
        )
        template.add(
                1,
                TextUnit("TXN ID:", TextUnit.TextSize.NORMAL, Align.LEFT)
                        .setBold(false),
                3,
                TextUnit("TH9960585857835", TextUnit.TextSize.NORMAL, Align.RIGHT)
                        .setBold(false)
        )

        template.add(
                TextUnit(
                        "",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setWordWrap(false)
                        .setBold(true)
        )



        val transactiontype = call.argument<String>("transactiontype")!!
        template.add(
                TextUnit(
                        "==== $transactiontype ====",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(false)
        )

        template.add(
                TextUnit("")
                        .setWordWrap(false)
                        .setBold(false)
        )

        val datetime = call.argument<String>("datetime")!!
        template.add(
                1,
                TextUnit("DATE:", TextUnit.TextSize.NORMAL, Align.LEFT)
                        .setBold(false),
                3,
                TextUnit(datetime, TextUnit.TextSize.NORMAL, Align.RIGHT)
                        .setBold(false)
        )


        val rrn = call.argument<String>("rrn")!!
        template.add(
                1,
                TextUnit("FROM:", TextUnit.TextSize.NORMAL, Align.LEFT)
                        .setBold(false),
                3,
                TextUnit(rrn, TextUnit.TextSize.NORMAL, Align.RIGHT)
                        .setBold(false)
        )


        if (call.argument<String>("pan") != null) {
            val pan = call.argument<String>("pan")!!
            template.add(
                    1,
                    TextUnit("FROM ACC:", TextUnit.TextSize.NORMAL, Align.LEFT)
                            .setBold(false),
                    2,
                    TextUnit(pan, TextUnit.TextSize.NORMAL, Align.RIGHT)
                            .setBold(false)
            )
        }



        if (call.argument<String>("expiry") != null) {
            val expiry = call.argument<String>("expiry")!!
            template.add(
                    1,
                    TextUnit("CARD EXPIRY", TextUnit.TextSize.NORMAL, Align.LEFT)
                            .setBold(true),
                    1,
                    TextUnit(expiry, TextUnit.TextSize.NORMAL, Align.RIGHT)
                            .setBold(true)
            )
        }

        if (call.argument<String>("accountnumber") != null) {
            val accountnumber = call.argument<String>("accountnumber")!!
            template.add(
                    1,
                    TextUnit("ACCOUNT NUMBER:", TextUnit.TextSize.NORMAL, Align.LEFT)
                            .setBold(true),
                    1,
                    TextUnit(accountnumber, TextUnit.TextSize.NORMAL, Align.RIGHT)
                            .setBold(true)
            )
        }
        if (call.argument<String>("bank") != null) {
            val bank = call.argument<String>("bank")!!
            template.add(
                    1,
                    TextUnit("BANK", TextUnit.TextSize.NORMAL, Align.LEFT)
                            .setBold(true),
                    1,
                    TextUnit(bank, TextUnit.TextSize.NORMAL, Align.RIGHT)
                            .setBold(true)
            )
        }


        template.add(
                TextUnit(
                        "",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(false)
        )


        val amount = call.argument<String>("amount")!!

        template.add(
                1,
                TextUnit("AMOUNT", TextUnit.TextSize.NORMAL, Align.LEFT)
                        .setBold(false),
                2,
                TextUnit("$amount TZS", TextUnit.TextSize.NORMAL, Align.RIGHT)
                        .setBold(false)
        )

        template.add(
                TextUnit(
                        "",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(true)
        )

        var qrCode = getQrCodeBitmap(txnId = "TH3i4347i4", amount = "2,000,000", from = "MEDSON NAFTALI KIULA")

        template.add(ImageUnit(Align.CENTER,qrCode, 170, 170, ))

        template.add(
                TextUnit(
                        "",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(true)
        )

        val bottommessage = call.argument<String>("bottommessage")!!

        template.add(
                TextUnit(
                        "----- $bottommessage -----",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(false)
        )

        template.add(
                TextUnit(
                        "",
                        TextUnit.TextSize.NORMAL,
                        Align.CENTER
                )
                        .setBold(true)
        )

        val nums = arrayOf(1, 5, 10)
        for (i in nums) {
            template.add(
                    TextUnit(
                            "",
                            TextUnit.TextSize.NORMAL,
                            Align.CENTER
                    )
            )
        }

        topWiseDevice.printDoc(template = template) //perform print operation

    }
    private fun startcustomPrint(call: MethodCall) {

        val marchantname = call.argument<String>("textprint")
        Log.e("TAG", "startcustomPrint: "+ marchantname, )
        val printmodel = Printmodel.fromJson(marchantname!!)
        val template: PrintTemplate = PrintTemplate.getInstance()
        template.init(binding.activity, null)
        template.clear()

        for (i in printmodel){
            if (i.data.size == 1 &&i.data[0].image != null){
                template.add(imageunit(i.data[0]))

            }else if (i.data[0].text != null && i.data.size == 2){
                template.add(
                        i.data[0].flex,
                        textunit(i.data[0]),
                        i.data[1].flex,
                        textunit(i.data[1])
                )
            }else{
                template.add(textunit(i.data[0]))
            }
        }

        val nums = arrayOf(1, 5, 10)
        for (i in nums) {
            template.add(
                    TextUnit(
                            "",
                            TextUnit.TextSize.NORMAL,
                            Align.CENTER
                    )
            )
        }

        topWiseDevice.printDoc(template) //perform print operation


    }
    private fun imageunit( i: Datum): ImageUnit {
        val base64String = i.image
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        return if (i.imageheight == null && i.imagewidth == null){
            ImageUnit(
                    i.align?.let { align(it) },bitmap, bitmap.width, bitmap.height, )
        }else if (i.imageheight == null && i.imagewidth != null){
            ImageUnit(
                    i.align?.let { align(it) },bitmap, i.imagewidth, bitmap.height, )
        }else if (i.imageheight != null && i.imagewidth == null){
            ImageUnit(
                    i.align?.let { align(it) },bitmap, bitmap.width, i.imageheight, )
        }else {
            ImageUnit(
                    i.align?.let { align(it) },bitmap, i.imagewidth!!, i.imageheight!!, )

        }

    }
    private fun textunit(text: Datum): TextUnit {
        return if (text.align == null){
            TextUnit( text.text)
        }else {
            TextUnit(
                    text.text,
                    textsize(text.textsize!!),
                    align(text.align)
            )

        }
                .setBold(text.bold == true)
                .setWordWrap(text.textwrap == true)

    }
    private fun align(align: String): Align{
        return when (align) {
            "center" -> {
                Align.CENTER
            }
            "right" -> {
                Align.RIGHT
            }
            else -> {
                Align.LEFT
            }
        }
    }
    private fun textsize(align: String): Int{
        return when (align) {
            "normal" -> {
                TextUnit.TextSize.NORMAL
            }
            "large" -> {
                TextUnit.TextSize.LARGE
            }
            "small" -> {
                TextUnit.TextSize.SMALL
            }
            else -> {
                TextUnit.TextSize.XLARGE
            }
        }
    }


    /**
     * this is the call back that is invoked when the activity result returns a value after calling
     * startActivityForResult().
     * @param data is the intent that has the bundle where we can get our result [MonnifyTransactionResponse]
     * @param requestCode if it matches with our [REQUEST_CODE] it means the result if the one we
     * asked for.
     * @param resultCode, it is okay if it equals [RESULT_OK]
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {

        return true
    }

    /**
     * dispose the channel when this handler detaches from the activity
     */
    fun dispose() {
        channel?.setMethodCallHandler(null)
        channel = null
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ): Boolean {
        TODO("Not yet implemented")
    }


}