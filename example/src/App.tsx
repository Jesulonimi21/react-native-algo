import * as React from 'react';

import { SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  TouchableOpacity } from 'react-native';
import Algo from 'react-native-algo';

export default class App extends React.Component{
  state={
    accountInfo:"AccountInfo",
    signedTrans:"",
    assetId:""
  }


componentDidMount(){
  console.log("good");
}


handleCreateAccountInfoPress=(event)=>{
Algo.createNewAccount((publicAddr)=>{
  this.setState({accountInfo:publicAddr});
  console.log(publicAddr);
});



}
// "box wear empty voyage scout cheap arrive father wagon correct thought sand planet comfort also patient vast patient tide rather young cinnamon plastic abandon model"
// "cactus check vocal shuffle remember regret vanish spice problem property diesel success easily napkin deposit gesture forum bag talent mechanic reunion enroll buddy about attract"
// soup someone render seven flip woman olive great random color scene physical put tilt say route coin clutch repair goddess rack cousin decide abandon cream

handleRecoverAccount=(event)=>{
  Algo.recoverAccount("soup someone render seven flip woman olive great random color scene physical put tilt say route coin clutch repair goddess rack cousin decide abandon cream",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
    this.setState({accountInfo:result});
  })
}

handleSendFunds=(event)=>{
  Algo.sendFunds("EF64SJ6HMEK4FAKC3HI5NA76JMUNOWPVYZF3U7NUYPYQ5VMIE2QZZE5NCM","hi",10,
  (error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}

handleGetAccountBalance=(event)=>{
  Algo.getAccountBalance("VJQG6EJPZDAWFYLFF5XE3OMRQEK6RFFYSBVJOGXBH63ZQZ3QRRIUVIB7MY",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}
// FMBQKMGDE7LYNDHCSUPJBXNMMT3HC2TXMIFAJKGBYJQDZN4R3M554N4QTY
handleCreateMultisigAddress=(event)=>{
  Algo.createMultiSignatureAddress(1,2,["LL2ZGXSHW7FJGOOVSV76RRZ6IGU5ZF4DPCHQ23G7ZLIWCB4WEMIATDBTLY","FMBQKMGDE7LYNDHCSUPJBXNMMT3HC2TXMIFAJKGBYJQDZN4R3M554N4QTY"],(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}


handleCreateMultisigTransaction=(event)=>{
  Algo.createMultisigTransaction("EF64SJ6HMEK4FAKC3HI5NA76JMUNOWPVYZF3U7NUYPYQ5VMIE2QZZE5NCM","hi",11,
  (error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    this.setState({signedTrans:result})
    console.log(result);
  }); 
}

handleApproveMultisigTransaction=(event)=>{
  Algo.recoverAccount("box wear empty voyage scout cheap arrive father wagon correct thought sand planet comfort also patient vast patient tide rather young cinnamon plastic abandon model",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    Algo.approveMultiSigTransaction(this.state.signedTrans,(error,result)=>{
      if(error){
        console.error(error);
        return;
      }
      this.setState({signedTrans:result})
      console.log(result);
    });
    console.log(result);
  })
 
}

handleSubmitTransactionToNetwork=(event)=>{
  Algo.submitTransactionToNetwork(this.state.signedTrans,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
   
    console.log(result);
  });
}
// 13328773  13328807

handleCreateAsa=()=>{
  // (double assetTotal, boolean defaultFrozen, String unitName,
  //   String assetName, String url, String assetMetadataHash, String managerAddress, String reserveAddress,
  //   String freezeAddress, String clawbackAddress, int decimals,Callback callback)
  let{accountInfo}=this.state;
  Algo.createASA(1000,false,"algonati","niminati","github.com/jesulonimi21","16efaa3924a6fd9d3a4824799a4ac65d",
    accountInfo.publicAddress,accountInfo.publicAddress,accountInfo.publicAddress,accountInfo.publicAddress,0,
    (error,result)=>{
      if(error){
        console.error(error);
        return;
      }
      console.log(result);
      this.setState({assetId:result});
    });
}


handleChangeAsaManager=()=>{
  // String senderAddress,
    // String newManagerAddress,Double assetId,
    // String reserveAddress,   String freezeAddress, String clawbackAddress,
    // int decimals,Callback callback)
  let{accountInfo}=this.state;
  Algo.changeAccountManager(accountInfo.publicAddress,"EF64SJ6HMEK4FAKC3HI5NA76JMUNOWPVYZF3U7NUYPYQ5VMIE2QZZE5NCM",13338381,accountInfo.publicAddress,accountInfo.publicAddress,accountInfo.publicAddress,0,   (error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);

  });
}


handleOptInToReceiveAsa=()=>{
  // (Double assetId,Callback callback)
  let{accountInfo}=this.state;
  Algo.optInToReceiveAsa(13328807,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}


handleTransferAsa=()=>{
  // (Double assetId,String receiverAddress,Double assetAmount,Callback callback )
  let{accountInfo}=this.state;
  Algo.transferAsa(13328807,"V7QKGG7N2N2XAEH7FSLHEWEJ2YYMKSTKQE2A2WIPM2BPROKCIQNSXYAJUM",50,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}

handleFreezeAsa=()=>{
  let{accountInfo}=this.state;
  // (Double assetId, Boolean freezeState,String addressToFreezeAddress,Callback callback)
  Algo.freezeAsa(13328807,false,"V7QKGG7N2N2XAEH7FSLHEWEJ2YYMKSTKQE2A2WIPM2BPROKCIQNSXYAJUM",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}

handleRevokeAsa=()=>{
  let{accountInfo}=this.state;
  // (Double assetId,
  //   String addressToRevokeAddress,Double amountToRevoke,String receiverAddress,Callback callback)
  Algo.revokeAsa(  13328867,"V7QKGG7N2N2XAEH7FSLHEWEJ2YYMKSTKQE2A2WIPM2BPROKCIQNSXYAJUM",50,accountInfo.publicAddress,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}


handleDestroyAsa=()=>{
  let{accountInfo}=this.state;
  // (Double assetId,Callback callback)
  Algo.destroyASA(this.state.assetId,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}


handleCreateAndGroupTransaction=()=>{
  // (String receiverAdddress,String note,int valueToSend,String senderAddress,Callback callback)
  let{accountInfo}=this.state;
  Algo.createTransactionForGrouping("V7QKGG7N2N2XAEH7FSLHEWEJ2YYMKSTKQE2A2WIPM2BPROKCIQNSXYAJUM","Hi there",1,accountInfo.publicAddress,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}


handleGroupTransactions=()=>{
  Algo.groupTransactions((error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}

handleSignGrouptRANSACTION=()=>{
  Algo.signGroupedTransactions((error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}

handleAssembleSignedTransaction=()=>{
  Algo.assembleSignedTransaction((error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}

handleCreateClientFromPureStake=()=>{
  Algo.createClientFromPurestake("TESTNET",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}
render(){

  let{accountInfo}=this.state;
  return( <View style={{marginTop:30,alignItems:'stretch',flex:1,justifyContent:"center"}}>
      <TouchableOpacity onPress={this.handleCreateAccountInfoPress}><Text>Create Account</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleRecoverAccount}><Text>Recovr Account</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleGetAccountBalance}><Text>Get Account Balance</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleSendFunds}><Text>Send Funds</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateMultisigAddress}><Text>cREATE mULTISIG ADDRESS</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateMultisigTransaction}><Text>cREATE mULTISIG trans</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleApproveMultisigTransaction}><Text>Approve mULTISIG trans</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleSubmitTransactionToNetwork}><Text>Submit mULTISIG trans</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateAsa}><Text>Create ASA</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleChangeAsaManager}><Text>Change ASA manager</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleOptInToReceiveAsa}><Text>Opt In to RECEIVE ASA</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleTransferAsa}><Text>OTransfer ASA</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleFreezeAsa}><Text>Freeze ASA</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleRevokeAsa}><Text>revoke ASA</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleDestroyAsa}><Text>Destroy ASA</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateAndGroupTransaction}><Text>Add TRANSACTION</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleGroupTransactions}><Text>Group TRANSACTION</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleSignGrouptRANSACTION}><Text>Sign Group TRANSACTION</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleAssembleSignedTransaction}><Text>Assembl signd Transaction</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateClientFromPureStake}><Text>Create Purestake client</Text></TouchableOpacity>
      {/* <Text>{accountInfo}</Text> */}
    </View>     
  )
}
}