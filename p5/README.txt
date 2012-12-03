
CS 144 - Fall '12
Project 5

Partners:
    * Samuel Jun (003-955-212 - samuel.h.jun@gmail.com)
    * Allen Wu   (103-790-579 - allen.wu@ucla.edu)


Q1: For which communication(s) do you use the SSL encryption? If you are
    encrypting the communication from (1) to (2) in Figure 2, for example, write
    (1)→(2) in your answer.
Q2: How do you ensure that the item was purchased exactly at the Buy_Price of
    that particular item?
Q3: How do you guarantee that the user cannot scroll horizontally?
Q4: How do you guarantee that the width of your textbox component(s) can fit the
    screen width of a mobile device?

A1: (4)->(5)
    (5)->(6)
A2: A HttpSession object is able to keep track of sessions through cookies. The
    HttpSession object keeps track of the buy price of the item in the
    background. The only thing the user transmits to the server is the session
    id through the cookie.
A3:
A4:
