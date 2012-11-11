

CREATE TABLE Items (
  itemID      INT PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  currently   DECIMAL(8,2) NOT NULL,
  buy_price   DECIMAL(8,2),
  first_bid   DECIMAL(8,2) NOT NULL,
  num_of_bids INT NOT NULL,
  started     TIMESTAMP NOT NULL,
  ends        TIMESTAMP NOT NULL,
  sellerID    VARCHAR(40) NOT NULL,
  description VARCHAR(4000)
  -- FOREIGN KEY (sellerID) REFERENCES Users(userID)
);

CREATE TABLE Users (
  userID   VARCHAR(40) PRIMARY KEY,
  rating   INT NOT NULL,
  location VARCHAR(40),
  country  VARCHAR(40)
);

CREATE TABLE Bids (
  itemID INT NOT NULL,
  userID VARCHAR(40) NOT NULL,
  time   TIMESTAMP NOT NULL,
  amount DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (itemID, userID, time)
  -- FOREIGN KEY (itemID) REFERENCES Items(itemID),
  -- FOREIGN KEY (userID) REFERENCES Users(userID)
);

CREATE TABLE Categories (
  itemID   INT NOT NULL,
  category VARCHAR(40) NOT NULL,
  PRIMARY KEY (itemID, category)
  -- FOREIGN KEY (itemID) REFERENCES Items(itemID)
);
