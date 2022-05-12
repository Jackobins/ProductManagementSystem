# Product Management Project

## Uses of the Application
Company managers can use this product management system to keep track of their 
stock. They can add stock as it gets shipped in, as well as remove stock from 
the system when it is sold to customers. Using this application, they can keep
track of their stock and stay organized, reducing the chance of any lost or
extra stock, possibly resulting in wasted money.

## Why did I choose to make this project?
I have always been curious as to how a company keeps track of its products once
it grows to a big size. I also want to be a part of this management process in 
some capacity in my career. Working on this project would allow me to familiarize
myself with the basic idea of keeping stock organized.

## User Stories

As a *user*, I want to be able to:
### Phase 1
- Add or remove multiple products (with duplicates) to the current stock
- Change the prices of certain products
- View the total value of the stock (combined value of products)
- View all products in the stock, as well as any individual product
- View all clients that have made a purchase, as well as any individual client

### Phase 2
- Save the data of the stock to file at any point
- Load a previously worked-on stock when starting the program

## Phase 4 - Design

### Task 2:
- Laptop x1 was added to stock.
- Camera x2 was removed from stock.

### Task 3:
- Reduce coupling between the Stock, Product and Client classes. The Stock class uses both the Product and Client
classes, and there is a bidirectional association between the Client and Product classes, so a lot of behaviour 
needs to be updated when one class is updated.
- Make more use of the Writeable interface. Currently, the Writable interface is only used to abstract the toJson
method out of the Product and Stock classes, and is never used elsewhere. Perhaps it would improve coupling
and cohesion issues if the Writeable interface was used more, or if more behavior was abstracted out.
- Improve cohesion in the Stock class, it currently seems to be doing a lot of work on its own. Especially in the
add and remove methods, the Stock class performs multiple checks and updates a lot of fields in multiple classes.