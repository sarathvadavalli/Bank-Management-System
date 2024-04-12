# Bank-Management-System
Bank Management system is a project that allows users to manage their bank accounts and gives a real time experience of banking. It is built using 'java' programming language and JDBC. Firstly, It authenticates the user by asking username and password.

#### There are five features available for user after logging in:
1. Deposit amount
2. Withdraw amount
3. View balance
4. View previous transaction
5. Transaction history

### Schema Diagram:

  #### User                         -- Transaction
      
                     Referenced by     ## Trasac_id
    ## Username ---------------------->   Username    
       Password                           Transac_type
       Account no.                        Amount
       Balance                            Balance
                                        
