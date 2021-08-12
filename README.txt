Sima Radu

-- README --

In this README file, I will be giving some important notes about the vending machine program:

    1. This is a pretty generic implementation of a vending machine. It accepts
    all types of existing RON bills and coins and any prices for items (up to 2
    decimals).

    2. The user interface is a CLI. All commands are case-insensitive, besides
    the ones for giving money("RON50", for example). All command usage is also
    explained in the UI, at the start of the program, or by typing the "help"
    command.

    3. The items are loaded into the vending machine from a file (its file name
    or path needs to be given as a command line argument). Each line in the file
    must contain info about an item and must have the exact following format:
    "'item_UID','item_name','item_price','item_count'".

    4. The vending machine is pre-loaded with an amount of money (50 RON,
    made from different types of bills and coins), in order to more easily give
    change (especially to the first clients that buy products - otherwise these
    first clients would have to give the exact amount of money, or they would
    not be able to receive change).

    5. The method "public double giveChange(Item item)" from the BankStorage
    class needs to be refactored (it is way to big and does too many different
    things), but I did not yet find a good way to do it.