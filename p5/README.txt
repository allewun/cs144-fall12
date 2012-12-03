
CS 144 - Fall '12
Project 5

Partners:
    * Samuel Jun (003-955-212 - samuel.h.jun@gmail.com)
    * Allen Wu   (103-790-579 - allen.wu@ucla.edu)


Q1: For which communication(s) do you use the SSL encryption? If you are
    encrypting the communication from (1) to (2) in Figure 2, for example, write
    (1)->(2) in your answer.
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
A3: Using CSS, we set the main <div> container of the page to have a width of 
    95%, which is ensured to be smaller than 100%, the width of the screen.
    In addition to this, one of the <meta name="viewport"> tag attributes,
    "user-scalable = no" prevents the user from zooming in. Since the page is
    small enough and zooming is disabled, it's not possible to scroll
    horizontally.
A4: As previously mentioned in A3, the main <div> that wraps the contents of
    the page has a width of 95%. Likewise, we can do the same thing with the
    <input> elements within that <div>. Setting it to have a width of 100%
    ensures that it will have a width that matches its parent, namely the
    main <div>. Thus, it can't exceed the screen's width.
