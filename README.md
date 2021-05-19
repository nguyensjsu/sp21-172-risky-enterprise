#  Risky Enterprise Team Project Journal
_By Resky Enterprise: Ying Chang Cui, Chahatpreet Grewal, Xuefeng Xu_


--- 
&nbsp; 

> Content:
> - [Project Design](#project-design)
> - [Features and Implementation](#features-and-implementation)
> - [Challenges and Solutions](#challenges-and-solutions)
> - [Roles and Contribution](#roles-and-contribution)

---

## Project Design 

Starbucks Online Order is an online order application that used by Startbuck to process customers' order, payment process, and reward management. Also, the application allows customers to review their order and reward record, and manage payment method. Our solution includes Cashier's App, Customer App, and Office App as frontends. Order Process service and Payment Process serivce as backend services connecting with different databases. Here is the design for the project.

<img src="images/MicroServiceArchitecture-2.png"><br/>
_Project Architecture Design_

From the Figure, Cashier gets order with customized options directly from customer, then forward the order to Order Porcessing service. The OP service will push the order to Payment processing sevice. Customer, on the other hand, can login to the customer front end to check the order that just been placed. Also, customer could review his/her payment method, add credit card, or decide to redeem reward through the Customer fronend App. Lastly, cashier can login to office app to change the customer's reward.

#
## Features and Implementation

1. Cashier's App
   > What features were implemented?
   - Our Cashier Application uses our orderProcessing API and the cashier can place different types of drinks (coffee, hot drinks, tea, and frappuccino).
   - Drinks can be customized by different sizes (small,medium,large) and different milk options (whole milk, nonfat milk, almond milk, and soy milk)
   - Drinks would be saved in a database through our API call (submit button). 
   - The cashier can clear orders (clear order button in home page) and it would delete all the orders from the database. 
   - The customer can for their order either by credit card or rewards. 
   - The console would receive feedback if the transaction is successful. 
   - If the transaction is successful, the database would be cleared. 
   - The cashier can also see what drinks are currently in the database to clarify what information was submitted through the API call.
2. Backoffice Help Desk App
   > What features were implemented?
   - The office app provides login feature for cahiers who want to use the app. 
   - After login, cashier would search for a paticular customer by customer ID.
   - If the customer exist, the app would display the current reward of this customer.
   - Cashier could change the reward by input a new value. The updated value would be displayed as the current reward.
  
3. Online Store
   > What features were implemented?
   - Customer can use the app as a return customer or new customer. 
   - customer can review his/her order from the order page.
   - Customer can review his/her payment methods.
   - Customer can add a new credit card for payment process.
   - Customer can review his/her current reward record and decide whether or not to use it.

4. REST API 
   > Final design with sample request/response


5. Integrations
   - Which integrations were selected?
6. Cloud Deployments
   - Design Notes on GitHub an Architecture Diagram of the overall Deployment.
   - How does your Team's System Scale?  Can it handle > 1 Million Mobile Devices?
7. Technical Requirements
   - Discussion with screenshot evidence of how each technical requirement is meet.


   * MicroService Architecture
   <details><summary>Archtecure Design 1</summary><img src="images/MicroServiceArchitecture.png"></details>
   <details><summary>Archtecure Design 2</summary><img src="images/MicroServiceArchitecture-2.png"></details>

--- 
&nbsp;

## Challange and Solutions
Ying Chang Cui- One challenge that I had encountered was correctly implementing the API from the Cashier Application. Initially I was not getting any feedback from
our API so it was hard to debug this issue. I was able to solve this by referencing Lab 7 when we did the payment API calls to learn how to set parameters to be passed in
and how to turn json to string and vise versa. My teammates were also very communicative so I know what parameters to be sending and receiving. The architecture we created
intially also helped me understand the overall workflow and break down the necessary steps to implement Cashier Application.

## Roles and Contributions

Ying Chang Cui: Cashier Application

Chahatpreet Grewal: Order Processing Microservice

Xuefeng Xu: Customer front end


#


































## Meeting Journal

Mar-19: work distribution, group forming agreement
   - The project is divided into two phases (Phases 1: Cashier's App, Phase 2: Online Store), each phase consists of front-end and back-end. 
   - The four-person team will be divided into two groups. Each group member will assume different roles at different phases.

   - <details><summary>work distribution and group form</summary><img src="images/work%20distribution%20and%20group%20form.png" width="700"></details>
#
__Week 1__ Plan: Clearfy workflow, work assignment, and start working individually. <br/>

Apr-22: Forming workflow diagram, and assgin work for each team members.

Apr-23: Re-formating workflow diagram.
  + current work assignments: 
    + Chahat: Order Processing Microservice
    + Ying: Casheir App 
    + Xuefeng: Customer front end.
#
__Week 2__ Plan: test Casheir App, Customer Frontend with Order Processing Microservice and start working on backoffice. <br/>


#
__Week 3__ Plan: Test Payment processing microservice with relative frontend couple with databases<br/>


#
__Week 4__ Plan: General testing<br/>




