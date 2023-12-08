//Akshat Rastogi
//CIS4930
//EXTRA CREDIT ASSIGNMENT

//import statements
import java.util.HashMap;
import java.util.Map;

public class Main {
  // set initial variables
  private Map<String, Integer> database;
  private Map<String, Integer> dbTransactions;
  private boolean transactionOngoing;

  //main constructor
  public Main() {
    database = new HashMap<>();
    dbTransactions = new HashMap<>();
    transactionOngoing = false;
  }

  //get function will return the value associated with the key or null if the key doesn’t exist.
  public Integer get(String key) {
    return database.getOrDefault(key, null);
  }

  //put function will create a new key with the provided value if a key doesn’t exist. 
  //Otherwise it will update the value of an existing key.
  public void put(String key, int val) {
    if (transactionOngoing) {
      dbTransactions.put(key, val);
    } else {
      throw new IllegalStateException("No transaction in progress");
    }
  }
  
  // begin transaction function starts a new transaction.
  public void begin_transaction() {
    dbTransactions.clear();
    transactionOngoing = true;
  }

  //commit() applies changes made within the transaction to the main state. 
  //Allowing any future gets() to “see” the changes made within the transaction
  public void commit() {
    if (transactionOngoing) {
      database.putAll(dbTransactions);
      dbTransactions.clear();
      transactionOngoing = false;
    } else {
      throw new IllegalStateException("No open transaction in progress");
    }
  }

  //rollback() should abort all the changes made within the transaction and everything 
  //should go back to the way it was before.
  public void rollback() {
    if (transactionOngoing) {
      dbTransactions.clear();
      transactionOngoing = false;
    } else {
      throw new IllegalStateException("No ongoing transaction in progress");
    }
  }

  //main method for testing and running application
  public static void main(String[] args) {
    Main inmemoryDB = new Main();

    // should return null, because A doesn’t exist in the DB yet
    System.out.println("Get A: " + inmemoryDB.get("A"));

    // should throw an error because a transaction is not in progress
    try {
      inmemoryDB.put("A", 5);
    } catch (IllegalStateException exception) {
      System.out.println(exception.getMessage());
    }

    // starts a new transaction
    inmemoryDB.begin_transaction();

    // set’s value of A to 5, but its not committed yet
    inmemoryDB.put("A", 5);

    // should return null, because updates to A are not committed yet
    System.out.println("Get A: " + inmemoryDB.get("A"));

    // update A’s value to 6 within the transaction
    inmemoryDB.put("A", 6);

    // commits the open transaction
    inmemoryDB.commit();

    // should return 6, that was the last value of A to be committed
    System.out.println("Get A after commit: " + inmemoryDB.get("A"));

    // throws an error, because there is no open transaction
    try {
      inmemoryDB.commit();
    } catch (IllegalStateException exception) {
      System.out.println(exception.getMessage());
    }

    // throws an error because there is no ongoing transaction
    try {
      inmemoryDB.rollback();
    } catch (IllegalStateException exception) {
      System.out.println(exception.getMessage());
    }

    // should return null because B does not exist in the database
    System.out.println("Get B: " + inmemoryDB.get("B"));

    // starts a new transaction
    inmemoryDB.begin_transaction();

    // Set key B’s value to 10 within the transaction
    inmemoryDB.put("B", 10);

    // Rollback the transaction - revert any changes made to B
    inmemoryDB.rollback();

    // Should return null because changes to B were rolled back
    System.out.println("Get B after rollback: " + inmemoryDB.get("B"));
  }
}
