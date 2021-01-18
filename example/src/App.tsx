import * as React from 'react';

import { SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
 
  StatusBar,
  TouchableOpacity,} from 'react-native';
import Algo from 'react-native-algo';
import { Appbar } from 'react-native-paper';
import { RadioButton, Text,Button,Card,ActivityIndicator, Colors,TextInput,Divider,
  Banner} from 'react-native-paper';
  import Clipboard from '@react-native-community/clipboard';
export default class App extends React.Component{
 

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
  Algo.createClientFromPurestake("TESTNET",PURESTAKE_API_PORT,PURESTAKE_API_KEY,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}
state={
  accountInfo:"AccountInfo",
  signedTrans:"",
  assetId:"",
  node:"customnode",
  network:"testnet",
  seedphraseInput:"",
  recoverAcccountBanner:false,
  recoverAccountData:"",
  createAccountBanner:false,
  createAccountData:"",
  accountBalanceAddress:"",
  accountBalanceBanner:false,
  accountBalanceLoader:false,
  accountBalanceData:"",
  connectToNode:false,
  connectToNodeData:"",
  connectToNodeBanner:false,
  transferFundsBanner:false,
  transferFundsLoading:false,
  transferFundsData:"",
  transferFundsAddress:"",
  amount:"",
  note:"",
  customNodeAddress:"10.0.2.2",
  customNodePort:4001,
  customNodeToken:"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",


}

handleConnectToNodelicked=()=>{
  let{node,network}=this.state;
  if(node=="hackathon"){
    this.setState({connectToNode:true})
    Algo.createClientFromHackathonInstance((error,result)=>{
      if(error){
        console.error(error);
        console.log("error")
        this.setState({connectToNodeData:error,  connectToNodeBanner:true,connectToNode:false})
        return;
      }
      console.log("Success")
      console.log(error);
      console.log(result);
      this.setState({connectToNodeData:result,  connectToNodeBanner:true,connectToNode:false})
    });
  }else if(node=="purestake"){
    if(network=="testnet"){
      this.setState({connectToNode:true})
      Algo.createClientFromPurestake("TESTNET",443,PURESTAKE_API_KEY,(error,result)=>{
        if(error){
          this.setState({connectToNode:false})
          console.error(error);
          this.setState({connectToNodeData:error,  connectToNodeBanner:true,connectToNode:false})
          return;
        }
        console.log(result);
        this.setState({connectToNodeData:result,  connectToNodeBanner:true,connectToNode:false})
      });
    }else{
      this.setState({connectToNode:true})
      Algo.createClientFromPurestake("MAINNET",443,PURESTAKE_API_KEY,(error,result)=>{
        this.setState({connectToNode:false})
        if(error){
          console.error(error);
          this.setState({connectToNodeData:error,  connectToNodeBanner:true,connectToNode:false})
          return;
        }
        this.setState({connectToNodeData:result,  connectToNodeBanner:true,connectToNode:false})
        console.log(result);
      });

    }
  }else if(node=="customnode"){
    this.setState({connectToNode:true});
    let{customNodeAddress,customNodePort,customNodeToken}=this.state;
    Algo.createClientFromSandbox(customNodeAddress,parseInt(customNodePort),customNodeToken,
    (error,result)=>{
      if(error){
        console.error(error);
        this.setState({connectToNodeData:error,  connectToNodeBanner:true,connectToNode:false})
        return;
      }
      this.setState({connectToNodeData:result,  connectToNodeBanner:true,connectToNode:false})
      console.log(result);
    });
  }
}

handleRecoverAccountClicked=()=>{
  let{seedphraseInput}=this.state;
  console.log(seedphraseInput);
  Algo.recoverAccount(seedphraseInput,(error,result)=>{
    if(error){
      console.error(error);
      this.setState({recoverAccountData:error,recoverAcccountBanner:true})
      return;
    }
    console.log(result);
    this.setState({recoverAccountData:`Address : ${result.publicAddress}\n Mnemonic : ${result.mnemonic}`,recoverAcccountBanner:true});
  })
}

handleCreateAccountClicked=()=>{
  Algo.createNewAccount((result)=>{
    this.setState({createAccountData:`Address : ${result.publicAddress}\n Mnemonic : ${result.mnemonic}`});
    console.log(result);
  });
  
}

handleGetAccountBalanceClicked=()=>{
  let{accountBalanceAddress}=this.state;
  this.setState({accountBalanceLoader:true})
  Algo.getAccountBalance(accountBalanceAddress,(error,result)=>{
    if(error){
      console.error(error);
      this.setState({accountBalanceData:error,accountBalanceLoader:false,accountBalanceBanner:true})
      return;
    }
    console.log(result);
    this.setState({accountBalanceData:`${result} Algos`,accountBalanceLoader:false,accountBalanceBanner:true})
     
  });

}

handleTransferFundsClicked=()=>{
  let{transferFundsAddress,note,amount}=this.state;
  this.setState({transferFundsLoading:true})
  Algo.sendFunds(transferFundsAddress, note,parseInt(amount),
  (error,result)=>{
    if(error){
      console.error(error);
      this.setState({transferFundsData:error,transferFundsLoading:false,transferFundsBanner:true})
      return;
    }
    this.setState({transferFundsData:result,transferFundsLoading:false,transferFundsBanner:true})
    console.log(result);
  });
}


handleCreateClientFromSandbox=()=>{
  console.log("cREATING SANDBOX CLIOENT");
  Algo.createClientFromSandbox("10.0.2.2",4001,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
  (error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
}
render(){
  let{accountInfo,node,network,seedphraseInput,recoverAcccountBanner,
    createAccountBanner,accountBalanceAddress,accountBalanceBanner,
    connectToNode,connectToNodeData,connectToNodeBanner,
    recoverAccountData,createAccountData,accountBalanceLoader,
    accountBalanceData,transferFundsBanner,
    transferFundsData,transferFundsLoading,transferFundsAddress,
    amount,note,customNodeAddress,customNodePort,customNodeToken}=this.state;
  return( <View style={{marginTop:0,alignItems:'stretch',flex:1,justifyContent:"flex-start"}}>
<ScrollView>
<Appbar.Header >
       <Appbar.Content title="React-Native-Algo" subtitle={'Showcase'} />
    </Appbar.Header>

   <Card  style={{marginLeft:10,marginRight:10,paddingBottom:10}} >
   <Card.Title
    titleStyle={{textAlign:'center',}}
    title="Connect to a Node"
   />
   <ActivityIndicator style={{position:"absolute",top:0,right:0,bottom:0,left:0,elevation:10}} animating={connectToNode} color={Colors.red800} />
      <View style={{flexDirection:'row',alignItems:"center",
      justifyContent:"space-around",marginTop:15}}>
      <View style={{flexDirection:'row',alignItems:"center"}}>
        <Text>PureStake</Text>
        <RadioButton value= "purestake"
        status={node=="purestake"?"checked":"unchecked"}
        onPress={()=>{
          console.log("purree")
       
          this.setState({node:"purestake"})}}
          
          />
          
      </View>
      <View style={{flexDirection:'row',alignItems:"center"}}>
        <Text>Hackathon</Text>
        <RadioButton value="hackathon" 
        status={node=="hackathon"?"checked":"unchecked"}
        onPress={()=>{
          console.log("purree")
       
          this.setState({node:"hackathon"})}}
          
         
        />
      </View>
      <View style={{flexDirection:'row',alignItems:"center"}}>
        <Text>Custom Node</Text>
        <RadioButton value="customnode" 
        status={node=="customnode"?"checked":"unchecked"}
        onPress={()=>{
          console.log("purree")
       
          this.setState({node:"customnode"})}}
          
       
        />
        
      </View>
      </View>
      {node=="customnode"?<View>
      <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Api Address"
            mode="outlined"
            onChangeText={(text)=>this.setState({customNodeAddress:text})}
            value={customNodeAddress}
          />

    <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Port Number"
            mode="outlined"
            onChangeText={(text)=>this.setState({customNodePort:text})}
            value={`${customNodePort}`}
            keyboardType="numeric"
          />
           <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Api Token"
            mode="outlined"
            onChangeText={(text)=>this.setState({note:text})}
            value={customNodeToken}
          />

      </View>:      <View style={{flexDirection:'row',alignItems:"center",
      justifyContent:"space-around",marginTop:15}}>
      <View style={{flexDirection:'row',alignItems:"center"}}>
        <Text>Test net</Text>
        <RadioButton value= "testnet"
        status={network=="testnet"?"checked":"unchecked"}
        onPress={()=>{
          console.log("purree")
       
          this.setState({network:"testnet"})}}
          
          />
          
      </View>
      <View style={{flexDirection:'row',alignItems:"center"}}>
        <Text>Main Net</Text>
        <RadioButton value="mainnet" 
        status={network=="mainnet"?"checked":"unchecked"}
        onPress={()=>{
          console.log("purree")
       
          this.setState({network:"mainnet"})}}
          
         
        />
      </View> 
      </View>
}

     
  <View style={{alignItems:'center',marginTop:15}}>
      <Button  style={{width:"70%",}} mode="contained" onPress={this.handleConnectToNodelicked}>
          Connect
       </Button>
    
       </View>   
       <Banner
              visible={connectToNodeBanner}
              actions={[
                {
                  label: 'Ok',
                  onPress: () => this.setState({connectToNodeBanner:false}),
                },
                // {
                //   label: 'Learn more',
                //   onPress: () => this.setState({connectToNodeBanner:true}),
                // },
              ]}
     >
       {connectToNodeData}
     </Banner>
       </Card>
       <Divider />
       <Card style={{alignItems:'stretch',marginTop:15,marginLeft:10,marginRight:10,paddingBottom:10}}>
          <Card.Title
           titleStyle={{textAlign:'center'}}
           title="Account Creation And Recovery"
           subtitle="Recover Account "
          />


          <TextInput
           style={{backgroundColor:"#ffffff"}}
            label="Enter Seed Phrase"
            mode="outlined"
            onChangeText={(text)=>this.setState({seedphraseInput:text})}
            value={seedphraseInput}

          />
          <View style={{alignItems:'center',marginTop:15}}>
           <Button  style={{width:"70%",}} mode="contained" onPress={() => {
             this.handleRecoverAccountClicked()
             console.log('Pressed')}}>
             Recover Account
            </Button>
            </View>
            <Banner
              visible={recoverAcccountBanner}
              actions={[
                {
                  label: 'Copy Address',
                  onPress: () => {
                    Clipboard.setString(recoverAccountData)
                    this.setState({recoverAcccountBanner:false})},
                },
                {
                  label: 'Hide',
                  onPress: () => this.setState({recoverAcccountBanner:true}),
                },
              ]}
     >
      {recoverAccountData}
    </Banner>

    <Card.Title
           subtitle="Create Account "
          />
    <View style={{alignItems:'center',marginTop:0}}>
              <Button  style={{width:"70%",}} mode="contained" onPress={() => {
                this.handleCreateAccountClicked()
                this.setState({createAccountBanner:true})
                console.log('Pressed')}}>
                Create Account
                </Button>
                </View>
                <Banner
                  visible={createAccountBanner}
                  actions={[
                    {
                      label: 'Copy Address',
                      onPress: () => {
                        Clipboard.setString(createAccountData)
                        this.setState({createAccountBanner:false})},
                    },
                    {
                      label: 'Hide',
                      onPress: () => this.setState({createAccountBanner:true}),
                    },
                  ]}
        >
  {createAccountData}
        </Banner>

       </Card>


       <Card style={{marginTop:15,marginLeft:10,marginRight:10,paddingBottom:10,position:'relative'}}>
         <Card.Title
           title="Get Account Balance"
           titleStyle={{textAlign:'center'}}
         />
         <ActivityIndicator style={{position:'absolute',top:0,left:0,right:0,}} animating={accountBalanceLoader} color={Colors.black} />
          <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Address"
            mode="outlined"
            onChangeText={(text)=>this.setState({accountBalanceAddress:text})}
            value={accountBalanceAddress}
          />

<View style={{alignItems:'center',marginTop:15}}>
           <Button  style={{width:"70%",}} mode="contained" onPress={() => {
             this.handleGetAccountBalanceClicked();
             console.log('Pressed')}}>
             Get Account Balance
            </Button>
            </View>
       <Banner
              visible={accountBalanceBanner}
              actions={[
                {
                  label: 'Copy Amount',
                  onPress: () => {
                    Clipboard.setString(accountBalanceData)
                    this.setState({accountBalanceBanner:false})},
                },
                {
                  label: 'Hide',
                  onPress: () => this.setState({accountBalanceBanner:true}),
                },
              ]}
     >
      {accountBalanceData}
    </Banner>    
       </Card>

       <Card style={{marginTop:15,marginLeft:10,marginRight:10,paddingBottom:10,position:'relative'}}>
         <Card.Title
           title="Transfer Funds"
           titleStyle={{textAlign:'center'}}
         />
         <ActivityIndicator style={{position:'absolute',top:0,left:0,right:0,}} animating={transferFundsLoading} color={Colors.black} />
          <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Address"
            mode="outlined"
            onChangeText={(text)=>this.setState({transferFundsAddress:text})}
            value={transferFundsAddress}
          />

    <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Amount"
            mode="outlined"
            onChangeText={(text)=>this.setState({amount:text})}
            value={amount}
            keyboardType="numeric"
          />
           <TextInput
            style={{backgroundColor:"#ffffff"}}
            label="Enter Note"
            mode="outlined"
            onChangeText={(text)=>this.setState({note:text})}
            value={note}
          />

<View style={{alignItems:'center',marginTop:15}}>
           <Button  style={{width:"70%",}} mode="contained" onPress={() => {
             this.handleTransferFundsClicked();
             console.log('Pressed')}}>
            Transfer Funds
            </Button>
            </View>
       <Banner
              visible={transferFundsBanner}
              actions={[
                {
                  label: 'Copy Id',
                  onPress: () => {
                    this.setState({transferFundsBanner:false})
                    Clipboard.setString(transferFundsData)
                  },
                },
                {
                  label: 'Hide',
                  onPress: () => this.setState({transferFundsBanner:true}),
                },
              ]}
     >
      {transferFundsData}
    </Banner>    
       </Card>



           

      {/* <TouchableOpacity onPress={this.handleCreateAccountInfoPress}><Text>Create Account</Text></TouchableOpacity>
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateClientFromSandbox}><Text>cREATE sANDBOX cLIENT</Text></TouchableOpacity>
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
      <TouchableOpacity style={{marginTop:10}} onPress={this.handleCreateClientFromPureStake}><Text>Create Purestake client</Text></TouchableOpacity> */}
      {/* <Text>{accountInfo}</Text> */}
      </ScrollView>
    </View>    
     
  )
}
}
const styles = StyleSheet.create({
  bottom: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
  },
});