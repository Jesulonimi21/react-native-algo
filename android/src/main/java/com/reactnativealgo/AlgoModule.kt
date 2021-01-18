package com.reactnativealgo

import AlgoRepository
import android.telecom.Call
import android.util.Log
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.crypto.Ed25519PublicKey
import com.algorand.algosdk.crypto.MultisigAddress
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.fasterxml.jackson.core.JsonProcessingException
import com.google.gson.Gson
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.HashMap

class AlgoModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  val TAG: String = AlgoModule::class.java.getSimpleName()
  var algoRepository: AlgoRepository? = null
  var account: Account? = null
  var multisigAddress: MultisigAddress? = null
  var algodClient: AlgodClient? = null
  var transactionList: MutableList<String> = java.util.ArrayList()
  private var signedTransaction: MutableList<SignedTransaction>? = null
  private lateinit var groupedTransactions: Array<Transaction>


  init{

    Security.removeProvider("BC")
    Security.insertProviderAt(BouncyCastleProvider(), 0)
    try {
      algoRepository = AlgoRepository()
      algodClient = algoRepository!!.algodClient
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }


  override fun getName(): String {
    return "Algo"
  }


  @ReactMethod
  fun createNewAccount(callback: Callback) {
    account = algoRepository!!.createAccountWithoutMnemonic()
    val writableMap: WritableMap = Arguments.createMap()
    writableMap.putString("mnemonic", account!!.toMnemonic())
    writableMap.putString("publicAddress", account!!.getAddress().toString())
    callback.invoke(writableMap)
  }

  @ReactMethod
  fun recoverAccount(seedWords: String?, callback: Callback) {
    try {
      account = algoRepository!!.createAccountWithMnemonic(seedWords)
      val writableMap: WritableMap = Arguments.createMap()
      writableMap.putString("mnemonic", account!!.toMnemonic())
      writableMap.putString("publicAddress", account!!.getAddress().toString())
      callback.invoke(null, writableMap)
    } catch (e: GeneralSecurityException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }


  @ReactMethod
  fun getAccountBalance(address: String?, callback: Callback) {
    try {
      val address1: Address = algoRepository!!.createAnAddress(address)
      val balance: Double = algoRepository!!.getWalletBalance(address1)
      callback.invoke(null, balance)
    } catch (e: NoSuchAlgorithmException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun sendFunds(receiverAddress: String?, note: String?, amount: Int, callback: Callback) {
    if (account == null) {
      callback.invoke("Account was null, make sure createNewAccount or recoverAccount has been called at least once ", null)
      return
    }
    Thread(object : Runnable {
      override fun run() {
        try {
          val id: String = algoRepository!!.sendFunds(account!!, receiverAddress, note!!, amount)
          callback.invoke(null, id)
        } catch (e: Exception) {
          callback.invoke(e.message, null)
          e.printStackTrace()
        }
      }
    }).start()
  }


  @ReactMethod
  fun createMultiSignatureAddress(version: Int, threshold: Int, readableArray: ReadableArray, callback: Callback) {
    val ed25519PublicKeys: MutableList<Ed25519PublicKey> = java.util.ArrayList()
    Log.d("nimiDebug", readableArray.size().toString() + "Readable array size")
    for (i in 0 until readableArray.size()) {
      try {
        ed25519PublicKeys.add(Ed25519PublicKey(algoRepository!!.getClearTextPublicKey(Address(readableArray.getString(i)))))
        Log.d("nimiDebug", readableArray.getString(i))
      } catch (e: Exception) {
        callback.invoke(e.message, null)
        e.printStackTrace()
      }
    }
    try {
      multisigAddress = algoRepository!!.createMultiSigAddress(version, threshold, ed25519PublicKeys)
      callback.invoke(null, multisigAddress!!.toAddress().toString())
    } catch (e: NoSuchAlgorithmException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun createMultisigTransaction(receiverAdddress: String?, note: String?, valueToSend: Int, callback: Callback) {
    if (account == null) {
      callback.invoke("Account was null", null)
      return
    }
    if (multisigAddress == null) {
      callback.invoke("Multisignature Address was null", null)
      return
    }
    try {
      val transaction: Transaction = algoRepository!!.createTransaction(account, receiverAdddress, note!!, valueToSend, multisigAddress.toString())!!
      val signedTransaction: SignedTransaction = algoRepository!!.createAMultiSigTransaction(account!!, transaction, multisigAddress)!!
      val gson = Gson()
      val jsonString: String = gson.toJson(signedTransaction)
      callback.invoke(null, jsonString)
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun approveMultiSigTransaction(signedTransactionString: String?, callback: Callback) {
    if (account == null) {
      callback.invoke("Account was null", null)
      return
    }
    if (multisigAddress == null) {
      callback.invoke("Multisignature Address was null", null)
      return
    }
    var gson = Gson()
    val signedTransaction: SignedTransaction = gson.fromJson(signedTransactionString, SignedTransaction::class.java)
    try {
      val signedTransaction1: SignedTransaction = algoRepository!!.approveMultisigTransaction(account!!, signedTransaction, multisigAddress)!!
      gson = Gson()
      val jsonString: String = gson.toJson(signedTransaction1)
      callback.invoke(null, jsonString)
    } catch (e: NoSuchAlgorithmException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    } catch (e: JsonProcessingException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun submitTransactionToNetwork(signedTransactionString: String?, callback: Callback) {
    val gson = Gson()
    val signedTransaction: SignedTransaction = gson.fromJson(signedTransactionString, SignedTransaction::class.java)
    Thread(object : Runnable {
      override fun run() {
        try {
          val id: String = algoRepository!!.submitTransactionToNetwork(signedTransaction)
          callback.invoke(null, id)
        } catch (e: Exception) {
          callback.invoke(e.message, null)
          e.printStackTrace()
        }
      }
    }).start()
  }

  @ReactMethod
  fun createClientFromPurestake(net: String?,PURESTAKE_API_PORT:Int,PURESTAKE_API_KEY:String, callback: Callback) {
    Thread(
      object : Runnable{
        override fun run() {
          try {
            algoRepository!!.createClientFromPurestakeNode(net!!,PURESTAKE_API_PORT,PURESTAKE_API_KEY)
            callback.invoke(null, "Client created successfully")
          } catch (e: Exception) {
            callback.invoke(e.message, null)
            e.printStackTrace()
          }
        }
      }
    ).start()

  }

  @ReactMethod
  fun createClientFromHackathonInstance(callback: Callback) {
    try {
      algoRepository!!.createClientFromHackathonInstance()
      callback.invoke(null, "Client created successfully")
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun createClientFromSandbox(SANDBOX_ALGOD_ADDRESS:String,SANDBOX_ALGOD_PORT:Int,SANDBOX_ALGOD_API_TOKEN:String, callback: Callback) {

    try {
      algoRepository!!.createAlgodClientFromSandBox(SANDBOX_ALGOD_ADDRESS,SANDBOX_ALGOD_PORT,SANDBOX_ALGOD_API_TOKEN)
      callback.invoke(null, "Created client successfully")
    }catch (e:Exception){
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun createASA(assetTotal: Double, defaultFrozen: Boolean, unitName: String?,
                assetName: String?, url: String?, assetMetadataHash: String?, managerAddress: String?, reserveAddress: String?,
                freezeAddress: String?, clawbackAddress: String?, decimals: Int, callback: Callback) {
    try {
      val manager = Address(managerAddress)
      val freeze = Address(freezeAddress)
      val reserve = Address(reserveAddress)
      val clawback = Address(clawbackAddress)
      val dec: Int = java.lang.Integer.valueOf(decimals)
      val assTotal: BigInteger = BigDecimal.valueOf(assetTotal).toBigInteger()
//        BigInteger.valueOf((assetTotal as Int).toLong())
      Thread(
        object : Runnable {
          override fun run() {
            var assetId: Double? = null
            try {
              assetId = algoRepository!!.createASA(account!!, assTotal, defaultFrozen, unitName, assetName, url, assetMetadataHash, manager
                , reserve, freeze, clawback, dec)
              callback.invoke(null, assetId)
            } catch (e: Exception) {
              callback.invoke(e.message, null)
              e.printStackTrace()
            }
          }
        }
      ).start()
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun changeAccountManager(senderAddress: String?,
                           newManagerAddress: String?, assetId: Double,
                           reserveAddress: String?, freezeAddress: String?, clawbackAddress: String?,
                           decimals: Int, callback: Callback) {
    try {
      val freeze = Address(freezeAddress)
      val reserve = Address(reserveAddress)
      val clawback = Address(clawbackAddress)
      val dec: Int = java.lang.Integer.valueOf(decimals)
      val manager = Address(newManagerAddress)
      val sender = Address(senderAddress)
      Thread(
        object : Runnable {
          override fun run() {
            var id: String? = null
            try {
              id = algoRepository!!.changeAccountManager(sender, account!!, manager, assetId.toLong(), reserve, freeze
                , clawback, dec, algoRepository!!.algodClient)
              callback.invoke(null, id)
            } catch (e: Exception) {
              callback.invoke(e.message, null)
              e.printStackTrace()
            }
          }
        }
      ).start()
    } catch (e: NoSuchAlgorithmException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun optInToReceiveAsa(assetId: Double, callback: Callback) {
    Thread(object : Runnable {
      override fun run() {
        var id: String? = null
        try {
          id = algoRepository!!.optInToReceiveAsa(assetId.toLong(), algoRepository!!.algodClient, account!!)
          callback.invoke(null, id)
        } catch (e: Exception) {
          callback.invoke(e.message, null)
          e.printStackTrace()
        }
      }
    }).start()
  }

  @ReactMethod
  fun transferAsa(assetId: Double, receiverAddress: String?, assetAmount: Double?, callback: Callback) {
    try {
      val receiver = Address(receiverAddress)
      Thread(
        object : Runnable {
          override fun run() {
            var id: String? = null
            try {
              id = algoRepository!!.transferAsa(assetId.toLong(), algoRepository!!.algodClient, account!!,
                receiver, BigDecimal.valueOf(assetAmount!!).toBigInteger())
              callback.invoke(null, id)
            } catch (e: Exception) {
              callback.invoke(e.message, null)
              e.printStackTrace()
            }
          }
        }
      ).start()
    } catch (e: NoSuchAlgorithmException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun freezeAsa(assetId: Double, freezeState: Boolean?, addressToFreezeAddress: String?, callback: Callback) {
    Thread(object : Runnable {
      override fun run() {
        try {
          val addressToFreeze = Address(addressToFreezeAddress)
          val id: String = algoRepository!!.freezeAsa(assetId.toLong(), freezeState, account!!, account!!, addressToFreeze, algoRepository!!.algodClient)
          callback.invoke(null, id)
        } catch (e: NoSuchAlgorithmException) {
          callback.invoke(e.message, null)
          e.printStackTrace()
        } catch (e: Exception) {
          callback.invoke(e.message, null)
          e.printStackTrace()
        }
      }
    }).start()
  }

  @ReactMethod
  fun revokeAsa(assetId: Double,
                addressToRevokeAddress: String?, amountToRevoke: Double, receiverAddress: String?, callback: Callback) {
    Thread(
      object : Runnable {
        override fun run() {
          try {
            val addressToRevoke = Address(addressToRevokeAddress)
            val receiver = Address(receiverAddress)
            val id: String = algoRepository!!.revokeAsa(assetId.toLong(), account!!, account!!, addressToRevoke, algodClient!!, amountToRevoke.toLong(), receiver)
            callback.invoke(null, id)
          } catch (e: Exception) {
            callback.invoke(e.message, null)
            e.printStackTrace()
          }
        }
      }
    ).start()
  }

  @ReactMethod
  fun destroyASA(assetId: Double, callback: Callback) {
    Thread(object : Runnable {
      override fun run() {
        try {
          val id: String = algoRepository!!.destroyASA(assetId.toLong(), account!!, algoRepository!!.algodClient)
          callback.invoke(null, id)
        } catch (e: Exception) {
          callback.invoke(e.message, null)
          e.printStackTrace()
        }
      }
    }).start()
  }

  @ReactMethod
  fun createTransaction(receiverAdddress: String?, note: String?, valueToSend: Int, senderAddress: String?, callback: Callback) {
    try {
      val transaction: Transaction = algoRepository!!.createTransaction(account, receiverAdddress, note!!, valueToSend, senderAddress)!!
      val gson = Gson()
      val transactionString: String = gson.toJson(transaction, Transaction::class.java)
      callback.invoke(null, transactionString)
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun addTransaction(transaction: String) {
    transactionList.add(transaction)
  }

  @ReactMethod
  fun createTransactionForGrouping(receiverAdddress: String?, note: String?, valueToSend: Int, senderAddress: String?, callback: Callback) {
    try {
      val transaction: Transaction = algoRepository!!.createTransaction(account, receiverAdddress, note!!, valueToSend, senderAddress)!!
      val gson = Gson()
      val transactionString: String = gson.toJson(transaction, Transaction::class.java)
      addTransaction(transactionString)
      callback.invoke(null, transactionString)
    } catch (e: Exception) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }


  @ReactMethod
  fun groupTransactions(callback: Callback) {
    if (transactionList.size < 2) {
      callback.invoke("Make sure to call addTransaction passing in each transaction you want to group", null)
    }
    val transactions: MutableList<Transaction> = java.util.ArrayList()
    val gson = Gson()
    for (transaction in transactionList) {
      transactions.add(gson.fromJson(transaction, Transaction::class.java))
    }
    try {
      var transactionArray: Array<Transaction>
      transactionArray= transactions.toTypedArray();
      Log.d("nimiDebug", transactionArray[0]!!.amount.toString() + " " + transactionArray[1]!!.amount)
      groupedTransactions = algoRepository!!.groupTransactions(transactionArray)
      callback.invoke(null, "Successfully grouped trasactions")
    } catch (e: IOException) {
      callback.invoke(e.message, null)
      e.printStackTrace()
    }
  }

  @ReactMethod
  fun signGroupedTransactions(callback: Callback) {
    if (account == null) {
      callback.invoke("Account was null", null)
      return
    }
    if (groupedTransactions == null) {
      callback.invoke("You havent grouped any transaction yet", null)
      return
    }
    signedTransaction = java.util.ArrayList()
    val gson = Gson()
    for (transaction1 in groupedTransactions!!) {
//             transaction1=gson.fromJson(transaction,Transaction.class);
      try {
        signedTransaction!!.add(account!!.signTransaction(transaction1))
      } catch (e: NoSuchAlgorithmException) {
        callback.invoke(e.message, null)
        e.printStackTrace()
      }
    }
    callback.invoke(null, "Successfully signed group transaction")
  }

  @ReactMethod
  fun assembleSignedTransaction(callback: Callback) {
    if (account == null) {
      callback.invoke("Account was null", null)
      return
    }
    if (groupedTransactions == null) {
      callback.invoke("You havent grouped any transaction yet", null)
      return
    }
    if (signedTransaction == null) {
      callback.invoke("You havent signeed any group transaction yet", null)
      return
    }
    Thread(
      object : Runnable {
        override fun run() {
          try {
            val signedTransactionsArray: Array<SignedTransaction>;
            signedTransactionsArray=  signedTransaction!!.toTypedArray()
            val id: String = algoRepository!!.assembleTransactionGroup(signedTransactionsArray, algoRepository!!.algodClient)
            callback.invoke(null, id)
          } catch (e: Exception) {
            callback.invoke(e.message, null)
            e.printStackTrace()
          }
        }
      }
    ).start()
  }


}
