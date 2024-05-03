//java.io imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
//java.util imports
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;



public class CoffeeMenu {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice;
        ArrayList<menuItem> menuItems = new ArrayList<menuItem>(); //initialization of menu items
        buildMenu(menuItems);
        ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //initialization of transaction storage 
        //main menu loop
        do {
            System.out.printf("Main Menu: \n");
            System.out.printf("1 Process Purchase \n");
            System.out.printf("2 Update Products Menu \n");
            System.out.printf("0 EXIT \n");
            //Get user input
            choice = ReceiveInput(scan, 2);
            switch (choice) {
                case 0:
                    break;
                case 1:
                // if user picks process purchase
                    System.out.println(""); 
                    int numofproducts = menuItems.size(); 
                    Cart cart = new Cart();
                    processPurchases(menuItems, transactions, scan, numofproducts, cart);
                    break;
                case 2:
                    //else if user picks update menu
                    buildMenu(menuItems);
                    System.out.printf("Products updated successfully\n"); 
                    break;
                }
            System.out.println(""); 
        }  while (choice!= 0);

        //exit procedure
        if(transactions.size() != 0) {
            System.out.println("Saving transactions information to disk...");
            SaveTransactions(transactions);
        }
        System.out.println("Closing program...");
        scan.close();
    }
    
    //Save Transactions to txt file
    static void SaveTransactions(ArrayList<Transaction> transactions){
        try{
        FileWriter fwr = new FileWriter(new File("transaction.txt"), true);
        for (Transaction tran : transactions){
            String tranString = tran.CSVFormatTransaction(); //format Transaction information to CSV format
            fwr.append(tranString); // Append to file
        }
        fwr.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void processPurchases(ArrayList<menuItem> menuItems, ArrayList<Transaction> transactions, Scanner scan, int numofproducts, Cart cart) {
        int choice;
        boolean finished = false;
        //purchase loop
        do{
            printMenu(menuItems);
            //Get & verify user input
            choice = ReceiveInput(scan, numofproducts); 
            if(choice == 0){ //if choice is EXIT
                break;
            }
            menuItem selectedItem = menuItems.get(choice - 1);
            System.out.printf("selected Product: %s\n\n", selectedItem.getName());
            cart.addItem(selectedItem); //add item to cart 
            System.out.printf("%s Added to cart\n", selectedItem.getName());
            System.out.printf("Current Cart:\n");
            for (CartItem item : cart.getCartItems()) {
                System.out.printf("%dx %s $%.2f\n",item.getQuantity(), item.getName(), item.getAccumulatedPrice());
            }
            System.out.printf("would the customer like anything else?\n");
            System.out.printf("1 yes\n2 no\n");  
            choice = ReceiveInput(scan, 2);
            if(choice != 1){ //if customer does not want anything else, end loop
                finished = true;
            }
        }while (!finished);
        if (choice == 0){ // if customer changes their mind
        System.out.println("Cancelling transaction...");
        }
        else{
        processPayment(cart, scan, transactions);
        }
    }
    //payment procedure 
    static void processPayment(Cart cart, Scanner scan, ArrayList<Transaction> transactions){
        int choice;
        System.out.printf("Total to be paid: %.2f\n", cart.getTotalCost());
        System.out.printf("please select a payment method: \n1 Card \n2 Cash \n0 CANCEL\n");
        choice = ReceiveInput(scan, 2);
        if(choice == 0){
            System.out.println("Cancelling transaction...");
        }
        if(choice == 1){ // if CARD is selected
            System.out.printf("select type of card: \n1 Visa \n2 MasterCard \n0 RETURN\n");
            int cardType = ReceiveInput(scan,2);
            if(cardType == 0){
                processPayment(cart, scan, transactions);
            }
            else{
                System.out.printf("\nSending information to card Reader...\n");
                CardTransaction cardTransaction = new CardTransaction(cart.getTotalCost(), cart.getCartItems(), cardType);
                GenerateCardReceipt(cart.getTotalCost(), cart.getCartItems(), cardTransaction.getCardType());
                System.out.printf("Transaction saved to memory\n"); 
                transactions.add(cardTransaction);
            }
        }
        else if(choice == 2){ // if CASH is selected
            float cashTendered = 0;
            int tries = 5;
            while (cashTendered - cart.getTotalCost() < 0) {
                if(tries ==  0){
                    System.out.println("too many invalid inputs, Cancelling transaction...");
                    break;
                }
                System.out.printf("please enter amount tendered, input 0 to go return: ");
                try{
                    cashTendered = scan.nextFloat(); //Rechieve input does not work because we need a float value
                    if (cashTendered == 0) {
                        break;
                    }
                    else if (cashTendered - cart.getTotalCost() < 0 ){
                        System.out.println("not enough cash tendered");
                        tries--;
                    } 
                }catch(InputMismatchException e){
                    System.out.println("Invalid input");
                    tries--;
                }
            }
            if(tries == 0) {
                
            }
            if (cashTendered == 0){
                processPayment(cart, scan, transactions);
            }
            else{
                float changeGiven = cashTendered - cart.getTotalCost(); 
                System.out.printf("Change to Give: $%.2f\n", changeGiven);
                CashTransaction cashTransaction = new CashTransaction(cart.getTotalCost(), cart.getCartItems(), cashTendered, changeGiven);
                GenerateCashReceipt(cart.getTotalCost(), cart.getCartItems(), cashTendered, changeGiven);
                System.out.println("Transaction Saved to memory"); 
                transactions.add(cashTransaction);
            }
        }
    }

    static void GenerateCardReceipt(float totalCost, ArrayList<CartItem> cartItems, String cardType){
        //print Receipt to the console 
        System.out.println("--------------------------Receipt---------------------------");
        for (CartItem item : cartItems){ //print items from cart 
            System.out.printf("%dx %s $%.2f\n",item.getQuantity(), item.getName(), item.getAccumulatedPrice());
        }
        System.out.printf("Total: $%.2f\n", totalCost); // print total cost of items
        System.out.printf("paid in total with %s Card", cardType); // print card type 
        System.out.println("Thank you for your purchase! Please come again!");
        System.out.println("------------------------------------------------------------");
    }

    static void GenerateCashReceipt(float totalCost, ArrayList<CartItem> cartItems, float cashTendered, float changeGiven){
        //print Receipt to the console 
        System.out.println("--------------------------Receipt---------------------------");
        for (CartItem item : cartItems){ // print items from cart
            System.out.printf("%dx %s $%.2f\n",item.getQuantity(), item.getName(), item.getAccumulatedPrice());
        }
        System.out.printf("Total: $%.2f\n", totalCost); // print total cost of items 
        System.out.printf("Cash Tendered: $%.2f\n", cashTendered); // print cash tendered
        System.out.printf("Change Given: $%.2f\n", changeGiven); // print change given
        System.out.println("Thank you for your purchase! Please come again!");
        System.out.println("------------------------------------------------------------");
    }

    static void buildMenu(ArrayList<menuItem> menuitems){
        try{ // build menu items ArrayList
            BufferedReader br = new BufferedReader(new FileReader("Inventory.txt"));
            String line;
            if(!menuitems.isEmpty()){
                menuitems.clear();
            }
            while((line = br.readLine()) != null){
                String[] lines = line.split(", ");
                menuItem item = new menuItem(lines[0], Float.parseFloat(lines[1]));
                menuitems.add(item);
            }
            br.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    static void printMenu(ArrayList<menuItem> menuItems){ // print menu to screen
        System.out.println("-------------------------Menu-------------------------");
        for (int i = 0; i < menuItems.size(); i++){
            System.out.printf("%d %-40s $%.2f\n", i + 1, menuItems.get(i).getName(), menuItems.get(i).getPrice());
        }
        System.out.println("------------------------------------------------------");
        System.out.println("0 CANCEL PURCHASE");
    }

    // user input validation 
    static int ReceiveInput(Scanner scan ,int numberofChoices){
        int choice;
        try {
            System.out.print("Input: ");
            choice = scan.nextInt();
            if (choice > numberofChoices || choice < 0){
                System.out.println("invalid input");
                return ReceiveInput(scan, numberofChoices);
            }
            else {
                return choice;
            }
        }catch(InputMismatchException e){
            System.out.println("Invalid input");
            scan.next();
            return ReceiveInput(scan, numberofChoices);
        }
    }
}