import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class Block {
    private Block previousBlock;
    private byte[] previousHash;
    private ArrayList<Transaction> transactions;

    public Block() { transactions = new ArrayList<>(); }

    // getters and setters
    public Block getPreviousBlock() { return previousBlock; }
    public byte[] getPreviousHash() { return previousHash; }
    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setPreviousBlock(Block previousBlock) { this.previousBlock = previousBlock; }
    public void setPreviousHash(byte[] previousHash) { this.previousHash = previousHash; }
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String toString() {
        String cutOffRule = new String(new char[81]).replace("\0", "-") + "\n";
        String prevHashString = String.format("|PreviousHash:|%65s|\n",
                Base64.getEncoder().encodeToString(previousHash));
        String hashString = String.format("|CurrentHash:|%66s|\n",
                Base64.getEncoder().encodeToString(calculateHash()));
        String transactionsString = "";
        for (Transaction tx : transactions) {
            transactionsString += tx.toString();
        }
        return "Block:\n"
                + cutOffRule
                + hashString
                + cutOffRule
                + transactionsString
                + cutOffRule
                + prevHashString
                + cutOffRule;
    }

    /**
     * Calculates the hash of the current block
     * Uses SHA-256 hash function on the hash of the previous block
     * + all transactions in the form of "tx|<sender>|<content>"
     *
     * NOTE: Heavily inspired by assignment0
     *
     * @return hash of the current block
     */
    public byte[] calculateHash() {
        byte[] hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Creates DataOutputSteam with underlying ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            // Add hash of previous block to dos
            dos.write(previousHash);

            // Add transactions to dos
            for (Transaction transaction : transactions) {
                // Convert transaction to "tx|<sender>|<content>" form
                // and add to dos
                String stringTransaction = getTransactionString(transaction);
                dos.writeUTF(stringTransaction);
            }

            // Convert to byte array and apply hash function
            byte[] bytes = baos.toByteArray();
            hash = digest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /**
     * Converts a transaction into "tx|<sender>|<content>" String form
     * @param transaction Transaction to be converted
     * @return Converted transaction String
     */
    private String getTransactionString(Transaction transaction) {
        return "tx|" + transaction.getSender() + "|" + transaction.getContent();
    }
}