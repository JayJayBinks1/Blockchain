public class TestTask1 {

    public static void main(String args[]) {
        Blockchain blockchain = new Blockchain();
        String tx = "tx|jzan7976|Hello World!";
        System.out.println(blockchain.addTransaction(tx));
        blockchain.addTransaction("tx|jzan7976|1");
        System.out.println(blockchain.addTransaction("tx|jzan7976|butttwo"));
        blockchain.addTransaction("tx|jzan7976|buttthree");
        blockchain.addTransaction("tx|jzan7976|buttfour");
        blockchain.addTransaction("tx|jzan7976|buttfive");
        blockchain.addTransaction("tx|jzan7976|buttsix");
        blockchain.addTransaction("tx|jzan7976|buttseven");
        blockchain.addTransaction("tx|jzan7976|butteight");
        blockchain.addTransaction("tx|jzan7976|9");
        System.out.println(blockchain.toString());
    }

}
