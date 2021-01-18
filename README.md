# react-native-algo

This is a react-native wrapper around the java-algorand-sdk, it supports the following:
- Create Algorand Account
- Recover Algorand Account
- Get Account Balance
- Create and Sign Transaction
- Send Funds
- Create Multisignature Address,
- Create Multisignature Transaction
- Approve Multisignature Transaction
- Submit Transaction to the network
- Create ASA(Algorand Standard Asset)
- Change ASA Manager
- Opt-in To receive ASA
- Transfer ASA
- Freeze ASA
- Revoke ASA
- DeSTROY ASA
- Atomic Transactions
-Connect to a network(Hackathon,Purestake)


## Only Android Support
<img src="./React-native-algo_showcase.gif" alt="drawing" width="200"/>

<!-- ![screenshot](https://github.com/Jesulonimi21/react-native-algo/blob/main/Screenshot_1610673184.png) -->
 

## Installation

```sh
npm install react-native-algo
```
Make sure to include the multidex library in your app level build.gradle file like it is done below
```gradle
 defaultConfig {
     //....
    multiDexEnabled true
    }

dependencies {
    //....
     implementation 'com.android.support:multidex:1.0.3'
}


```


## Usage

```js
import Algo from "react-native-algo";
```
### Create And Recover Account
```JS
// Create Account
Algo.createNewAccount((publicAddr)=>{
  console.log(publicAddr);
});

//Recover Account
 Algo.recoverAccount("Mnemonic Address",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  })

```
###  Get Account Balance And Send Funds
```js
// make sure to have called either createNewAccount or recoverAccount before using the following methods

//Get Account Balance
 Algo.getAccountBalance("VJQG6EJPZDAWFYLFF5XE3OMRQEK6RFFYSBVJOGXBH63ZQZ3QRRIUVIB7MY",(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });

Algo.sendFunds("Address-to-send-funds-to","hi",10,
  (error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });

  // Transfer Funds


```

### Connect to a network
By Default, you are on the Hackathon Testnet network
```js
//Purestake
//you can pass MAINNET for mainnet
 Algo.createClientFromPurestake("TESTNET",PURESTAKE_API_PORT,PURESTAKE_API_KEY,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });

  //Hackathon Instance
  Algo.createClientFromHackathonInstance((error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
```

### Multisig Addresses and Transactions
```js
// make sure to have called either createNewAccount or recoverAccount before using the following methods
//Create Multisig Address
Algo.createMultiSignatureAddress(version,threshold,["Address 1","Address 2"],(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
  //Create Multisig Transaction
  Algo.createMultisigTransaction("Receivers Address","Note",amount,
  (error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  }); 

//Approve Multisignature Transaction
Algo.approveMultiSigTransaction(=signedTrans,(error,result)=>{
      if(error){
        console.error(error);
        return;
      }
      console.log(result);
    });

//Submit Transaction To Network
Algo.submitTransactionToNetwork(signedTransaction,(error,result)=>{
    if(error){
      console.error(error);
      return;
    }
    console.log(result);
  });
```

For more information on ASAs and Atomic Transactions including their usage, please check out the example project
## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
