import java.util.ArrayList;

public class Blockchain {
    private Block head;
    private ArrayList<Transaction> pool;
    private int length;

    private final int poolLimit = 3;

    public Blockchain() {
        pool = new ArrayList<>();
        length = 0;
    }

    // getters and setters
    public Block getHead() { return head; }
    public ArrayList<Transaction> getPool() { return pool; }
    public int getLength() { return length; }
    public void setHead(Block head) { this.head = head; }
    public void setPool(ArrayList<Transaction> pool) { this.pool = pool; }
    public void setLength(int length) { this.length = length; }

    /**
     * Adds a valid txString to the pool. If the pool exceeds length 3,
     * a new block is created in the blockchain
     * @param txString String in format of "tx|<sender>|<content>"
     * @return int 0 if invalid txString, 1 if added to the pool, 2 if new block is created
     */
    public int addTransaction(String txString) {

        // Null check + checks if txString contains tx and
        // delimiter "\"
        if (txString == null || !txString.contains("tx") || !txString.contains("|")) {
            //System.out.println("Rejected");
            return 0;
        }

        String[] txParts = txString.split("\\|");

        // If txPart is not of length 3
        // then it has not been successfully separated
        // into tx, <sender>, <content>
        if (txParts.length != 3) {
            //System.out.println("Rejected");
            return 0;
        }

        String sender = txParts[1];
        String content = txParts[2];

        // Checks if txString is in specified format
        // Returns 0 if not
        if (!isValidSender(sender) || !isValidContent(content)) {
            //System.out.println("Rejected");
            return 0;
        }

        //System.out.println("Accepted");

        // Else creates new transaction and adds to pool
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setContent(content);
        pool.add(transaction);

        // If pool size is 3, commit all transactions to block
        // Add block to blockchain
        // Return 2
        if (pool.size() == 3) {
            Block block = new Block();
            byte[] genesisHash = new byte[32];
            block.setPreviousHash(genesisHash);
            block.setTransactions(pool);

            // If blockchain already exists
            // Add the old head to current block
            if (head != null) {
                block.setPreviousBlock(head);
                block.setPreviousHash(head.calculateHash());
            }

            // Make current block the new head
            head = block;
            pool = new ArrayList<>();
            length++;

            return 2;
        }

        return 1;
    }

    /**
     * Checks if sender unikey is valid
     * Unikeys should be in form of (regex: [a-z]{4}[0-9]{4})
     * @param unikey unikey to be checked
     * @return boolean value indicating if unikey is valid
     */
    private boolean isValidSender(String unikey) {

        // Checks if unikey is correct length
        if (unikey.length() != 8) {
            return false;
        }

        char[] characters = unikey.toCharArray();
        // Checks if first four characters are lowercase letters
        for (int i = 0; i < 4; i++) {
            if (!Character.isLetter(characters[i]) || !Character.isLowerCase(characters[i])) {
                return false;
            }
        }

        // Checks if last four letters are digits
        for (int i = 4; i < 8; i++) {
            if (!Character.isDigit(characters[i])) {
                return false;
            }
        }

        // Returns true if valid
        return true;
    }

    /**
     * Checks if content is valid
     * Content should have no more than 70 English characters or contain
     * a '|' character. Should also not be null
     * @param content Content to be checked
     * @return boolean value indicating if content is valid
     */
    private boolean isValidContent(String content) {
        // && content.matches("^[ A-Za-z]+$")
        return (content != null && content.length() <= 70 && !content.contains("|"));
    }

    public String toString() {
        String cutOffRule = new String(new char[81]).replace("\0", "-") + "\n";
        String poolString = "";
        for (Transaction tx : pool) {
            poolString += tx.toString();
        }

        String blockString = "";
        Block bl = head;
        while (bl != null) {
            blockString += bl.toString();
            bl = bl.getPreviousBlock();
        }

        return "Pool:\n"
                + cutOffRule
                + poolString
                + cutOffRule
                + blockString;
    }

}