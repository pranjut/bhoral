# bhoral-dwar



### Quick setup slick
In order to interact with your database using bhoral-dwar you have to do make sure you hjave implemented the below stuffs.
1. Have a case class as a model to use VO for the tables.
2. SlickOps: You have to extend SlickOps, implement the abstract fields and methods in it. You can quickly take a look at the test case UserOps. Along with the implementation you should have the table mapping as well. Here you would also use the VO that you have just made in the previous step.
3. SlickRepo: Have a separate class to implement the operations on the table by extending and implementing the SlickRepo. Take a look at UserRepo for example. In the example we are using SlickPostgresRepo which extends the SlickRepo. You can directly use SlickPostgresRepo like the example or create it according to your database.
4. DBComponent: DBComponent is another trait that needs the implementaiton. If you are using postgres you can use the default implementation available in the codebase. You can take a look at the test cases to see how we are using h2db component for testing.
