import { NativeModules } from 'react-native';

type AlgoType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Algo } = NativeModules;

export default Algo as AlgoType;
