import android.util.Log
import com.algorand.algosdk.crypto.Digest
import com.algorand.algosdk.crypto.Ed25519PublicKey
import com.algorand.algosdk.crypto.MultisigAddress
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.TxGroup
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.*
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.Transaction
import com.fasterxml.jackson.core.JsonProcessingException
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException

class AlgoRepository {
  var HACKATHON_API_ADDRESS = "http://hackathon.algodev.network"
  var HACKATHON_API_TOKEN = "ef920e2e7e002953f4b29a8af720efe8e4ecc75ff102b165e0472834b25832c1"
  var HACKATHON_API_PORT = 9100
  var PURESTAKE_ALGOD_API_TESTNET_ADDRESS = "https://testnet-algorand.api.purestake.io/ps2"
  var PURESTAKE_ALGOD_API_MAINNET_ADDRESS = "https://mainnet-algorand.api.purestake.io/ps2"
  var PURESTAKE_API_KEY = ""
  var clientIsPureStake=false;
  var PURESTAKE_API_PORT = 443
  val TESTNET = "TESTNET"
  val MAINNET = "MAINNET"
  var SANDBOX_ALGOD_ADDRESS = "10.0.2.2"
  val SANDBOX_ALGOD_PORT = 4001
  val SANDBOX_ALGOD_API_TOKEN = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
  var algodClient: AlgodClient
  fun getClearTextPublicKey(address: Address): ByteArray {
    var b = ByteArray(0)
    try {
      b = address.toVerifyKey().getEncoded()
    } catch (e: InvalidKeySpecException) {
      e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    }
    return if (b.size != 44) {
      throw RuntimeException("Generated public key and X.509 prefix is the wrong size")
    } else {
      val raw = ByteArray(32)
      java.lang.System.arraycopy(b, 12, raw, 0, 32)
      raw
    }
  }

  //"soup someone render seven flip woman olive great random color scene physical put tilt say route coin clutch repair goddess rack cousin decide abandon cream"
  //    LL2ZGXSHW7FJGOOVSV76RRZ6IGU5ZF4DPCHQ23G7ZLIWCB4WEMIATDBTLY
  //    algod account address: FMBQKMGDE7LYNDHCSUPJBXNMMT3HC2TXMIFAJKGBYJQDZN4R3M554N4QTY
  //    algod account MNEMONIC: box wear empty voyage scout cheap arrive father wagon correct thought sand planet comfort also patient vast patient tide rather young cinnamon plastic abandon model
  //Multisog address
  //WN3CZQ3ANKKHA5YZEVGWQHAEPKDMAKAX5EMSLH3LZX3ASY4OFIIXW53UAA
  @kotlin.jvm.Throws(Exception::class)
  fun createClientFromHackathonInstance(): AlgodClient {
    clientIsPureStake=false;
    algodClient = AlgodClient(HACKATHON_API_ADDRESS,
      HACKATHON_API_PORT, HACKATHON_API_TOKEN) as AlgodClient
    val headers = arrayOf("X-API-Key")
    val values = arrayOf(HACKATHON_API_TOKEN)
    val status: NodeStatusResponse = algodClient.GetStatus().execute().body()
    java.lang.System.out.println("algod last round: " + status.lastRound)
    return algodClient
  }

  @kotlin.jvm.Throws(Exception::class)
  fun createClientFromPurestakeNode(net: String,PURESTAKE_API_PORT:Int,PURESTAKE_API_KEY:String): AlgodClient {
    val headers = arrayOf("X-API-Key")
    val values = arrayOf(PURESTAKE_API_KEY)
    this.PURESTAKE_API_KEY=PURESTAKE_API_KEY
    this.PURESTAKE_API_PORT=PURESTAKE_API_PORT

    return if (net == TESTNET) {
      algodClient = AlgodClient(PURESTAKE_ALGOD_API_TESTNET_ADDRESS, PURESTAKE_API_PORT, PURESTAKE_API_KEY)
      val status: NodeStatusResponse = algodClient.GetStatus().execute(headers, values).body()
      java.lang.System.out.println("algod last round: " + status.lastRound)
      clientIsPureStake=true;
      algodClient
    } else if (net === MAINNET) {
      algodClient = AlgodClient(PURESTAKE_ALGOD_API_MAINNET_ADDRESS, PURESTAKE_API_PORT, PURESTAKE_API_KEY)
      val status: NodeStatusResponse = algodClient.GetStatus().execute(headers, values).body()
      java.lang.System.out.println("algod last round: " + status.lastRound)
      clientIsPureStake=true;
      algodClient
    } else throw Exception("$net is not currently supported by this sdk")
  }

  @kotlin.jvm.Throws(Exception::class)
  fun createAlgodClientFromSandBox(SANDBOX_ALGOD_ADDRESS:String=this.SANDBOX_ALGOD_ADDRESS,SANDBOX_ALGOD_PORT:Int=this.SANDBOX_ALGOD_PORT,SANDBOX_ALGOD_API_TOKEN:String=this.SANDBOX_ALGOD_API_TOKEN): AlgodClient {
    algodClient = AlgodClient(SANDBOX_ALGOD_ADDRESS,
      SANDBOX_ALGOD_PORT, SANDBOX_ALGOD_API_TOKEN) as AlgodClient
    val headers = arrayOf("X-API-Key")
    val values = arrayOf(SANDBOX_ALGOD_API_TOKEN)
      val status = algodClient.GetStatus().execute(headers, values).body()
   Log.d("algodebug", "${status.lastRound} algod last round ")
    return algodClient
  }

  fun createAccountWithoutMnemonic(): Account? {
    var myAccount1: Account? = null
    try {
      myAccount1 = Account()
      java.lang.System.out.println(" algod account address: " + myAccount1.getAddress())
      java.lang.System.out.println(" algod account MNEMONIC: " + myAccount1.toMnemonic())
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
      println(" Eror while creating new account $e")
    }
    return myAccount1
  }

  @kotlin.jvm.Throws(GeneralSecurityException::class)
  fun createAccountWithMnemonic(mnemonic: String?): Account? {
    var myAccount1: Account? = null
    myAccount1 = Account(mnemonic)
    java.lang.System.out.println(" algod account address: " + myAccount1.getAddress())
    java.lang.System.out.println(" algod account MNEMONIC: " + myAccount1.toMnemonic())
    return myAccount1
  }

  fun getWalletBalance(address: Address?): Double {
    val headers = arrayOf("X-API-Key")
    val values = arrayOf(PURESTAKE_API_KEY)
    var accountInfo: com.algorand.algosdk.v2.client.model.Account? = null
    try {
      if(clientIsPureStake){
        accountInfo = algodClient.AccountInformation(address).execute(headers,values).body()
      }else{
        accountInfo = algodClient.AccountInformation(address).execute().body()
      }

      java.lang.System.out.println("Account Balance: " + accountInfo!!.amount.toString() + " microAlgos")
    } catch (e: Exception) {
      println(e.message)
      e.printStackTrace()
    }
    return accountInfo!!.amount.toDouble()
  }

  @kotlin.jvm.Throws(NoSuchAlgorithmException::class)
  fun createAnAddress(base58PKey: String?): Address {
    return Address(base58PKey)
  }

  @kotlin.jvm.Throws(Exception::class)
  fun sendFunds(senderAccount: Account, receiverAdddress: String?, note: String, valueToSend: Int): String {
    val transactionParametersResponse: TransactionParametersResponse
    val transaction: Transaction
    val senderAddress: String = senderAccount.getAddress().toString()
    val pendingTransactionResponse: PendingTransactionResponse
    transactionParametersResponse = algodClient.TransactionParams().execute().body()
    transaction = Transaction.PaymentTransactionBuilder().sender(senderAddress)
      .amount(valueToSend).receiver(Address(receiverAdddress)).note(note.toByteArray()).suggestedParams(transactionParametersResponse).build()
    val signedTransaction: SignedTransaction = senderAccount.signTransaction(transaction)
    val encodedTransaction: ByteArray = Encoder.encodeToMsgPack(signedTransaction)
    val id: String = algodClient.RawTransaction().rawtxn(encodedTransaction).execute().body().txId
    waitForConfirmation(id)
    pendingTransactionResponse = algodClient.PendingTransactionInformation(id).execute().body()
    java.lang.System.out.println("Transaction information (with notes): " + pendingTransactionResponse.toString())
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun waitForConfirmation(txID: String) {
    var lastRound: Long = algodClient.GetStatus().execute().body().lastRound
    while (true) {
      try {
        // Check the pending tranactions
        val pendingInfo: Response<PendingTransactionResponse> = algodClient.PendingTransactionInformation(txID).execute()
        if (pendingInfo.body().confirmedRound != null && pendingInfo.body().confirmedRound > 0) {
          // Got the completed Transaction
          println("Transaction " + txID + " confirmed in round " + pendingInfo.body().confirmedRound)
          break
        }
        lastRound++
        algodClient.WaitForBlock(lastRound).execute()
      } catch (e: Exception) {
        throw e
      }
    }
  }

  @kotlin.jvm.Throws(NoSuchAlgorithmException::class)
  fun createMultiSigAddress(version: Int, threshold: Int, ed25519PublicKeys: List<Ed25519PublicKey>): MultisigAddress {
    val publicKeys: MutableList<Ed25519PublicKey> = java.util.ArrayList()
    Log.d("nimiDebug", ed25519PublicKeys.size.toString() + "Public key size")
    for (ed25519PublicKey in ed25519PublicKeys) {
      publicKeys.add(ed25519PublicKey)
    }
    val msig = MultisigAddress(version, threshold, publicKeys)
    java.lang.System.out.println(msig.toAddress())
    return msig
  }

  @kotlin.jvm.Throws(Exception::class)
  fun createTransaction(senderAccount: Account?, receiverAdddress: String?, note: String, valueToSend: Int, senderAddress: String?): Transaction? {
    val transactionParametersResponse: TransactionParametersResponse
    var transaction: Transaction? = null
    //        String senderAddress=senderAccount.getAddress().toString();
    var pendingTransactionResponse: PendingTransactionResponse
    transactionParametersResponse = algodClient.TransactionParams().execute().body()
    transaction = Transaction.PaymentTransactionBuilder().sender(senderAddress)
      .amount(valueToSend).receiver(Address(receiverAdddress)).note(note.toByteArray()).suggestedParams(transactionParametersResponse).build()
    return transaction
  }


  //    public    SignedTransaction createSignedTransaction(Transaction transaction){
  //
  //    }
  //    public   void submitSignedTransaction(){
  //
  //    }
  @kotlin.jvm.Throws(NoSuchAlgorithmException::class)
  fun createAMultiSigTransaction(account: Account, transaction: Transaction?, msig: MultisigAddress?): SignedTransaction? {
    var signedTransaction: SignedTransaction? = null
    var pendingTransactionResponse: PendingTransactionResponse
    signedTransaction = account.signMultisigTransaction(msig, transaction)
    //            byte[] encodedTransaction= Encoder.encodeToMsgPack(signedTransaction);
//            String id=algodClient.RawTransaction().rawtxn(encodedTransaction).execute().body().txId;
//            waitForConfirmation(id);
//            pendingTransactionResponse=algodClient.PendingTransactionInformation(id).execute().body();
//            System.out.println("Transaction information (with notes): " + pendingTransactionResponse.toString());
    return signedTransaction
  }

  @kotlin.jvm.Throws(NoSuchAlgorithmException::class, JsonProcessingException::class)
  fun approveMultisigTransaction(account: Account, transaction: SignedTransaction?, msig: MultisigAddress?): SignedTransaction? {
    var signedTransaction: SignedTransaction? = null
    var pendingTransactionResponse: PendingTransactionResponse
    signedTransaction = account.appendMultisigTransaction(msig, transaction)
    val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(signedTransaction)
    //            String id=  algodClient.RawTransaction().rawtxn(encodedTxBytes).execute().body().txId;
//            waitForConfirmation(id);
//            pendingTransactionResponse = algodClient.PendingTransactionInformation(id).execute().body();
//            System.out.println("Transaction information (with notes): " + pendingTransactionResponse.toString());
//            submitTransactionToNetwork(signedTransaction);
    return signedTransaction
  }

  @kotlin.jvm.Throws(Exception::class)
  fun createASA(senderAccount: Account, assetTotal: BigInteger?, defaultFrozen: Boolean, unitName: String?,
                assetName: String?, url: String?, assetMetadataHash: String?, manager: Address?, reserve: Address?,
                freeze: Address?, clawback: Address?, decimals: Int?): Double {
    val params: TransactionParametersResponse = algodClient.TransactionParams().execute().body()
    val tx: Transaction = Transaction.AssetCreateTransactionBuilder().sender(senderAccount.getAddress()).assetTotal(assetTotal)
      .assetDecimals(decimals!!).assetUnitName(unitName).assetName(assetName).url(url)
      .metadataHashUTF8(assetMetadataHash).manager(manager).reserve(reserve).freeze(freeze)
      .defaultFrozen(defaultFrozen).clawback(clawback).suggestedParams(params).build()
    val signedTx: SignedTransaction = senderAccount.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    println("Transaction ID: $id")
    waitForConfirmation(id)
    // Read the transaction
    val pTrx: PendingTransactionResponse = algodClient.PendingTransactionInformation(id).execute().body()
    // Now that the transaction is confirmed we can get the assetID
    val assetID: Long = pTrx.assetIndex
    println("AssetID = $assetID")
    printCreatedAsset(senderAccount, assetID, algodClient)
    printAssetHolding(senderAccount, assetID, algodClient)
    return java.lang.Double.valueOf(assetID.toDouble())
  }

  @kotlin.jvm.Throws(Exception::class)
  fun printCreatedAsset(account: Account, assetID: Long, client: AlgodClient?) {
    var client: AlgodClient? = client
    if (client == null) client = createClientFromHackathonInstance()
    val accountInfo: String = client.AccountInformation(account.getAddress()).execute().toString()
    val jsonObj = JSONObject(accountInfo)
    val jsonArray: JSONArray = jsonObj.get("created-assets") as JSONArray
    if (jsonArray.length() > 0) try {
      for (i in 0 until jsonArray.length()) {
        val ca: JSONObject = jsonArray.get(i) as JSONObject
        val myassetIDInt = ca.get("index") as Int
        if (assetID == myassetIDInt.toLong()) {
          java.lang.System.out.println("Created Asset Info: " + ca.toString(2)) // pretty print
          break
        }
      }
    } catch (e: Exception) {
      throw e
    }
  }

  // utility function to print asset holding
  @kotlin.jvm.Throws(Exception::class)
  fun printAssetHolding(account: Account, assetID: Long, client: AlgodClient?) {
    var client: AlgodClient? = client
    if (client == null) client = createClientFromHackathonInstance()
    val accountInfo: String = client.AccountInformation(account.getAddress()).execute().toString()
    val jsonObj = JSONObject(accountInfo)
    val jsonArray: JSONArray = jsonObj.get("assets") as JSONArray
    if (jsonArray.length() > 0) try {
      for (i in 0 until jsonArray.length()) {
        val ca: JSONObject = jsonArray.get(i) as JSONObject
        val myassetIDInt = ca.get("asset-id") as Int
        if (assetID == myassetIDInt.toLong()) {
          java.lang.System.out.println("Asset Holding Info: " + ca.toString(2)) // pretty print
          break
        }
      }
    } catch (e: Exception) {
      throw e
    }
  }

  @kotlin.jvm.Throws(Exception::class)
  fun changeAccountManager(sender: Address?, presentManager: Account, newManager: Address?, assetId: Long
                           , reserve: Address?, freeze: Address?, clawback: Address?, decimals: Int?, client: AlgodClient): String {
    val params: TransactionParametersResponse = client.TransactionParams().execute().body()
    params.fee = 1000.toLong()
    val tx: Transaction = Transaction.AssetConfigureTransactionBuilder().sender(sender).assetIndex(assetId)
      .manager(newManager).reserve(reserve).freeze(freeze).clawback(clawback).suggestedParams(params)
      .build()
    val signedTx: SignedTransaction = presentManager.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    println("Transaction ID: $id")
    waitForConfirmation(id)
    // We can now list the account information for acct3
    // and see that it can accept the new asset
    java.lang.System.out.println("Account 3 = " + presentManager.getAddress().toString())
    printAssetHolding(presentManager, assetId, client)
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun optInToReceiveAsa(assetId: Long, client: AlgodClient, sender: Account): String {
    val params: TransactionParametersResponse = client.TransactionParams().execute().body()
    params.fee = 1000.toLong()
    val tx: Transaction = Transaction.AssetAcceptTransactionBuilder().acceptingAccount(sender.getAddress()).assetIndex(assetId)
      .suggestedParams(params).build()
    val signedTx: SignedTransaction = sender.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    println("Transaction ID: $id")
    waitForConfirmation(signedTx.transactionID)
    java.lang.System.out.println("Account 3 = " + sender.getAddress().toString())
    printAssetHolding(sender, assetId, client)
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun transferAsa(assetId: Long, client: AlgodClient, sender: Account, receiver: Address?, assetAmount: BigInteger?): String {
    val params: TransactionParametersResponse = client.TransactionParams().execute().body()
    params.fee = 1000.toLong()
    val tx: Transaction = Transaction.AssetTransferTransactionBuilder().sender(sender.getAddress()).assetReceiver(receiver)
      .assetAmount(assetAmount).assetIndex(assetId).suggestedParams(params).build()
    val signedTx: SignedTransaction = sender.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    println("Transaction ID: $id")
    waitForConfirmation(signedTx.transactionID)
    java.lang.System.out.println("Account 3  = " + sender.getAddress().toString())
    printAssetHolding(sender, assetId, client)
    java.lang.System.out.println("Account 1  = " + sender.getAddress().toString())
    printAssetHolding(sender, assetId, client)
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun freezeAsa(assetId: Long?, freezeState: Boolean?, sender: Account, manager: Account, addressToFreeze: Address, client: AlgodClient): String {
    val params: TransactionParametersResponse = client.TransactionParams().execute().body()
    params.fee = 1000.toLong()
    val tx: Transaction = Transaction.AssetFreezeTransactionBuilder().sender(sender.getAddress()).freezeTarget(addressToFreeze)
      .freezeState(freezeState!!).assetIndex(assetId).suggestedParams(params).build()
    val signedTx: SignedTransaction = manager.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    println("Transaction ID: $id")
    waitForConfirmation(signedTx.transactionID)
    println("Account 3 = $addressToFreeze")
    //        printAssetHolding(addressToFreeze, assetId,client);
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun revokeAsa(assetId: Long?, sender: Account, clawback: Account,
                addressToRevoke: Address?, client: AlgodClient, amountToRevoke: Long?, receiver: Address?): String {
    val params: TransactionParametersResponse = client.TransactionParams().execute().body()
    params.fee = 1000.toLong()
    val assetAmount: BigInteger = BigInteger.valueOf(amountToRevoke!!)
    val tx: Transaction = Transaction.AssetClawbackTransactionBuilder().sender(sender.getAddress())
      .assetClawbackFrom(addressToRevoke).assetReceiver(receiver).assetAmount(assetAmount)
      .assetIndex(assetId).suggestedParams(params).build()
    val signedTx: SignedTransaction = clawback.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    println("Transaction ID: $id")
    waitForConfirmation(signedTx.transactionID)
    //        System.out.println("Account 3 = " + addressToFreeze);
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun destroyASA(assetId: Long, manager: Account, client: AlgodClient): String {
    val params: TransactionParametersResponse = client.TransactionParams().execute().body()
    params.fee = 1000.toLong()
    val tx: Transaction = Transaction.AssetDestroyTransactionBuilder().sender(manager.getAddress()).assetIndex(assetId)
      .suggestedParams(params).build()
    val signedTx: SignedTransaction = manager.signTransaction(tx)
    val id = submitTransactionToNetwork(signedTx)
    //        System.out.println("Transaction ID: " + id);
    waitForConfirmation(signedTx.transactionID)
    println("Nothing should print after this, Account 1 asset is sucessfully deleted")
    printAssetHolding(manager, assetId, client)
    printCreatedAsset(manager, assetId, client)
    return id
  }

  @kotlin.jvm.Throws(Exception::class)
  fun submitTransactionToNetwork(signedTransaction: SignedTransaction?): String {
    var pendingTransactionResponse: PendingTransactionResponse? = null
    var encodedTxBytes = ByteArray(0)
    encodedTxBytes = Encoder.encodeToMsgPack(signedTransaction)
    val id: String = algodClient.RawTransaction().rawtxn(encodedTxBytes).execute().body().txId
    waitForConfirmation(id)
    pendingTransactionResponse = algodClient.PendingTransactionInformation(id).execute().body()
    java.lang.System.out.println("Transaction information (with notes): " + pendingTransactionResponse.toString())
    return id
  }

  @kotlin.jvm.Throws(IOException::class)
  fun groupTransactions(transactions: Array<Transaction>): Array<Transaction> {
    val gid: Digest = TxGroup.computeGroupID(*transactions)
    for (i in transactions.indices) {
      transactions[i].assignGroupID(gid)
    }
    return transactions
  }

  fun signTransaction(account: Account, transaction: Transaction?): SignedTransaction? {
    var signedTransaction: SignedTransaction? = null
    try {
      signedTransaction = account.signTransaction(transaction)
      return signedTransaction
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    }
    return signedTransaction
  }

  @kotlin.jvm.Throws(Exception::class)
  fun assembleTransactionGroup(transactions: Array<SignedTransaction>, algodClient: AlgodClient): String {
    val byteOutputStream = ByteArrayOutputStream()
    for (i in transactions.indices) {
      val encodedTxBytes1: ByteArray = Encoder.encodeToMsgPack(transactions[i])
      byteOutputStream.write(encodedTxBytes1)
    }
    val groupTransactionBytes: ByteArray = byteOutputStream.toByteArray()
    val id: String = algodClient.RawTransaction().rawtxn(groupTransactionBytes).execute().body().txId
    println("Successfully sent tx with ID: $id")
    waitForConfirmation(id)
    return id
  }

//  fun getAlgodClient(): AlgodClient {
//    return algodClient
//  }

  init {
    algodClient = createClientFromHackathonInstance()
  }
}
