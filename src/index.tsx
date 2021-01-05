import { NativeModules } from 'react-native';

type Callback=(error:any,result:any)=>void;
type AlgoType = {
    createNewAccount(callback:Callback):void,
    recoverAccount(seedWords:string,callback:Callback):void,
    getAccountBalance(address:string,callback:Callback):void,
    sendFunds(receiverAddress: string,note:string,amount:number,callback:Callback):void,
    createMultiSignatureAddress(version:number, threshold:number,readableArray:string[],callback:Callback):void,
    createMultisigTransaction(receiverAddress: string,note:string,amount:number,callback:Callback):void,
    approveMultiSigTransaction(signedTransactionString: string, callback: Callback):void,
    submitTransactionToNetwork(signedTransactionString: string, callback: Callback):void,
    createClientFromPurestake(net: string,PURESTAKE_API_PORT:number,PURESTAKE_API_KEY:string, callback: Callback):void,
    createClientFromHackathonInstance(callback: Callback):void,
    createClientFromSandbox(callback:Callback):void,
    createASA(assetTotal: number, defaultFrozen: boolean, unitName: string,
        assetName: string, url: string, assetMetadataHash: string, managerAddress: string, reserveAddress: string,
        freezeAddress: string, clawbackAddress: string, decimals: number, callback: Callback):void,
    changeAccountManager(senderAddress: string,
        newManagerAddress: string, assetId: number,
        reserveAddress: string, freezeAddress: string, clawbackAddress: string,
        decimals: number, callback: Callback):void,
    optInToReceiveAsa(assetId: number, callback: Callback):void, 
    transferAsa(assetId: number, receiverAddress: string, assetAmount: number, callback: Callback):void,
    freezeAsa(assetId: number, freezeState: boolean, addressToFreezeAddress: string, callback: Callback):void,
    revokeAsa(assetId: number, addressToRevokeAddress: string, amountToRevoke: number, receiverAddress: string, callback: Callback):void,
    destroyASA(assetId: number, callback: Callback):void,
    createTransaction(receiverAdddress: string, note: string, valueToSend: number, senderAddress: string, callback: Callback):void,
    addTransaction(transaction: string):void,
    createTransactionForGrouping(receiverAdddress: string, note: string, valueToSend: number, senderAddress: string, callback: Callback):void,
    groupTransactions(callback: Callback) :void,
    signGroupedTransactions(callback: Callback):void,
    assembleSignedTransaction(callback: Callback):void,
     










};

const { Algo } = NativeModules;

export default Algo as AlgoType;
