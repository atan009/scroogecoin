//copied from DropboxTestBlockChain.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Comparator;

public class TxHandler {

	/* Creates a public ledger whose current UTXOPool (collection of unspent 
	 * transaction outputs) is utxoPool. This should make a defensive copy of 
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	private UTXOPool upool;
	
	public UTXOPool getUpool() {
		return upool;
	}

	public void setUpool(UTXOPool upool) {
		this.upool = upool;
	}

	public TxHandler(UTXOPool utxoPool) {
		// IMPLEMENT THIS
		setUpool(new UTXOPool(utxoPool));
	}

	/* Returns true if 
	 * (1) all outputs claimed by tx are in the current UTXO pool, 
	 * (2) the signatures on each input of tx are valid, 
	 * (3) no UTXO is claimed multiple times by tx, 
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of   
	        its output values;
	   and false otherwise.
	 */

	public boolean isValidTx(Transaction tx) {
		// IMPLEMENT THIS
		double sumIn = 0.0;
		double sumOut = 0.0;
		
		ArrayList<UTXO> UTXOchk = new ArrayList<UTXO>();
		
		//condition 1
		for(Transaction.Input it : tx.getInputs()){
			UTXO currUTXO = new UTXO(it.prevTxHash, it.outputIndex);
			
			if(UTXOchk.contains(currUTXO)){
				return false;
			}
			
			UTXOchk.add(currUTXO);
			
			if(getUpool().contains(currUTXO) == false){
				return false;
			}
			
			sumIn += getUpool().getTxOutput(currUTXO).value;
			
			
		}
		
		//condition 2
		int index = 0;
		for(Transaction.Input it : tx.getInputs())
		{
			UTXO currUTXO = new UTXO(it.prevTxHash, it.outputIndex);

			
			//if (tx.getRawTx() != it.signature)FUCK MY LIFE
			//if (tx.getInput(it.outputIndex) != it)
			//if (!tx.getOutput(currUTXO.getIndex()).address.verifySignature(tx.getRawDataToSign(it.outputIndex), it.signature))
			//if (!tx.getOutput(currUTXO.getIndex()).address.verifySignature(tx.getRawTx(), it.signature))
			//if (!tx.getOutput(it.outputIndex).address.verifySignature(tx.getRawTx(), it.signature))
			//if (!tx.getOutput(0).address.verifySignature(tx.getRawTx(), it.signature))
			//if (!tx.getOutput(index).address.verifySignature(tx.getRawTx(), it.signature))
			//if (!tx.getOutput(it.outputIndex).address.verifySignature(tx.getRawDataToSign(it.outputIndex), it.signature))
			//if (!tx.getOutput(it.outputIndex).address.verifySignature(tx.getRawDataToSign(currUTXO.getIndex()), it.signature))
			//if (!tx.getOutput(currUTXO.getIndex()).address.verifySignature(tx.getRawDataToSign(currUTXO.getIndex()), it.signature))
			//if (!upool.getTxOutput(currUTXO).address.verifySignature(tx.getRawTx(), it.signature))
			//if (!upool.getTxOutput(currUTXO).address.verifySignature(tx.getRawDataToSign(0), it.signature))
			//int index = 0;
			
			//ArrayList<Output> OPs = tx.getOutputs();
			if (!upool.getTxOutput(currUTXO).address.verifySignature(tx.getRawDataToSign(index), it.signature))//10 damn hours
			{
				return false;
			}
			index++;
		}
		
		//condition 3
		
		
		//condition 4
		for(Transaction.Output it : tx.getOutputs())
		{
			
			if (it.value < 0)
			{
				return false;
			}
			sumOut += it.value;
		}
		
		//condition 5
		if (sumOut > sumIn)
		{
			return false;
		}
		
		return true;
	}

	/* Handles each epoch by receiving an unordered array of proposed 
	 * transactions, checking each transaction for correctness, 
	 * returning a mutually valid array of accepted transactions, 
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		// IMPLEMENT THIS
		//Transaction[] validArray;
		//TransactionPool validArray;
		//UTXO currUTXO;
		//currUTXO.
		
		ArrayList<Transaction> validArray;
		
		for (int i = 0; i < possibleTxs.length;i++)
		{
			if(!isValidTx(possibleTxs[i]))
			{
				possibleTxs[i] = null;
			}
			else
			{
				for(Transaction.Input it : possibleTxs[i].getInputs())
				{
					UTXO currUTXO = new UTXO(it.prevTxHash, it.outputIndex);
					

					upool.removeUTXO(currUTXO);
					//possibleTxs[i] = null;
				}
			}
		}
		
		//possibleTxs = validArray.to;
		setUpool(upool);
		return possibleTxs;
	}

} 